#include <mpi.h>
#include <iostream>
#include <vector>
#include <fstream>
#include <cmath>

using namespace std;

int main6(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int world_rank, world_size;
    MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);

    // Verificăm să avem un număr par de procese (M = 2*N)
    if (world_size < 2 || world_size % 2 != 0) {
        if (world_rank == 0) {
            cerr << "Eroare: Numarul de procese trebuie sa fie par si > 2." << endl;
        }
        MPI_Finalize();
        return 0;
    }

    // Numarul de perechi (N din enunt)
    int numar_perechi = world_size / 2;
    int n = 0;
    int chunk_size = 0;

    double puterea_locala = 0;
    vector<double> segment_local;

    // Procesul 0 gestionează inputA.txt și Echipa Pară
    if (world_rank == 0) {
        ifstream fin("inputA.txt");
        if (!fin.is_open()) {
            cerr << "Nu pot deschide inputA.txt" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        double a_global;
        fin >> a_global >> n;

        chunk_size = n / numar_perechi;

        // Anunțăm pe toată lumea cât de mare e bucata (ca să aloce memorie)
        MPI_Bcast(&chunk_size, 1, MPI_INT, 0, MPI_COMM_WORLD);

        vector<double> dateA(n);
        for (int i = 0; i < n; i++) fin >> dateA[i];
        fin.close();

        // 1. Distribuim 'a' și bucățile de vector către celelalte procese PARE
        puterea_locala = a_global; // Pentru Rank 0
        
        // Copiem bucata proprie a lui Rank 0
        for(int i=0; i<chunk_size; ++i) segment_local.push_back(dateA[i]);

        // Trimitem la 2, 4, 6...
        for (int i = 1; i < numar_perechi; i++) {
            int destinatar = i * 2; // Proces par
            // Trimitem puterea 'a'
            MPI_Send(&a_global, 1, MPI_DOUBLE, destinatar, 0, MPI_COMM_WORLD);
            // Trimitem bucata de vector (offset i * chunk_size)
            MPI_Send(&dateA[i * chunk_size], chunk_size, MPI_DOUBLE, destinatar, 1, MPI_COMM_WORLD);
        }

    } else {
        // Celelalte procese așteaptă să afle dimensiunea
        //bcast mai si primeste daca procesul e diferit de numarul procesului root ce e trimis ca paramtru, adica in cauzl nostru 0
        //deci el desi pare ca trimite valoarea 0 defapt el aici primeste acel chunk size
        MPI_Bcast(&chunk_size, 1, MPI_INT, 0, MPI_COMM_WORLD);
        segment_local.resize(chunk_size);
    }

    // Procesele pare (altele decat 0) primesc datele de la 0
    if (world_rank != 0 && world_rank % 2 == 0) {
        MPI_Recv(&puterea_locala, 1, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        MPI_Recv(segment_local.data(), chunk_size, MPI_DOUBLE, 0, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    }


    // ---------------------------------------------------------
    // ETAPA 2: Procesul 1 gestionează inputB.txt și Echipa Impară
    // ---------------------------------------------------------
    if (world_rank == 1) {
        ifstream fin("inputB.txt");
        if (!fin.is_open()) {
            cerr << "Nu pot deschide inputB.txt" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        double b_global;
        int n_check;
        fin >> b_global >> n_check; // Citim b si n

        vector<double> dateB(n_check);
        for (int i = 0; i < n_check; i++) fin >> dateB[i];
        fin.close();

        puterea_locala = b_global;

        // CORECTIE: Folosim atribuire directă (=) nu push_back,
        // pentru că vectorul are deja dimensiunea setată din resize-ul anterior.
        for(int i=0; i<chunk_size; ++i) {
            segment_local[i] = dateB[i];
        }

        // Trimitem la 3, 5, 7...
        for (int i = 1; i < numar_perechi; i++) {
            int destinatar = i * 2 + 1; // Proces impar
            // Trimitem puterea 'b'
            MPI_Send(&b_global, 1, MPI_DOUBLE, destinatar, 0, MPI_COMM_WORLD);
            // Trimitem bucata de vector
            MPI_Send(&dateB[i * chunk_size], chunk_size, MPI_DOUBLE, destinatar, 1, MPI_COMM_WORLD);
        }
    }

    // Procesele impare (altele decat 1) primesc datele de la 1
    if (world_rank != 1 && world_rank % 2 != 0) {
        MPI_Recv(&puterea_locala, 1, MPI_DOUBLE, 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        MPI_Recv(segment_local.data(), chunk_size, MPI_DOUBLE, 1, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    }

    // ---------------------------------------------------------
    // ETAPA 3: Calcul și Agregare
    // ---------------------------------------------------------
    
    // Pasul 3.1: Toată lumea calculează puterea pe segmentul propriu
    for (int i = 0; i < chunk_size; i++) {
        segment_local[i] = pow(segment_local[i], puterea_locala);
    }

    // Pasul 3.2: Imparii trimit rezultatul la Parii lor (Rank Impar -> Rank Impar - 1)
    if (world_rank % 2 != 0) {
        int partener_par = world_rank - 1;
        MPI_Send(segment_local.data(), chunk_size, MPI_DOUBLE, partener_par, 99, MPI_COMM_WORLD);
    } 
    else { 
        // Sunt proces PAR. Primesc de la partenerul meu IMPAR (Rank Par + 1)
        int partener_impar = world_rank + 1;
        vector<double> segment_partener(chunk_size);
        MPI_Recv(segment_partener.data(), chunk_size, MPI_DOUBLE, partener_impar, 99, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        // Calculăm produsul a^A * b^B și adunăm la suma parțială
        double suma_partiala = 0;
        for (int i = 0; i < chunk_size; i++) {
            suma_partiala += (segment_local[i] * segment_partener[i]);
        }

        // Dacă sunt Master (0), aștept sumele de la ceilalți pari
        if (world_rank == 0) {
            double suma_totala = suma_partiala;
            cout << "Rank 0 (Par): Suma mea partiala este: " << suma_partiala << endl;

            for (int i = 1; i < numar_perechi; i++) {
                int expeditor_par = i * 2;
                double suma_primita;
                MPI_Recv(&suma_primita, 1, MPI_DOUBLE, expeditor_par, 100, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
                suma_totala += suma_primita;
            }

            cout << "--------------------------------------" << endl;
            cout << "REZULTAT FINAL S = " << suma_totala << endl;
            cout << "--------------------------------------" << endl;

        } else {
            // Dacă sunt alt proces par, trimit suma la 0
            MPI_Send(&suma_partiala, 1, MPI_DOUBLE, 0, 100, MPI_COMM_WORLD);
        }
    }

    MPI_Finalize();
    return 0;
}