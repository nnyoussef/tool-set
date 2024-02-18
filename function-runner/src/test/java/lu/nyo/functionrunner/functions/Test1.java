package lu.nyo.functionrunner.functions;

import lu.nyo.functionrunner.interfaces.ExecutionUnit;
import lu.nyo.functionrunner.dto.State;

import static lu.nyo.functionrunner.enums.PostAction.CONTINUE;

public class Test1 implements ExecutionUnit<String> {

    @Override
    public String adapt(Object data) {
        return data.toString();
    }

    @Override
    public void execute(String input, State state) {
        Integer abc = input.length() * 10;
        state.setState(abc, CONTINUE);
    }
}
