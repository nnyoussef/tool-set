package lu.nyo.sbjsconfig;

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
