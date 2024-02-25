package lu.nyo.functionrunner.predefined.functionfactory;

import lu.nyo.functionrunner.interfaces.ExecutionUnit;
import lu.nyo.functionrunner.interfaces.FunctionFactory;
import lu.nyo.functionrunner.predefined.executionunits.ExecutionUnitFunctionSelector;

public class DefaultFunctionFactory extends FunctionFactory {
    private static final ExecutionUnitFunctionSelector EXECUTION_UNIT_FUNCTION_SELECTOR = new ExecutionUnitFunctionSelector();


    @Override
    public <T extends ExecutionUnit> T create(Class<? extends ExecutionUnit> tClass) {
        if (tClass == ExecutionUnitFunctionSelector.class)
            return (T) EXECUTION_UNIT_FUNCTION_SELECTOR;
        else return null;
    }
}


