package lu.nyo.functionrunner;

import com.google.common.collect.ImmutableMap;

import java.util.function.Function;

public class FunctionExecutionController {

    public static <T> T run(final Context context,
                            final FunctionIterator functionIterator,
                            final ImmutableMap<String, Object> args,
                            final Function<Object, T> outputConvertionFunction) {

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
        if (outputConvertionFunction != null)
            return outputConvertionFunction.apply(executionUnitOutput.getIntermediateResults());
        else
            return ((T) executionUnitOutput.getIntermediateResults());
    }

}
