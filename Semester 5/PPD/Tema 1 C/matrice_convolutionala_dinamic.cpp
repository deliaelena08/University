#include <iostream>
#include <thread>
#include <fstream>
#include <chrono>
#include <iomanip>

using namespace std;
using namespace std::chrono;

int n, m, k;
int* matriceInput;
int* matriceOutput;
int* kernel;

inline int idx(int i, int j, int cols) {
    return i * cols + j;
}

int valoareBordata(int i, int j) {
    if (i < 0) i = 0;
    if (i >= n) i = n - 1;
    if (j < 0) j = 0;
    if (j >= m) j = m - 1;
    return matriceInput[idx(i, j, m)];
}

void convolutie_worker(int linieStart, int linieEnd) {
    for (int i = linieStart; i < linieEnd; i++) {
        for (int j = 0; j < m; j++) {
            int suma = 0;
            for (int u = 0; u < k; u++) {
                for (int v = 0; v < k; v++) {
                    int ii = i + u - k / 2;
                    int jj = j + v - k / 2;
                    suma += valoareBordata(ii, jj) * kernel[idx(u, v, k)];
                }
            }
            matriceOutput[idx(i, j, m)] = suma;
        }
    }
}

void convolutie_worker_vertical(int colStart, int colEnd) {
    for (int i = 0; i < n; i++) {
        for (int j = colStart; j < colEnd; j++) {
            int suma = 0;
            for (int u = 0; u < k; u++) {
                for (int v = 0; v < k; v++) {
                    int ii = i + u - k / 2;
                    int jj = j + v - k / 2;
                    suma += valoareBordata(ii, jj) * kernel[idx(u, v, k)];
                }
            }
            matriceOutput[idx(i, j, m)] = suma;
        }
    }
}

void convolutie_secventiala() {
    convolutie_worker(0, n);
}

void convolutie_paralela(int numarThreads) {
    thread* threads = new thread[numarThreads];
    int liniiPeThread = n / numarThreads;
    int rest = n % numarThreads;
    int startLinie = 0;

    for (int t = 0; t < numarThreads; t++) {
        int endLinie = startLinie + liniiPeThread;
        if (rest > 0) { endLinie++; rest--; }
        threads[t] = thread(convolutie_worker, startLinie, endLinie);
        startLinie = endLinie;
    }

    for (int t = 0; t < numarThreads; t++)
        threads[t].join();

    delete[] threads;
}

void convolutie_paralela_vertical(int numarThreads) {
    thread* threads = new thread[numarThreads];
    int coloanePeThread = m / numarThreads;
    int rest = m % numarThreads;
    int startCol = 0;

    for (int t = 0; t < numarThreads; t++) {
        int endCol = startCol + coloanePeThread;
        if (rest > 0) { endCol++; rest--; }
        threads[t] = thread(convolutie_worker_vertical, startCol, endCol);
        startCol = endCol;
    }

    for (int t = 0; t < numarThreads; t++)
        threads[t].join();

    delete[] threads;
}

int main2(int argc, char* argv[]) {
    ifstream fin("date.txt");
    if (!fin.is_open()) {
        cerr << "Eroare: nu pot deschide fisierul date.txt\n";
        return 1;
    }

    fin >> n >> m >> k;

    matriceInput = new int[n * m];
    matriceOutput = new int[n * m];
    kernel = new int[k * k];

    for (int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            fin >> matriceInput[idx(i, j, m)];

    for (int i = 0; i < k; i++)
        for (int j = 0; j < k; j++)
            fin >> kernel[idx(i, j, k)];

    fin.close();

    int p = 1;
    if (argc > 1)
        p = stoi(argv[1]);

    auto start = high_resolution_clock::now();

    if (p == 1)
        convolutie_secventiala();
    else
        convolutie_paralela(p);

    auto end = high_resolution_clock::now();
    double durataMs = duration_cast<microseconds>(end - start).count() / 1000.0;

    cout << fixed << setprecision(6) << durataMs;

    string outputFile = (p == 1) ? "output_seq_din.txt" : "output_par_din.txt";
    ofstream fout(outputFile);
    if (!fout.is_open()) {
        cerr << "Eroare la deschiderea fisierului " << outputFile << endl;
        return 2;
    }

    fout << "Rezultatul convolutiei (" << (p == 1 ? "secvential" : "paralel") << "):\n";
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++)
            fout << setw(6) << matriceOutput[idx(i, j, m)] << " ";
        fout << "\n";
    }
    fout.close();

    delete[] matriceInput;
    delete[] matriceOutput;
    delete[] kernel;

    return 0;
}
