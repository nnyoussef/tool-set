package lu.nyo.functionrunner;

import lu.nyo.functionrunner.enums.PostAction;
import lu.nyo.functionrunner.interfaces.Context;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;

import java.util.LinkedList;
import java.util.Map;

import static java.util.Optional.ofNullable;

public final class FunctionsRunner {

    private final Context context;

    private FunctionsRunner() throws IllegalAccessException {
        throw new IllegalAccessException("");
    }

    private FunctionsRunner(Context context) {
        this.context = context;
    }

    public static FunctionsRunner create(Context context) {
        return new FunctionsRunner(context);
    }

    public <T> T runWithResult(Object firstInput,
                               T defaultIfNull,
                               LinkedList<Class<? extends ExecutionUnit<?>>> executionUnits) {

        ExecutionUnitOutput executionUnitOutput = new ExecutionUnitOutput();
        executionUnitOutput.setOutput(firstInput, PostAction.CONTINUE, Map.of());

        ExecutionUnit<Object> executionUnit = null;

        while (!executionUnits.isEmpty()) {
            if (executionUnitOutput.getPostAction() == null)
                throw new UnsupportedOperationException("Unsupported Post Action");
            switch (executionUnitOutput.getPostAction()) {
                case CONTINUE -> {
                    Class<? extends ExecutionUnit<?>> clazz = executionUnits.pop();
                    executionUnit = (ExecutionUnit<Object>) context.getFunctionFactory().get(clazz);
                    executionUnit.execute(executionUnit.adapt(executionUnitOutput.getResultToTransfer(), context, executionUnitOutput.getNextStepArgs()), context, executionUnitOutput, executionUnitOutput.getNextStepArgs());
                    if (executionUnitOutput.getPostAction() == PostAction.REPEAT)
                        executionUnits.push(clazz);
                }
                case REPEAT -> {
                    executionUnit.execute(executionUnit.adapt(executionUnitOutput.getResultToTransfer(), context, executionUnitOutput.getNextStepArgs()), context, executionUnitOutput, executionUnitOutput.getNextStepArgs());
                }
                default -> executionUnits.clear();
            }
        }
        return ofNullable(executionUnitOutput.getResultToTransfer()).map(d -> ((T) d)).orElse(defaultIfNull);
    }
}
