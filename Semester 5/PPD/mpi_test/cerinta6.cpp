#include <mpi.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <cfloat>

using namespace std;

double calculeazaF(double x, double A, double B, double C, double D) {
    return A * (B * x + C) + D;
}

int main8(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (size < 2) {
        if (rank == 0) cerr << "Eroare: Ruleaza cu mpiexec -n <P> (min 2)" << endl;
        MPI_Finalize();
        return 0;
    }

    int num_workers = size - 1;
    double coef[4];
    int N = 0;

    // --- ETAPA 1: MASTER (0) INIT ---
    if (rank == 0) {
        ifstream f_func("functie.txt");
        if(f_func.is_open()) {
            f_func >> coef[0] >> coef[1] >> coef[2] >> coef[3];
            f_func.close();
        }

        ifstream f_in("input.txt");
        double tmp;
        while(f_in >> tmp) N++;
        f_in.close();
    }

    MPI_Bcast(coef, 4, MPI_DOUBLE, 0, MPI_COMM_WORLD);
    MPI_Bcast(&N, 1, MPI_INT, 0, MPI_COMM_WORLD);

    // Calculam distributia cu REST
    int base_chunk = N / num_workers;
    int remainder = N % num_workers;

    // --- ETAPA 2: MASTER (0) DISTRIBUIE SI COLECTEAZA ---
    if (rank == 0) {
        ifstream f_in("input.txt");

        // A. Bucla "Repeta o citeste ... o trimite"
        // Atentie: Aici chunk-ul difera de la proces la proces daca avem rest!
        for (int i = 1; i <= num_workers; ++i) {
            // Calculam cat primeste worker-ul 'i'
            // Daca (i-1) < remainder, primeste base+1, altfel base
            int current_count = base_chunk + ((i - 1) < remainder ? 1 : 0);

            vector<double> buffer(current_count);
            for (int j = 0; j < current_count; ++j) {
                f_in >> buffer[j];
            }

            // Trimitem catre worker 'i'
            MPI_Send(buffer.data(), current_count, MPI_DOUBLE, i, 0, MPI_COMM_WORLD);
        }
        f_in.close();

        // B. Bucla "Repeta o Primeste ... o Scrie"
        ofstream f_out("output.txt");

        for (int i = 1; i <= num_workers; ++i) {
            // Trebuie sa stim cat asteptam de la worker 'i'
            int current_count = base_chunk + ((i - 1) < remainder ? 1 : 0);

            vector<double> recv_buffer(current_count);
            MPI_Recv(recv_buffer.data(), current_count, MPI_DOUBLE, i, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            for (double val : recv_buffer) f_out << val << " ";
        }
        f_out.close();
        cout << "[Master] Datele (inclusiv restul) au fost procesate si salvate." << endl;
    }

    // --- ETAPA 3: WORKERS (Rank > 0) ---
    else {
        // 1. Workerul isi calculeaza singur cat trebuie sa primeasca
        // (rank - 1) este indexul workerului (0, 1, 2...)
        int my_count = base_chunk + ((rank - 1) < remainder ? 1 : 0);

        vector<double> local_chunk(my_count);
        vector<double> results(my_count);

        double local_max = -DBL_MAX;
        double local_min = DBL_MAX;

        // 2. Primeste date (Chunk-ul lui specific)
        MPI_Recv(local_chunk.data(), my_count, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        // 3. Calculeaza F(x)
        for (int i = 0; i < my_count; ++i) {
            double val = calculeazaF(local_chunk[i], coef[0], coef[1], coef[2], coef[3]);
            results[i] = val;

            if (val > local_max) local_max = val;
            if (val < local_min) local_min = val;
        }

        // 4. Trimite vectorul rezultat la Master (0)
        MPI_Send(results.data(), my_count, MPI_DOUBLE, 0, 1, MPI_COMM_WORLD);

        // --- LOGICA SPECIFICA DE TRIMITERE MAX / AFISARE MIN ---
        int last_proc = size - 1;

        if (rank != last_proc) {
            // Trimite valoarea MAXIMA ultimului proces
            MPI_Send(&local_max, 1, MPI_DOUBLE, last_proc, 2, MPI_COMM_WORLD);
        }
        else {
            // Ultimul proces:
            double global_min_result = local_min; // Pornim cu minimul meu local

            for (int source = 1; source < last_proc; ++source) {
                double val_primita; // Primim MAXIMUL de la ceilalti
                MPI_Recv(&val_primita, 1, MPI_DOUBLE, source, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

                // Calculam minimul dintre (Minimul Meu) si (Maximele Lor) - conform cerintei
                if (val_primita < global_min_result) {
                    global_min_result = val_primita;
                }
            }

            cout << "-----------------------------------" << endl;
            cout << "Ultimul Proces (" << rank << ") afiseaza MINIMUL calculat: " << global_min_result << endl;
            cout << "-----------------------------------" << endl;
        }
    }

    MPI_Finalize();
    return 0;
}