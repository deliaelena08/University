#pragma once
#include"Reposoitory.h"

class Atelier {
private:
	string denumire = "";
	vector<Elev> elevi;
public:
	//Constructor atelier
	Atelier(string denumire) :denumire{ denumire } {}
	//Adauga elev in atelierul respectiv
	void addElev(Elev& el) {
		elevi.push_back(el);
	}
	//Obtine elevii unui atelier
	vector<Elev>& getELevi(){
		return elevi;
	}
};

class Service {
private:
	RepoFile& repo;
public:
	Service(RepoFile& repo) :repo{ repo } {}
	Service(const Service& ot) = delete;
	//Sorteaza dupa nume
	vector<Elev> sortNyName(vector<Elev> elevi);
	//Creaza un atelier
	void addAtelier(string denumire, Elev& el);
	//Obtine toti elecii
	vector<Elev> getAllElevi() {
		return repo.getAll();
	}
};