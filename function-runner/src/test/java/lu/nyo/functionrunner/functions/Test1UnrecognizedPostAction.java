package lu.nyo.functionrunner.functions;

import lu.nyo.functionrunner.interfaces.Context;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;
import lu.nyo.functionrunner.dto.State;

public class Test1UnrecognizedPostAction implements ExecutionUnit<String> {

    @Override
    public String adapt(Object data, Context context) {
        return data.toString();
    }

    @Override
    public void execute(String input, State state, Context context) {
        Integer abc = input.length() * 10;
        state.setState(abc, null);
    }
}
