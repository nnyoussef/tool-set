package lu.nyo.functionrunner;

import lu.nyo.functionrunner.dto.State;
import lu.nyo.functionrunner.enums.PostAction;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;
import lu.nyo.functionrunner.interfaces.FunctionFactory;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;

public final class FunctionsRunner {

    private final FunctionFactory functionFactory;

    private FunctionsRunner() throws IllegalAccessException {
        throw new IllegalAccessException("");
    }

    private FunctionsRunner(FunctionFactory functionFactory) {
        this.functionFactory = functionFactory;
    }

    public static FunctionsRunner create(FunctionFactory functionFactory) {
        return new FunctionsRunner(functionFactory);
    }

    public <T> T runWithResult(Object firstInput,
                               T defaultIfNull,
                               LinkedList<Class<? extends ExecutionUnit<?>>> executionUnits) {

        State state = new State();

        ExecutionUnit<Object> executionUnit = functionFactory.create(executionUnits.pop());

        executionUnit.execute(executionUnit.adapt(firstInput), state);

        while (!executionUnits.isEmpty()) {
            try {
                if (Objects.requireNonNull(state.getPostAction()) == PostAction.STOP) {
                    executionUnits = new LinkedList<>();
                } else {
                    Class<? extends ExecutionUnit<?>> clazz = executionUnits.pop();
                    executionUnit = functionFactory.create(clazz);
                    executionUnit.execute(executionUnit.adapt(state.getResultToTransfer()), state);
                }
            } catch (NullPointerException e) {
                throw new UnsupportedOperationException();
            }

        }

        return Optional.ofNullable(state.getResultToTransfer()).map(d -> ((T) d)).orElse(defaultIfNull);
    }
}
