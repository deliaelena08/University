#include <iostream>
#include <fstream>
#include <vector>
#include <cstdlib>
#include <ctime>

using namespace std;

struct Intrare {
    int id;
    double temp;
};

void genereazaFisier(string nume, int nrCartiere, int nrMasuratoriPerCartier) {
    ofstream fout(nume);
    // Scriem numarul total de cartiere pe prima linie (ajuta la alocare)
    // Deși cerința nu zice explicit, e good practice.
    // Dar vom respecta strict formatul "ID Valoare"

    vector<Intrare> date;

    // Generam datele
    for (int id = 0; id < nrCartiere; ++id) {
        for (int m = 0; m < nrMasuratoriPerCartier; ++m) {
            // Temp intre 10.0 si 30.0
            double t = 10.0 + (rand() % 2000) / 100.0;
            date.push_back({id, t});
        }
    }

    // Le amestecam putin ca sa nu fie toate ID-urile grupate (optional, dar realist)
    for (int i = 0; i < date.size(); ++i) {
        int r = rand() % date.size();
        swap(date[i], date[r]);
    }

    for (const auto& d : date) {
        fout << d.id << " " << d.temp << endl;
    }

    fout.close();
    cout << "Generat " << nume << " cu " << date.size() << " inregistrari." << endl;
}

int main() {
    srand(time(NULL));
    int optiune;
    cout << "Alege Test Case (1 sau 2): ";
    cin >> optiune;

    if (optiune == 1) {
        // Caz 1: 3 cartiere, 5 temp fiecare
        genereazaFisier("temperaturi.txt", 3, 5);
    } else {
        // Caz 2: 17 cartiere, 59 temp fiecare
        genereazaFisier("temperaturi.txt", 17, 59);
    }
    return 0;
}