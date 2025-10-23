package ru.javapro;

import ru.javapro.runner.TestRunner;
import ru.javapro.tests.TestClass;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Стартуем выполнение тестов класса" + TestClass.class.getSimpleName());
        System.out.println();

        TestRunner.runTests(TestClass.class);

        System.out.println();
        System.out.println("Все тесты завершены.");
    }
}