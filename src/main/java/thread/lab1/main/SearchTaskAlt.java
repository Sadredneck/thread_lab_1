package thread.lab1.main;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchTaskAlt implements Runnable {

    private ConcurrentLinkedQueue<String> lines = new ConcurrentLinkedQueue<>();
    private AtomicInteger resultSum;
    private AtomicBoolean isWorking;
    private int count = 0;

    public void addElem(String line) {
        lines.add(line);
    }

    public SearchTaskAlt(AtomicInteger resultSum, AtomicBoolean isWorking) {
        this.resultSum = resultSum;
        this.isWorking = isWorking;
    }

    public void run() {
        while (isWorking.get() || !lines.isEmpty()) {
            String line = lines.poll();
            if (line != null) {
                resultSum.addAndGet(readLine(line));
                count++;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        //System.out.println(count);
    }

    private int readLine(String line) {
        String[] array = line.split(" ");
        int answer = 0;
        for (int i = 0; i < 10; i++)
            switch (array[0]) {
                case "circle":
                    answer = (int) (Integer.valueOf(array[1]) * Math.PI);
                    answer = (int) (Integer.valueOf(array[1]) * Math.PI);
                    break;
                case "box":
                    answer = Integer.valueOf(array[1]) * (Integer.valueOf(array[2]));
                    answer = Integer.valueOf(array[1]) * (Integer.valueOf(array[2]));
                    break;
                case "triangle":
                    answer = (int) triangleArea(Integer.valueOf(array[1]), Integer.valueOf(array[2]), Integer.valueOf(array[3]));
                    answer = (int) triangleArea(Integer.valueOf(array[1]), Integer.valueOf(array[2]), Integer.valueOf(array[3]));
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

