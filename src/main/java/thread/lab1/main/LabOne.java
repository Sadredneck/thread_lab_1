package thread.lab1.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LabOne {

    private AtomicInteger resultSum = new AtomicInteger(0);
    private AtomicInteger linesDone = new AtomicInteger(0);

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
        } catch (IOException e) {
            System.out.println("Can't find file buddy");
        } catch (InterruptedException e) {
            System.out.println("There should be at least 1 thread buddy");
        }
    }

    public void performCalculation(Path inputPath, int threadCount) throws InterruptedException, IOException {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<String> lines = Files.readAllLines(inputPath);
        int size = lines.size();

        for (int i = 0; i < threadCount; i++) {
            Runnable task = new SearchTaskAlt(250_000 * i / threadCount, 250_000 * (i + 1) / threadCount,
                    lines, resultSum, linesDone);
            executor.execute(task);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            System.out.print("\r" + (int) ((linesDone.get() * 100.0f) / size) + " %");
            Thread.sleep(10);
        }
        System.out.print("\rDone.");
    }

}
