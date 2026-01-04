#include <mpi.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <numeric>
#include <iomanip>

using namespace std;

const int MAX_CARTIERE = 20;

int main1234(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (size < 2) {
        if (rank == 0) {
            cerr << "Eroare: Ruleaza cu minim 2 procese!" << endl;
        }
        MPI_Finalize();
        return 0;
    }

    vector<int> all_ids;
    vector<double> all_temps;
    int n_total = 0;

    // --- ETAPA 1: MASTER CITESTE FISIERUL ---
    if (rank == 0) {
        ifstream fin("temperaturi.txt");
        if (fin.is_open()) {
            int id;
            double t;
            while (fin >> id >> t) {
                all_ids.push_back(id);
                all_temps.push_back(t);
            }
            fin.close();
            n_total = all_ids.size();
            cout << "[Master] Citit " << n_total << " inregistrari." << endl;
        } else {
            cout << "Nu am gasit fisierul temperaturi.txt" << endl;
        }
    }


    // 1. Masterul trimite numarul total (optional, dar util)
    MPI_Bcast(&n_total, 1, MPI_INT, 0, MPI_COMM_WORLD);

    vector<int> local_ids;
    vector<double> local_temps;
    int my_count = 0;

    if (rank == 0) {
        int current_index = 0;
        int chunk = n_total / size;
        int remainder = n_total % size;

        for (int p = 0; p < size; ++p) {
            // Calculam cat primeste procesul 'p'
            // Daca p < remainder, primeste un element in plus (ca sa impartim restul echitabil)
            int count = chunk + (p < remainder ? 1 : 0);

            if (p == 0) {
                // Masterul isi ia partea lui direct (copiere locala)
                my_count = count;
                local_ids.assign(all_ids.begin(), all_ids.begin() + count);
                local_temps.assign(all_temps.begin(), all_temps.begin() + count);
            } else {
                // Trimitem la celelalte procese
                // 1. Trimitem CATE elemente primeste
                MPI_Send(&count, 1, MPI_INT, p, 0, MPI_COMM_WORLD);
                // 2. Trimitem ID-urile
                MPI_Send(&all_ids[current_index], count, MPI_INT, p, 1, MPI_COMM_WORLD);
                // 3. Trimitem Temperaturile
                MPI_Send(&all_temps[current_index], count, MPI_DOUBLE, p, 2, MPI_COMM_WORLD);
            }
            // Avansam indexul in vectorul mare
            current_index += count;
        }
    }
    else {
        // === LOGICA WORKERILOR (PRIMIRE) ===
        // 1. Aflam cate elemente primim
        MPI_Recv(&my_count, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        // 2. Alocam spatiu
        local_ids.resize(my_count);
        local_temps.resize(my_count);

        // 3. Primim datele efective
        MPI_Recv(local_ids.data(), my_count, MPI_INT, 0, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        MPI_Recv(local_temps.data(), my_count, MPI_DOUBLE, 0, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    }

    // --- ETAPA 3: CALCUL LOCAL ---

    vector<double> local_sum(MAX_CARTIERE, 0.0);
    vector<int> local_count(MAX_CARTIERE, 0);
    double local_total_sum = 0.0;

    for (int i = 0; i < my_count; ++i) {
        int id = local_ids[i];
        double val = local_temps[i];

        if (id < MAX_CARTIERE) {
            local_sum[id] += val;
            local_count[id]++;
        }
        local_total_sum += val;
    }

    // --- ETAPA 4: AGREGARE LA ROOT (Reduce) ---

    vector<double> global_sum(MAX_CARTIERE);
    vector<int> global_count(MAX_CARTIERE);
    double global_total_sum = 0.0;

    // Adunam toate sumele partiale la Master
    MPI_Reduce(local_sum.data(), global_sum.data(), MAX_CARTIERE, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);
    MPI_Reduce(local_count.data(), global_count.data(), MAX_CARTIERE, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);
    MPI_Reduce(&local_total_sum, &global_total_sum, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);

    // --- ETAPA 5: AFISARE (ROOT) ---

    if (rank == 0) {
        cout << fixed << setprecision(2);
        cout << "\n--- REZULTATE ---" << endl;

        // Media pe fiecare cartier
        cout << "Temperaturi medii pe cartiere:" << endl;
        for (int i = 0; i < MAX_CARTIERE; ++i) {
            if (global_count[i] > 0) { // Evitam impartirea la zero
                double avg = global_sum[i] / global_count[i];
                cout << "Cartier ID " << i << ": " << avg << " C" << endl;
            }
        }

        // Media pe oras
        int total_masuratori = 0;
        for(int c : global_count) total_masuratori += c;

        if (total_masuratori > 0) {
            cout << "\nTemperatura medie ORAS: " << global_total_sum / total_masuratori << " C" << endl;
        }
    }

    MPI_Finalize();
    return 0;
}