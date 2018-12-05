package thread.lab1.generator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public enum Figures {
    TRIANGLE((x) -> {
        double halfP = (x.get(0) + x.get(1) + x.get(2)) / 2;
        return (int) Math.sqrt(
                halfP * (halfP - x.get(0)) * (halfP - x.get(1)) * (halfP - x.get(2))
        );
    }, 30),
    CIRCLE((x) -> (int) (x.get(0) * Math.PI), 9),
    BOX((x) -> x.get(0) * x.get(1), 8);

    private static final List<Figures> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    private Function<List<Integer>, Integer> function;
    public int difficulty;

    Figures(Function<List<Integer>, Integer> function, int difficulty) {
        this.function = function;
        this.difficulty = difficulty;
    }

    public int getArea(List<Integer> values) {
        return function.apply(values);
    }

    public static String getRandomLine() {
        switch (VALUES.get(RANDOM.nextInt(SIZE))) {
            case TRIANGLE:
                int one = RANDOM.nextInt(10) + 1, two = RANDOM.nextInt(10) + 1;
                return TRIANGLE.toString() + " " + one + " " + two + " " + (RANDOM.nextInt(one + two) + 1);
            case CIRCLE:
                return CIRCLE.toString() + " " + (RANDOM.nextInt(10) + 1);
            case BOX:
                return BOX.toString() + " " + (RANDOM.nextInt(10) + 1) + " " + (RANDOM.nextInt(10) + 1);
        }
        return null;
    }
}