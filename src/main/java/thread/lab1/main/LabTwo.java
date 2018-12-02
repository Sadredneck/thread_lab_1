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
    public enum ThreadFunction {ROUND_ROBIN, LEAST_LOADED, PREDICTIVE}
    private AtomicInteger resultSum = new AtomicInteger(0);
    private AtomicBoolean isWorking = new AtomicBoolean(true);
    private SearchTask searchTasks[];

    public Long[] getWorkingTime() {
        return Arrays.stream(searchTasks).map(SearchTask::getUsefulTime).toArray(Long[]::new);
    }

    public Long[] getWaitingTime() {
        return Arrays.stream(searchTasks).map(SearchTask::getWaitingTime).toArray(Long[]::new);
    }

    public Integer[] getCount() {
        return Arrays.stream(searchTasks).map(SearchTask::getCount).toArray(Integer[]::new);
    }

    public int getResultSum() {
        return resultSum.get();
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Only three arguments can be passed buddy");
            return;
        }
        try {
            int threadCount = Integer.parseInt(args[0]);
            System.out.println("Started with " + threadCount + " threads");
            LabTwo example = new LabTwo();

            long start = System.nanoTime();
            example.performCalculation(Paths.get(args[1]), threadCount, ThreadFunction.valueOf(args[2]));
            long duration = System.nanoTime() - start;

            System.out.println("\nTime passed: " + duration / 1_000_000 + " ms\nResult: " + example.getResultSum());
            for (SearchTask task : example.searchTasks) {
                long totalTime = task.getUsefulTime() + task.getWaitingTime();
                System.out.println("\nUseful time: " + (task.getUsefulTime() / totalTime));
                System.out.println("\nWaiting time: " + (task.getWaitingTime() / totalTime));
                System.out.println("\nData received: " + (task.getCount() / totalTime));
            }
        } catch (NumberFormatException e) {
            System.out.println("Can't parse number of threads");
        } catch (InterruptedException e) {
            System.out.println("There should be at least 1 thread buddy");
        }
    }

    public void performCalculation(Path inputPath, int threadCount, ThreadFunction function) throws InterruptedException {
        Thread tasks[] = new Thread[threadCount];
        searchTasks = new SearchTask[threadCount];

        for (int i = 0; i < threadCount; i++) {
            searchTasks[i] = new SearchTask(resultSum, isWorking);
            tasks[i] = new Thread(searchTasks[i]);
            tasks[i].start();
        }

        try (BufferedReader reader = Files.newBufferedReader(inputPath)) {
            switch (function) {
                case ROUND_ROBIN:
                    doRoundRobin(reader, searchTasks, threadCount);
                    break;
                case LEAST_LOADED:
                    doLeastLoaded(reader, searchTasks, threadCount);
                    break;
                case PREDICTIVE:
                    doPredictive(reader, searchTasks, threadCount);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.isWorking.set(false);
        boolean isWorking = true;

        while (isWorking) {
            int count = 0;
            for (Thread thread : tasks) {
                if (!thread.isAlive()) {
                    count++;
                }
            }
            isWorking = count != threadCount;

            Thread.sleep(0);
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
        int count = 0;
        SearchTask currentTask = searchTasks[0];
        while ((line = reader.readLine()) != null) {
            if (count == 0) {
                currentTask = Arrays.stream(searchTasks).min(Comparator.comparing(SearchTask::getDifficultyScore)).get();
                count = 100;
            }
            currentTask.addElem(line);
            count--;
        }
    }
}
