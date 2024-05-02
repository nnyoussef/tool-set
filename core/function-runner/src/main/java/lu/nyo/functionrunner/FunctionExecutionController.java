package lu.nyo.functionrunner;

import com.google.common.collect.ImmutableMap;

import java.util.function.Function;

public class FunctionExecutionController {

    private final FunctionManager functionManager;
    private final Context context;

    private FunctionExecutionController(FunctionManager functionManager,
                                        Context context) {
        this.functionManager = functionManager;
        this.context = context;
    }

    public static FunctionExecutionController create(FunctionManager functionManager,
                                                     Context context) {
        return new FunctionExecutionController(functionManager, context);
    }

    public <T> T run(final ImmutableMap<String, Object> args,
                     final Function<Object, T> outputConvertionFunction) {

        ExecutionUnitOutput executionUnitOutput = ExecutionUnitOutput.create(null, args);

        while (functionManager.hasNext()) {

            try {
                Object intermediateResults = executionUnitOutput.getIntermediateResults();
                ImmutableMap<String, Object> intermediateArgs = executionUnitOutput.getIntermediateArgs();

                functionManager.reportIntermediateResults(context, executionUnitOutput);
                FunctionExecutionUnit<Object> executionUnit = functionManager.next();

                Object adaptedIntermediateResults = executionUnit.adapt(context, intermediateResults, intermediateArgs);

                executionUnitOutput = executionUnit.execute(context, adaptedIntermediateResults, args);
            } catch (Exception exception) {

                functionManager.reportException(context, executionUnitOutput, exception);
            }
        }

        return outputConvertionFunction.apply(executionUnitOutput.getIntermediateResults());
    }

}
