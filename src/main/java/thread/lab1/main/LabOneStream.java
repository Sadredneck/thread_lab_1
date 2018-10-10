package thread.lab1.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class LabOneStream {

    private AtomicInteger resultSum = new AtomicInteger(0);

    public int getResultSum() {
        return resultSum.get();
    }

    public static void main(String[] args) throws IOException {
        LabOneStream example = new LabOneStream();
        long start = System.nanoTime();
        example.performCalculation(Paths.get("input_var_1_1.txt"));
        long duration = System.nanoTime() - start;
        System.out.println("time passed: " + duration + "\n" + "result: " + example.getResultSum());
    }

    public void performCalculation(Path inputPath) throws IOException {
        Files.readAllLines(inputPath)
                .parallelStream()
                .map(this::readLine)
                .forEach(resultSum::addAndGet);
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
