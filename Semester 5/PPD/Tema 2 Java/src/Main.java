import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.util.concurrent.CyclicBarrier;

public class Main {

    private static int idx(int i, int j, int cols) {
        return i * cols + j;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("date.txt");
        if (!file.exists()) {
            System.err.println("Eroare: fisierul date.txt nu exista!");
            return;
        }

        Scanner fin = new Scanner(file);
        int n = fin.nextInt();
        int m = fin.nextInt();
        int k = 3;

        int[] input = new int[n * m];
        int[] kernel = new int[k * k];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                input[idx(i, j, m)] = fin.nextInt();

        for (int i = 0; i < k; i++)
            for (int j = 0; j < k; j++)
                kernel[idx(i, j, k)] = fin.nextInt();

        fin.close();

        int p = (args.length > 0) ? Integer.parseInt(args[0]) : 1;

        long startTime = System.nanoTime();

        if (p == 1) {
            Worker worker = new Worker(0, n, n, m, k, input, kernel, null);
            worker.run();
        } else {
            int numThreads = Math.min(p, n);
            CyclicBarrier barrier = new CyclicBarrier(numThreads);

            Worker[] threads = new Worker[numThreads];
            int rowsPerThread = n / numThreads;
            int remainder = n % numThreads;
            int startRow = 0;

            for (int t = 0; t < numThreads; t++) {
                int endRow = startRow + rowsPerThread + (remainder-- > 0 ? 1 : 0);
                threads[t] = new Worker(startRow, endRow, n, m, k, input, kernel, barrier);
                threads[t].start();
                startRow = endRow;
            }

            for (Worker t : threads)
                t.join();
        }

        long endTime = System.nanoTime();
        double durataMs = (endTime - startTime) / 1_000_000.0;
        System.out.println(new DecimalFormat("#0.000000").format(durataMs));

        String outputFile = (p == 1) ? "output_seq_java.txt" : "output_par_java.txt";
        PrintWriter fout = new PrintWriter(new FileWriter(outputFile));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++)
                fout.printf("%6d ", input[idx(i, j, m)]);
            fout.println();
        }
        fout.close();
    }
}
