package lu.nyo.webfluxstarter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import static java.nio.file.Files.newInputStream;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
@ComponentScan(basePackages = "lu.nyo.webfluxstarter")
public class DynamicRouterConfigs {

    @Bean("routerConfigs")
    public Map<String, Map<String, String>> get() throws IOException {
        InputStream inputStream = newInputStream(Path.of("C:\\Users\\nassi\\AquaProjects\\spring-boot-dynamic-api-creation\\src\\main\\resources\\router.json"));
        List<RouteConfig> routeConfigs = new ObjectMapper().readValue(inputStream, new TypeReference<>() {
        });
        return routeConfigs.stream()
                .collect(Collectors.groupingBy(RouteConfig::getMethod,
                        Collectors.toMap(RouteConfig::getPath, RouteConfig::getHandlerClass, (e1, e2) -> e1, () -> new HashMap<>(100))));
    }

    @Bean(name = "handlerFunctions")
    public HashMap<String, HandlerFunction> getHandlerFunctionsFromServiceProvider() {
        return ServiceLoader.load(HandlerFunction.class)
                .stream()
                .collect(Collectors.toMap(e -> e.get().getClass().getCanonicalName(), e -> e.get(), (e1, e2) -> e1, () -> new HashMap<>(100)));
    }

    @Bean
    public RouterFunction<ServerResponse> configureDynamicRouterFunction() throws IOException {
        Map<String, Map<String, String>> routerConfiguration = get();
        HashMap<String, HandlerFunction> functionsMap = getHandlerFunctionsFromServiceProvider();
        RouterFunctions.Builder builder = RouterFunctions.route();
        builder.GET("/api/**", accept(MediaType.ALL), serverRequest -> {
            String url = serverRequest.path().replaceFirst("/api", "");
            String handlerClassName = routerConfiguration.get("GET").get(url);
            return functionsMap.get(handlerClassName).handle(serverRequest);
        });
        builder.POST("/api/**", accept(MediaType.ALL), serverRequest -> {
            String url = serverRequest.path().replaceFirst("/api", "");
            String handlerClassName = routerConfiguration.get("POST").get(url);
            return functionsMap.get(handlerClassName).handle(serverRequest);
        });
        builder.PUT("/api/**", accept(MediaType.ALL), serverRequest -> {
            String url = serverRequest.path().replaceFirst("/api", "");
            String handlerClassName = routerConfiguration.get("PUT").get(url);
            return functionsMap.get(handlerClassName).handle(serverRequest);
        });
        builder.DELETE("/api/**", accept(MediaType.ALL), serverRequest -> {
            String url = serverRequest.path().replaceFirst("/api", "");
            String handlerClassName = routerConfiguration.get("DELETE").get(url);
            return functionsMap.get(handlerClassName).handle(serverRequest);
        });
        builder.HEAD("/api/**", accept(MediaType.ALL), serverRequest -> {
            String url = serverRequest.path().replaceFirst("/api", "");
            String handlerClassName = routerConfiguration.get("HEAD").get(url);
            return functionsMap.get(handlerClassName).handle(serverRequest);
        });
        builder.OPTIONS("/api/**", accept(MediaType.ALL), serverRequest -> {
            String url = serverRequest.path().replaceFirst("/api", "");
            String handlerClassName = routerConfiguration.get("OPTIONS").get(url);
            return functionsMap.get(handlerClassName).handle(serverRequest);
        });
        builder.PATCH("/api/**", accept(MediaType.ALL), serverRequest -> {
            String url = serverRequest.path().replaceFirst("/api", "");
            String handlerClassName = routerConfiguration.get("PATCH").get(url);
            return functionsMap.get(handlerClassName).handle(serverRequest);
        });
        return builder.build();
    }
}
