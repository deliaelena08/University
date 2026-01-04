#include <iostream>
#include <thread>
#include <vector>
#include <fstream>
#include <chrono>
#include <iomanip>
#include <mutex>
#include <condition_variable>

using namespace std;
using namespace std::chrono;

int n, m;
int* matriceInput;
int* kernel;
const int k = 3;
const int halfK = k / 2;

struct Barrier {
    int count, waiting, phase;
    mutex mtx;
    condition_variable cv;
    Barrier(int c) : count(c), waiting(0), phase(0) {}

    void wait() {
        unique_lock<mutex> lock(mtx);
        int local_phase = phase;
        waiting++;
        if (waiting == count) {
            waiting = 0;
            phase++;
            cv.notify_all();
        } else {
            cv.wait(lock, [&]{ return local_phase != phase; });
        }
    }
};

inline int idx(int i, int j) {
    if (i < 0) i = 0;
    if (i >= n) i = n - 1;
    if (j < 0) j = 0;
    if (j >= m) j = m - 1;
    return i * m + j;
}

int convolutiePixel(int i, int j, const vector<int>& prevLine, const vector<int>& currLine, const vector<int>& nextLine) {
    int sum = 0;
    for (int u = 0; u < k; u++) {
        for (int v = 0; v < k; v++) {
            int ii = i + u - halfK;
            int jj = j + v - halfK;

            if (jj < 0) jj = 0;
            if (jj >= m) jj = m - 1;

            int val;
            if (ii < i)
                val = prevLine[jj];
            else if (ii == i)
                val = currLine[jj];
            else // ii > i
                val = nextLine[jj];

            sum += val * kernel[u * k + v];
        }
    }
    return sum;
}

void worker(int start, int end, Barrier& bar) {
    vector<int> prevLine(m), currLine(m), nextLine(m), tempLine(m);
    vector<int> topBuffer(m), bottomBuffer(m);

    for (int j = 0; j < m; j++) {
        prevLine[j] = matriceInput[idx(start - 1, j)];
        currLine[j] = matriceInput[idx(start, j)];
        nextLine[j] = matriceInput[idx(start + 1, j)];
    }

    bar.wait();

    for (int i = start; i < end; i++) {
        int nextRow = i + 1 >= n ? n - 1 : i + 1;
        for (int j = 0; j < m; j++)
            nextLine[j] = matriceInput[idx(nextRow, j)];

        for (int j = 0; j < m; j++)
            tempLine[j] = convolutiePixel(i, j, prevLine, currLine, nextLine);

        if (i == start)
            topBuffer = tempLine;
        else if (i == end - 1)
            bottomBuffer = tempLine;
        else
            for (int j = 0; j < m; j++)
                matriceInput[idx(i, j)] = tempLine[j];

        prevLine = currLine;
        currLine = nextLine;
    }

    bar.wait();

    for (int j = 0; j < m; j++) {
        matriceInput[idx(start, j)] = topBuffer[j];
        matriceInput[idx(end - 1, j)] = bottomBuffer[j];
    }
}


void convolutie_paralela(int p) {
    int numThreads = min(p, n);
    Barrier bar(numThreads);
    vector<thread> threads;

    int linesPerThread = n / numThreads;
    int remainder = n % numThreads;
    int start = 0;

    for (int t = 0; t < numThreads; t++) {
        int end = start + linesPerThread + (remainder-- > 0 ? 1 : 0);
        threads.emplace_back(worker, start, end, ref(bar));
        start = end;
    }

    for (auto& th : threads)
        th.join();
}

void convolutie_secventiala() {
    vector<int> prevLine(m), currLine(m), nextLine(m), tempLine(m);

    for (int j = 0; j < m; j++) {
        prevLine[j] = matriceInput[idx(-1, j)];
        currLine[j] = matriceInput[idx(0, j)];
        nextLine[j] = matriceInput[idx(1, j)];
    }

    for (int i = 0; i < n; i++) {
        int nextRow = i + 1 >= n ? n - 1 : i + 1;
        for (int j = 0; j < m; j++)
            nextLine[j] = matriceInput[idx(nextRow, j)];

        for (int j = 0; j < m; j++)
            tempLine[j] = convolutiePixel(i, j, prevLine, currLine, nextLine);

        for (int j = 0; j < m; j++)
            matriceInput[idx(i, j)] = tempLine[j];

        prevLine = currLine;
        currLine = nextLine;
    }
}

void scrieFisier(const string& filename) {
    ofstream fout(filename);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++)
            fout << matriceInput[idx(i, j)] << " ";
        fout << "\n";
    }
}

int main1(int argc, char* argv[]) {
    ifstream fin("date.txt");
    fin >> n >> m;
    matriceInput = new int[n * m];
    kernel = new int[k * k];

    for (int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            fin >> matriceInput[idx(i, j)];

    for (int i = 0; i < k; i++)
        for (int j = 0; j < k; j++)
            fin >> kernel[i * k + j];
    fin.close();

    int p = 1;
    if (argc > 1)
        p = stoi(argv[1]);

    auto startTime = high_resolution_clock::now();
    if (p == 1)
        convolutie_secventiala();
    else
        convolutie_paralela(p);
    auto endTime = high_resolution_clock::now();

    cout << fixed << setprecision(6) << duration_cast<microseconds>(endTime - startTime).count() / 1000.0;

    scrieFisier(p == 1 ? "output_secvential.txt" : "output_paralel.txt");

    delete[] matriceInput;
    delete[] kernel;
    return 0;
}
