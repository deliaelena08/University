#include "basket.h"
#include <random>

void Basket::empty() {
	basket.clear();
	sum = 0;
}

void Basket::store(const Product& p) {
	basket.push_back(p);
	sum += p.getprice();
}

int Basket::getsum() {
	return sum;
}

vector<Product>& Basket::getall() {
	return basket;
}

void Basket::generator(int nr, vector<Product> products) {
	std::mt19937 mt{ std::random_device{}() };
	std::uniform_int_distribution<> dist(0, products.size() - 1);
	for (int i = 0; i < nr; i++) {
		int number = dist(mt);
		basket.push_back(products[number]);
		sum += products[number].getprice();
	}
}

void Basket::export1(string name) {
	ofstream f(name + ".csv");
	f << "Numele produsului;Tip;Pret;Producator\n";
	for (auto& p : basket) {
		f << p.getname() << ";" << p.gettype() << ";" << p.getprice() << ";" << p.getproductor() << "\n";
	}
	f.close();
}