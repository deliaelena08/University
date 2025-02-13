#include "domain.h"

int Tractor::getId() const {
	return this->id;
}

string Tractor::getName() const {
	return this->name;
}

string Tractor::getType() const {
	return this->type;
}

int Tractor::getWheels() const {
	return this->wheels;
}

void Tractor::setId(int id) {
	this->id = id;
}

void Tractor::setName(string name) {
	this->name = name;
}

void Tractor::setType(string type) {
	this->type = type;
}

void Tractor::setWheels(int wheels) {
	this->wheels = wheels;
}