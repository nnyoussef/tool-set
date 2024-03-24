package lu.nyo.functionrunner;

import com.google.common.collect.ImmutableMap;
import lu.nyo.functionrunner.interfaces.Context;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static lu.nyo.functionrunner.enums.PostAction.CONTINUE;

public final class FunctionsRunner {

    private final Context context;

    private FunctionsRunner() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    private FunctionsRunner(Context context) {
        this.context = context;
    }

    public static FunctionsRunner create(Context context) {
        return new FunctionsRunner(context);
    }

    public <T> T runWithResult(final Object firstInput,
                               final T defaultIfNull,
                               final Class<? extends ExecutionUnit<FunctionRunnerException>> fallback,
                               final Class<? extends ExecutionUnit<?>>... executionUnitChain) {

        ExecutionUnitOutput executionUnitOutput = new ExecutionUnitOutput();
        executionUnitOutput.setOutput(firstInput, CONTINUE, of());

        ExecutionUnit<Object> executionUnit;

        try {
            for (Class<? extends ExecutionUnit<?>> clazz : executionUnitChain) {
                checkIfValidPostAction(executionUnitOutput);
                if (requireNonNull(executionUnitOutput.getPostAction()) == CONTINUE) {
                    executionUnit = (ExecutionUnit<Object>) context.getFunctionFactory().get(clazz);
                    runFunction(executionUnitOutput, executionUnit);
                } else {
                    break;
                }
            }
            return ofNullable(executionUnitOutput.getResultToTransfer()).map(d -> ((T) d)).orElse(defaultIfNull);
        } catch (Exception e) {
            try {
                throw getExceptionToThrow(e, executionUnitOutput, fallback, context);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    private void runFunction(ExecutionUnitOutput executionUnitOutput,
                             ExecutionUnit<Object> executionUnit) {
        Object outputResults = executionUnitOutput.getResultToTransfer();
        Object adaptedObject = executionUnit.adapt(outputResults, context, executionUnitOutput.getNextStepArgs());
        executionUnit.execute(adaptedObject, context, executionUnitOutput, executionUnitOutput.getNextStepArgs());
    }

    private void checkIfValidPostAction(ExecutionUnitOutput executionUnitOutput) {
        if (executionUnitOutput.getPostAction() == null)
            throw new UnsupportedOperationException("Unsupported Post Action");
    }

    private RuntimeException getExceptionToThrow(Exception e,
                                                 ExecutionUnitOutput executionUnitOutput,
                                                 Class<? extends ExecutionUnit<FunctionRunnerException>> fallback,
                                                 Context context) throws ClassNotFoundException {
        if (e instanceof UnsupportedOperationException)
            return (UnsupportedOperationException) e;
        Object outputResults = executionUnitOutput.getResultToTransfer();
        ImmutableMap<String, Object> nextStepArgs = executionUnitOutput.getNextStepArgs();
        FunctionRunnerException functionRunnerException = new FunctionRunnerException(outputResults, nextStepArgs, context, e);
        context.getFunctionFactory().get(fallback).execute(functionRunnerException, context, null, null);
        return functionRunnerException;

    }

}
