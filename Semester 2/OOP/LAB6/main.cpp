/*
 * Memorarea tuturor perechilor de forma(cheie,valoare)
 * MDO reprezentare sub forma unei Tabele de disp cu rezolvare coliziuni  prin
 * adresare deschisa verificare patratica
 */

#include <iostream>
#include "TestScurt.h"
#include "TestExtins.h"
using namespace std;



int main() {
    cout<<"Test scurt"<<endl;
      testAll();
      testAllExtins();
      cout<<"Test functionaitate"<<endl;
      testFunctionalitate();
    cout << "Finished Tests!" << std::endl;

}
