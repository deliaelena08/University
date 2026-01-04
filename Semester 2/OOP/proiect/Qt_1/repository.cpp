#include "repository.h"
#include <algorithm>

bool RepositoryProducts::exists(const Product& p) {
	auto rez = find_if(allProducts.begin(), allProducts.end(), [p](const Product& prod) {
		return p == prod;
		});
	return rez != allProducts.end();
}

Product& RepositoryProducts::find(string name, string productor) {

	auto rez = find_if(allProducts.begin(), allProducts.end(), [name, productor](const Product& p)
		{return p.getname() == name && p.getproductor() == productor;
		});
	if (rez != allProducts.end())
		return *rez;
	else
		throw RepoException("Produsul cu numele " + name + " si cu producatorul " + productor + " nu exista");
}

void RepositoryProducts::store(const Product& p) {
	if (exists(p)) {
		throw RepoException("Produsul deja exista cu numele: " + p.getname() + " si producatorul: " + p.getproductor());
	}
	this->allProducts.push_back(p);
}

vector<Product>& RepositoryProducts::getallproducts() {
	return this->allProducts;
}

int RepositoryProducts::findpoz(const Product& p) {
	int poz = 0;
	for (auto& prod : this->allProducts)
	{
		if (prod == p)
			return poz;
		poz++;
	}
	return -1;
}


void RepositoryProducts::modify_product(Product& p, string new_name, string new_type, int price) {
	int poz = findpoz(p);
	if (poz != -1)
	{
		p.setname(new_name);
		p.setprice(price);
		p.settype(new_type);
		this->allProducts[poz] = p;
	}
	else
		throw RepoException("Produsul nu exista cu numele: " + p.getname() + " si producatorul: " + p.getproductor());
}

void RepositoryProducts::delete_product(const Product& p) {
	int poz = findpoz(p);
	if (poz != -1)
		allProducts.erase(allProducts.begin() + poz);
}

ostream& operator<<(ostream& stream, const RepoException& exception)
{
	stream << exception.message << "\n";
	return stream;
}
void RepositoryProductsFile::loadFromFile() {
	string product, type, productor;
	int price;
	ifstream in(fName);
	if (!in.is_open()) {
		throw RepoException("Nu am putut deschide fisierul " + fName);
	}
	while (in >> product >> type >> price >> productor) {
		if (in.eof()) {
			break;
		}
		Product p{ product,type,price,productor };
		RepositoryProducts::store(p);
	}
	in.close();
}

void RepositoryProductsFile::writeToFile() {
	ofstream out(fName);
	if (!out.is_open()) {
		throw RepoException("Nu am putut gasi fisierul " + fName);
	}
	for (auto& p : getallproducts()) {
		out << p.getname() << " " << p.gettype() << " " << p.getprice() << " " << p.getproductor() << endl;
	}
	out.close();
}
