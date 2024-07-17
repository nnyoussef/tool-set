package lu.nyo.sbjsconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {JsPropertiesImporter.class})
public class SpringBootWithJsPropertiesApplicationTests {

    @Value("${server.port}")
    private String port;

    public void test1() {
        System.out.println(port);
    }

}
