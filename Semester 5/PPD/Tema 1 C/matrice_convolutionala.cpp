#include <iostream>
#include <thread>
#include <vector>
#include <fstream>
#include <chrono>
#include <iomanip>

using namespace std;
using namespace std::chrono;

int n, m, k;
vector<vector<int>> matriceInput;
vector<vector<int>> matriceOutput;
vector<vector<int>> kernel;

int valoareBordata(int i, int j) {
    if (i < 0) i = 0;
    if (i >= n) i = n - 1;
    if (j < 0) j = 0;
    if (j >= m) j = m - 1;
    return matriceInput[i][j];
}
//.\scriptC.ps1 matrice.exe 4 5
void convolutie_worker(int linieStart, int linieEnd) {
    for (int i = linieStart; i < linieEnd; i++) {
        for (int j = 0; j < m; j++) {
            int suma = 0;
            for (int u = 0; u < k; u++) {
                for (int v = 0; v < k; v++) {
                    int ii = i + u - k / 2;
                    int jj = j + v - k / 2;
                    suma += valoareBordata(ii, jj) * kernel[u][v];
                }
            }
            matriceOutput[i][j] = suma;
        }
    }
}

void convolutie_secventiala() {
    convolutie_worker(0, n);
}

void convolutie_paralela(int numarThreads) {
    vector<thread> threaduri(numarThreads);
    int liniiPeThread = n / numarThreads;
    int rest = n % numarThreads;
    int startLinie = 0;

    for (int t = 0; t < numarThreads; t++) {
        int endLinie = startLinie + liniiPeThread;
        if (rest > 0) {
            endLinie++;
            rest--;
        }
        threaduri[t] = thread(convolutie_worker, startLinie, endLinie);
        startLinie = endLinie;
    }

    for (auto &th : threaduri)
        th.join();
}

void convolutie_worker_vertical(int colStart, int colEnd) {
    for (int i = 0; i < n; i++) {
        for (int j = colStart; j < colEnd; j++) {
            int suma = 0;
            for (int u = 0; u < k; u++) {
                for (int v = 0; v < k; v++) {
                    int ii = i + u - k / 2;
                    int jj = j + v - k / 2;
                    suma += valoareBordata(ii, jj) * kernel[u][v];
                }
            }
            matriceOutput[i][j] = suma;
        }
    }
}

void convolutie_paralela_vertical(int numarThreads) {
    vector<thread> threads(numarThreads);
    int coloanePeThread = m / numarThreads;
    int rest = m % numarThreads;
    int startCol = 0;

    for (int t = 0; t < numarThreads; t++) {
        int endCol = startCol + coloanePeThread;
        if (rest > 0) { endCol++; rest--; }
        threads[t] = thread(convolutie_worker_vertical, startCol, endCol);
        startCol = endCol;
    }

    for (auto &th : threads)
        th.join();
}

int main1(int argc, char* argv[]) {
    ifstream fin("date.txt");
    if (!fin.is_open()) {
        cerr << "Eroare: nu pot deschide fisierul date.txt\n";
        return 1;
    }
    fin >> n >> m >> k;

    matriceInput.assign(n, vector<int>(m));
    matriceOutput.assign(n, vector<int>(m));
    kernel.assign(k, vector<int>(k));

    for (int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            fin >> matriceInput[i][j];

    for (int i = 0; i < k; i++)
        for (int j = 0; j < k; j++)
            fin >> kernel[i][j];

    int p = 1;
    if (argc > 1)
        p = stoi(argv[1]);

    auto start = high_resolution_clock::now();

    //daca nr de thread-uri e 1 -> convolutie secventiala, altfel paralela
    // if (p == 1)
    //     convolutie_secventiala();
    // else
    //     convolutie_paralela(p);

    if (p == 1)
        convolutie_secventiala();
    else
        convolutie_paralela_vertical(p);


    auto end = high_resolution_clock::now();

    double durataMicro = duration_cast<microseconds>(end - start).count();
    double durataMs = durataMicro / 1000.0;
    cout << fixed << setprecision(6)  << durataMs;
    fin.close();

    string outputFile = (p == 1) ? "output_seq.txt" : "output_par.txt";
    ofstream fout(outputFile);

    if (!fout.is_open()) {
        cerr << "Eroare deschidere fisier " << outputFile << endl;
        return 2;
    }

    fout << "Rezultatul convolutiei (" << (p == 1 ? "secvential" : "paralel") << "):\n";

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            fout << setw(6) << matriceOutput[i][j] << " ";
        }
        fout << "\n";
    }

    fout.close();
    return 0;
}
