package thread.lab1.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LabTwo {
    interface ThreeParamConsumer<One, Two, Three> {
        void accept(One one, Two two, Three three) throws IOException;
    }

    public enum ThreadFunction {
        ROUND_ROBIN((reader, searchTasks, threadCount) -> {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                searchTasks[i++ % threadCount].addElem(line);
            }
        }), LEAST_LOADED((reader, searchTasks, threadCount) -> {
            String line;
            while ((line = reader.readLine()) != null) {
                Arrays.stream(searchTasks).min(Comparator.comparing(SearchTask::getQueueSize)).get().addElem(line);
            }
        }),
        PREDICTIVE((reader, searchTasks, threadCount) -> {
            String line;
            while ((line = reader.readLine()) != null) {
                Arrays.stream(searchTasks).min(Comparator.comparing(SearchTask::getQueueSize)).get().addElem(line);
            }
        });
        private ThreeParamConsumer<BufferedReader, SearchTask[], Integer> consumer;

        public void perform(BufferedReader reader, SearchTask[] searchTasks, Integer threadCount) throws IOException {
            consumer.accept(reader, searchTasks, threadCount);
        }

        ThreadFunction(ThreeParamConsumer<BufferedReader, SearchTask[], Integer> consumer) {
            this.consumer = consumer;
        }
    }

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

    int getResultSum() {
        return resultSum.get();
    }

    SearchTask[] getSearchTasks() {
        return searchTasks;
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
            function.perform(reader, searchTasks, threadCount);
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
}
