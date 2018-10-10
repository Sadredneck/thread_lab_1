package thread.lab1.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileCreatorVariationOne {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Only one argument can be passed buddy.");
            return;
        }
        try {
            int numberLines = Integer.valueOf(args[0]);
            if (numberLines < 1) {
                System.out.println("Positive number of lines should be there buddy.");
                return;
            }
            createFile(numberLines);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void createFile(int numberLines) throws IOException {
        Path path = getAvailableFile();
        Files.write(path,
                (Iterable<String>) Stream.generate(Figures::getRandomLine).limit(numberLines)::iterator);
    }

    private static Path getAvailableFile() {
        int i = 0;
        Path path;
        do {
            path = Paths.get("lab_1_" + i++ + ".input");
        }
        while (Files.exists(path));
        return path;
    }
}
