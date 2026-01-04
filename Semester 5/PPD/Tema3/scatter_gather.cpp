#include <mpi.h>
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <algorithm>
#include <chrono>
#include <cmath>

using namespace std;

vector<int> readBigIntFromFileVectorScatter(const string& path, int& numDigits) {
    ifstream file(path);
    if (!file.is_open()) {
        cerr << "Error opening file: " << path << endl;
        numDigits = 0;
        return {};
    }

    char firstChar;
    file.get(firstChar);
    if ((unsigned char)firstChar == 0xEF) {
        file.ignore(2);
    } else {
        file.unget();
    }

    if (!(file >> numDigits)) {
        cerr << "Error reading number of digits from file: " << path << endl;
        numDigits = 0;
        return {};
    }

    if (numDigits <= 0) {
        cerr << "Invalid number of digits (" << numDigits << ") in file: " << path << endl;
        numDigits = 0;
        return {};
    }

    vector<int> digits(numDigits);
    for (int i = numDigits - 1; i >= 0; --i) {
        char ch;
        if (!(file >> ch)) {
            cerr << "Not enough digits in file: " << path << endl;
            numDigits = 0;
            return {};
        }
        if (ch < '0' || ch > '9') {
            cerr << "Invalid character '" << ch << "' in file: " << path << endl;
            numDigits = 0;
            return {};
        }
        digits[i] = ch - '0';
    }

    return digits;
}

void writeResultScatter(const string &path, const vector<int> &result) {
    ofstream file(path);
    if (!file.is_open()) {
        cerr << "Error writing to file: " << path << endl;
        return;
    }

    int last = result.size() - 1;
    while (last >= 0 && result[last] == 0) {
        last--;
    }

    if (last < 0) {
        file << "0" << endl;
    } else {
        for (int i = last; i >= 0; --i) {
            file << result[i];
        }
        file << endl;
    }

    file.close();
}

int main(int argc, char **argv) {
    MPI_Init(&argc, &argv);

    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    auto start = chrono::high_resolution_clock::now();

    int N = 0;
    vector<int> global_num1, global_num2;
    int chunk_size = 0;
    string outFile;

    if (rank == 0) {
        if (argc < 4) {
            cerr << "Usage: mpirun -np <p> " << argv[0]
                 << " <Numar1.txt> <Numar2.txt> <Numar3.txt>\n";
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        string file1 = argv[1];
        string file2 = argv[2];
        outFile = argv[3];

        int n1, n2;
        vector<int> temp_num1 = readBigIntFromFileVectorScatter(file1, n1);
        vector<int> temp_num2 = readBigIntFromFileVectorScatter(file2, n2);

        if (temp_num1.empty() || temp_num2.empty()) {
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        int max_initial_digits = max(n1, n2);

        N = max_initial_digits;
        if (N % size != 0) {
            N = ((N / size) + 1) * size;
        }

        chunk_size = N / size;

        temp_num1.resize(N, 0);
        temp_num2.resize(N, 0);

        global_num1 = temp_num1;
        global_num2 = temp_num2;
    }

    MPI_Bcast(&N, 1, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Bcast(&chunk_size, 1, MPI_INT, 0, MPI_COMM_WORLD);

    vector<int> local_a(chunk_size), local_b(chunk_size), local_sum(chunk_size);

    MPI_Scatter(rank == 0 ? global_num1.data() : nullptr, chunk_size, MPI_INT,
                local_a.data(), chunk_size, MPI_INT, 0, MPI_COMM_WORLD);

    MPI_Scatter(rank == 0 ? global_num2.data() : nullptr, chunk_size, MPI_INT,
                local_b.data(), chunk_size, MPI_INT, 0, MPI_COMM_WORLD);

    int carryIn = 0;
    if (rank > 0) {
        MPI_Recv(&carryIn, 1, MPI_INT, rank - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    }

    int carryOut = carryIn;
    for (int i = 0; i < chunk_size; ++i) {
        int s = local_a[i] + local_b[i] + carryOut;
        local_sum[i] = s % 10;
        carryOut = s / 10;
    }

    if (rank < size - 1) {
        MPI_Send(&carryOut, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD);
    }

    vector<int> result(N);
    MPI_Gather(local_sum.data(), chunk_size, MPI_INT,
               rank == 0 ? result.data() : nullptr, chunk_size, MPI_INT,
               0, MPI_COMM_WORLD);

    if (rank == 0) {
        int final_carry = 0;
        if (size > 1) {
            MPI_Recv(&final_carry, 1, MPI_INT, size - 1, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        } else {
            final_carry = carryOut;
        }

        if (final_carry > 0) {
            result.push_back(final_carry);
        }
        auto end = chrono::high_resolution_clock::now();
        double ms = chrono::duration<double, milli>(end - start).count();

        cout << "Timp total: " << ms << " ms" << endl;
        writeResultScatter(outFile, result);
    }

    if (rank == size - 1 && size > 1) {
        MPI_Send(&carryOut, 1, MPI_INT, 0, 1, MPI_COMM_WORLD);
    }

    MPI_Finalize();
    return 0;
}