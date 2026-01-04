#include <mpi.h>
#include <iostream>
#include <vector>
#include <fstream>
#include <cmath>

using namespace std;

int main111(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int world_rank, world_size;
    MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);

    if (world_size < 2 || world_size % 2 != 0) {
        if (world_rank == 0) cerr << "Eroare: Numar par de procese necesar!" << endl;
        MPI_Finalize();
        return 0;
    }

    // 1. IMPARTIM LUMEA IN DOUA ECHIPE: PARE (color=0) si IMPARE (color=1)
    int color = world_rank % 2;
    MPI_Comm new_comm;
    // MPI_Comm_split creeaza un nou comunicator.
    // Toate procesele cu aceeasi 'color' vor fi in acelasi grup.
    MPI_Comm_split(MPI_COMM_WORLD, color, world_rank, &new_comm);

    // Aflam noul rank si size in sub-grup
    int new_rank, new_size;
    MPI_Comm_rank(new_comm, &new_rank);
    MPI_Comm_size(new_comm, &new_size);

    // Variabile comune
    int n = 0;
    int chunk_size = 0;
    double valoare_globala = 0; // a_global sau b_global
    vector<double> vector_mare;
    vector<double> segment_local;

    // ---------------------------------------------------------
    // ETAPA 1 & 2: CITIRE SI SCATTER (COMUN PENTRU AMBELE ECHIPE)
    // ---------------------------------------------------------

    // Logica e identica: Seful echipei (new_rank == 0) citeste si da Scatter.
    // Pentru echipa Para: Seful e World_Rank 0.
    // Pentru echipa Impara: Seful e World_Rank 1 (dar are new_rank 0 in echipa lui!)

    if (new_rank == 0) {
        // Daca sunt seful echipei mele (fie Par, fie Impar)
        string filename = (color == 0) ? "inputA.txt" : "inputB.txt";
        ifstream fin(filename);

        if (fin.is_open()) {
            fin >> valoare_globala >> n;
            chunk_size = n / new_size; // new_size este N/2 (numar perechi)

            vector_mare.resize(n);
            for(int i=0; i<n; ++i) fin >> vector_mare[i];
            fin.close();
        }
    }

    // Pas A: Broadcast la N si Valoare Globala in interiorul echipei
    MPI_Bcast(&n, 1, MPI_INT, 0, new_comm);
    MPI_Bcast(&valoare_globala, 1, MPI_DOUBLE, 0, new_comm);

    // Calculam chunk_size local
    chunk_size = n / new_size;
    segment_local.resize(chunk_size);

    // Pas B: SCATTER
    // Distribuim vectorul mare doar membrilor echipei mele
    MPI_Scatter(vector_mare.data(), chunk_size, MPI_DOUBLE,
                segment_local.data(), chunk_size, MPI_DOUBLE,
                0, new_comm);

    // ---------------------------------------------------------
    // ETAPA 3: CALCUL LOCAL (PUTEREA)
    // ---------------------------------------------------------
    for(int i=0; i<chunk_size; ++i) {
        segment_local[i] = pow(segment_local[i], valoare_globala);
    }

    // ---------------------------------------------------------
    // ETAPA 4: SCHIMB SI AGREGARE
    // ---------------------------------------------------------

    // Aici avem nevoie de comunicare intre Echipe (folosim MPI_COMM_WORLD)
    // Imparii trimit la Pari.

    if (color == 1) {
        // Sunt IMPAR. Trimit rezultatul procesului PAR din stanga mea (rank-1)
        int partener = world_rank - 1;
        MPI_Send(segment_local.data(), chunk_size, MPI_DOUBLE, partener, 0, MPI_COMM_WORLD);
    }
    else {
        // Sunt PAR. Primesc de la IMPAR (rank+1)
        int partener = world_rank + 1;
        vector<double> segment_partener(chunk_size);
        MPI_Recv(segment_partener.data(), chunk_size, MPI_DOUBLE, partener, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        // Calculam produsul a*b (Suma Partiala)
        double suma_partiala = 0;
        for(int i=0; i<chunk_size; ++i) {
            suma_partiala += (segment_local[i] * segment_partener[i]);
        }

        // REDUCE: Adunam toate sumele partiale la Seful echipei PARE (Rank 0)
        // Folosim comunicatorul echipei pare (new_comm)
        double suma_totala = 0;
        MPI_Reduce(&suma_partiala, &suma_totala, 1, MPI_DOUBLE, MPI_SUM, 0, new_comm);

        if (new_rank == 0) {
            cout << "--------------------------------------" << endl;
            cout << "REZULTAT FINAL S = " << suma_totala << endl;
            cout << "--------------------------------------" << endl;
        }
    }

    // Eliberam comunicatorul creat
    MPI_Comm_free(&new_comm);

    MPI_Finalize();
    return 0;
}