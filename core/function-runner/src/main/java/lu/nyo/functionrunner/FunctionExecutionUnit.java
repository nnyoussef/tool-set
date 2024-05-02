package lu.nyo.functionrunner;

import com.google.common.collect.ImmutableMap;

public interface FunctionExecutionUnit<T> {

    default T adapt(Context context,
                    Object data,
                    ImmutableMap<String, Object> args) {
        return (T) data;
    }

    ExecutionUnitOutput execute(Context context,
                                T input,
                                ImmutableMap<String, Object> args);

}
