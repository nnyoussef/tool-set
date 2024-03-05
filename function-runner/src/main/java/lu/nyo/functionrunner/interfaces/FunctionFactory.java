package lu.nyo.functionrunner.interfaces;

import static java.lang.String.format;

public abstract class FunctionFactory {

    protected abstract <T extends ExecutionUnit> T resolve(Class<? extends ExecutionUnit> tClass);

    public final <T extends ExecutionUnit> T get(Class<? extends ExecutionUnit> tClass) throws ClassNotFoundException {
        try {
            T t = resolve(tClass);
            if (t == null)
                throw new ClassNotFoundException("Class couldn't be resolved");
            return t;
        } catch (Exception e) {
            throw new ClassNotFoundException(format("%s Cannot resolve %s", this.getClass().getCanonicalName(), tClass.getName()));
        }
    }

}