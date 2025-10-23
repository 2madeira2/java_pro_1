package ru.javapro.runner;

import ru.javapro.annotations.AfterSuite;
import ru.javapro.annotations.AfterTest;
import ru.javapro.annotations.BeforeSuite;
import ru.javapro.annotations.BeforeTest;
import ru.javapro.annotations.CsvSource;
import ru.javapro.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestRunner {

    public static void runTests(Class<?> clazz) throws Exception {
        Method beforeSuite = null;
        Method afterSuite = null;
        List<Method> testMethods = new ArrayList<>();
        List<Method> beforeTestMethods = new ArrayList<>();
        List<Method> afterTestMethods = new ArrayList<>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeSuite != null) throw new IllegalStateException("Ошибка!!! В тестовом классе не может быть больше одного метода с аннотацией @BeforeSuite!!!");
                validateIsStatic(method);
                beforeSuite = method;
            } else if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterSuite != null) throw new IllegalStateException("Ошибка!!! В тестовом классе не может быть больше одного метода с аннотацией @AfterSuite!!!");
                validateIsStatic(method);
                afterSuite = method;
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            } else if (method.isAnnotationPresent(BeforeTest.class)) {
                beforeTestMethods.add(method);
            } else if (method.isAnnotationPresent(AfterTest.class)) {
                afterTestMethods.add(method);
            }
        }

        if (testMethods.isEmpty()) {
            System.out.println("Ошибка!!! В тестовом классе " + clazz.getSimpleName() + " не найдено тестовых методов.");
            return;
        }

        testMethods.sort(Comparator.comparingInt((Method m) -> m.getAnnotation(Test.class).priority()).reversed());

        Object instance = clazz.getConstructor().newInstance();

        executeLifecycle(instance, beforeSuite, afterSuite, testMethods, beforeTestMethods, afterTestMethods);
    }


    private static void executeLifecycle(Object instance, Method beforeSuite, Method afterSuite, List<Method> tests,
                                         List<Method> beforeEachMethods, List<Method> afterEachMethods) throws IllegalAccessException, InvocationTargetException {
        try {
            if (beforeSuite != null) {
                beforeSuite.invoke(null);
            }

            for (Method testMethod : tests) {
                for (Method beforeEach : beforeEachMethods) {
                    beforeEach.invoke(instance);
                }

                runSingleTest(instance, testMethod);

                for (Method afterEach : afterEachMethods) {
                    afterEach.invoke(instance);
                }
            }
        } finally {
            if (afterSuite != null) {
                afterSuite.invoke(null);
            }
        }
    }

    private static void runSingleTest(Object instance, Method testMethod) throws InvocationTargetException, IllegalAccessException {
        if (testMethod.isAnnotationPresent(CsvSource.class)) {
            runParametrizedTest(instance, testMethod);
        } else {
            testMethod.invoke(instance);
        }
    }

    private static void runParametrizedTest(Object instance, Method method) throws InvocationTargetException, IllegalAccessException {
        CsvSource source = method.getAnnotation(CsvSource.class);
        String[] params = source.value().split("\\s*,\\s*");
        Class<?>[] paramTypes = method.getParameterTypes();

        if (params.length != paramTypes.length) {
            throw new IllegalArgumentException(String.format(
                    "Ошибка! Аннотация @CsvSource содержит %d значений, а метод %s принимает %d параметров.",
                    params.length, method.getName(), paramTypes.length
            ));
        }

        Object[] args = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            args[i] = convertStringToType(params[i], paramTypes[i]);
        }
        method.invoke(instance, args);
    }

    private static Object convertStringToType(String value, Class<?> targetType) {
        if (targetType == String.class) return value;
        if (targetType == int.class || targetType == Integer.class) return Integer.parseInt(value);
        if (targetType == boolean.class || targetType == Boolean.class) return Boolean.parseBoolean(value);
        throw new IllegalArgumentException("Ошибка! Не удалось преобразовать строковое значение в тип " + targetType.getSimpleName() + " поддержка данного типа отсутствует");
    }

    private static void validateIsStatic(Method method) {
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new IllegalStateException("Ошибка! Метод '" + method.getName() + "' должен быть статическим.");
        }
    }
}
