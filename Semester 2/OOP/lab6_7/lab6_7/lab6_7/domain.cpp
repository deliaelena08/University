#include "domain.h"

string Product::getname() const {
	return this->name;
}

string Product::gettype() const {
	return this->type;
}

int Product::getprice() const noexcept{
	return this->price;
}

string Product::getproductor() const {
	return this->productor;
}

void Product::setname(string name1) {
	this->name = name1;
}

void Product::settype(string type1) {
	this->type = type1;
}

void Product::setprice(int price1) noexcept {
	this->price = price1;
}

void Product::setproductor(string productor1) {
	this->productor = productor1;
}
