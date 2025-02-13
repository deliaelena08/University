#include "repository.h"
#include <algorithm>

bool RepositoryProducts::exists(const Product& p) {
	
	try {
		find(p.getname(), p.getproductor());
		return true;
	}
	catch (RepoException&) {
		return false;
	}
}

const Product& RepositoryProducts::find(string name, string productor) {
	for (int i=0;i<allProducts.size();i++)
	{
		if (allProducts[i].getname() == name && allProducts[i].getproductor() == productor)
			return allProducts[i];
	}
	throw RepoException("Produsul cu numele " + name + " si cu producatorul " + productor + " nu exista");
}

void RepositoryProducts::store(const Product& p) {
	if (exists(p)) {
		throw RepoException("Produsul deja exista cu numele: " + p.getname() + " si producatorul: " + p.getproductor());
	}
	this->allProducts.push_back(p);
}

const DynamicVector<Product>& RepositoryProducts ::getallproducts() noexcept {

	return this->allProducts;
}

int RepositoryProducts::findpoz(const Product& p) {
	for (int i=0;i<allProducts.size();i++)
	{
		Product prod = find(allProducts[i].getname(), allProducts[i].getproductor());
		if (prod == p)
			return i;
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
		this->allProducts[poz]=p;
	}
	else
		throw RepoException("Produsul nu exista cu numele: " + p.getname() + " si producatorul: " + p.getproductor());
}

void RepositoryProducts:: delete_product(const Product& p) {
	int poz = findpoz(p);
	if (poz != -1)
		allProducts.erase(poz);
}
