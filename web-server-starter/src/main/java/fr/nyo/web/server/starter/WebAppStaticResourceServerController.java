package fr.nyo.web.server.starter;

import com.google.common.collect.ImmutableMap;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ui")
public final class WebAppStaticResourceServerController {

    private static final String UI_URL_PATH = "/ui";

    private final ImmutableMap<String, Mono<ResponseEntity<Flux<DataBuffer>>>> cache;

    public WebAppStaticResourceServerController(ImmutableMap<String, Mono<ResponseEntity<Flux<DataBuffer>>>> cache) {
        this.cache = cache;
    }

    @GetMapping("**")
    public Mono<ResponseEntity<Flux<DataBuffer>>> downloadFile(ServerWebExchange serverWebExchange) {
        final String requestPath = serverWebExchange.getRequest().getPath().toString();
        final Mono<ResponseEntity<Flux<DataBuffer>>> response = cache.get(requestPath);
        if (response != null)
            return response;
        else return cache.get(UI_URL_PATH);
    }

}
