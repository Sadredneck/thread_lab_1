package thread.lab1.main;

import thread.lab1.generator.Figures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class SearchTask implements Runnable {
    private ConcurrentLinkedQueue<String> lines = new ConcurrentLinkedQueue<>();
    private AtomicInteger resultSum;
    private AtomicBoolean isWorking;
    private int count = 0;
    private long usefulTime = 0, waitingTime = 0;
    private AtomicLong difficultyScore = new AtomicLong(0);

    public int getCount() {
        return count;
    }

    public long getUsefulTime() {
        return usefulTime;
    }

    public long getWaitingTime() {
        return waitingTime;
    }

    public long getDifficultyScore() {
        return difficultyScore.get();
    }

    public int getQueueSize() {
        return lines.size();
    }

    public void addElem(String line) {
        lines.add(line);
        difficultyScore.addAndGet(Figures.valueOf(line.split(" ", 2)[0]).difficulty);
    }

    public SearchTask(AtomicInteger resultSum, AtomicBoolean isWorking) {
        this.resultSum = resultSum;
        this.isWorking = isWorking;
    }

    public void run() {
        while (isWorking.get() || !lines.isEmpty()) {
            long start = System.nanoTime();
            String line = lines.poll();
            if (line != null) {
                resultSum.addAndGet(readLine(line));
                count++;
                usefulTime += System.nanoTime() - start;
            } else {
                waitingTime += System.nanoTime() - start;
            }
        }
    }

    private int readLine(String line) {
        List<String> array = new ArrayList<>(Arrays.asList(line.split(" ")));
        Figures figure = Figures.valueOf(array.get(0));

        difficultyScore.addAndGet(figure.difficulty);
        return figure.getArea(array.stream().skip(1).map(Integer::valueOf).collect(Collectors.toList()));
    }
}

