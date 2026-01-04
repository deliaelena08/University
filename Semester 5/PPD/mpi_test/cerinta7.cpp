#include <mpi.h>
#include <iostream>
#include <vector>
#include <fstream>
#include <cmath>

using namespace std;

// Funcție pentru calculul sumei cifrelor
int  sumaCifre7(int n) {
    int sum = 0;
    n = abs(n);
    while (n > 0) {
        sum += n % 10;
        n /= 10;
    }
    return sum;
}

// Funcția de procesare f(numar, X)
int proceseazaNumar7(int &numar, int X) {
    if ( sumaCifre7(numar) < X) {
        numar *= 2;
        return 1; // Cazul 1
    } else {
        numar /= 2;
        return 2; // Cazul 2
    }
}

int mainnnn(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int world_size, world_rank;
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);
    MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);

    if (world_size < 2) {
        if (world_rank == 0) cerr << "Eroare: Ruleaza cu mpiexec -n <P> (min 2)" << endl;
        MPI_Finalize();
        return 0;
    }

    int X = 0;
    int N = 0;

    // Vectorul local (diferit ca marime pentru fiecare proces)
    vector<int> date_locale;

    // --- ETAPA 1: CITIRE SI DISTRIBUTIE ---
    if (world_rank == 0) {
        ifstream fin("numbers.txt");
        if (!fin.is_open()) {
            cerr << "Eroare: Nu pot deschide numbers.txt" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        fin >> X;
        vector<int> toate_numerele;
        int val;
        while (fin >> val) {
            toate_numerele.push_back(val);
        }
        fin.close();

        N = toate_numerele.size();
        cout << "[Master] X=" << X << ", N=" << N << endl;

        // Broadcast X (N nu mai e critic pentru workeri, ca primesc count individual)
        MPI_Bcast(&X, 1, MPI_INT, 0, MPI_COMM_WORLD);

        // Separare Pare / Impare
        vector<int> numere_poz_pare;
        vector<int> numere_poz_impare;
        for(int i=0; i<N; ++i) {
            if (i % 2 == 0) numere_poz_pare.push_back(toate_numerele[i]);
            else numere_poz_impare.push_back(toate_numerele[i]);
        }

        // Calculam logica de impartire cu REST
        int nr_proc_pare = (world_size + 1) / 2; // ex: 5 procese -> 0, 2, 4 (3 procese)
        int nr_proc_impare = world_size / 2;     // ex: 5 procese -> 1, 3 (2 procese)

        int idx_buf_pare = 0;
        int idx_buf_impare = 0;

        for (int p = 0; p < world_size; ++p) {
            int count_to_send = 0;
            bool e_proces_par = (p % 2 == 0);

            if (e_proces_par) {
                // Suntem in echipa para.
                // Indexul meu in echipa para este p/2
                int my_team_rank = p / 2;
                int total_items = numere_poz_pare.size();
                int base = total_items / nr_proc_pare;
                int rem = total_items % nr_proc_pare;

                // Cei cu rank mic in echipa primesc restul
                count_to_send = base + (my_team_rank < rem ? 1 : 0);
            } else {
                // Suntem in echipa impara
                int my_team_rank = (p - 1) / 2;
                int total_items = numere_poz_impare.size();
                int base = total_items / nr_proc_impare;
                int rem = total_items % nr_proc_impare;

                count_to_send = base + (my_team_rank < rem ? 1 : 0);
            }

            if (p == 0) {
                // Masterul isi ia datele singur
                for(int k=0; k<count_to_send; ++k) {
                    date_locale.push_back(numere_poz_pare[idx_buf_pare++]);
                }
            } else {
                // Trimitem catre procesul P
                // 1. Trimitem CATE numere
                MPI_Send(&count_to_send, 1, MPI_INT, p, 99, MPI_COMM_WORLD);

                // 2. Trimitem Vectorul
                if (count_to_send > 0) {
                    if (e_proces_par) {
                        MPI_Send(&numere_poz_pare[idx_buf_pare], count_to_send, MPI_INT, p, 0, MPI_COMM_WORLD);
                        idx_buf_pare += count_to_send;
                    } else {
                        MPI_Send(&numere_poz_impare[idx_buf_impare], count_to_send, MPI_INT, p, 0, MPI_COMM_WORLD);
                        idx_buf_impare += count_to_send;
                    }
                }
            }
        }
    } else {
        // Workerii primesc X
        MPI_Bcast(&X, 1, MPI_INT, 0, MPI_COMM_WORLD);

        // 1. Aflam cate numere primim
        int count_recv = 0;
        MPI_Recv(&count_recv, 1, MPI_INT, 0, 99, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        // 2. Primim vectorul
        if (count_recv > 0) {
            date_locale.resize(count_recv);
            MPI_Recv(date_locale.data(), count_recv, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        }
    }

    // --- ETAPA 2: PRELUCRARE ---
    int local_A = 0;
    int local_B = 0;

    for (int &val : date_locale) {
        int tip = proceseazaNumar7(val, X);
        if (tip == 1) local_A++;
        else local_B++;
    }

    // --- ETAPA 3: RECOLECTARE SI AFISARE (STRICT RANK 0) ---

    if (world_rank == 0) {
        // Colectam rezultatele in doua liste separate pentru a le putea interclasa corect
        vector<int> rezultate_pare;
        vector<int> rezultate_impare;

        // Adaugam datele procesate de Master (care e Par)
        for(int val : date_locale) rezultate_pare.push_back(val);

        int total_A = local_A;
        int total_B = local_B;

        for (int p = 1; p < world_size; ++p) {
            // 1. Primim cate numere vin inapoi (Count)
            int count_recv = 0;
            MPI_Recv(&count_recv, 1, MPI_INT, p, 99, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            vector<int> buffer(count_recv);
            if (count_recv > 0) {
                MPI_Recv(buffer.data(), count_recv, MPI_INT, p, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            }

            // 2. Primim Statistici
            int recv_A, recv_B;
            MPI_Recv(&recv_A, 1, MPI_INT, p, 10, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            MPI_Recv(&recv_B, 1, MPI_INT, p, 11, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            total_A += recv_A;
            total_B += recv_B;

            // Stocam in lista corespunzatoare
            if (p % 2 == 0) {
                for(int val : buffer) rezultate_pare.push_back(val);
            } else {
                for(int val : buffer) rezultate_impare.push_back(val);
            }
        }

        // Reconstructie finala (Interclasare Pare cu Impare)
        // Pozitiile pare din fisier original -> rezultate_pare
        // Pozitiile impare din fisier original -> rezultate_impare
        vector<int> rezultat_final;
        int p_idx = 0;
        int i_idx = 0;

        for(int i=0; i<N; ++i) {
            if (i % 2 == 0) {
                if (p_idx < rezultate_pare.size()) rezultat_final.push_back(rezultate_pare[p_idx++]);
            } else {
                if (i_idx < rezultate_impare.size()) rezultat_final.push_back(rezultate_impare[i_idx++]);
            }
        }

        // Scriere fisier
        ofstream fout("result7.txt");
        for (int x : rezultat_final) fout << x << endl;
        fout.close();

        cout << "--- STATISTICI FINALE (Proces 0) ---" << endl;
        cout << "Total A (inmultite): " << total_A << endl;
        cout << "Total B (impartite): " << total_B << endl;

    } else {
        // Workerii trimit inapoi la 0
        int count_to_send = date_locale.size();

        // 1. Count
        MPI_Send(&count_to_send, 1, MPI_INT, 0, 99, MPI_COMM_WORLD);

        // 2. Vector
        if (count_to_send > 0) {
            MPI_Send(date_locale.data(), count_to_send, MPI_INT, 0, 0, MPI_COMM_WORLD);
        }

        // 3. Statistici
        MPI_Send(&local_A, 1, MPI_INT, 0, 10, MPI_COMM_WORLD);
        MPI_Send(&local_B, 1, MPI_INT, 0, 11, MPI_COMM_WORLD);
    }

    MPI_Finalize();
    return 0;
}