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

    private static final String EMPTY = "";

    @Bean
    public WebAppStaticResourceServerController provideWebAppStaticResourceServerController() throws IOException {
        Properties mimeMappings = new Properties();
        mimeMappings.load(getClass().getClassLoader().getResourceAsStream(MIME_MAPPINGS_CLASSPATH_PATH));
        loadAllWebResourcesInCache(mimeMappings);
        return new WebAppStaticResourceServerController(new StaticResourceCache(CACHE.build()));
    }

    private void loadAllWebResourcesInCache(final Properties mimeMappings) throws IOException {
        File uiResourcesDir = new ClassPathResource(UI_BASE_PATH_IN_CLASSPATH).getFile();
        try (Stream<Path> pathStream = walk(of(uiResourcesDir.getAbsolutePath()))) {
            pathStream.filter(Files::isRegularFile)
                    .forEach(filePath -> {

                        final String filePathRelativeToUiFolder = filePath.toString()
                                .replace(uiResourcesDir.getParent(), EMPTY)
                                .replace("\\", "/")
                                .replace(FILE_TYPE_OF_CONTENT_ENCODING, EMPTY);

                        final ResponseEntity<Flux<DataBuffer>> responseEntity = ok()
                                .headers(getHttpHeaders(filePath.toString(), mimeMappings))
                                .body(read(filePath, DATA_BUFFER_FACTORY, DATA_BUFFER_SIZE).cache());

                        CACHE.put(filePathRelativeToUiFolder, just(responseEntity));
                        if (filePath.endsWith(INDEX_FILE_NAME)) {
                            CACHE.put(UI_URL_PATH, just(responseEntity));
                        }

                    });
        }
    }

    private HttpHeaders getHttpHeaders(final String fileSystemPath,
                                       final Properties mimeMappings) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(getFileMediaType(fileSystemPath, mimeMappings));
        headers.set("Content-Encoding", CONTENT_ENCODING);
        headers.setCacheControl(maxAge(CACHE_CONTROL_MAX_AGE));
        return headers;
    }

    private MediaType getFileMediaType(final String fileSystemPath,
                                       final Properties mimeMappings) {
        final String[] filePathContent = fileSystemPath.split("[.]");
        String extension = filePathContent[filePathContent.length - 2];
        parseMediaType(mimeMappings.get(extension).toString());
        return parseMediaType(mimeMappings.get(extension).toString());
    }

    public static class StaticResourceCache {

        private final ImmutableMap<String, Mono<ResponseEntity<Flux<DataBuffer>>>> cache;
        private final Mono<ResponseEntity<Flux<DataBuffer>>> indexFileResponse;

        public StaticResourceCache(ImmutableMap<String, Mono<ResponseEntity<Flux<DataBuffer>>>> cache) {
            this.cache = cache;
            this.indexFileResponse = cache.get(UI_URL_PATH);
        }

        public Mono<ResponseEntity<Flux<DataBuffer>>> get(String key) {
            return cache.getOrDefault(key, indexFileResponse);
        }
    }
}