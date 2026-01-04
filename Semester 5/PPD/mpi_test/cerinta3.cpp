#include <mpi.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <numeric>

using namespace std;

int main11(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int world_rank, world_size;
    MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);

    // Verificare număr procese (minim 2: 1 Master + 1 Worker)
    if (world_size < 2) {
        if (world_rank == 0) cerr << "Ai nevoie de minim 2 procese!" << endl;
        MPI_Finalize();
        return 0;
    }

    int N = 0;
    const int NUM_WORKERS = world_size - 1;

    // --- PROCESUL 0 (MASTER) ---
    if (world_rank == 0) {
        ifstream fin("input.txt");
        if (!fin.is_open()) {
            cerr << "Eroare: Nu pot deschide input.txt" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        fin >> N;
        cout << "[Master] N=" << N << ". Incep distributia catre " << NUM_WORKERS << " workeri." << endl;

        // Trimitem N tuturor workerilor ca sa stie cat sa aloce
        MPI_Bcast(&N, 1, MPI_INT, 0, MPI_COMM_WORLD);

        // Structuri de date
        // matrice_buffer: ține minte datele ca să fie valide pentru Isend
        vector<vector<int>> matrice_buffer(N, vector<int>(N));
        vector<long long> rezultate(N); // Vectorul coloana rezultat (sumele)

        // Request-uri pentru comunicarea asincrona
        // Avem nevoie de un request pentru fiecare Send si fiecare Recv
        vector<MPI_Request> reqs_send(N);
        vector<MPI_Request> reqs_recv(N);

        // 1. Postam primirile ASINCRON (MPI_Irecv)
        // Ii spunem MPI-ului: "Fii pregatit sa primesti rezultatul pentru linia i"
        // Folosim TAG-ul 'i' pentru a sti exact carei linii ii apartine suma
        for (int i = 0; i < N; ++i) {
            int worker_id = i % NUM_WORKERS + 1; // Formula din enunt
            MPI_Irecv(&rezultate[i], 1, MPI_LONG_LONG, worker_id, i, MPI_COMM_WORLD, &reqs_recv[i]);
        }

        // 2. Citim si trimitem ASINCRON (MPI_Isend)
        for (int i = 0; i < N; ++i) {
            // Citim linia
            for (int j = 0; j < N; ++j) {
                fin >> matrice_buffer[i][j];
            }

            // Calculam destinatarul conform formulei: id = i % (p-1) + 1
            int dest_id = i % NUM_WORKERS + 1;

            // Trimitem linia neblocant
            // Tag-ul este 'i' (indexul liniei)
            MPI_Isend(matrice_buffer[i].data(), N, MPI_INT, dest_id, i, MPI_COMM_WORLD, &reqs_send[i]);

            cout << "[Master] Trimis linia " << i << " catre Worker " << dest_id << endl;
        }
        fin.close();

        // 3. Asteptam sa se termine toate operatiile
        // MPI_Waitall blocheaza pana cand toate request-urile din vector sunt complete
        MPI_Waitall(N, reqs_send.data(), MPI_STATUSES_IGNORE); // Asteptam sa plece toate datele
        MPI_Waitall(N, reqs_recv.data(), MPI_STATUSES_IGNORE); // Asteptam sa vina toate rezultatele

        // 4. Scriere rezultat
        ofstream fout("output.txt");
        cout << "[Master] Toate rezultatele primite. Scriu in output.txt..." << endl;
        for (int i = 0; i < N; ++i) {
            fout << rezultate[i] << endl;
        }
        fout.close();

    }
    // --- PROCESELE WORKER (ID > 0) ---
    else {
        // Primim N
        MPI_Bcast(&N, 1, MPI_INT, 0, MPI_COMM_WORLD);

        // Workerii trebuie să știe câte linii vor primi și care anume,
        // sau pur și simplu intră într-o buclă știind formula distribuției.

        int my_count = 0;
        vector<int> buffer_linie(N);

        // Simulam distributia pentru a vedea ce linii imi revin mie
        for (int i = 0; i < N; ++i) {
            int dest_id = i % NUM_WORKERS + 1;

            if (dest_id == world_rank) {
                my_count++;

                // Primim linia specifica (Tag-ul va fi i)
                MPI_Status status;
                MPI_Recv(buffer_linie.data(), N, MPI_INT, 0, i, MPI_COMM_WORLD, &status);

                // Calculam suma
                long long suma_linie = 0;
                for (int x : buffer_linie) {
                    suma_linie += x;
                }

                // Trimitem rezultatul inapoi la 0
                // Pastram tag-ul 'i' ca Masterul sa stie unde sa puna suma
                MPI_Send(&suma_linie, 1, MPI_LONG_LONG, 0, i, MPI_COMM_WORLD);

                // cout << "[Worker " << world_rank << "] Procesat linia " << i << ", Suma=" << suma_linie << endl;
            }
        }
    }

    MPI_Finalize();
    return 0;
}