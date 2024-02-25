package lu.nyo.functionrunner.interfaces;

import lu.nyo.functionrunner.ExecutionUnitOutput;

import java.util.Map;

public interface ExecutionUnit<T> {

    default T adapt(Object data,
                    Context context,
                    Map<String, Object> args) {
        return (T) data;
    }

    void execute(T input,
                 Context context,
                 ExecutionUnitOutput executionUnitOutput,
                 Map<String, Object> args);

}
