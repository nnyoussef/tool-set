package lu.nyo.functionrunner;

import lu.nyo.functionrunner.functions.*;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;
import lu.nyo.functionrunner.interfaces.FunctionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Map;

import static java.util.Map.of;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@ExtendWith(MockitoExtension.class)
public class FunctionsRunnerTest {

    FunctionFactory functionFactory = new FunctionFactory() {
        @Override
        public <T> T create(Class<?> tClass) {
            if (tClass == Test1.class)
                return (T) new Test1();
            else if (tClass == Test1UnrecognizedPostAction.class) {
                return (T) new Test1UnrecognizedPostAction();
            } else if (tClass == Test2.class) {
                return (T) new Test2();
            } else if (tClass == Test2Stop.class)
                return (T) new Test2Stop();
            else
                return (T) new Test3();
        }
    };

    private static final Map<String, String> DATA = of("abc", "123");

    @Test
    public void functionRunnerDefaultConstructorAccessTest() {
        Constructor<?> constructor = FunctionsRunner.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Assertions.assertEquals(e.getCause().getClass(), IllegalAccessException.class);
        }
    }

    @Test
    public void test1() {
        FunctionsRunner functionsRunner = FunctionsRunner.create(functionFactory);

        LinkedList<Class<? extends ExecutionUnit<?>>> classLinkedHashMap = new LinkedList<>() {{
            add(Test1.class);
            add(Test2.class);
        }};


        Integer a = functionsRunner.runWithResult(DATA,
                0,
                classLinkedHashMap);

        Assertions.assertEquals(a, 0);
    }

    @Test
    public void test2() {

        FunctionsRunner functionsRunner = FunctionsRunner.create(functionFactory);

        LinkedList<Class<? extends ExecutionUnit<?>>> classLinkedHashMap = new LinkedList<>() {{
            add(Test1.class);
            add(Test2Stop.class);
            add(Test3.class);
        }};

        Integer a = functionsRunner.runWithResult(DATA,
                0,
                classLinkedHashMap);

        Assertions.assertEquals(a, 0);
    }

    @Test
    public void test3() {

        FunctionsRunner functionsRunner = FunctionsRunner.create(functionFactory);

        LinkedList<Class<? extends ExecutionUnit<?>>> classLinkedHashMap = new LinkedList<>() {{
            add(Test1UnrecognizedPostAction.class);
            add(Test2.class);
        }};

        assertThrowsExactly(UnsupportedOperationException.class, () -> functionsRunner.runWithResult(DATA, 0, classLinkedHashMap));
    }
}
