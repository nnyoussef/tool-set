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

import static reactor.core.scheduler.Schedulers.boundedElastic;

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
        return Mono.just(serverWebExchange)
                .map(request -> serverWebExchange.getRequest().getPath().toString())
                .flatMap(requestPath -> cache.getOrDefault(requestPath, cache.get(UI_URL_PATH)))
                .subscribeOn(boundedElastic());
    }

}
