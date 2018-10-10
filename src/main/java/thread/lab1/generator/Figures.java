package thread.lab1.generator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Figures {
    triangle, circle, box;

    private static final List<Figures> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Figures getRandom() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static String getRandomLine() {
        switch (VALUES.get(RANDOM.nextInt(SIZE))) {
            case triangle:
                int one = RANDOM.nextInt(10) + 1, two = RANDOM.nextInt(10) + 1;
                return triangle.toString() + " " + one + " " + two + " " + (RANDOM.nextInt(one + two) + 1);
            case circle:
                return circle.toString() + " " + (RANDOM.nextInt(10) + 1);
            case box:
                return box.toString() + " " + (RANDOM.nextInt(10) + 1) + " " + (RANDOM.nextInt(10) + 1);
        }
        return null;
    }
}