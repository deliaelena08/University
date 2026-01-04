#include "UI.h"

#include <iostream>
using namespace std;



void testAll()
{
	testeDomain();
	cout << "Testele din domain trecute cu SUCCES.\n";
	testeService();
	cout << "Testele din service trecute cu SUCCES.\n";
	testeRepo();
	cout << "Testele din repo trecute cu SUCCES.\n";
}



void startApp() {

	BookRepository repo;
	BookValidator val;
	BookService srv{ repo, val };
	ConsoleUI ui{ srv };

	ui.run();
}

int main()
{
	testAll();
	//startApp();
}