package thread.lab1.main;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchTask implements Runnable {

    private ConcurrentLinkedQueue<String> lines = new ConcurrentLinkedQueue<>();
    private AtomicInteger resultSum;
    private AtomicBoolean isWorking;
    private int count = 0;
    private long usefulTime = 0, uselessTime = 0, difficultyScore = 0;

    public int getCount() {
        return count;
    }

    public long getUsefulTime() {
        return usefulTime;
    }

    public long getUselessTime() {
        return uselessTime;
    }

    public long getDifficultyScore() {
        return difficultyScore;
    }

    public void addElem(String line) {
        lines.add(line);
        switch (line.split(" ", 2)[0]) {
            case "circle":
                difficultyScore += 2;
                break;
            case "box":
                difficultyScore += 1;
                break;
            case "triangle":
                difficultyScore += 4;
                break;
            default:
                difficultyScore += 1;
        }
    }

    public SearchTask(AtomicInteger resultSum, AtomicBoolean isWorking) {
        this.resultSum = resultSum;
        this.isWorking = isWorking;
    }

    public int getQueueSize(){
        return lines.size();
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
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                uselessTime += System.nanoTime() - start;
            }
        }
    }

    private int readLine(String line) {
        String[] array = line.split(" ");
        int answer = 0;
        for (int i = 0; i < 10; i++)
            switch (array[0]) {
                case "circle":
                    answer = (int) (Integer.valueOf(array[1]) * Math.PI);
                    answer = (int) (Integer.valueOf(array[1]) * Math.PI);
                    difficultyScore -= 2;
                    break;
                case "box":
                    answer = Integer.valueOf(array[1]) * (Integer.valueOf(array[2]));
                    answer = Integer.valueOf(array[1]) * (Integer.valueOf(array[2]));
                    difficultyScore -= 1;
                    break;
                case "triangle":
                    answer = (int) triangleArea(Integer.valueOf(array[1]), Integer.valueOf(array[2]), Integer.valueOf(array[3]));
                    answer = (int) triangleArea(Integer.valueOf(array[1]), Integer.valueOf(array[2]), Integer.valueOf(array[3]));
                    difficultyScore -= 4;
                    break;
            }
        return answer;
    }

    private double triangleArea(int one, int two, int three) {
        double halfP = (one + two + three) / 2;
        return Math.sqrt(
                halfP * (halfP - one) * (halfP - two) * (halfP - three)
        );
    }
}

