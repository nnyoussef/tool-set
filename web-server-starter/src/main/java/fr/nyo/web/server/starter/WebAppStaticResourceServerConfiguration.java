package fr.nyo.web.server.starter;

import com.google.common.collect.ImmutableMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Properties;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableMap.builder;
import static java.nio.file.Files.walk;
import static java.nio.file.Path.of;
import static java.time.Duration.ofDays;
import static org.springframework.core.io.buffer.DataBufferUtils.read;
import static org.springframework.http.CacheControl.maxAge;
import static org.springframework.http.MediaType.parseMediaType;
import static org.springframework.http.ResponseEntity.ok;
import static reactor.core.publisher.Mono.just;

@Configuration
public class WebAppStaticResourceServerConfiguration {

    private static final Duration CACHE_CONTROL_MAX_AGE = ofDays(365);
    private static final String CONTENT_ENCODING = "br";
    private static final String FILE_TYPE_OF_CONTENT_ENCODING = "." + CONTENT_ENCODING;

    private static final int DATA_BUFFER_SIZE = 10_048_576;
    private static final DefaultDataBufferFactory DATA_BUFFER_FACTORY = new DefaultDataBufferFactory();
    private static final ImmutableMap.Builder<String, Mono<ResponseEntity<Flux<DataBuffer>>>> CACHE = builder();
    private static final String INDEX_FILE_NAME = "index.html" + FILE_TYPE_OF_CONTENT_ENCODING;
    private static final String MIME_MAPPINGS_CLASSPATH_PATH = "mime-mappings.properties";

    private static final String UI_BASE_PATH_IN_CLASSPATH = "ui";
    private static final String UI_URL_PATH = "/" + UI_BASE_PATH_IN_CLASSPATH;

    @Bean
    public StaticResourceCache provideWebApplicationStaticResourcesCache() throws IOException {
        Properties mimeMappings = new Properties();
        mimeMappings.load(getClass().getClassLoader().getResourceAsStream(MIME_MAPPINGS_CLASSPATH_PATH));
        loadAllWebResourcesInCache(mimeMappings);
        return new StaticResourceCache(CACHE.build());
    }

    private void loadAllWebResourcesInCache(Properties mimeMappings) throws IOException {
        File uiResourcesDir = new ClassPathResource(UI_BASE_PATH_IN_CLASSPATH).getFile();
        try (Stream<Path> pathStream = walk(of(uiResourcesDir.getAbsolutePath()))) {
            pathStream.filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        final MediaType fileMimeType = getFileMediaType(filePath.toString(), mimeMappings);

                        String filePathRelativeToUiFolder = filePath.toString()
                                .replace(uiResourcesDir.getParent(), "")
                                .replace("\\", "/")
                                .replace(FILE_TYPE_OF_CONTENT_ENCODING, "");

                        final ResponseEntity<Flux<DataBuffer>> responseEntity = ok()
                                .headers(getHttpHeaders(fileMimeType))
                                .body(read(filePath, DATA_BUFFER_FACTORY, DATA_BUFFER_SIZE).cache());

                        CACHE.put(filePathRelativeToUiFolder, just(responseEntity));
                        if (filePath.endsWith(INDEX_FILE_NAME)) {
                            CACHE.put(UI_URL_PATH, just(responseEntity));
                        }
                    });
        }
    }

    private HttpHeaders getHttpHeaders(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.set("Content-Encoding", CONTENT_ENCODING);
        headers.setCacheControl(maxAge(CACHE_CONTROL_MAX_AGE));
        return headers;
    }

    private MediaType getFileMediaType(String fileSystemPath,
                                       Properties mimeMappings) {
        final String[] filePathContent = fileSystemPath.split("[.]");
        String extension = filePathContent[filePathContent.length - 2];
        parseMediaType(mimeMappings.get(extension).toString());
        return parseMediaType(mimeMappings.get(extension).toString());
    }

    public static class StaticResourceCache {

        private final ImmutableMap<String, Mono<ResponseEntity<Flux<DataBuffer>>>> cache;

        public StaticResourceCache(ImmutableMap<String, Mono<ResponseEntity<Flux<DataBuffer>>>> cache) {
            this.cache = cache;
        }

        public Mono<ResponseEntity<Flux<DataBuffer>>> get(String key) {
            return cache.getOrDefault(key, cache.get(UI_URL_PATH));
        }
    }
}