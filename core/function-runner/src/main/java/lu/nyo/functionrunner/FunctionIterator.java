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

    public abstract void handleReportedOutput(Context context,
                                                 ExecutionUnitOutput executionUnitOutput);

    public abstract void handleReportedException(Context context,
                                                    ExecutionUnitOutput executionUnitOutput,
                                                    Exception exception);

    public abstract boolean hasNext();

    public abstract FunctionExecutionUnit<Object> next();
}
