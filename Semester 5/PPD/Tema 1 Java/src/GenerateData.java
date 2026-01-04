import java.io.*;
import java.util.*;

public class GenerateData {
    public static void main(String[] args) throws IOException {
        Random rnd = new Random();

        // Testul 1: N = M = 10, k = 3
        int n = 10, m = 10, k = 3;

        // Testul 2: N = M = 1000, k = 5
//         int n = 1000, m = 1000, k = 5;

        // Testul 3: N = 10, M = 10000, k = 5
//         int n = 10, m = 10000, k = 5;

        // Testul 4: N = 10000, M = 10, k = 5
//         int n = 10000, m = 10, k = 5;

        PrintWriter fout = new PrintWriter(new FileWriter("date_java.txt"));
        fout.printf("%d %d %d%n", n, m, k);

        // matricea de input
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++)
                fout.printf("%d ", rnd.nextInt(100));
            fout.println();
        }

        // kernel-ul
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++)
                fout.printf("%d ", rnd.nextInt(3) - 1); // valori între -1,0,1
            fout.println();
        }

        fout.close();
        System.out.println("Datele au fost scrise cu succes în date.txt!");
    }
}
