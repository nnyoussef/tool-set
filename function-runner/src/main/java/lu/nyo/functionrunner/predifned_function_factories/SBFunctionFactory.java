package lu.nyo.functionrunner.predifned_function_factories;

import lu.nyo.functionrunner.interfaces.FunctionFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class SBFunctionFactory implements FunctionFactory {

    DefaultListableBeanFactory defaultListableBeanFactory;

    public SBFunctionFactory(DefaultListableBeanFactory defaultListableBeanFactory) {
        this.defaultListableBeanFactory = defaultListableBeanFactory;
    }

    @Override
    public <T> T create(Class<?> tClass) {
        return (T) defaultListableBeanFactory.getBean(tClass);
    }
}
