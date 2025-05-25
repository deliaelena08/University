#include"Service.h"


vector<Elev> Service::sortNyName(vector<Elev> elevi) {
	for (int i = 0; i < elevi.size()-1; i++) {
		for (int j = i + 1; j < elevi.size(); j++) {
			if (elevi[i].getnume() > elevi[j].getnume()) {
				Elev aux = elevi[i];
				elevi[i] = elevi[j];
				elevi[j] = aux;
			}
		}
	}
	return elevi;
}

void Service::addAtelier(string denumire, Elev& el) {
	Atelier at{ denumire };
	at.addElev(el);
}