#pragma once
#include "domain.h"
#include <vector>
#include<utility>
#include <random>
#include<map>
#include <fstream>
using std::vector;
using std::ostream;
using std::map;

class RepoException {
private:
	string message;
public:
	RepoException(string message) : message{ message } {};
	string getErrorMessage() {
		return this->message;
	}
	friend ostream& operator<<(ostream& stream, const RepoException& exception);
};

class RepoAbstract {
public:
	RepoAbstract() = default;
	RepoAbstract(const RepoAbstract& product) = delete;
	virtual void store(const Product& p) = 0;
	virtual void delete_product(const Product& p) = 0;
	virtual int findpoz(const Product& p) = 0;
	virtual void modify_product(Product& p, string new_name, string new_type, int price) = 0;
	virtual vector<Product>& getallproducts() = 0;
	virtual Product& find(string name, string productor) = 0;
	virtual bool exists(const Product& p) = 0;
	//virtual ~RepoAbstract()=0;
};

class RepositoryProducts :public RepoAbstract {
private:
	vector<Product> allProducts;
public:
	RepositoryProducts() noexcept = default;
	RepositoryProducts(const RepositoryProducts& ot) = delete;

	void store(const Product& p) override;
	/*
	Adauga in lista un produs
	Parametrii: p(Product type)
	Preconditii:Datele lui p sa fie valide
	*/

	void modify_product(Product& p, string new_name, string new_type, int price) override;
	/*
	Modifica un produs
	Parametrii:p(Product type),new_name(string),new_type(string),price(intreg)
	*/

	vector<Product>& getallproducts() override;
	/*
	Returneaza toate produsele
	*/

	Product& find(string name, string productor) override;
	/*
	Cauta un produs dupa nume si producator
	Parametrii: name(string),producator(string)
	Preconditii: sa existe produsul in lista
	*/

	int findpoz(const Product& p) override;
	/*
	Returneaza pozitia lui p
	Parametrii:p(Product Type)
	*/

	void delete_product(const Product& p) override;
	/*
	Sterge un produs
	Parametru:p(Product Type)
	Preconditii:sa existe p in lista
	*/

	bool exists(const Product& p) override;
	/*
	Returneaaza true daca exista in lista acest p,false altfel
	Parametrii: p(Product Type)
	*/
};

class RepositoryProductsFile : public RepositoryProducts {
private:
	string fName;
	void loadFromFile();
	void writeToFile();
public:
	RepositoryProductsFile(string fName) : RepositoryProducts(), fName{ fName } {
		loadFromFile();
	}
	void store(const Product& p) override {
		RepositoryProducts::store(p);
		writeToFile();
	}

	void delete_product(const Product& p) override {
		RepositoryProducts::delete_product(p);
		writeToFile();
	}
};

class RepositoryLab :public RepoAbstract {
private:
	map <int, Product> products_map;
	double probabilitate;
	void randomly() const;
public:
	void setProbability(double p);
	RepositoryLab(const RepoAbstract& alt) = delete;
	void store(const Product& p) override;
	void delete_product(const Product& p) override;
	vector<Product>& getallproducts() override;
	Product& find(string name, string productor) override;
};
