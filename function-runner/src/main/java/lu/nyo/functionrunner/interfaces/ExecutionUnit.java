package lu.nyo.functionrunner.interfaces;

import com.google.common.collect.ImmutableMap;
import lu.nyo.functionrunner.ExecutionUnitOutput;

public interface ExecutionUnit<T> {

    default T adapt(Object data,
                    Context context,
                    ImmutableMap<String, Object> args) {
        return (T) data;
    }

    void execute(T input,
                 Context context,
                 ExecutionUnitOutput executionUnitOutput,
                 ImmutableMap<String, Object> args);

}
