package thread.lab1.main;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchTaskAlt implements Runnable {
    private int start;
    private int end;
    private List<String> lines;
    private AtomicInteger outerCounter;
    private AtomicInteger linesCounter;

    public SearchTaskAlt(int start, int end, List<String> words, AtomicInteger outerCounter, AtomicInteger linesCounter) {
        this.start = start;
        this.end = end;
        this.lines = words;
        this.outerCounter = outerCounter;
        this.linesCounter = linesCounter;
    }

    public void run() {
        for (int i = start; i < end; i++) {
            outerCounter.addAndGet(readLine(lines.get(i)));
            linesCounter.incrementAndGet();
        }
    }

    private int readLine(String line) {
        String[] array = line.split(" ");
        switch (array.length) {
            case 2:
                return (int) (Integer.valueOf(array[1]) * Math.PI);
            case 3:
                return Integer.valueOf(array[1]) * (Integer.valueOf(array[2]));
            case 4:
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

