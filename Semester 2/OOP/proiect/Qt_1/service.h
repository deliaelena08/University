#pragma once
#include "validator.h"
#include "repository.h"
#include "basket.h"
#include "undo.h"
#include <map>
#include <functional>
using std::function;
using std::map;
using std::unique_ptr;

class ServiceProduct {
	vector<unique_ptr<UndoAction>> undoActions;
private:
	RepositoryProducts& repo;
	//RepositoryProductsFile repo{ "Products.txt" };
	ProductValidator& valid;
	Basket& basket;
	vector<Product> generalSort(bool(*funct)(const Product&, const Product&));
	vector<Product> filtre(function<bool(const Product&)>fct);
public:
	//Constructor
	ServiceProduct(/*RepositoryProductsFile repo*/RepositoryProducts& repo, ProductValidator& valid, Basket& basket) noexcept :repo{ repo }, valid{ valid }, basket{ basket } {};

	//Destructor
	ServiceProduct(const ServiceProduct& ot) = delete;

	void addProduct(string name, string type, int price, string productor);
	/*
	Adauga un produs
	Parametrii:name(string),type(string),price(intreg),productor(string)
	Preconditii: Datele sa fie valide si sa nu se repede
	*/

	void deleteProduct(string name, string productor);
	/*
	Sterge produsul
	Parametrii:name(string),productor(string)
	Preconditii:Sa existe un produs cu datele introduse
	*/

	void modifyProduct(string name, string productor, string new_name, string new_type, int price);
	/*
	Modifica un produs
	Parametrii:name(string),productor(string),new_name(string),new_type(string),price(intreg)
	Preconditii: sa existe un produs cu datele introduse
	*/

	const vector<Product> sort_by_name();
	/*
	Ordoneaza crescator dupa nume
	*/

	const vector<Product> sort_by_price();
	/*
	Ordoneaza crescator dupa pret
	*/

	const vector<Product> sort_by_name_and_price();
	/*
	Ordoneaza crecscator dupa nume iar daca numele sunt egale va lua in considerare pretul cel mai mic
	*/

	const vector<Product> filtre_by_name(string name);
	/*
	Selecteaza produsele cu numele dat de utilizator
	*/

	const vector<Product> filtre_by_price(int price);
	/*
	Selecteaza produsele care au un pret mai mica sau egal decat cel introdus de utilizator
	*/

	const vector<Product> filtre_by_productor(string productor);
	/*
	Selecteaza produsele care sunt facute de un producator dat
	*/

	const Product& find_product(string name, string productor);
	/*
	Cauta un produs
	Parametrii:name(string),productor(string)
	Preconditii:sa existe produsul cu datele introdus
	*/

	//Returneaza toate produsele
	const vector<Product>& getAll() noexcept {
		return this->repo.getallproducts();
	}

	/*
	Adauga in cos un produs
	Parametrii:product(numele unui produs existent),productor(Producatorul acelui produs)
	Preconditii: sa existe acel produs
	*/
	void addToBasket(string product, string productor);

	//Goleste cosul actual
	void emptyBasket();

	/*
	Exporta datele cosului in fisier
	Parametrii:numele unui fisier
	Preconditii: sa existe produse in cos
	*/
	void exportBasket(string name);

	Basket& getBasket() {
		return basket;
	}

	vector<Product>& getAllBasket();

	/*
	Genereaza aleatoriu produse
	Parametrii: un numar intreg pentru numarul de generare a produselor
	*/
	void generate1(int number);

	/*
	Returneaza suma Cosului
	*/
	int getSumBasket();

	/*
	Creaza un dictionar pentru lista primita ca parametru
	Parametrii:o lista de produse
	 Map-ul este de forma Tip produse-numarul de astfel de produse existent
	*/
	map<string, int> createMap(vector<Product> list);
	void undo();
};