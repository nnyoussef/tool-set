package lu.nyo.functionrunner.impl;

import com.google.common.collect.ImmutableMap;
import lu.nyo.functionrunner.*;

import java.util.Optional;
import java.util.function.Function;

public class DefaultFunctionExecutionController implements FunctionExecutionController {

    private static final DefaultFunctionExecutionController DEFAULT_FUNCTION_EXECUTION_CONTROLLER = new DefaultFunctionExecutionController();

    public static DefaultFunctionExecutionController getInstance() {
        return DEFAULT_FUNCTION_EXECUTION_CONTROLLER;
    }

    @Override
    public <T> T apply(Object... arguments) {

        final Context context = ((Context) arguments[0]);
        final FunctionIterator functionIterator = ((FunctionIterator) arguments[1]);
        final Function<Object, T> outputConvertionFunction = ((Function<Object, T>) arguments[2]);
        final ImmutableMap<String, Object> args = (ImmutableMap<String, Object>) arguments[3];

        ExecutionUnitOutput executionUnitOutput = ExecutionUnitOutput.create(null, args);
        while (functionIterator.hasNext()) {

            try {
                Object intermediateResults = executionUnitOutput.getIntermediateResults();
                ImmutableMap<String, Object> intermediateArgs = executionUnitOutput.getIntermediateArgs();

                functionIterator.reportIntermediateResults(context, executionUnitOutput);
                FunctionExecutionUnit<Object> executionUnit = functionIterator.next();

                Object adaptedIntermediateResults = executionUnit.adapt(context, intermediateResults, intermediateArgs);

                executionUnitOutput = executionUnit.execute(context, adaptedIntermediateResults, args);
            } catch (Exception exception) {
                functionIterator.reportException(context, executionUnitOutput, exception);
            }
        }

        ExecutionUnitOutput finalExecutionUnitOutput = executionUnitOutput;
        return Optional.ofNullable(outputConvertionFunction)
                .map(f -> f.apply(finalExecutionUnitOutput.getIntermediateResults()))
                .orElse(((T) executionUnitOutput.getIntermediateResults()));

    }

    @Override
    public String getInstanceId() {
        return getClass().getName();
    }
}
