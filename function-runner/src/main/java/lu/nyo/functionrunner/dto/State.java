package lu.nyo.functionrunner.dto;

import lu.nyo.functionrunner.enums.PostAction;

public class State {
    private Object resultToTransfer;
    private PostAction postAction;
    private Object[] extraArg;

    public State() {
    }

    public void setState(Object resultToTransfer, PostAction postAction, Object... extraArg) {
        this.resultToTransfer = resultToTransfer;
        this.postAction = postAction;
        this.extraArg = extraArg;
    }

    public Object getResultToTransfer() {
        return resultToTransfer;
    }

    public PostAction getPostAction() {
        return postAction;
    }

    public Object[] getExtraArg() {
        return extraArg;
    }
}
