#include <iostream>
#include <fstream>
#include <ctime>

using namespace std;

int main1() {
    srand(time(0));

    // testul 1: N=M=10, n=m=3
    int n = 10, m = 10, k = 3;

    // testul 2: N=M=1000 si n=m=5; p=2,4,8,16
    // int n = 1000, m = 1000, k = 5;

    // testul 3: N=10, M=10000 n=m=5
    // int n = 10, m = 10000, k = 5;

    //test 4: N=10000, M=10; n=m=5
    // int n = 10000, m = 10, k = 5;
    ofstream fout("date.txt");

    fout << n << " " << m << " " << k << "\n";

    // matricea de input
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++)
            fout << rand() % 100 << " ";
        fout << "\n";
    }

    // kernel-ul
    for (int i = 0; i < k; i++) {
        for (int j = 0; j < k; j++)
            fout << rand() % 3 - 1 << " "; //valori intre -1, 0, 1
        fout << "\n";
    }

    fout.close();
    cout << "Datele au fost scrie cu succes in date.txt\n";
    return 0;
}
