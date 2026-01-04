#include <mpi.h>
#include <iostream>
#include <vector>
#include <fstream>
#include <numeric>
#include <cmath>

using namespace std;

int sumaCifre(int n) {
    int sum = 0;
    n = abs(n);
    while (n > 0) {
        sum += n % 10;
        n /= 10;
    }
    return sum;
}

// Funcția de procesare f(numar, X)
// Returnează 1 dacă a fost cazul 1 (înmulțire), 2 dacă a fost cazul 2 (împărțire)
int proceseazaNumar(int &numar, int X) {
    if (sumaCifre(numar) < X) {
        numar *= 2;
        return 1; // Cazul 1
    } else {
        numar /= 2;
        return 2; // Cazul 2
    }
}

int main1(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int world_size, world_rank;
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);
    MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);

    int X = 10;
    int N = 0;
    vector<int> date_procesat;

    //CITIRE SI DISTRIBUTIE
    if (world_rank == 0) {
        ifstream fin("numbers.txt");
        vector<int> toate_numerele;
        int val;
        while (fin >> val) {
            toate_numerele.push_back(val);
        }
        fin.close();

        N = toate_numerele.size();
        if (N == 0 || N % 2 != 0 || world_size % 2 != 0) {
            cerr << "Eroare: N si P trebuie sa fie pare, iar fisierul sa nu fie gol." << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        cout << "[Master] Citit " << N << " numere. X este " << X << "." << endl;

        // Broadcast N și X la toată lumea
        MPI_Bcast(&N, 1, MPI_INT, 0, MPI_COMM_WORLD);
        MPI_Bcast(&X, 1, MPI_INT, 0, MPI_COMM_WORLD);

        // Pregătim distribuția: Pare la Pare, Impare la Impare
        // Calculăm câte numere primește fiecare proces
        int chunk_size = N / world_size;

        // Separăm numerele
        vector<int> numere_poz_pare;
        vector<int> numere_poz_impare;
        for(int i=0; i<N; ++i) {
            if (i % 2 == 0) numere_poz_pare.push_back(toate_numerele[i]);
            else numere_poz_impare.push_back(toate_numerele[i]);
        }

        // Trimitem datele
        // Procesul 0 își păstrează prima bucată din numerele pare
        for (int i = 0; i < chunk_size; ++i) {
            date_procesat.push_back(numere_poz_pare[i]);
        }

        int idx_even = chunk_size; // cursor pentru vectorul de pare
        int idx_odd = 0;           // cursor pentru vectorul de impare

        for (int p = 1; p < world_size; ++p) {
            if (p % 2 == 0) {
                // Proces par: primește din numere_poz_pare
                MPI_Send(&numere_poz_pare[idx_even], chunk_size, MPI_INT, p, 0, MPI_COMM_WORLD);
                idx_even += chunk_size;
            } else {
                // Proces impar: primește din numere_poz_impare
                MPI_Send(&numere_poz_impare[idx_odd], chunk_size, MPI_INT, p, 0, MPI_COMM_WORLD);
                idx_odd += chunk_size;
            }
        }

    } else {
        // Celelalte procese primesc N și X
        MPI_Bcast(&N, 1, MPI_INT, 0, MPI_COMM_WORLD);
        MPI_Bcast(&X, 1, MPI_INT, 0, MPI_COMM_WORLD);

        int chunk_size = N / world_size;
        date_procesat.resize(chunk_size);
        MPI_Recv(date_procesat.data(), chunk_size, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    }

    //PRELUCRARE

    int local_A = 0; // Contor caz 1
    int local_B = 0; // Contor caz 2

    for (int &val : date_procesat) {
        int tip = proceseazaNumar(val, X);
        if (tip == 1) local_A++;
        else local_B++;
    }

    //RECOLECTARE SI AFISARE

    // Recolectare vector modificat de către Procesul 0
    if (world_rank == 0) {
        int chunk_size = N / world_size;
        vector<int> rezultat_final(N);

        // Trebuie să le punem la loc în vectorul final pe pozițiile corespunzătoare.
        // Formula: primele chunk_size numere pare sunt la indicii 0, 2, 4 ... 2*(chunk-1)
        for(int i=0; i<chunk_size; ++i) {
            rezultat_final[i * 2] = date_procesat[i];
        }

        // Primim de la ceilalți
        vector<int> buffer_primire(chunk_size);
        int current_even_start_idx = chunk_size * 2; // Următorul index par în vectorul mare
        int current_odd_start_idx = 1;               // Primul index impar în vectorul mare

        for (int p = 1; p < world_size; ++p) {
            MPI_Recv(buffer_primire.data(), chunk_size, MPI_INT, p, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            if (p % 2 == 0) {
                // Proces par -> a procesat numere de pe poziții pare
                for(int i=0; i<chunk_size; ++i) {
                    rezultat_final[current_even_start_idx + i*2] = buffer_primire[i];
                }
                current_even_start_idx += chunk_size * 2;
            } else {
                // Proces impar -> a procesat numere de pe poziții impare
                for(int i=0; i<chunk_size; ++i) {
                    rezultat_final[current_odd_start_idx + i*2] = buffer_primire[i];
                }
                current_odd_start_idx += chunk_size * 2;
            }
        }

        ofstream fout("result.txt");
        for (int x : rezultat_final) fout << x << " ";
        fout.close();
        cout << "[Master] Rezultatul scris in result.txt" << endl;

    } else {
        MPI_Send(date_procesat.data(), date_procesat.size(), MPI_INT, 0, 0, MPI_COMM_WORLD);
    }

    // 3b. Procesul 1 calculează totalurile A și B
    // Folosim MPI_Reduce. Toți trimit la procesul 1 (root = 1).

    int total_A = 0;
    int total_B = 0;

    // Reducem local_A în total_A pe procesul 1
    MPI_Reduce(&local_A, &total_A, 1, MPI_INT, MPI_SUM, 1, MPI_COMM_WORLD);

    // Reducem local_B în total_B pe procesul 1
    MPI_Reduce(&local_B, &total_B, 1, MPI_INT, MPI_SUM, 1, MPI_COMM_WORLD);

    if (world_rank == 1) {
        cout << "[Proces 1] Statistici finale:" << endl;
        cout << "   Total elemente prelucrate Cazul 1 (inmultite): " << total_A << endl;
        cout << "   Total elemente prelucrate Cazul 2 (impartite): " << total_B << endl;
    }
    MPI_Finalize();
    return 0;
}