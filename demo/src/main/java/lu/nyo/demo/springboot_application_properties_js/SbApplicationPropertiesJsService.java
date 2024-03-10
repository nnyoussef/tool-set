package lu.nyo.demo.springboot_application_properties_js;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SbApplicationPropertiesJsService {

    @Value("${B.H.I.J}")
    private String val;

    public String getVal() {
        return val;
    }
}
