package lu.nyo.functionrunner.predefined.functionfactories;

import lu.nyo.functionrunner.interfaces.ExecutionUnit;
import lu.nyo.functionrunner.interfaces.FunctionFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;

public class SBFunctionFactory implements FunctionFactory {

    DefaultListableBeanFactory defaultListableBeanFactory;

    public SBFunctionFactory(DefaultListableBeanFactory defaultListableBeanFactory) {
        this.defaultListableBeanFactory = defaultListableBeanFactory;
    }

    @Override
    public <T> T create(Class<?> tClass) {
        return (T) defaultListableBeanFactory.getBean(tClass);
    }

    public void registerNewFunction(Class<? extends ExecutionUnit<?>> classz) {
        if (defaultListableBeanFactory.containsBean(classz.getCanonicalName()))
            defaultListableBeanFactory.removeBeanDefinition(classz.getCanonicalName());
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(classz);
        defaultListableBeanFactory.registerBeanDefinition(classz.getCanonicalName(), genericBeanDefinition);
    }
}
