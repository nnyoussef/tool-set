package lu.nyo.functionrunner;

import com.google.common.collect.ImmutableMap;

public final class ExecutionUnitOutput {
    private final Object intermediateResults;
    private final ImmutableMap<String, Object> intermediateArgs;

    private ExecutionUnitOutput(Object intermediateResults,
                                ImmutableMap<String, Object> intermediateArgs) {
        this.intermediateResults = intermediateResults;
        this.intermediateArgs = intermediateArgs;
    }

    public static ExecutionUnitOutput create(Object intermediateResults,
                                             ImmutableMap<String, Object> nextArgs) {
        return new ExecutionUnitOutput(intermediateResults, nextArgs);
    }

    public Object getIntermediateResults() {
        return intermediateResults;
    }

    public ImmutableMap<String, Object> getIntermediateArgs() {
        return intermediateArgs;
    }
}
