import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

public class Main {

    private static int idx(int i, int j, int cols) {
        return i * cols + j;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("date_java.txt");
        if (!file.exists()) {
            System.err.println("Eroare: fisierul date_java.txt nu exista!");
            return;
        }

        Scanner fin = new Scanner(file);
        int n = fin.nextInt();
        int m = fin.nextInt();
        int k = fin.nextInt();

        int[] input = new int[n * m];
        int[] output = new int[n * m];
        int[] kernel = new int[k * k];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                input[idx(i, j, m)] = fin.nextInt();

        for (int i = 0; i < k; i++)
            for (int j = 0; j < k; j++)
                kernel[idx(i, j, k)] = fin.nextInt();

        fin.close();

        int p = (args.length > 0) ? Integer.parseInt(args[0]) : 1;

        long start = System.nanoTime();

        if (p == 1) {
            // rulare secventaiala
            Worker worker = new Worker(0, n, n, m, k, input, output, kernel);
            worker.run();
        } else {
            // rulare paralela linii
//            Worker[] threads = new Worker[p];
//            int liniiPeThread = n / p;
//            int rest = n % p;
//            int startLinie = 0;
//
//            for (int t = 0; t < p; t++) {
//                int endLinie = startLinie + liniiPeThread;
//                if (rest > 0) { endLinie++; rest--; }
//
//                threads[t] = new Worker(startLinie, endLinie, n, m, k, input, output, kernel);
//                threads[t].start();
//
//                startLinie = endLinie;
//        }
//
//        for (Worker t : threads)
//            t.join();

            // rulare paralela coloane
            Worker2[] threads = new Worker2[p];
            int coloanePeThread = m / p;
            int rest = m % p;
            int startCol = 0;

            for (int t = 0; t < p; t++) {
                int endCol = startCol + coloanePeThread;
                if (rest > 0) { endCol++; rest--; }

                threads[t] = new Worker2(startCol, endCol, n, m, k, input, output, kernel);
                threads[t].start();

                startCol = endCol;
            }

            for (Worker2 t : threads)
                t.join();

        }

        long end = System.nanoTime();
        double durataMs = (end - start) / 1_000_000.0;
        System.out.println(new DecimalFormat("#0.000000").format(durataMs));

        String outputFile = (p == 1) ? "output_seq_java.txt" : "output_par_java.txt";
        PrintWriter fout = new PrintWriter(new FileWriter(outputFile));

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++)
                fout.printf("%6d ", output[idx(i, j, m)]);
            fout.println();
        }

        fout.close();
    }
}
