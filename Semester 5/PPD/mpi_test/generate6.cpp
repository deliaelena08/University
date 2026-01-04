#include <iostream>
#include <fstream>
#include <vector>
#include <cstdlib>
#include <ctime>

using namespace std;

void genereazaFisiere(int n, double min_val, double max_val) {
    ofstream f_func("functie.txt");
    double A = 1 + rand() % 5;
    double B = 1 + rand() % 5;
    double C = rand() % 10;
    double D = rand() % 10;
    f_func << A << " " << B << " " << C << " " << D << endl;
    f_func.close();
    cout << "Generat functie.txt: A=" << A << " B=" << B << " C=" << C << " D=" << D << endl;

    ofstream f_in("input.txt");
    for (int i = 0; i < n; ++i) {
        double val = min_val + (double)rand() / RAND_MAX * (max_val - min_val);
        f_in << val << " ";
    }
    f_in.close();
    cout << "Generat input.txt cu " << n << " valori." << endl;
}

int main9() {
    srand(time(NULL));
    int optiune;
    cout << "1. Test Case 1 (10 valori, P=5)" << endl;
    cout << "2. Test Case 2 (10000 valori)" << endl;
    cout << "Alege: ";
    cin >> optiune;

    if (optiune == 1) {
        genereazaFisiere(10, 0.0, 10.0);
    } else {
        genereazaFisiere(10000, 0.0, 2.0);
    }

    return 0;
}