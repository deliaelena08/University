#include <iostream>
#include <fstream>
#include <vector>
#include <cstdlib>
#include <ctime>
#include <algorithm>

using namespace std;

// Functie pentru a genera si scrie un numar mare aleator intr-un fisier
void generate_and_write(const string& filename, int num_digits) {
    // Deschide fisierul pentru scriere
    ofstream outfile(filename);

    if (!outfile.is_open()) {
        cerr << "Eroare la deschiderea fisierului: " << filename << endl;
        exit(1);
    }

    // 1. Scrie numarul de cifre (N) pe prima linie
    outfile << num_digits << endl;

    // 2. Genereaza cifrele.
    for (int i = 0; i < num_digits; ++i) {
        int digit;
        
        if (i == 0) {
            digit = (num_digits == 1) ? (rand() % 10) : (rand() % 9 + 1);
        } else {
            digit = rand() % 10;
        }

        outfile << digit;
    }

    outfile.close();
    cout << "Fisierul " << filename << " generat cu succes (" << num_digits << " cifre)." << endl;
}

int main32(int argc, char* argv[]) {
    srand(time(0));

    if (argc < 3) {
        cerr << "Utilizare: " << argv[0] << " <N1_cifre> <N2_cifre> [NumeFisier1] [NumeFisier2]" << endl;
        cerr << "Exemplu: " << argv[0] << " 10000 100000 NumarA.txt NumarB.txt" << endl;
        cerr << "Se vor folosi Numar1.txt si Numar2.txt daca nu sunt specificate." << endl;
        return 1;
    }

    int n1_digits = atoi(argv[1]);
    int n2_digits = atoi(argv[2]);

    if (n1_digits <= 0 || n2_digits <= 0) {
        cerr << "Numarul de cifre trebuie sa fie pozitiv." << endl;
        return 1;
    }

    string file1_name = (argc > 3) ? argv[3] : "Numar1.txt";
    string file2_name = (argc > 4) ? argv[4] : "Numar2.txt";

    generate_and_write(file1_name, n1_digits);
    generate_and_write(file2_name, n2_digits);

    return 0;
}