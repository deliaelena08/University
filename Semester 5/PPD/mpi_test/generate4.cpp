#include <iostream>
#include <fstream>
#include <vector>
#include <cstdlib>
#include <ctime>

using namespace std;

void creazaTestCase1() {
    // 1. polinom.txt: Grad=3, Coef: 1, 2, 3, 4
    // Polinomul: 1 + 2x + 3x^2 + 4x^3
    ofstream fp("polinom.txt");
    fp << 3 << endl;        // Grad N
    fp << 1 << " " << 2 << " " << 3 << " " << 4 << endl; // a0 a1 a2 a3
    fp.close();

    // 2. input.txt: n=4 puncte: 1, 10, 100, 1000
    ofstream fi("input.txt");
    fi << 4 << endl;        // Numar puncte n
    fi << 1 << " " << 10 << " " << 100 << " " << 1000 << endl;
    fi.close();
    
    cout << "Generat Test Case 1 (Small)" << endl;
}

void creazaTestCase2() {
    srand(time(NULL));
    // N=50 (grad), n=1000 (puncte)
    int N = 50;
    int n = 1000;

    // polinom.txt
    ofstream fp("polinom.txt");
    fp << N << endl;
    for(int i=0; i<=N; ++i) {
        fp << (rand() % 10) << " "; // Coeficienti mici (0-9) sa nu dea overflow instant
    }
    fp.close();

    // input.txt
    ofstream fi("input.txt");
    fi << n << endl;
    for(int i=0; i<n; ++i) {
        // Generam numere reale intre 0 si 10
        double val = (double)(rand() % 1000) / 100.0; 
        fi << val << " ";
    }
    fi.close();
    
    cout << "Generat Test Case 2 (Random Large)" << endl;
}

int mainnn() {
    // ALEGE AICI CE TEST VREI SA GENEREZI:
    
    creazaTestCase1(); 
    // creazaTestCase2();
    
    return 0;
}