package ru.javapro;

import ru.javapro.thread.pool.CustomThreadPool;

import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CustomThreadPool threadPool = new CustomThreadPool(3);

        System.out.println("Пул стартует с " + threadPool.getCapacity() + " потоками");

        IntStream.rangeClosed(1, 20)
                .forEach(i -> threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + " выполняет задачу " + i);
                    try {
                        System.out.println();
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println(Thread.currentThread().getName() + " закончил выполнять задачу " + i);
                }));

        System.out.println("Задач в очереди сейчас: " + threadPool.getTasksCount());

        threadPool.shutdown();
        System.out.println("shutdown() вызван, isShutdown = " + threadPool.isShutdown());


        try {
            threadPool.execute(() -> System.out.println("Задача после того как вызвали shutdown"));
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }

        threadPool.awaitTermination();
        System.out.println("Все потоки завершены");
    }
}