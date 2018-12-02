package thread.lab1.main;

import java.nio.file.Paths;

public class Main {

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
            example.performCalculation(Paths.get(args[1]), threadCount, LabTwo.ThreadFunction.valueOf(args[2]));
            long duration = System.nanoTime() - start;

            System.out.println("\nTime passed: " + duration / 1_000_000 + " ms\nResult: " + example.getResultSum());
            for (SearchTask task : example.getSearchTasks()) {
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

}
