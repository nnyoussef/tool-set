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
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.file.Files.walk;
import static java.nio.file.Path.of;
import static java.util.concurrent.TimeUnit.DAYS;
import static org.springframework.core.io.buffer.DataBufferUtils.read;
import static org.springframework.http.CacheControl.maxAge;
import static org.springframework.http.MediaType.*;
import static org.springframework.http.ResponseEntity.ok;
import static reactor.core.publisher.Mono.just;

@Configuration
public class WebAppStaticResourceServerConfiguration {

    private static final int DATA_BUFFER_SIZE = 10_048_576;
    private static final DefaultDataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
    private static final String INDEX_FILE_NAME = "index.html.br";
    private static final String UI_URL_PATH = "/ui";
    private static final String CONTENT_ENCODING = "br";
    private static final String CONTENT_ENCODING_FILE_EXTENSION = ".".concat(CONTENT_ENCODING);
    private static final String UI_BASE_PATH_IN_CLASSPATH = "ui";

    private final ImmutableMap.Builder<String, Mono<ResponseEntity<Flux<DataBuffer>>>> cache = ImmutableMap.builder();

    @Bean()
    public WebAppStaticResourceServerController provideWebAppStaticResourceServerController() throws IOException {
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
        return new WebAppStaticResourceServerController(cache.build());
    }

    private void loadAllWebResourcesInCache(Map<String, HttpHeaders> httpHeadersMap) throws IOException {
        File uiResourcesDir = new ClassPathResource(UI_BASE_PATH_IN_CLASSPATH).getFile();
        try (Stream<Path> pathStream = walk(of(uiResourcesDir.getAbsolutePath()))) {
            pathStream.filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        final String fileExtension = getFileExtension(filePath.toString());
                        String filePathWithUiFolderAsParent = filePath.toString()
                                .replace(uiResourcesDir.getParent(), "")
                                .replace("\\", "/")
                                .replace(CONTENT_ENCODING_FILE_EXTENSION, "");
                        final ResponseEntity<Flux<DataBuffer>> responseEntity = ok()
                                .headers(httpHeadersMap.get(fileExtension))
                                .body(read(filePath, dataBufferFactory, DATA_BUFFER_SIZE).cache());
                        cache.put(filePathWithUiFolderAsParent, just(responseEntity));
                        if (filePath.endsWith(INDEX_FILE_NAME)) {
                            cache.put(UI_URL_PATH, just(responseEntity));
                        }
                    });
        }
    }

    private HttpHeaders getHttpHeaders(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.set("Content-Encoding", "br");
        headers.setCacheControl(maxAge(365, DAYS));
        return headers;
    }

    private String getFileExtension(String fileSystemPath) {
        final String[] filePathContent = fileSystemPath.split("[.]");
        return filePathContent[filePathContent.length - 2];
    }
}