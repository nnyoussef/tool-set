package lu.nyo.webfluxstarter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import static java.nio.channels.FileChannel.MapMode.READ_WRITE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.EnumSet.of;

@RestController
@RequestMapping("/ops")
public class Operation {

    @Autowired
    @Qualifier("routerConfigs")
    Map<String, Map<String, String>> endPointsConfigs;

    @Autowired
    @Qualifier("handlerFunctions")
    HashMap<String, HandlerFunction<ServerResponse>> handlerFunctionHashMap;


    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;

    private static final Logger LOOGER = LoggerFactory.getLogger(Operation.class);

    @PostMapping(path = "/add-end-point", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    void create(@RequestBody List<RouteConfig> routeConfigs) throws IOException {
        routeConfigs.forEach(routeConfig -> {
            Map<String, String> pathToHandlerFunctionMap = new HashMap<>();
            pathToHandlerFunctionMap.put(routeConfig.getPath(), routeConfig.getHandlerClass());
            endPointsConfigs.putIfAbsent(routeConfig.getMethod(), HashMap.newHashMap(100));
            endPointsConfigs.get(routeConfig.getMethod()).putAll(pathToHandlerFunctionMap);
        });
    }

    @PostMapping(value = "/install/classes")
    void installJars(@RequestPart("jars") Part jars) throws IOException {
        Path target = Path.of("C:\\Users\\nassi\\AquaProjects\\spring-boot-dynamic-api-creation\\src\\main\\resources\\data.jar");

        jars.content().subscribe(s -> {
            MappedByteBuffer mappedByteBuffer = getMappedByteBuffer(target, s.capacity());
            s.toByteBuffer(mappedByteBuffer);
            mappedByteBuffer.force();
        });

        URLClassLoader classLoader = new URLClassLoader(new URL[]{new File(target.toString()).toURI().toURL()});
        ServiceLoader<HandlerFunction> s = ServiceLoader.load(HandlerFunction.class, classLoader);

        s.forEach(ss -> {
            GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
            genericBeanDefinition.setBeanClass(ss.getClass());

            String beanName = ss.getClass().getName();
            if (defaultListableBeanFactory.containsBeanDefinition(beanName))
                defaultListableBeanFactory.removeBeanDefinition(beanName);

            defaultListableBeanFactory.registerBeanDefinition(beanName, genericBeanDefinition);
            handlerFunctionHashMap.put(ss.getClass().getCanonicalName(), beanFactory.getBean(beanName, ss.getClass()));
        });

    }

    private MappedByteBuffer getMappedByteBuffer(Path target,
                                                 Integer capacity) {
        MappedByteBuffer mappedByteBuffer = null;
        try {
            mappedByteBuffer = ((FileChannel) Files.newByteChannel(target, of(READ, WRITE))).map(READ_WRITE, 0, capacity);
        } catch (IOException ignored) {
        }
        return mappedByteBuffer;
    }
}
