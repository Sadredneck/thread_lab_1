package thread.lab1.main;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class SearchTaskAlt implements Runnable {

    private ConcurrentLinkedQueue<String> lines;
    private AtomicInteger resultSum;
    private AtomicBoolean isWorking;

    public SearchTaskAlt(ConcurrentLinkedQueue<String> words, AtomicInteger resultSum, AtomicBoolean isWorking) {
        this.lines = words;
        this.resultSum = resultSum;
        this.isWorking = isWorking;
    }

    public void run() {
        while (isWorking.get() || !lines.isEmpty()) {
            String line = lines.poll();
            if (line != null) {
                resultSum.addAndGet(readLine(line));
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int readLine(String line) {
        String[] array = line.split(" ");
        switch (array[0]) {
            case "circle":
                return (int) (Integer.valueOf(array[1]) * Math.PI);
            case "box":
                return Integer.valueOf(array[1]) * (Integer.valueOf(array[2]));
            case "triangle":
                return (int) triangleArea(Integer.valueOf(array[1]), Integer.valueOf(array[2]), Integer.valueOf(array[3]));
        }
        return 0;
    }

    private double triangleArea(int one, int two, int three) {
        double halfP = (one + two + three) / 2;
        return Math.sqrt(
                halfP * (halfP - one) * (halfP - two) * (halfP - three)
        );
    }
}

