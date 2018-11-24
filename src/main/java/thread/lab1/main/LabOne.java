package thread.lab1.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LabOne {

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
            LabOne example = new LabOne();

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
        SearchTaskAlt searchTasks[] = new SearchTaskAlt[threadCount];

        for (int i = 0; i < threadCount; i++) {
            searchTasks[i] = new SearchTaskAlt(resultSum, isWorking);
            tasks[i] = new Thread(searchTasks[i]);
            tasks[i].start();
        }

        try (BufferedReader br = Files.newBufferedReader(inputPath)) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
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

}
