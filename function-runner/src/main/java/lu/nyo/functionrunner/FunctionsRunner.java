package lu.nyo.functionrunner;

import lu.nyo.functionrunner.dto.State;
import lu.nyo.functionrunner.interfaces.Context;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;
import lu.nyo.functionrunner.interfaces.FunctionFactory;

import java.util.LinkedList;

import static java.util.Optional.ofNullable;

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
                               Context context,
                               LinkedList<Class<? extends ExecutionUnit<?>>> executionUnits) {

        State state = new State();

        ExecutionUnit<Object> executionUnit = functionFactory.create(executionUnits.pop());

        executionUnit.execute(executionUnit.adapt(firstInput, context), state, context);

        while (!executionUnits.isEmpty()) {
            if (state.getPostAction() == null)
                throw new UnsupportedOperationException();
            switch (state.getPostAction()) {
                case CONTINUE -> {
                    Class<? extends ExecutionUnit<?>> clazz = executionUnits.pop();
                    executionUnit = functionFactory.create(clazz);
                    executionUnit.execute(executionUnit.adapt(state.getResultToTransfer(), context), state, context);
                }
                default -> executionUnits.clear();
            }

        }

        return ofNullable(state.getResultToTransfer()).map(d -> ((T) d)).orElse(defaultIfNull);
    }
}
