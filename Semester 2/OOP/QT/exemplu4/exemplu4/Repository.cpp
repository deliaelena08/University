#include"Repository.h"
#include <fstream>

void RepoFile::loadFromFile() {
	std::ifstream fin(fName);
	if (!fin.is_open()) {
		throw RepoException("File is not open");
	}
	string firma, model, culoare;
	int an;
	while (fin >> firma >> model >> an >> culoare) {
		if (fin.eof())
			break;
		Masina m1{ firma,model,an,culoare };
		store(m1);
	}
	fin.close();
}

void RepoFile::writeToFile() {
	std::ofstream fout(fName);
	if (!fout.is_open())
		throw RepoException("File is not open");
	for (auto& mas : masini) {
		if (fout.eof())
			break;
		fout << mas.getFirma() << " | " << mas.getModel() << " | " << mas.getAn() << " | " << mas.getCuloARE() << std::endl;
	}
	fout.close();
}

void RepoFile::store(Masina& mas) {
	masini.push_back(mas);
	writeToFile();
}