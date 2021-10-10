import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class TestRunner {

    private static final List<Class<?>> TESTS = List.of(CalculatorTest.class);

    public static void main(String[] args) throws Exception {

        List<String> passed = new ArrayList<>();
        List<String> failed = new ArrayList<>();

        for (Class<?> klass : TESTS) {

            if (!UnitTest.class.isAssignableFrom(klass)) {
                continue;
            }

            UnitTest unitTest = (UnitTest) klass.getConstructor().newInstance();

            for (final Method method : klass.getDeclaredMethods()) {
                if (null != method.getAnnotation(Test.class)) {
                    try {
                        unitTest.beforeEachTest();
                        method.invoke(unitTest);
                        unitTest.afterEachTest();
                        passed.add(getTestName(klass, method));
                    } catch (Throwable throwable) {
                        failed.add(getTestName(klass, method));
                    }

                }
            }
        }
        System.out.println("Passed tests: " + passed);
        System.out.println("FAILED tests: " + failed);
    }

    private static String getTestName (Class<?> klass, Method method){
        return klass.getName() + "#" + method.getName();
    }
}

