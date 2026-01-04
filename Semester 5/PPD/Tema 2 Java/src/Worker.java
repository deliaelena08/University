import java.util.concurrent.CyclicBarrier;

public class Worker extends Thread {
    private final int startRow, endRow;
    private final int n, m, k;
    private final int[] input;
    private final int[] kernel;
    private final CyclicBarrier barrier;

    public Worker(int startRow, int endRow, int n, int m, int k, int[] input, int[] kernel, CyclicBarrier barrier) {
        this.startRow = startRow;
        this.endRow = endRow;
        this.n = n;
        this.m = m;
        this.k = k;
        this.input = input;
        this.kernel = kernel;
        this.barrier = barrier;
    }

    private int idx(int i, int j) {
        if (i < 0) i = 0;
        if (i >= n) i = n - 1;
        if (j < 0) j = 0;
        if (j >= m) j = m - 1;
        return i * m + j;
    }

    private int convolutiePixel(int i, int j, int[] prevLine, int[] currLine, int[] nextLine) {
        int sum = 0;
        int halfK = k / 2;
        for (int u = 0; u < k; u++) {
            for (int v = 0; v < k; v++) {
                int ii = i + u - halfK;
                int jj = j + v - halfK;
                int val;
                if (ii < i)
                    val = prevLine[jj < 0 ? 0 : (jj >= m ? m - 1 : jj)];
                else if (ii == i)
                    val = currLine[jj < 0 ? 0 : (jj >= m ? m - 1 : jj)];
                else
                    val = nextLine[jj < 0 ? 0 : (jj >= m ? m - 1 : jj)];
                sum += val * kernel[u * k + v];
            }
        }
        return sum;
    }

    @Override
    public void run() {
        int[] prevLine = new int[m];
        int[] currLine = new int[m];
        int[] nextLine = new int[m];
        int[] tempLine = new int[m];

        boolean parallel = (barrier != null);

        for (int j = 0; j < m; j++) {
            prevLine[j] = input[idx(startRow - 1, j)];
            currLine[j] = input[idx(startRow, j)];
            nextLine[j] = input[idx(startRow + 1, j)];
        }

        if (parallel) {
            try { barrier.await(); } catch (Exception e) { e.printStackTrace(); }
        }

        for (int i = startRow; i < endRow; i++) {
            int nextRow = (i + 1 >= n) ? n - 1 : i + 1;
            for (int j = 0; j < m; j++)
                nextLine[j] = input[idx(nextRow, j)];

            for (int j = 0; j < m; j++)
                tempLine[j] = convolutiePixel(i, j, prevLine, currLine, nextLine);

            System.arraycopy(tempLine, 0, input, i * m, m);

            int[] tmp = prevLine;
            prevLine = currLine;
            currLine = nextLine;
            nextLine = tmp;
        }
    }
}
