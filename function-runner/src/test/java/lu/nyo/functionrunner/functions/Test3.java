package lu.nyo.functionrunner.functions;

import lu.nyo.functionrunner.interfaces.ExecutionUnit;
import lu.nyo.functionrunner.dto.State;

public class Test3 implements ExecutionUnit<Integer> {

    @Override
    public void execute(Integer input, State state) {
        String a = null;
        a.length();
    }

}
