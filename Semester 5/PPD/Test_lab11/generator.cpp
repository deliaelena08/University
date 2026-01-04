#include <iostream>
#include <fstream>
#include <cstdlib>
#include <ctime>

using namespace std;

int main1() {
    srand(time(NULL));
    int N = 12; 
    int X = 5;  
    
    ofstream fout("numbers.txt");
    if (!fout.is_open()) {
        cerr << "Error creating file." << endl;
        return 1;
    }

    fout << X << endl;

    for (int i = 0; i < N; ++i) {
        int val = 1 + rand() % 10; 
        fout << val << endl;
    }
    fout.close();

    cout << "Generated numbers.txt with X=" << X << " and " << N << " numbers." << endl;
    return 0;
}