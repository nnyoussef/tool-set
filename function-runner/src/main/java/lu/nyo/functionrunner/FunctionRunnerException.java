package lu.nyo.functionrunner;

import com.google.common.collect.ImmutableMap;
import lu.nyo.functionrunner.interfaces.Context;

import java.util.Map;

public class FunctionRunnerException extends RuntimeException {
    private final transient Object dataResult;
    private final transient ImmutableMap<String, Object> nextStepArgs;

    private final transient Context context;

    private final transient Exception exceptionToHandle;

    public FunctionRunnerException(Object dataResult,
                                   ImmutableMap<String, Object> nextStepArgs,
                                   Context context,
                                   Exception exceptionToHandle) {
        this.dataResult = dataResult;
        this.nextStepArgs = nextStepArgs;
        this.context = context;
        this.exceptionToHandle = exceptionToHandle;
    }

    public Object getDataResult() {
        return dataResult;
    }

    public Map<String, Object> getNextStepArgs() {
        return nextStepArgs;
    }

    public Context getContext() {
        return context;
    }

    public Exception getExceptionToHandle() {
        return exceptionToHandle;
    }
}
