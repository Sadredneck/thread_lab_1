package thread.lab1.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LabTwo {

    private AtomicInteger resultSum = new AtomicInteger(0);
    private AtomicBoolean isWorking = new AtomicBoolean(true);

    public int getResultSum() {
        return resultSum.get();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Only two arguments can be passed buddy");
            return;
        }
        try {
            int threadCount = Integer.parseInt(args[0]);
            System.out.println("Started with " + threadCount + " threads");
            LabTwo example = new LabTwo();

            long start = System.nanoTime();
            example.performCalculation(Paths.get(args[1]), threadCount);
            long duration = System.nanoTime() - start;

            System.out.println("\nTime passed: " + duration / 1_000_000 + " ms\nResult: " + example.getResultSum());
        } catch (NumberFormatException e) {
            System.out.println("Can't parse number of threads");
        } catch (InterruptedException e) {
            System.out.println("There should be at least 1 thread buddy");
        }
    }

    public void performCalculation(Path inputPath, int threadCount) throws InterruptedException {
        Thread tasks[] = new Thread[threadCount];
        SearchTask searchTasks[] = new SearchTask[threadCount];

        for (int i = 0; i < threadCount; i++) {
            searchTasks[i] = new SearchTask(resultSum, isWorking);
            tasks[i] = new Thread(searchTasks[i]);
            tasks[i].start();
        }



        try (BufferedReader reader = Files.newBufferedReader(inputPath)) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                searchTasks[i++ % threadCount].addElem(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.isWorking.set(false);
        boolean isWorking = true;

        while (isWorking) {
            int count = 0;
            for (int i = 0; i < threadCount; i++) {
                if (!tasks[i].isAlive())
                    count++;
            }
            if (count == threadCount)
                isWorking = false;
            Thread.sleep(1);
        }
        System.out.print("\rDone.");
    }

    private void doRoundRobin(BufferedReader reader, SearchTask[] searchTasks, int threadCount) throws IOException {
        String line;
        int i = 0;
        while ((line = reader.readLine()) != null) {
            searchTasks[i++ % threadCount].addElem(line);
        }
    }

    private void doLeastLoaded(BufferedReader reader, SearchTask[] searchTasks, int threadCount) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            Arrays.stream(searchTasks).min(Comparator.comparing(SearchTask::getQueueSize)).get().addElem(line);
        }
    }

    private void doPredictive(BufferedReader reader, SearchTask[] searchTasks, int threadCount) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            Arrays.stream(searchTasks).min(Comparator.comparing(SearchTask::getDifficultyScore)).get().addElem(line);
        }
    }
}
