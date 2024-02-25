package lu.nyo.functionrunner;

import lu.nyo.functionrunner.enums.PostAction;

import java.util.Map;
import java.util.Optional;

public class ExecutionUnitOutput {
    private Object resultToTransfer;
    private PostAction postAction;
    private Map<String, Object> nextStepArgs;

    public void setOutput(Object resultToTransfer,
                          PostAction postAction,
                          Map<String, Object> nextStepArgs) {
        this.resultToTransfer = resultToTransfer;
        this.postAction = postAction;
        this.nextStepArgs = Optional.ofNullable(nextStepArgs).orElse(Map.of());
    }

    Object getResultToTransfer() {
        return resultToTransfer;
    }

    PostAction getPostAction() {
        return postAction;
    }

    Map<String, Object> getNextStepArgs() {
        return nextStepArgs;
    }

}
