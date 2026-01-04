#include <iostream>
#include <fstream>
#include <vector>
#include <cstdlib> // pentru rand() si srand()
#include <ctime>   // pentru time()

using namespace std;

// 1. Generator pentru problema cu Puteri (Vectori)
// Format: 
// Valoare_Globala (a sau b)
// N (numar elemente)
// v[0] v[1] ... v[n-1]
void genereazaInputVector(string numeFisier, int N, int valoareGlobala) {
    ofstream fout(numeFisier);
    if (!fout.is_open()) {
        cerr << "Eroare la crearea fisierului " << numeFisier << endl;
        return;
    }

    fout << valoareGlobala << endl;
    fout << N << endl;

    for (int i = 0; i < N; ++i) {
        // Generam numar intre 1 si 99 (evitam 0 la puteri pentru simplitate)
        int val = 1 + rand() % 99; 
        fout << val << " ";
    }
    fout.close();
    cout << "--> Generat " << numeFisier << " cu N=" << N << " si val=" << valoareGlobala << endl;
}

// 2. Generator pentru problema cu Matrice (Suma Coloane)
// Format:
// N (dimensiune matrice patratica)
// linie1...
// linie2...
void genereazaInputMatrice(string numeFisier, int N) {
    ofstream fout(numeFisier);
    if (!fout.is_open()) {
        cerr << "Eroare la crearea fisierului " << numeFisier << endl;
        return;
    }

    fout << N << endl;

    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < N; ++j) {
            // Generam numar intre 0 si 99
            int val = rand() % 100;
            fout << val << " ";
        }
        fout << endl; // Linie noua pentru aspect (optional, cin citeste oricum)
    }
    fout.close();
    cout << "--> Generat " << numeFisier << " (Matrice " << N << "x" << N << ")" << endl;
}

// 3. Generator simplu (Doar N si vectorul) - pentru problema cu suma cifrelor
void genereazaVectorSimplu(string numeFisier, int N) {
    ofstream fout(numeFisier);
    // Aici scriem direct numerele, fara N la inceput (depinde cum cere problema)
    // Daca problema cere N la inceput, decomenteaza linia de mai jos:
    // fout << N << endl; 

    for (int i = 0; i < N; ++i) {
        int val = rand() % 100;
        fout << val << " ";
    }
    fout.close();
    cout << "--> Generat " << numeFisier << " cu " << N << " numere." << endl;
}

int main44() {
    // Initializam generatorul random cu ceasul curent
    srand(time(NULL));

    int N_vector = 20; // Schimba aici marimea vectorilor
    int N_matrice = 6; // Schimba aici marimea matricei

    cout << "--- GENERATOR INPUT MPI ---" << endl;

    // 1. Generam fisierele pentru problema cu VECTORI (inputA, inputB)
    // inputA.txt: a=2, N=20
    genereazaInputVector("inputA.txt", N_vector, 2);
    // inputB.txt: b=3, N=20
    genereazaInputVector("inputB.txt", N_vector, 3);

    // 2. Generam fisierul pentru problema cu MATRICE (input.txt)
    genereazaInputMatrice("input.txt", N_matrice);

    // 3. Generam fisierul pentru problema cu Pare/Impare (numbers.txt)
    // numbers.txt contine doar numerele, separate prin spatiu
    genereazaVectorSimplu("numbers.txt", N_vector);

    cout << "Gata! Fisierele au fost create in folderul curent." << endl;
    
    // Opreste consola sa se inchida imediat (pt Visual Studio)
    int k; cin >> k; 
    return 0;
}