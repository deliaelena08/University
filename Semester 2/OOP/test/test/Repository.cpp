#include"Reposoitory.h"

void RepoFile::loadData() {
	ifstream fin(fName);
	if (!fin.is_open()) {
		throw RepoException("Fisierul nu a putit fi deschis");
	}
	string nume, scoala;
	int nrmat;
	while (fin >> nrmat >> nume >> scoala) {
		if (fin.eof())
			break;
		Elev e{ nrmat,nume,scoala };
		storeEl(e);
	}
	fin.close();
}

void RepoFile::writeToFile() {
	ofstream fout(fName);
	if (!fout.is_open())
		throw RepoException("Fisierul nu a putu fi deschis");
	for (auto& el : elevi) {
		if (fout.eof())
			break;
		fout << el.getNrMatricol() <<' '<< el.getnume() <<' '<< el.getScoala() << endl;
	}
	fout.close();
}


void RepoFile::storeEl(Elev& el) {
	elevi.push_back(el);
	writeToFile();
}

bool RepoFile::exists(int nrMatricol) {
	bool ok = false;
	for (auto& el : elevi) {
		if (el.getNrMatricol() == nrMatricol) {
			ok = true;
			break;
		}
	}
	return ok;
}
