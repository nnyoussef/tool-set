package lu.nyo.functionrunner.functions;

import lu.nyo.functionrunner.interfaces.ExecutionUnit;
import lu.nyo.functionrunner.dto.State;

import static lu.nyo.functionrunner.enums.PostAction.STOP;

public class Test2Stop implements ExecutionUnit<Integer> {

    @Override
    public void execute(Integer input, State state) {
        state.setState(input * 0, STOP);
    }

}
