#pragma once
#include <vector>
#include "domain.h"
#include <fstream>


using std::vector;

class Basket {
private:
	vector<Product> basket;
	int sum;
public:
	//constructorul de cos
	Basket() noexcept = default;

	//returneaza suma
	int getsum();

	//goleste cosul
	void empty();

	//Stocheaza produse in Cos
	//Adauga un produs in cos si actualizeaza suma
	void store(const Product& p);

	//Genereeaza produse aleatoriu
	//Primeste un numar de produse pe care sa le genereze aleatoriu si le adauga in cos
	void generator(int nr, vector<Product> products);

	//Exporta intr-un fisier de extensi csv
	//Primeste numele fisierului ca si parametru si exporta datele in acesta
	void export1(string name);

	vector<Product>& getall();
};