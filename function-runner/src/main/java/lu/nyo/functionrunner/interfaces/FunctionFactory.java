package lu.nyo.functionrunner.interfaces;

public abstract class FunctionFactory {
    private static final ClassNotFoundException IMPL_NOT_FOUND = new ClassNotFoundException("Class couldn't be resolved");

    public abstract <T extends ExecutionUnit> T resolve(Class<? extends ExecutionUnit> tClass);

    public final <T extends ExecutionUnit> T get(Class<? extends ExecutionUnit> tClass) throws ClassNotFoundException {
        try {
            T t = resolve(tClass);
            if (t == null)
                throw IMPL_NOT_FOUND;
            return t;
        } catch (Exception e) {
            throw IMPL_NOT_FOUND;
        }
    }

}