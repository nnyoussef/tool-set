package lu.nyo.utils;


import java.util.function.BiFunction;

public abstract class AdaptiveInputBiFunction<I1, I2, O> implements BiFunction<Object, Object, O> {

    protected abstract I1 castInput1(Object o);

    protected abstract I2 castInput2(Object o);

    protected abstract O _apply(I1 input1, I2 input2);

    @Override
    public O apply(Object o, Object o2) {
        return _apply(castInput1(o), castInput2(o2));
    }
}
