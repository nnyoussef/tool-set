package lu.nyo.functionrunner;

import lu.nyo.functionrunner.functions.*;
import lu.nyo.functionrunner.interfaces.Context;
import lu.nyo.functionrunner.interfaces.ExecutionUnit;
import lu.nyo.functionrunner.interfaces.FunctionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static java.util.Map.of;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@ExtendWith(MockitoExtension.class)
public class FunctionsRunnerTest {
    class TestFunctionFactory extends FunctionFactory {
        @Override
        public <T extends ExecutionUnit> T create(Class<? extends ExecutionUnit> tClass) {
            Map<Class, T> inits = new HashMap<>() {{
                put(Test1.class, (T) new Test1());
                put(Test1UnrecognizedPostAction.class, (T) new Test1UnrecognizedPostAction());
                put(Test2.class, (T) new Test2());
                put(Test2Stop.class, (T) new Test2Stop());
                put(Test3.class, (T) new Test3());
                put(Test4.class, (T) new Test4());
                put(Test5.class, (T) new Test5());
                put(Test6.class, (T) new Test6());
                put(Test7.class, (T) new Test7());
            }};
            return inits.getOrDefault(tClass, null);
        }
    }

    Context context = new Context() {
        @Override
        public FunctionFactory getFunctionFactory() {
            return new TestFunctionFactory();
        }
    };

    private static final Map<String, String> DATA = of("abc", "123");

    FunctionsRunner functionsRunner = FunctionsRunner.create(context);

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

        LinkedList<Class<? extends ExecutionUnit<?>>> classLinkedHashMap = new LinkedList<>() {{
            add(Test1UnrecognizedPostAction.class);
            add(Test2.class);
        }};

        assertThrowsExactly(UnsupportedOperationException.class, () -> functionsRunner.runWithResult(DATA, 0, classLinkedHashMap));
    }

    @Test
    public void test4() {
        LinkedList<Class<? extends ExecutionUnit<?>>> classLinkedHashMap = new LinkedList<>() {{
            add(Test4.class);
        }};
        Assertions.assertEquals(functionsRunner.runWithResult(0, 0, classLinkedHashMap), 4);
    }


}
