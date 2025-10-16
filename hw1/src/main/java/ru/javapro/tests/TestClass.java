package ru.javapro.tests;

import ru.javapro.annotations.AfterSuite;
import ru.javapro.annotations.AfterTest;
import ru.javapro.annotations.BeforeSuite;
import ru.javapro.annotations.BeforeTest;
import ru.javapro.annotations.CsvSource;
import ru.javapro.annotations.Test;

public class TestClass {
    @BeforeSuite
    public static void globalSetUp() {
        System.out.println("Метод с аннотацией @BeforeSuite, запускается перед всеми тестами");
    }

    @AfterSuite
    public static void globalTearDown() {
        System.out.println("Метод с аннотацией @AfterSuite, запускается после всех тестов");
    }

    @BeforeTest
    public void setUp() {
        System.out.println("Метод с аннотацией @BeforeTest, запускается перед каждым тестом");
    }

    @AfterTest
    public void tearDown() {
        System.out.println("Метод с аннотацией @AfterTest, запускается после каждого теста");
    }

    @Test(priority = 1)
    public void lowPriorityTest() {
        System.out.println("Тест с приоритетом 1");
    }

    @Test(priority = 9)
    public void highPriorityTest() {
        System.out.println("Тест с приоритетом 9");
    }

    @Test
    public void defaultPriorityTest() {
        System.out.println("Тест с дефолтным приоритетом");
    }

    @CsvSource("12, TEST, 34, true")
    @Test(priority = 7)
    public void parametrizedTestMethod(int num, String str, int num2, boolean bool) {
        System.out.println("Параметризованный тест метод с приоритетом 7");
        System.out.println("Входные данные: num=" + num + ", str='" + str + "', num2=" + num2 + ", bool=" + bool);
    }
}
