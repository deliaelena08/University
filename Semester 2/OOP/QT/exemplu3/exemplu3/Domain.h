#pragma once
#include<iostream>
#include<string.h>
using std::string;

class Apartament {
private:
	string suprafata = "";
	string strada = "";
	int pret = 0;
public:
	Apartament() = delete;
	Apartament(string suprafata, string strada, int pret) :suprafata{ suprafata }, strada{ strada }, pret{ pret } {};
	Apartament(const Apartament& ot) :suprafata{ ot.suprafata }, strada{ ot.strada }, pret{ ot.pret }{}

	string getSuprafata();
	string getStrada();
	int getPret();

	void setSuprafata(string suprafata);
	void setStrada(string strada);
	void setPret(int pret);

};