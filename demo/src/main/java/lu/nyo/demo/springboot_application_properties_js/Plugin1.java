package lu.nyo.demo.springboot_application_properties_js;

import lu.nyo.sbjsconfig.Plugin;

public class Plugin1 implements Plugin<String, String> {
    @Override
    public String getName() {
        return "getData";
    }

    @Override
    public String apply(String s) {
        return s.toUpperCase();
    }
}
