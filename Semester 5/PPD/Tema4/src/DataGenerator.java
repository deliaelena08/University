import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DataGenerator {

    private static final int NUM_FILES = 10;
    private static final String FILE_PREFIX = "proiect";
    private static final int NUM_STUDENTS = 200;
    private static final int MIN_GRADES = 80;

    public static void main(String[] args) {
        System.out.println("Generating data...");
        try {
            generateData();
            System.out.println("Done! Generated 10 files (proiect1.txt - proiect10.txt).");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateData() throws IOException {
        Random rand = new Random();

        for (int i = 1; i <= NUM_FILES; i++) {
            String fileName = FILE_PREFIX + i + ".txt";
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
                int notesCount = MIN_GRADES + rand.nextInt(121);

                for (int j = 0; j < notesCount; j++) {
                    int studentId = rand.nextInt(NUM_STUDENTS);
                    int nota = rand.nextInt(10) + 1;
                    bw.write("Stud" + studentId + "," + nota);
                    bw.newLine();
                }
            }
        }
    }
}