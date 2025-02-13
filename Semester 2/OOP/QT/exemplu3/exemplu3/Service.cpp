#include"Service.h"

void Service::storeAp(string suprafata, string strada, int pret) {
	Apartament ap{ suprafata,strada,pret };
	repo.store(ap);
}

void Service::delteAp(string suprafata, string strada,int pret) {
	Apartament ap{ suprafata,strada,pret };
	repo.deleted(ap);
}

vector<Apartament> Service::filtreBySpace(vector<Apartament> apartamente,string suprafata1, string suprafata2) {
	vector<Apartament> filtred;
	for (auto& ap : apartamente) {
		if (ap.getSuprafata() >= suprafata1 && ap.getSuprafata() <= suprafata2)
			filtred.push_back(ap);
	}
	return filtred;
}

vector<Apartament> Service::filtreByPrice(vector<Apartament> apartamente,int price1, int price2) {
	vector<Apartament> filtred;
	for (auto& ap : apartamente) {
		if (ap.getPret() >= price1 && ap.getPret() <= price2)
			filtred.push_back(ap);
	}
	return filtred;
}