#include <mpi.h>
#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

// Functie pentru calculul polinomului (Varianta a0 * x^N + ... + aN)
// Exemplu: pt x=10 si coef 1,2,3,4 => 1*1000 + 2*100 + 3*10 + 4 = 1234
long long calculeazaPolinom(const vector<long long>& coeficienti, long long x) {
    long long result = 0;
    // Parcurgem normal: a0 este coeficientul puterii celei mai mari
    for (long long coef : coeficienti) {
        result = result * x + coef;
    }
    return result;
}

int mainnn(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int world_rank, world_size;
    MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);

    int n_puncte = 0;
    int grad_polinom = 0;

    // Folosim long long pentru a evita virgula mobila si overflow-ul la numere mari
    vector<long long> coeficienti;
    vector<long long> toate_punctele;
    vector<long long> rezultate_finale;

    // --- ETAPA 1: CITIRE SI DISTRIBUTIE ---

    if (world_rank == 0) {
        // 1. Citire Coeficienti
        ifstream f_poly("polinom.txt");
        if (!f_poly.is_open()) {
            cerr << "Eroare: Nu pot deschide polinom.txt" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        f_poly >> grad_polinom;
        // Sunt N+1 coeficienti (de la a0 la aN)
        coeficienti.resize(grad_polinom + 1);
        for (int i = 0; i <= grad_polinom; ++i) {
            f_poly >> coeficienti[i];
        }
        f_poly.close();

        // 2. Citire Puncte
        ifstream f_in("input.txt");
        if (!f_in.is_open()) {
            cerr << "Eroare: Nu pot deschide input.txt" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        f_in >> n_puncte;
        toate_punctele.resize(n_puncte);
        for (int i = 0; i < n_puncte; ++i) {
            f_in >> toate_punctele[i];
        }
        f_in.close();

        if (n_puncte % world_size != 0) {
            cerr << "Eroare: n (" << n_puncte << ") nu se divide cu p (" << world_size << ")" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }
    }

    // A. Broadcast dimensiuni
    MPI_Bcast(&grad_polinom, 1, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Bcast(&n_puncte, 1, MPI_INT, 0, MPI_COMM_WORLD);

    if (world_rank != 0) {
        coeficienti.resize(grad_polinom + 1);
    }

    // B. Broadcast Coeficienti (Tip MPI_LONG_LONG)
    MPI_Bcast(coeficienti.data(), grad_polinom + 1, MPI_LONG_LONG, 0, MPI_COMM_WORLD);

    // C. Scatter Puncte
    int chunk_size = n_puncte / world_size;
    vector<long long> segment_local(chunk_size);

    MPI_Scatter(toate_punctele.data(), chunk_size, MPI_LONG_LONG,
                segment_local.data(), chunk_size, MPI_LONG_LONG,
                0, MPI_COMM_WORLD);

    // --- ETAPA 2: CALCUL ---

    vector<long long> rezultate_locale(chunk_size);
    for (int i = 0; i < chunk_size; ++i) {
        rezultate_locale[i] = calculeazaPolinom(coeficienti, segment_local[i]);
    }

    // Gather Rezultate
    if (world_rank == 0) {
        rezultate_finale.resize(n_puncte);
    }

    MPI_Gather(rezultate_locale.data(), chunk_size, MPI_LONG_LONG,
               rezultate_finale.data(), chunk_size, MPI_LONG_LONG,
               0, MPI_COMM_WORLD);

    // --- ETAPA 3: SCRIERE ---

    if (world_rank == 0) {
        ofstream f_out("output.txt");
        for (int i = 0; i < n_puncte; ++i) {
            f_out << rezultate_finale[i];
            if (i < n_puncte - 1) f_out << ", ";
        }
        f_out << endl;
        f_out.close();
        // Afisam si la consola ca sa vezi rapid
        cout << "Output: ";
        for (int i = 0; i < n_puncte; ++i) cout << rezultate_finale[i] << " ";
        cout << endl;
    }

    MPI_Finalize();
    return 0;
}