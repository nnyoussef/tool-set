package lu.nyo.functionrunner.interfaces;

import static java.lang.String.format;

public abstract class FunctionFactory<TOKEN> {

    protected abstract <T extends ExecutionUnit> T resolve(TOKEN token);

    public final <T extends ExecutionUnit> T get(TOKEN token) throws ClassNotFoundException {
        try {
            T t = resolve(token);
            if (t == null)
                throw new ClassNotFoundException("Class couldn't be resolved");
            return t;
        } catch (Exception e) {
            throw new ClassNotFoundException(format("%s Cannot resolve %s", this.getClass().getCanonicalName(), token));
        }
    }

}