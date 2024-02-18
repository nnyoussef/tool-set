package lu.nyo.functionrunner.interfaces;

public interface FunctionFactory {

    <T> T create(Class<?> tClass);

}
