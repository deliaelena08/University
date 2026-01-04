public class Worker2 extends Thread {
    private final int startCol, endCol;
    private final int n, m, k;
    private final int[] input, output, kernel;

    public Worker2(int startCol, int endCol, int n, int m, int k, int[] input, int[] output, int[] kernel) {
        this.startCol = startCol;
        this.endCol = endCol;
        this.n = n;
        this.m = m;
        this.k = k;
        this.input = input;
        this.output = output;
        this.kernel = kernel;
    }

    private int idx(int i, int j, int cols) {
        return i * cols + j;
    }

    private int valoareBordata(int i, int j) {
        if (i < 0) i = 0;
        if (i >= n) i = n - 1;
        if (j < 0) j = 0;
        if (j >= m) j = m - 1;
        return input[idx(i, j, m)];
    }

    @Override
    public void run() {
        for (int i = 0; i < n; i++) {
            for (int j = startCol; j < endCol; j++) {
                int suma = 0;
                for (int u = 0; u < k; u++) {
                    for (int v = 0; v < k; v++) {
                        int ii = i + u - k / 2;
                        int jj = j + v - k / 2;
                        suma += valoareBordata(ii, jj) * kernel[idx(u, v, k)];
                    }
                }
                output[idx(i, j, m)] = suma;
            }
        }
    }
}
