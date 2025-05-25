#pragma once
#include "validator.h"
#include "repository.h"

class ServiceProduct {
private:
	RepositoryProducts& repo;
	ProductValidator& valid;
public:
	ServiceProduct(RepositoryProducts& repo, ProductValidator& valid) noexcept :repo{ repo }, valid{ valid } {};
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

	void modifyProduct(string name, string productor,string new_name,string new_type,int price);
	/*
	Modifica un produs
	Parametrii:name(string),productor(string),new_name(string),new_type(string),price(intreg)
	Preconditii: sa existe un produs cu datele introduse
	*/

	const DynamicVector<Product> sort_by_name();
	/*
	Ordoneaza crescator dupa nume
	*/

	const DynamicVector<Product> sort_by_price();
	/*
	Ordoneaza crescator dupa pret
	*/

	const DynamicVector<Product> sort_by_name_and_price();
	/*
	Ordoneaza crecscator dupa nume iar daca numele sunt egale va lua in considerare pretul cel mai mic
	*/

	const DynamicVector<Product> filtre_by_name(string name);
	/*
	Selecteaza produsele cu numele dat de utilizator
	*/

	const DynamicVector<Product> filtre_by_price(int price);
	/*
	Selecteaza produsele care au un pret mai mica sau egal decat cel introdus de utilizator
	*/

	const DynamicVector<Product> filtre_by_productor(string productor);
	/*
	Selecteaza produsele care sunt facute de un producator dat
	*/

	const Product& find_product(string name, string productor);
	/*
	Cauta un produs
	Parametrii:name(string),productor(string)
	Preconditii:sa existe produsul cu datele introdus
	*/

	const DynamicVector<Product>& getAll() noexcept {
		return this->repo.getallproducts();
	}

};