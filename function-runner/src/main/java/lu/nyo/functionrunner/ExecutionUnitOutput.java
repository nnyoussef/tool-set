package lu.nyo.functionrunner;

import com.google.common.collect.ImmutableMap;
import lu.nyo.functionrunner.enums.PostAction;

import java.util.Optional;

public class ExecutionUnitOutput {
    private Object resultToTransfer;
    private PostAction postAction;
    private ImmutableMap<String, Object> nextStepArgs;

    public void setOutput(Object resultToTransfer,
                          PostAction postAction,
                          ImmutableMap<String, Object> nextStepArgs) {
        this.resultToTransfer = resultToTransfer;
        this.postAction = postAction;
        this.nextStepArgs = Optional.ofNullable(nextStepArgs).orElse(ImmutableMap.of());
    }

    Object getResultToTransfer() {
        return resultToTransfer;
    }

    PostAction getPostAction() {
        return postAction;
    }

    ImmutableMap<String, Object> getNextStepArgs() {
        return nextStepArgs;
    }

}
