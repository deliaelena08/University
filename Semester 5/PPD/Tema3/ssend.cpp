#include <mpi.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <chrono>
using namespace std;

int main22222222222(int argc, char **argv) {
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
        int id_curent = 1;
        int total_citit = 0;

        auto start = chrono::high_resolution_clock::now();

        while (total_citit < N && id_curent < size) {
            int to_read = min(chunk, N - total_citit);

            for (int i = to_read - 1; i >= 0; --i) {
                char c1 = '0', c2 = '0';
                if (f1 >> c1) buf1[i] = c1 - '0';
                if (f2 >> c2) buf2[i] = c2 - '0';
            }

            MPI_Ssend(&to_read, 1, MPI_INT, id_curent, TAG_LEN, MPI_COMM_WORLD);
            MPI_Ssend(buf1.data(), to_read, MPI_INT, id_curent, TAG_A, MPI_COMM_WORLD);
            MPI_Ssend(buf2.data(), to_read, MPI_INT, id_curent, TAG_B, MPI_COMM_WORLD);

            total_citit += to_read;
            id_curent++;
        }

        for (int i = 1; i < id_curent; ++i) {
            int len;
            MPI_Status st;
            MPI_Recv(&len, 1, MPI_INT, i, TAG_RESULT, MPI_COMM_WORLD, &st);
            vector<int> rez(len);
            MPI_Recv(rez.data(), len, MPI_INT, i, TAG_RESULT, MPI_COMM_WORLD, &st);

            int carry;
            MPI_Recv(&carry, 1, MPI_INT, i, TAG_CARRY, MPI_COMM_WORLD, &st);

            if (carry) fout << carry;
            for (int j = len - 1; j >= 0; --j)
                fout << rez[j];
        }

        auto end = chrono::high_resolution_clock::now();
        cout << "Timp total: " << chrono::duration<double, milli>(end - start).count() << " ms\n";
    }
    else {
        MPI_Status st;
        int len;
        if (MPI_Recv(&len, 1, MPI_INT, 0, TAG_LEN, MPI_COMM_WORLD, &st) == MPI_SUCCESS) {
            vector<int> a(len), b(len), sum(len);
            MPI_Recv(a.data(), len, MPI_INT, 0, TAG_A, MPI_COMM_WORLD, &st);
            MPI_Recv(b.data(), len, MPI_INT, 0, TAG_B, MPI_COMM_WORLD, &st);

            int carryIn = 0;
            if (rank > 1)
                MPI_Recv(&carryIn, 1, MPI_INT, rank - 1, TAG_CARRY, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            int carryOut = carryIn;
            for (int i = 0; i < len; ++i) {
                int s = a[i] + b[i] + carryOut;
                sum[i] = s % 10;
                carryOut = s / 10;
            }

            if (rank < size - 1)
                MPI_Ssend(&carryOut, 1, MPI_INT, rank + 1, TAG_CARRY, MPI_COMM_WORLD);

            MPI_Ssend(&len, 1, MPI_INT, 0, TAG_RESULT, MPI_COMM_WORLD);
            MPI_Ssend(sum.data(), len, MPI_INT, 0, TAG_RESULT, MPI_COMM_WORLD);
            MPI_Ssend(&carryOut, 1, MPI_INT, 0, TAG_CARRY, MPI_COMM_WORLD);
        }
    }

    MPI_Finalize();
    return 0;
}
