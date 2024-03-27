package lu.nyo.functionrunner.interfaces;

import static java.lang.String.format;

public abstract class FunctionFactory<TOKEN> {

    protected abstract <T extends ExecutionUnit> T resolve(TOKEN tClass);

    public final <T extends ExecutionUnit> T get(TOKEN tClass) throws ClassNotFoundException {
        try {
            T t = resolve(tClass);
            if (t == null)
                throw new ClassNotFoundException("Class couldn't be resolved");
            return t;
        } catch (Exception e) {
            throw new ClassNotFoundException(format("%s Cannot resolve %s", this.getClass().getCanonicalName(), tClass));
        }
    }

}