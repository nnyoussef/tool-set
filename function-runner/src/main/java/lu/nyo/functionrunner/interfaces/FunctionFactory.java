package lu.nyo.functionrunner.interfaces;

import static java.lang.String.format;

public abstract class FunctionFactory {

    public abstract <T extends ExecutionUnit> T create(Class<? extends ExecutionUnit> tClass);

    public <T extends ExecutionUnit> T get(Class<? extends ExecutionUnit> tClass) {
        Object t = create(tClass);
        if (t == null) {
            throw new UnsupportedOperationException(format("Class %s cannot be resolved by the function factory %s", tClass.getName(), getClass().getName()));
        }
        return (T) t;
    }
}