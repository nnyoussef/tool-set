package lu.nyo.demo.springboot_application_properties_js;

import lu.nyo.sbjsconfig.Plugin;

public class Plugin2 implements Plugin<String, String> {

    @Override
    public String getName() {
        return "getData1";
    }

    @Override
    public String apply(String s) {
        return s.substring(6);
    }
}
