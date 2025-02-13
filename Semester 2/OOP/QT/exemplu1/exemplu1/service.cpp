#include "service.h"
#include<algorithm>

void Service::addTractor(int id, string name, string type, int wheels) {
	Tractor t1{ id,name,type,wheels };
	valid.valid(t1);
	repo.store(t1);
}

void Service::modifyTractors(int id, int wheels) {
	for (Tractor tr : getAllTractors()) {
		if (tr.getId() == id)
			repo.modifyTractor(tr, wheels);
	}
}

const vector<Tractor> Service::sorted() {
	int sz = getAllTractors().size();
	vector<Tractor> tractors = getAllTractors();
	for (int i = 0; i < sz-1; i++) {
		for (int j = i+1; j < sz; j++) {
			if (tractors[i].getName() > tractors[j].getName()) {
				Tractor aux = tractors[i];
				tractors[i] = tractors[j];
				tractors[j] = aux;
			}
		}
	}
	return tractors;
}

const vector<Tractor> Service::filtred(string type) {
	vector<Tractor> filtred;
	for (Tractor t : getAllTractors()) {
		if (t.getType() == type)
			filtred.push_back(t);
	}
	return filtred;
}