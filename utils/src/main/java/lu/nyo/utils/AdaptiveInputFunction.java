package lu.nyo.utils;


import java.util.function.Function;

public abstract class AdaptiveInputFunction<I, O> implements Function<Object, O> {

    protected abstract I cast(Object o);

    protected abstract O _apply(I input);

    @Override
    public final O apply(Object o) {
        return _apply(cast(o));
    }
}
