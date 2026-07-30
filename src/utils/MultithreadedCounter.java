package utils;

import car.Car;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class MultithreadedCounter {

    public static int countCars(List<Car> cars, Predicate<Car> condition) {
        if (cars == null || cars.isEmpty()) {
            return 0;
        }

        int threadCount = Math.min(4, Runtime.getRuntime().availableProcessors());
        int chunkSize = (int) Math.ceil((double) cars.size() / threadCount);

        int[] results = new int[threadCount];
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            final int start = i * chunkSize;
            final int end = Math.min((i + 1) * chunkSize, cars.size());

            threads[i] = new Thread(() -> {
                int count = 0;
                for (int j = start; j < end; j++) {
                    if (condition.test(cars.get(j))) {
                        count++;
                    }
                }
                results[threadIndex] = count;
            });

            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Ошибка ожидания потока: " + e.getMessage());
                return 0;
            }
        }

        int total = 0;
        for (int count : results) {
            total += count;
        }

        return total;
    }

    public static boolean isSorted(List<Car> cars, Comparator<Car> comparator) {
        if (cars == null || cars.size() <= 1) {
            return true;
        }

        for (int i = 0; i < cars.size() - 1; i++) {
            if (comparator.compare(cars.get(i), cars.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }
}