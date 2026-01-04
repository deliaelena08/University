#include "service.h"
#include <functional>
#include <algorithm>
using std::sort;

vector<Product> ServiceProduct::generalSort(bool(*funct)(const Product&, const Product&)) {
	vector<Product>v{ repo.getallproducts() };
	sort(v.begin(), v.end(), funct);
	return v;
}

vector<Product> ServiceProduct::filtre(function<bool(const Product&)>fct) {
	vector<Product> filtredlist;
	vector<Product> list = repo.getallproducts();
	copy_if(list.begin(), list.end(), std::back_inserter(filtredlist), fct);
	return filtredlist;
}
void ServiceProduct::addProduct(string name, string type, int price, string productor) {
	Product p1{ name,type,price,productor };
	valid.valid(p1);
	repo.store(p1);
	undoActions.push_back(std::make_unique<UndoAdd>(p1, repo));
}

void ServiceProduct::deleteProduct(string name, string productor) {
	Product p1 = repo.find(name, productor);
	repo.delete_product(p1);
	undoActions.push_back(std::make_unique<UndoDelete>(p1, repo));
}

const Product& ServiceProduct::find_product(string name, string productor) {
	return repo.find(name, productor);
}

void ServiceProduct::modifyProduct(string name, string productor, string new_name, string new_type, int price) {
	Product p = repo.find(name, productor);
	auto copy = p;
	repo.modify_product(p, new_name, new_type, price);
	undoActions.push_back(std::make_unique<UndoModify>(p, copy, repo));

}



const vector<Product> ServiceProduct::filtre_by_name(string name) {

	return filtre([name](const Product& p) {
		return p.getname() == name;
		});
}

const vector<Product> ServiceProduct::filtre_by_price(int price) {
	return filtre([price](const Product& p) {
		return p.getprice() <= price;
		});
}

const vector<Product> ServiceProduct::filtre_by_productor(string productor) {
	return filtre([productor](const Product& p) {
		return p.getproductor() == productor;
		});
}

const vector<Product>  ServiceProduct::sort_by_name() {
	return generalSort([](const Product& p1, const Product& p2) {
		return p1.getname() < p2.getname();
		});
}

const vector<Product> ServiceProduct::sort_by_price() {
	return generalSort([](const Product& p1, const Product& p2) {
		return p1.getprice() < p2.getprice();
		});
}

const vector<Product>  ServiceProduct::sort_by_name_and_price() {
	return generalSort([](const Product& p1, const Product& p2) {
		return p1.getname() < p2.getname() or (p1.getname() == p2.getname() and p1.getprice() < p2.getprice());
		});
}

void ServiceProduct::addToBasket(string product, string productor) {
	auto& p = repo.find(product, productor);
	basket.store(p);
}

int ServiceProduct::getSumBasket() {
	return basket.getsum();
}

void ServiceProduct::emptyBasket() {
	basket.empty();
}

void ServiceProduct::exportBasket(string name) {
	basket.export1(name);
}

void ServiceProduct::generate1(int number) {
	basket.generator(number, repo.getallproducts());
}

vector<Product>& ServiceProduct::getAllBasket(){
	return basket.getall();
}

map<string, int> ServiceProduct::createMap(vector<Product> list) {
	map<string, int> mp;
	for (auto& prod : list) {
		if (mp[prod.gettype()] >= 1)
			mp[prod.gettype()]++;
		else
			mp[prod.gettype()] = 1;
	}
	return mp;
}

void ServiceProduct::undo() {
	if (undoActions.empty()) {
		throw RepoException{ "Nu mai exista operatii undo" };
	}
	undoActions.back()->doUndo();
	undoActions.pop_back();
}