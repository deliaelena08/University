#include <mpi.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <chrono>

using namespace std;

void writeResultAsincron(const string &path, const vector<int> &result, int carry) {
    ofstream file(path);
    if (!file.is_open()) {
        cerr << "Error writing to file: " << path << endl;
        return;
    }

    if (carry) file << carry;
    for (int i = result.size() - 1; i >= 0; --i)
        file << result[i];
    file << endl;
    file.close();
}

int main2312(int argc, char **argv) {
    MPI_Init(&argc, &argv);

    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    const int TAG_A = 0;
    const int TAG_B = 1;
    const int TAG_LEN = 2;
    const int TAG_RESULT = 3;
    const int TAG_CARRY = 4;

    auto start = chrono::high_resolution_clock::now();

    if (rank == 0) {
        if (argc < 4) {
            cerr << "Usage: mpirun -np <p> " << argv[0]
                 << " <Numar1.txt> <Numar2.txt> <Numar3.txt>\n";
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        string file1 = argv[1];
        string file2 = argv[2];
        string outFile = argv[3];

        ifstream f1(file1);
        ifstream f2(file2);

        if (!f1.is_open() || !f2.is_open()) {
            cerr << "Error opening input files!\n";
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        int n1, n2;
        f1 >> n1;
        f2 >> n2;

        int maxDigits = max(n1, n2);
        int workers = size - 1;
        int chunk = (workers > 0) ? (maxDigits + workers - 1) / workers : maxDigits;

        vector<MPI_Request> sendReqs;
        vector<MPI_Request> recvReqs;
        vector<int> result(maxDigits + 1, 0);

        int id_curent = 1;
        int digits_sent = 0;

        while (id_curent <= workers && digits_sent < maxDigits) {
            int to_send = min(chunk, maxDigits - digits_sent);

            vector<int> a(to_send, 0), b(to_send, 0);

            for (int i = 0; i < to_send; ++i) {
                char ca, cb;
                if (f1 >> ca) a[i] = ca - '0'; else a[i] = 0;
                if (f2 >> cb) b[i] = cb - '0'; else b[i] = 0;
            }

            reverse(a.begin(), a.end());
            reverse(b.begin(), b.end());

            MPI_Request r1, r2, r3;
            MPI_Isend(&to_send, 1, MPI_INT, id_curent, TAG_LEN, MPI_COMM_WORLD, &r1);
            MPI_Isend(a.data(), to_send, MPI_INT, id_curent, TAG_A, MPI_COMM_WORLD, &r2);
            MPI_Isend(b.data(), to_send, MPI_INT, id_curent, TAG_B, MPI_COMM_WORLD, &r3);

            sendReqs.push_back(r1);
            sendReqs.push_back(r2);
            sendReqs.push_back(r3);

            MPI_Request r4;
            MPI_Irecv(result.data() + digits_sent, to_send, MPI_INT, id_curent, TAG_RESULT, MPI_COMM_WORLD, &r4);
            recvReqs.push_back(r4);

            digits_sent += to_send;
            id_curent++;
        }

        if (!sendReqs.empty()) MPI_Waitall((int)sendReqs.size(), sendReqs.data(), MPI_STATUSES_IGNORE);
        if (!recvReqs.empty()) MPI_Waitall((int)recvReqs.size(), recvReqs.data(), MPI_STATUSES_IGNORE);

        int finalCarry = 0;
        if (workers > 0) {
            MPI_Request carryReq;
            MPI_Irecv(&finalCarry, 1, MPI_INT, workers, TAG_CARRY, MPI_COMM_WORLD, &carryReq);
            MPI_Wait(&carryReq, MPI_STATUS_IGNORE);
        }

        auto end = chrono::high_resolution_clock::now();
        double ms = chrono::duration<double, milli>(end - start).count();

        cout << "Timp total: " << ms << " ms" << endl;

        writeResultAsincron(outFile, result, finalCarry);
    }

    else {
        int len;
        MPI_Request lenReq;
        MPI_Irecv(&len, 1, MPI_INT, 0, TAG_LEN, MPI_COMM_WORLD, &lenReq);
        MPI_Wait(&lenReq, MPI_STATUS_IGNORE);

        if (len <= 0) {
            MPI_Finalize();
            return 0;
        }

        vector<int> a(len), b(len), sum(len);
        MPI_Request r1, r2;
        MPI_Irecv(a.data(), len, MPI_INT, 0, TAG_A, MPI_COMM_WORLD, &r1);
        MPI_Irecv(b.data(), len, MPI_INT, 0, TAG_B, MPI_COMM_WORLD, &r2);
        MPI_Waitall(2, (MPI_Request[]){r1, r2}, MPI_STATUSES_IGNORE);

        int carryIn = 0, carryOut = 0;
        MPI_Request carryReq;
        bool hasCarry = false;
        if (rank > 1) {
            MPI_Irecv(&carryIn, 1, MPI_INT, rank - 1, TAG_CARRY, MPI_COMM_WORLD, &carryReq);
            hasCarry = true;
        }

        for (int i = 0; i < len; ++i) {
            int s = a[i] + b[i] + carryOut;
            sum[i] = s % 10;
            carryOut = s / 10;
        }

        if (hasCarry) {
            MPI_Wait(&carryReq, MPI_STATUS_IGNORE);
            if (carryIn > 0) {
                int temp = carryIn;
                for (int i = 0; i < len && temp > 0; ++i) {
                    int s = sum[i] + temp;
                    sum[i] = s % 10;
                    temp = s / 10;
                }
                carryOut += temp;
            }
        }

        MPI_Request sends[3];
        int scount = 0;

        MPI_Isend(sum.data(), len, MPI_INT, 0, TAG_RESULT, MPI_COMM_WORLD, &sends[scount++]);

        if (rank < size - 1)
            MPI_Isend(&carryOut, 1, MPI_INT, rank + 1, TAG_CARRY, MPI_COMM_WORLD, &sends[scount++]);

        if (rank == size - 1)
            MPI_Isend(&carryOut, 1, MPI_INT, 0, TAG_CARRY, MPI_COMM_WORLD, &sends[scount++]);

        MPI_Waitall(scount, sends, MPI_STATUSES_IGNORE);
    }

    MPI_Finalize();
    return 0;
}