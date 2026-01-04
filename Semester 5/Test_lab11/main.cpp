#include <mpi.h>
#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

// h(number, X)
void h(int &number, int X) {
    if (number < X) {
        number = number * X;
    } else {
        number = number / X;
    }
}

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int rank, size;
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    if (size < 2) {
        if (rank == 0) {
            cerr << "Error: Run with at least 2 processes!" << endl;
        }
        MPI_Finalize();
        return 0;
    }

    int X = 0;
    int N = 0;
    vector<int> numbers;

    // Etapa 1
    if (rank == 0) {
        ifstream fin("numbers.txt");
        if (!fin.is_open()) {
            cerr << "Error: Cannot open numbers.txt" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        fin >> X;

        int val;
        while (fin >> val) {
            numbers.push_back(val);
        }
        fin.close();
        N = numbers.size();

        cout << "[Master] N=" << N << ", X=" << X << endl;

        if (N == 0) {
            cerr << "Error: numbers.txt is empty or could not be read!" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        if (N % size != 0) {
            cerr << "Error: N (" << N << ") must be divisible by P (" << size << ")" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }
    }

    // Broadcast N and X
    MPI_Bcast(&N, 1, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Bcast(&X, 1, MPI_INT, 0, MPI_COMM_WORLD);

    int chunk = N / size;
    vector<int> local_vec(chunk);

    MPI_Scatter(numbers.data(), chunk, MPI_INT,
                local_vec.data(), chunk, MPI_INT,
                0, MPI_COMM_WORLD);

    // Etapa 2

    long long local_product = 1;

    for (int i = 0; i < chunk; i++) {
        local_product *= local_vec[i];
        h(local_vec[i], X);
    }

    // transmiterea in cascada a produsului
    long long received_product = 1;
    long long cumulative_product = local_product;

    if (rank == 0) {
        MPI_Send(&local_product, 1, MPI_LONG_LONG, 1, 0, MPI_COMM_WORLD);
    } else {
        MPI_Recv(&received_product, 1, MPI_LONG_LONG, rank - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        cumulative_product = local_product * received_product;

        if (rank < size - 1) {
            MPI_Send(&cumulative_product, 1, MPI_LONG_LONG, rank + 1, 0, MPI_COMM_WORLD);
        } else {
            MPI_Send(&cumulative_product, 1, MPI_LONG_LONG, 0, 0, MPI_COMM_WORLD);
        }
    }

    if (rank != 0) {
        MPI_Send(local_vec.data(), chunk, MPI_INT, 0, 1, MPI_COMM_WORLD);
    }

    //Etapa 3
    if (rank == 0) {
        vector<int> final_result(N);

        // copiem mai intai ce a prelucrat master
        for (int i = 0; i < chunk; i++) {
            final_result[i] = local_vec[i];
        }

        // primim de la celelalte procese vectorul modificat
        for (int p = 1; p < size; ++p) {
            MPI_Recv(final_result.data() + (p * chunk), chunk, MPI_INT, p, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        }

        // scriem vectorul in fisier
        ofstream fout("result.txt");
        for (int val : final_result) {
            fout << val << endl;
        }
        fout.close();
        cout << "Processed vector was written in result.txt" << endl;

        // primim inmultirea
        long long total_product = 0;
        MPI_Recv(&total_product, 1, MPI_LONG_LONG, size - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        cout << "Final product of the original numbers is: " << total_product << endl;
    }

    MPI_Finalize();
    return 0;
}