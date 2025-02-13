#pragma once
#include"Repository.h"

class Service {
private:
	RepoFile& repo;
public:
	Service(RepoFile& repo) :repo{ repo } {};
	Service(const Service& ot) = delete;
	void storeCar(string firma, string model, int an, string culoare);
	vector<Masina> getAllCars() {
		return repo.getAll();
	}
	vector<Masina> sortByFirma();
	vector<Masina> filtreByFirma(string firma);

};