package lu.nyo.functionrunner;

public abstract class FunctionIterator {

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

    protected abstract boolean hasNext();

    protected abstract FunctionExecutionUnit<Object> next();
}
