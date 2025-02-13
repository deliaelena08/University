#pragma once
#include"Repository.h"

class Service {
private:
	RepoFile& repo;
public:
	Service(RepoFile& repo) :repo{ repo } {}
	Service(const Service& ot) = delete;
	void storeAp(string suprafata, string strada, int pret);
	void delteAp(string suprafata, string strada,int pret);
	vector<Apartament> filtreBySpace(vector<Apartament> list,string suprafata1, string suprafata2);
	vector<Apartament> filtreByPrice(vector<Apartament>list, int price1, int price2);
	vector<Apartament> getALlAp() {
		return repo.getAll();
	}
};