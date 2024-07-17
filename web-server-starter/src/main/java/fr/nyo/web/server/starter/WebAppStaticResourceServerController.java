package fr.nyo.web.server.starter;

import fr.nyo.web.server.starter.WebAppStaticResourceServerConfiguration.StaticResourceCache;
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

    private final StaticResourceCache cache;

    public WebAppStaticResourceServerController(StaticResourceCache cache) {
        this.cache = cache;
    }

    @GetMapping("**")
    public Mono<ResponseEntity<Flux<DataBuffer>>> downloadFile(ServerWebExchange serverWebExchange) {
        return Mono.just(serverWebExchange)
                .map(request -> serverWebExchange.getRequest().getPath().toString())
                .flatMap(cache::get)
                .subscribeOn(boundedElastic());
    }

}
