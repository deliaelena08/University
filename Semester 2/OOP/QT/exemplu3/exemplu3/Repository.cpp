#include"Repository.h"

void RepoFile::loadFromFile() {
	std::ifstream fin(fName);
	if (!fin.is_open()) {
		throw RepoException("Fisierul nu poate fi deschis");
	}
	string suprafata, strada;
	int pret;
	while (fin >> suprafata >> strada >> pret) {
		if (fin.eof())
			break;
		Apartament ap{ suprafata,strada,pret };
		store(ap);
	}
	fin.close();
}
void RepoFile::writeToFile() {
	std::ofstream fout(fName);
	if (!fout.is_open()) {
		throw RepoException("Fisierul nu poate fi deschis");
	}
	for (auto& ap : apartamente) {
		if (fout.eof())
			break;
		fout << ap.getSuprafata() << ' ' << ap.getStrada() << ' ' << ap.getPret() << std::endl;
	}
	fout.close();
}

void RepoFile::store(Apartament& ap) {
	if (exists(ap.getStrada() ,ap.getSuprafata()) == true)
		throw RepoException("Apartamentul exista deja");
	apartamente.push_back(ap);
	writeToFile();
}

void RepoFile::deleted(Apartament& ap) {
	if (exists(ap.getStrada(), ap.getSuprafata()) == false)
		throw RepoException("Apartamentul nu exista");
	int index = 0;
	for (int i = 0; i < apartamente.size(); i++) {
		if (apartamente[i].getStrada() == ap.getStrada() && apartamente[i].getSuprafata() == ap.getSuprafata()){
			index = i;
			break;
		}
	}
	apartamente.erase(apartamente.begin() + index);
	writeToFile();
}

bool RepoFile::exists(string strada, string suprafata) {
	bool ok = false;
	for (auto& ap : apartamente) {
		if (ap.getStrada() == strada && ap.getSuprafata() == suprafata) {
			ok = true;
			break;
		}
	}
	return ok;

}