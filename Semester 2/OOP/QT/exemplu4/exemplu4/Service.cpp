#include"Service.h"

void Service::storeCar(string firma, string model, int an, string culoare) {
	Masina m1{ firma,model,an,culoare };
	repo.store(m1);
}

vector<Masina> Service::sortByFirma() {
	vector<Masina> sorted = getAllCars();
	for (int i = 0; i < sorted.size() - 1; i++) {
		for (int j = i + 1; j < sorted.size(); j++) {
			if (sorted[i].getFirma() > sorted[j].getFirma()) {
				Masina aux = sorted[i];
				sorted[i] = sorted[j];
				sorted[j] = aux;
			}
		}
	}
	return sorted;
}

vector<Masina> Service::filtreByFirma(string firma) {
	vector<Masina> filtred;
	for (auto& mas : getAllCars()) {
		if (mas.getFirma() == firma)
			filtred.push_back(mas);
	}
	return filtred;
}