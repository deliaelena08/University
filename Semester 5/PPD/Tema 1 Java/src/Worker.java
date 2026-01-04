public class Worker extends Thread {
    private final int startLinie, endLinie;
    private final int n, m, k;
    private final int[] input, output, kernel;

    public Worker(int startLinie, int endLinie, int n, int m, int k, int[] input, int[] output, int[] kernel) {
        this.startLinie = startLinie;
        this.endLinie = endLinie;
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
        for (int i = startLinie; i < endLinie; i++) {
            for (int j = 0; j < m; j++) {
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
