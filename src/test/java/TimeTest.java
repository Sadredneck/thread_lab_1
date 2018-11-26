import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import thread.lab1.main.LabTwo;

import java.io.IOException;
import java.nio.file.Paths;

@RunWith(Theories.class)
public class TimeTest {

    @Test
    @Ignore
    public void timeTestOneToSixteenThreads() throws IOException, InterruptedException {
        for (int j = 1; j <= 16; j++) {
            System.out.println(j + ":");
            for (int i = 0; i < 25; i++) {
                doTimeCheck(j);
            }
        }
    }

    @Test
    public void timeTestThreads() throws IOException, InterruptedException {
        for (int i = 0; i < 1; i++) {
            doTimeCheck(1);
        }
    }

    @Theory
    @Ignore
    public void timeTestThreads(int threadNumber) throws IOException, InterruptedException {
        System.out.println("\n"+threadNumber + ":\n");
        for (int i = 0; i < 25; i++) {
            doTimeCheck(threadNumber);
        }
    }

    public static @DataPoints
    int[] candidates = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};


    private void doTimeCheck(int threads) throws InterruptedException {
        LabTwo example = new LabTwo();
        long start = System.nanoTime();
        example.performCalculation(Paths.get("lab_1_4.input"), threads);
        long duration = System.nanoTime() - start;
        System.out.println("\r" + duration / 1_000_000);
    }
}
