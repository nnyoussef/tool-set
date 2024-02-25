package lu.nyo.functionrunner.interfaces;

public abstract class FunctionFactory {

    public abstract <T extends ExecutionUnit> T create(Class<? extends ExecutionUnit> tClass);

    public <T extends ExecutionUnit> T get(Class<? extends ExecutionUnit> tClass) {
        Object t = create(tClass);
        if (t == null) {
            throw new UnsupportedOperationException("ExecutionUnit definition not found");
        }
        return (T) t;
    }
}
