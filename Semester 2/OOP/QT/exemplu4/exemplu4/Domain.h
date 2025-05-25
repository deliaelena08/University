#pragma once
#include<iostream>
#include<string>
using std::string;
class Masina {
private:
	string firma="";
	string model = "";
	int an = 0;
	string culoare = "";
public:
	Masina() = delete;
	Masina(string firma, string model, int an, string culoare) :firma{ firma }, model{ model }, an{ an }, culoare{ culoare } {};
	Masina(const Masina& ot):firma{ot.firma},model{ot.model},an{ot.an},culoare{ot.culoare}{}
	
	string getFirma() {
		return this->firma;
	}

	string getModel() {
		return this->model;
	}

	int getAn() {
		return this->an;
	}

	string getCuloARE() {
		return this->culoare;
	}
 };