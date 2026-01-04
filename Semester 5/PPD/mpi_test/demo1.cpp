#include <mpi.h>
#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

// Function h(number, X)
void process_number(long long &number, int X) {
    if (number < X) {
        number = number * X; // Case 1
    } else {
        number = number / X; // Case 2
    }
}

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    int X = 0;
    int N = 0;
    vector<long long> numbers; // Large vector (only for Rank 0)

    // --- STAGE 1: READ AND SCATTER ---

    if (rank == 0) {
        ifstream fin("numbers.txt");
        if (!fin.is_open()) {
            cerr << "Error: Cannot open numbers.txt" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        // 1. Read X from the first line
        fin >> X;

        // 2. Read all remaining numbers
        long long val;
        while (fin >> val) {
            numbers.push_back(val);
        }
        fin.close();

        N = numbers.size();

        cout << "[Master] Read " << N << " numbers. X is " << X << "." << endl;

        // Validation for Scatter (N must be divisible by P)
        if (N % size != 0) {
            cerr << "Error: N (" << N << ") must be divisible by P (" << size << ") for MPI_Scatter." << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }
    }

    // 1. Broadcast N and X to all processes
    MPI_Bcast(&N, 1, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Bcast(&X, 1, MPI_INT, 0, MPI_COMM_WORLD);

    // 2. Prepare local buffer
    int chunk_size = N / size;
    vector<long long> local_vec(chunk_size);

    // 3. MPI_Scatter divides 'numbers' into equal chunks
    MPI_Scatter(numbers.data(), chunk_size, MPI_LONG_LONG,
                local_vec.data(), chunk_size, MPI_LONG_LONG,
                0, MPI_COMM_WORLD);

    // --- STAGE 2: PROCESSING AND CASCADE ---

    // A. Local Calculation
    long long local_product = 1;

    for (int i = 0; i < chunk_size; ++i) {
        // a. Calculate product of ORIGINAL numbers in the segment
        local_product *= local_vec[i];

        // b. Modify the number according to function h(number, X)
        process_number(local_vec[i], X);
    }

    // B. Cascade Transmission of Product
    // Logic:
    // Rank 0 sends to 1.
    // Rank i receives from i-1, multiplies, sends to i+1.
    // Rank P-1 sends back to 0.

    long long received_product = 1;
    long long cumulative_product = local_product;

    if (rank == 0) {
        // Step a: Rank 0 sends its product to Rank 1
        MPI_Send(&local_product, 1, MPI_LONG_LONG, 1, 0, MPI_COMM_WORLD);
    }
    else {
        // Step i: Receive from Rank-1
        MPI_Recv(&received_product, 1, MPI_LONG_LONG, rank - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        // Step ii: Multiply
        cumulative_product = received_product * local_product;

        // Step iii: Transmit to Rank+1 (if not the last one)
        if (rank < size - 1) {
            MPI_Send(&cumulative_product, 1, MPI_LONG_LONG, rank + 1, 0, MPI_COMM_WORLD);
        }
        // Step iv: If last process, send to Rank 0
        else {
            MPI_Send(&cumulative_product, 1, MPI_LONG_LONG, 0, 0, MPI_COMM_WORLD);
        }
    }

    // C. Send processed vectors back to 0 (Manual, using MPI_Send)
    if (rank != 0) {
        MPI_Send(local_vec.data(), chunk_size, MPI_LONG_LONG, 0, 1, MPI_COMM_WORLD);
    }

    // --- STAGE 3: RECONSTRUCTION AND FINAL RESULT (RANK 0 ONLY) ---

    if (rank == 0) {
        vector<long long> final_result(N);

        // 1. Copy Master's own processed part
        for (int i = 0; i < chunk_size; ++i) {
            final_result[i] = local_vec[i];
        }

        // 2. Receive parts from other processes
        for (int p = 1; p < size; ++p) {
            MPI_Recv(&final_result[p * chunk_size], chunk_size, MPI_LONG_LONG, p, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        }

        // 3. Write to result.txt
        ofstream fout("result.txt");
        for (long long val : final_result) {
            fout << val << endl;
        }
        fout.close();
        cout << "[Master] Processed vector written to result.txt." << endl;

        // 4. Receive Final Product from Rank P-1
        long long total_final_product;
        MPI_Recv(&total_final_product, 1, MPI_LONG_LONG, size - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        cout << "------------------------------------------------" << endl;
        cout << "[Master] Final product of original numbers: " << total_final_product << endl;
        cout << "------------------------------------------------" << endl;
    }

    MPI_Finalize();
    return 0;
}