package lu.nyo.functionrunner;

import java.util.Iterator;

public abstract class FunctionManager implements Iterator<FunctionExecutionUnit<Object>> {

    public final void reportIntermediateResults(Context context,
                                                ExecutionUnitOutput executionUnitOutput) {
        handleReportedOutput(context, executionUnitOutput);
    }

    public final void reportException(Context context,
                                      ExecutionUnitOutput executionUnitOutput,
                                      Exception exception) {
        handleReportedException(context, executionUnitOutput, exception);
    }

    protected abstract void handleReportedOutput(Context context,
                                                 ExecutionUnitOutput executionUnitOutput);

    protected abstract void handleReportedException(Context context,
                                                    ExecutionUnitOutput executionUnitOutput,
                                                    Exception exception);
}
