#include <mpi.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <iomanip>

using namespace std;

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (size < 2) {
        if (rank == 0) cerr << "Eroare: Ruleaza cu mpiexec -n <P> (min 2)" << endl;
        MPI_Finalize();
        return 0;
    }

    double my_coeff = 0.0;
    int n_points = 0;

    // --- ETAPA 1: DISTRIBUIREA COEFICIENTILOR ---
    if (rank == 0) {
        ifstream f_poly("polinom.txt");
        if (!f_poly.is_open()) {
            cerr << "Eroare: Nu pot deschide polinom.txt" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        vector<double> all_coeffs(size);
        for (int i = 0; i < size; ++i) {
            f_poly >> all_coeffs[i];
        }
        f_poly.close();

        // Procesul 0 isi pastreaza a0
        my_coeff = all_coeffs[0];

        // Procesul 0 distribuie a1..aN catre procesele 1..N
        for (int i = 1; i < size; ++i) {
            MPI_Send(&all_coeffs[i], 1, MPI_DOUBLE, i, 0, MPI_COMM_WORLD);
        }
    } else {
        // Celelalte procese isi primesc coeficientul a_ID
        MPI_Recv(&my_coeff, 1, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    }

    ifstream f_in;
    if (rank == 0) {
        f_in.open("input.txt");
        if (f_in.is_open()) {
            f_in >> n_points;
        } else {
            cerr << "Eroare: Nu pot deschide input.txt" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }
    }

    ofstream f_out;
    if (rank == size - 1) {
        f_out.open("output.txt");
        f_out << fixed << setprecision(0); // Setam formatare (optional)
    }

    // Toti trebuie sa stie cate puncte procesam (n) pentru a rula bucla corect
    MPI_Bcast(&n_points, 1, MPI_INT, 0, MPI_COMM_WORLD);

    // --- ETAPA 2: PROCESAREA VALORILOR (PIPELINE) ---
    // Se repeta pana cand se termina valorile din fisier
    for (int k = 0; k < n_points; ++k) {
        double x;

        // a. Procesul 0 citeste x si il trimite prin broadcast
        if (rank == 0) {
            f_in >> x;
        }
        MPI_Bcast(&x, 1, MPI_DOUBLE, 0, MPI_COMM_WORLD);

        double v = 0.0;

        // Logica specifica fiecarui proces
        if (rank == 0) {
            // "daca id=0 atunci v=a0"
            // Procesul 0 initiaza calculul trimitand a0 catre procesul 1
            // (Conform Horner: primul pas este inmultirea a0 cu x in urmatorul proces)
            v = my_coeff;
            
            // Trimite la urmatorul (rank + 1)
            if (size > 1) {
                MPI_Send(&v, 1, MPI_DOUBLE, rank + 1, 1, MPI_COMM_WORLD);
            }
        } 
        else {
            // b. Procesul id > 0 primeste v de la procesul precedent
            MPI_Recv(&v, 1, MPI_DOUBLE, rank - 1, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            // c. Inmulteste v cu x si aduna coeficientul (v = v*x + a_id)
            v = v * x + my_coeff;

            // d. Trimite la urmatorul SAU scrie in fisier daca e ultimul
            if (rank < size - 1) {
                MPI_Send(&v, 1, MPI_DOUBLE, rank + 1, 1, MPI_COMM_WORLD);
            } else {
                f_out << v;
                if (k < n_points - 1) f_out << ", ";
            }
        }
    }

    if (rank == 0) f_in.close();
    if (rank == size - 1) {
        f_out << endl;
        f_out.close();
        cout << "[Ultimul Proces] Calcul terminat. Rezultate in output.txt" << endl;
    }

    MPI_Finalize();
    return 0;
}