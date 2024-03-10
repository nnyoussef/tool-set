package lu.nyo.demo.springboot_application_properties_js;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SbApplicationPropertiesJs {

    public static void main(String[] args) {
        SbApplicationPropertiesJsService sbApplicationPropertiesJsService = SpringApplication.run(SbApplicationPropertiesJs.class).getBeanFactory().getBean(SbApplicationPropertiesJsService.class);
        System.out.println(sbApplicationPropertiesJsService.getVal());
    }
}
