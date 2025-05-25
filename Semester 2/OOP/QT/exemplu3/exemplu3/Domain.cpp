#include"Domain.h"

string Apartament::getSuprafata() {
	return this->suprafata;
}

string Apartament::getStrada() {
	return this->strada;
}

int Apartament::getPret() {
	return this->pret;
}

void Apartament::setSuprafata(string suprafata) {
	this->suprafata = suprafata;
}

void Apartament::setStrada(string strada) {
	this->strada = strada;
}

void Apartament::setPret(int pret) {
	this->pret = pret;
}