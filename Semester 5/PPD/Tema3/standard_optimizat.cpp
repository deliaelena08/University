#include <mpi.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <chrono>

using namespace std;

int main22(int argc, char **argv) {
    MPI_Init(&argc, &argv);

    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    const int TAG_LEN = 1, TAG_A = 2, TAG_B = 3, TAG_CARRY = 4, TAG_RESULT = 5;

    if (rank == 0) {
        if (argc < 4) {
            cerr << "Utilizare: mpirun -np <p> " << argv[0]
                 << " <Numar1.txt> <Numar2.txt> <Numar3.txt>\n";
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        ifstream f1(argv[1]), f2(argv[2]);
        ofstream fout(argv[3]);
        if (!f1.is_open() || !f2.is_open() || !fout.is_open()) {
            cerr << "Eroare la deschiderea fisierelor.\n";
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        int n1, n2;
        f1 >> n1;
        f2 >> n2;
        int N = max(n1, n2);

        int workers = size - 1;
        int chunk = (N + workers - 1) / workers;

        vector<int> buf1(chunk), buf2(chunk);
        int totalRead = 0;
        int curWorker = 1;

        auto start = chrono::high_resolution_clock::now();

        while (totalRead < N && curWorker <= workers) {
            int toRead = min(chunk, N - totalRead);

            for (int i = toRead - 1; i >= 0; --i) {
                char c1 = '0', c2 = '0';
                if (f1 >> c1) buf1[i] = c1 - '0';
                else buf1[i] = 0;
                if (f2 >> c2) buf2[i] = c2 - '0';
                else buf2[i] = 0;
            }

            MPI_Send(&toRead, 1, MPI_INT, curWorker, TAG_LEN, MPI_COMM_WORLD);
            MPI_Send(buf1.data(), toRead, MPI_INT, curWorker, TAG_A, MPI_COMM_WORLD);
            MPI_Send(buf2.data(), toRead, MPI_INT, curWorker, TAG_B, MPI_COMM_WORLD);

            totalRead += toRead;
            curWorker++;
        }

        int carry = 0;
        for (int i = 1; i <= workers; ++i) {
            MPI_Status status;
            int len;
            MPI_Recv(&len, 1, MPI_INT, i, TAG_RESULT, MPI_COMM_WORLD, &status);
            vector<int> partial(len);
            MPI_Recv(partial.data(), len, MPI_INT, i, TAG_RESULT, MPI_COMM_WORLD, &status);
            int carryOut;
            MPI_Recv(&carryOut, 1, MPI_INT, i, TAG_CARRY, MPI_COMM_WORLD, &status);

            for (int j = 0; j < len; ++j) {
                int sumWithCarry = partial[j] + carry;
                partial[j] = sumWithCarry % 10;
                carry = sumWithCarry / 10;
            }

            for (int j = len - 1; j >= 0; --j)
                fout << partial[j];
        }

        if (carry) fout << carry;

        auto end = chrono::high_resolution_clock::now();
        cout << "\nTimp total: " << chrono::duration<double, milli>(end - start).count() << " ms\n";
    }
    else {
        MPI_Status status;
        int len;
        MPI_Recv(&len, 1, MPI_INT, 0, TAG_LEN, MPI_COMM_WORLD, &status);

        vector<int> a(len), b(len), sum(len);
        MPI_Recv(a.data(), len, MPI_INT, 0, TAG_A, MPI_COMM_WORLD, &status);
        MPI_Recv(b.data(), len, MPI_INT, 0, TAG_B, MPI_COMM_WORLD, &status);

        int carryOut = 0;
        for (int i = 0; i < len; ++i) {
            int s = a[i] + b[i] + carryOut;
            sum[i] = s % 10;
            carryOut = s / 10;
        }

        int carryIn = 0;
        if (rank > 1)
            MPI_Recv(&carryIn, 1, MPI_INT, rank - 1, TAG_CARRY, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        if (carryIn > 0) {
            int tempCarry = carryIn;
            for (int i = 0; i < len && tempCarry > 0; ++i) {
                int s = sum[i] + tempCarry;
                sum[i] = s % 10;
                tempCarry = s / 10;
            }
            carryOut += tempCarry;
        }

        if (rank < size - 1)
            MPI_Send(&carryOut, 1, MPI_INT, rank + 1, TAG_CARRY, MPI_COMM_WORLD);

        MPI_Send(&len, 1, MPI_INT, 0, TAG_RESULT, MPI_COMM_WORLD);
        MPI_Send(sum.data(), len, MPI_INT, 0, TAG_RESULT, MPI_COMM_WORLD);
        MPI_Send(&carryOut, 1, MPI_INT, 0, TAG_CARRY, MPI_COMM_WORLD);
    }

    MPI_Finalize();
    return 0;
}
