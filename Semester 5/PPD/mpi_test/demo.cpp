#include <iostream>
#include <fstream>
#include <vector>
#include <cstdlib>
#include <ctime>

using namespace std;

int main() {
    srand(time(NULL));

    // Configurare
    int N = 12; // Alege un N divizibil cu nr de procese (ex: 4 procese)
    
    ofstream fout("numbers.txt");
    if (!fout.is_open()) {
        cerr << "Eroare la crearea fisierului." << endl;
        return 1;
    }

    // Generam numerele
    for (int i = 0; i < N; ++i) {
        int val = 1 + rand() % 20; // Valori mici (1-20) sa nu dea overflow produsul imediat
        fout << val << endl;
    }
    fout.close();

    cout << "Generat numbers.txt cu " << N << " numere." << endl;
    return 0;
}