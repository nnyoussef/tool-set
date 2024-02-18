package lu.nyo.functionrunner.interfaces;

import lu.nyo.functionrunner.dto.State;

public interface ExecutionUnit<T> {

    default T adapt(Object data) {
        return (T) data;
    }

    void execute(T input, State state);

}
