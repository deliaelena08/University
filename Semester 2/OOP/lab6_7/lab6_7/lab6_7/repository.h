#pragma once
#include "domain.h"
#include "DynamicVector.h"
#include "DynamicVector.cpp"

class RepoException {
private:
	string message;
public:
	RepoException(string message) : message{ message } {};
	string getErrorMessage() {
		return this->message;
	}
};


class RepositoryProducts {
	private:
		DynamicVector<Product> allProducts;
	public:
		RepositoryProducts() noexcept = default;
		//RepositoryProducts(const RepositoryProducts& ot) = delete;

		void store(const Product& p);
		/*
		Adauga in lista un produs
		Parametrii: p(Product type)
		Preconditii:Datele lui p sa fie valide
		*/

		void modify_product(Product& p, string new_name, string new_type, int price);
		/*
		Modifica un produs 
		Parametrii:p(Product type),new_name(string),new_type(string),price(intreg)
		*/

		const DynamicVector<Product>&  getallproducts() noexcept;
		/*
		Returneaza toate produsele
		*/

		const Product& find(string name, string productor);
		/*
		Cauta un produs dupa nume si producator
		Parametrii: name(string),producator(string)
		Preconditii: sa existe produsul in lista
		*/

		int findpoz(const Product& p);
		/*
		Returneaza pozitia lui p
		Parametrii:p(Product Type)
		*/

		void delete_product(const Product& p);
		/*
		Sterge un produs
		Parametru:p(Product Type)
		Preconditii:sa existe p in lista
		*/

		bool exists(const Product& p);
		/*
		Returneaaza true daca exista in lista acest p,false altfel
		Parametrii: p(Product Type)
		*/
};