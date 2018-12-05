import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;
import thread.lab1.main.LabTwo;

import java.nio.file.Paths;

@RunWith(Theories.class)
public class TimeTest {

    @Test
    public void timeTestThreads() throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            doTimeCheck(16, LabTwo.ThreadFunction.PREDICTIVE);
        }
    }

    private void doTimeCheck(int threads, LabTwo.ThreadFunction function) throws InterruptedException {
        LabTwo example = new LabTwo();
        long start = System.nanoTime();
        example.performCalculation(Paths.get("lab_1_0.input"), threads, function);
        long duration = System.nanoTime() - start;
        System.out.println("\r" + duration / 1_000_000);
        Long[] working = example.getWorkingTime();
        Long[] waiting = example.getWaitingTime();
        Integer[] count = example.getCount();
        for (int i = 0; i < threads; i++) {
            System.out.println(
                    ((double)working[i] / (working[i] + waiting[i])) + "\t"
                    + ((double)waiting[i] / (working[i] + waiting[i])) + "\t"
                    + (count[i]) + " ");
        }
    }
}
