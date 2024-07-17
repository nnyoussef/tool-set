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
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.file.Files.walk;
import static java.nio.file.Path.of;
import static org.springframework.core.io.buffer.DataBufferUtils.read;
import static org.springframework.http.CacheControl.maxAge;
import static org.springframework.http.MediaType.*;
import static org.springframework.http.ResponseEntity.ok;
import static reactor.core.publisher.Mono.just;

@Configuration
public class WebAppStaticResourceServerConfiguration {

    private static final int DATA_BUFFER_SIZE = 10_048_576;
    private static final DefaultDataBufferFactory DATA_BUFFER_FACTORY = new DefaultDataBufferFactory();
    private static final String INDEX_FILE_NAME = "index.html.br";
    private static final String UI_URL_PATH = "/ui";
    private static final String CONTENT_ENCODING = "br";
    private static final String FILE_TYPE_OF_CONTENT_ENCODING = ".".concat(CONTENT_ENCODING);
    private static final String UI_BASE_PATH_IN_CLASSPATH = "ui";
    private static final ImmutableMap.Builder<String, Mono<ResponseEntity<Flux<DataBuffer>>>> cache = ImmutableMap.builder();
    private static final Duration CACHE_CONTROL_MAX_AGE = Duration.ofDays(365);

    @Bean
    public StaticResourceCache provideWebApplicationStaticResourcesCache() throws IOException {
        Map<String, HttpHeaders> httpHeadersMap = ImmutableMap.<String, HttpHeaders>builder()
                .put("html", getHttpHeaders(TEXT_HTML))
                .put("js", getHttpHeaders(parseMediaType("text/javascript")))
                .put("webp", getHttpHeaders(parseMediaType("image/webp")))
                .put("json", getHttpHeaders(APPLICATION_JSON))
                .put("ico", getHttpHeaders(parseMediaType("image/x-icon")))
                .put("jpeg", getHttpHeaders(IMAGE_JPEG))
                .put("png", getHttpHeaders(IMAGE_PNG))
                .put("gif", getHttpHeaders(IMAGE_GIF))
                .put("txt", getHttpHeaders(TEXT_PLAIN))
                .put("xml", getHttpHeaders(TEXT_XML))
                .put("pdf", getHttpHeaders(APPLICATION_PDF))
                .put("css", getHttpHeaders(parseMediaType("text/css")))
                .build();
        loadAllWebResourcesInCache(httpHeadersMap);
        return new StaticResourceCache(cache.build());
    }

    private void loadAllWebResourcesInCache(Map<String, HttpHeaders> httpHeadersMap) throws IOException {
        File uiResourcesDir = new ClassPathResource(UI_BASE_PATH_IN_CLASSPATH).getFile();
        try (Stream<Path> pathStream = walk(of(uiResourcesDir.getAbsolutePath()))) {
            pathStream.filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        final String fileExtension = getFileExtension(filePath.toString());
                        String filePathRelativeToUiFolder = filePath.toString()
                                .replace(uiResourcesDir.getParent(), "")
                                .replace("\\", "/")
                                .replace(FILE_TYPE_OF_CONTENT_ENCODING, "");
                        final ResponseEntity<Flux<DataBuffer>> responseEntity = ok()
                                .headers(httpHeadersMap.get(fileExtension))
                                .body(read(filePath, DATA_BUFFER_FACTORY, DATA_BUFFER_SIZE).cache());
                        cache.put(filePathRelativeToUiFolder, just(responseEntity));
                        if (filePath.endsWith(INDEX_FILE_NAME)) {
                            cache.put(UI_URL_PATH, just(responseEntity));
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

    private String getFileExtension(String fileSystemPath) {
        final String[] filePathContent = fileSystemPath.split("[.]");
        return filePathContent[filePathContent.length - 2];
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
