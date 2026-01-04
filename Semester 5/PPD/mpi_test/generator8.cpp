#include <iostream>
#include <fstream>
#include <vector>
#include <cstdlib>
#include <ctime>

using namespace std;

void genereazaTest1() {
    // N=3 (Grad 3 -> 4 coeficienti: a0, a1, a2, a3)
    // Coef: 1, 2, 3, 4
    ofstream fp("polinom.txt");
    fp << "1 2 3 4" << endl;
    fp.close();

    // n=4 puncte: 1, 10, 100, 1000
    ofstream fi("input.txt");
    fi << 4 << endl; // Numarul n pe prima linie
    fi << "1 10 100 1000" << endl;
    fi.close();

    cout << "Generat Test Case 1 (Grad 3, ruleaza cu -n 4)" << endl;
}

void genereazaTest2() {
    // N=7 (Grad 7 -> 8 coeficienti: a0..a7)
    // Coef: 1 2 3 4 5 6 7 8
    ofstream fp("polinom.txt");
    fp << "1 2 3 4 5 6 7 8" << endl;
    fp.close();

    // n=100 puncte random
    int n = 100;
    ofstream fi("input.txt");
    fi << n << endl;
    for (int i = 0; i < n; ++i) {
        // Valori intre 0 si 100
        double val = (double)(rand() % 10000) / 100.0;
        fi << val << " ";
    }
    fi.close();

    cout << "Generat Test Case 2 (Grad 7, ruleaza cu -n 8)" << endl;
}

int mainm() {
    srand(time(NULL));
    int op;
    cout << "1. Test Case 1 (N=3, p=4)" << endl;
    cout << "2. Test Case 2 (N=7, p=8)" << endl;
    cout << "Alege: ";
    cin >> op;

    if (op == 1) genereazaTest1();
    else genereazaTest2();

    return 0;
}