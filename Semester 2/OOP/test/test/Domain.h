#pragma once
#include<string>
#include<iostream>
using std::string;
class Elev {
private:
	int nrMatricol = 0;
	string nume = "";
	string scoala = "";
public:
	Elev() = delete;
	Elev(int nrMatricol, string nume, string scoala) :nrMatricol{ nrMatricol }, nume{ nume }, scoala{ scoala } {};
	Elev(const Elev& ot) :nrMatricol{ ot.nrMatricol },nume { ot.nume }, scoala{ ot.scoala } {}
	//Obtine numarul matricol
	int getNrMatricol();
	//Obtine numele
	string getnume();
	//Obtine scoala
	string getScoala();
};