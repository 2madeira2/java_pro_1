package ru.javapro.thread.pool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class CustomThreadPool {
    private final int capacity;
    private final LinkedList<Runnable> tasks;
    private final List<ThreadWorker> workerList;
    private volatile boolean isShutdown;


    public CustomThreadPool(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Размер пула потоков должен быть больше 0");
        }
        this.capacity = capacity;
        this.tasks = new LinkedList<>();
        this.workerList = new ArrayList<>(capacity);
        this.isShutdown = false;

        startWorkers();
    }

    public void execute(Runnable task) {
        if (task == null) {
            throw new IllegalArgumentException("task не может быть null");
        }

        synchronized (tasks) {
            if (isShutdown) {
                throw new IllegalStateException(
                        "Пул потоков выключен, новые таски не принимает");
            }
            tasks.addLast(task);
            tasks.notify();
        }
    }

    public void shutdown() {
        synchronized (tasks) {
            if (!isShutdown) {
                isShutdown = true;
                tasks.notifyAll();
                System.out.println("Завершение работы пула потоков");
            }
        }
    }

    public void awaitTermination() throws InterruptedException {
        for (ThreadWorker worker : workerList) {
            worker.join();
        }
    }

    public boolean isShutdown() {
        return isShutdown;
    }

    public boolean isTerminated() {
        if (!isShutdown) {
            return false;
        }
        for (ThreadWorker worker : workerList) {
            if (worker.isAlive()) {
                return false;
            }
        }
        return true;
    }

    public int getTasksCount() {
        synchronized (tasks) {
            return tasks.size();
        }
    }

    public int getCapacity() {
        return capacity;
    }

    private void startWorkers() {
        IntStream.range(0, capacity)
                .forEach((i) -> {
                    var worker = new ThreadWorker();
                    workerList.add(worker);
                    worker.start();
                });
    }

    private class ThreadWorker extends Thread {
        @Override
        public void run() {
            Runnable task;
            while (true) {
                synchronized (tasks) {
                    while (tasks.isEmpty() && !isShutdown) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }

                    if (tasks.isEmpty() && isShutdown) {
                        return;
                    }
                    task = tasks.poll();
                }

                if (task != null) {
                    try {
                        task.run();
                    } catch (RuntimeException e) {
                        System.out.println("Ошибка при выполнении задачи: " + e.getMessage());
                    }
                }
            }
        }
    }
}
