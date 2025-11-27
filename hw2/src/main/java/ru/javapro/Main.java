package ru.javapro;

import ru.javapro.model.Employee;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

public class Main {
    public static void main(String[] args) {
        var nums1 = new ArrayList<>(List.of(5, 2, 10, 9, 4, 3, 10, 1, 13));
        System.out.println(findThirdLargestNumber(nums1));
        System.out.println(findThirdUniqueLargestNumber(nums1));
        var employees = new ArrayList<>(List.of(new Employee("Алексей", 25, "Инженер"),
                                                new Employee("Михаил", 23, "Инженер"),
                                                new Employee("Александр", 29, "Сеньор"),
                                                new Employee("Ирина", 36, "Дизайнер"),
                                                new Employee("Артем", 44, "Инженер"),
                                                new Employee("Илья", 33, "Стажер"),
                                                new Employee("Евлампия", 22, "Аналитик"),
                                                new Employee("Максим", 65, "Инженер"),
                                                new Employee("Ержан", 58, "Бухгалтер"),
                                                new Employee("Игорь", 49, "Инженер"),
                                                new Employee("Анатолий", 54, "Архитектор"))
        );
        var words = new ArrayList<>(List.of("Найдите", "в", "списке", "слов", "самое", "длинное"));

        String inputString = "бетон ложка бетон поток карта свитч ложка бетон карта поток";

        List<String> words2 = Arrays.asList(
                "синхрофазотрон",
                "бор",
                "алмаз",
                "мак",
                "куб",
                "рубин",
                "а"
        );

        String[] arrayOfStringsWithWords = {
                "вектор скаляр матрица тензор интеграл",
                "ноль один два три пять",
                "монитор мышь клавиатура электростанция процессор"
        };

        System.out.println(findListNames3MostEldestEmployeesWithPositionEngineerInDescendingAgeOrder(employees));
        System.out.println(findAverageAgeOfEmployeesWithPositionEngineer(employees));
        System.out.println(findLongestWord(words));
        System.out.println(countWordsInString(inputString));
        printWordsSortedByLengthAndAlphabet(words2);
        System.out.println(findLongestWordInArrayOfStringsWithWords(arrayOfStringsWithWords));

    }

    private static int findThirdLargestNumber(List<Integer> nums) {
        return nums
                .stream()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst()
                .orElseThrow();
    }

    private static int findThirdUniqueLargestNumber(List<Integer> nums) {
        return nums
                .stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst()
                .orElseThrow();
    }

    private static List<String> findListNames3MostEldestEmployeesWithPositionEngineerInDescendingAgeOrder(List<Employee> employees) {
        return employees
                .stream()
                .filter(e -> "Инженер".equals(e.getPosition()))
                .sorted(comparingInt(Employee::getAge).reversed())
                .limit(3)
                .map(Employee::getName)
                .toList();
    }

    private static int findAverageAgeOfEmployeesWithPositionEngineer(List<Employee> employees) {
        return (int) employees
                .stream()
                .filter(e -> "Инженер".equals(e.getPosition()))
                .mapToInt(Employee::getAge)
                .average()
                .getAsDouble();
    }

    private static String findLongestWord(List<String> words) {
        return words
                .stream()
                .max(comparingInt(String::length))
                .orElseThrow();
    }

    private static Map<String, Long> countWordsInString(String inputStr) {
        return Arrays.stream(inputStr.split(" "))
                     .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private static void printWordsSortedByLengthAndAlphabet(List<String> words) {
        words.stream()
                .sorted(Comparator
                        .comparingInt(String::length)
                        .thenComparing(Comparator.naturalOrder()))
                .forEach(System.out::println);
    }

    private static String findLongestWordInArrayOfStringsWithWords(String[] strArray) {
        return Arrays.stream(strArray)
                .flatMap(line -> Arrays.stream(line.split(" ")))
                .max(Comparator.comparingInt(String::length))
                .orElseThrow();
    }

}