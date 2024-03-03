package lu.nyo.functionrunner.interfaces;

public abstract class FunctionFactory {

    public abstract <T extends ExecutionUnit> T get(Class<? extends ExecutionUnit> tClass);

}