#include "repository.h"

bool RepoFile::exist(int id) {
	for (auto& tr : tractoare) {
		if (tr.getId() == id)
			return true;
	}
	return false;
}

void RepoFile::store(const Tractor& tr) {
	if (exist(tr.getId()) == true) {
		throw RepoException("Tractorul exista!!!");
	}
	tractoare.push_back(tr);
	writeToFile();
}

vector<Tractor>& RepoFile::getAll() {
	return tractoare;
}

void RepoFile::modifyTractor(Tractor& tr, int wheels) {
	if (exist(tr.getId()) == false) {
		throw RepoException("Tractorul nu exista!!!");
	}
	for (auto& aux : tractoare) {
		if (tr.getId() == aux.getId())
			tr.setWheels(wheels);
	}
	writeToFile();
}

void RepoFile::loadFromFile() {
	string name, type;
	int id, wheels;
	ifstream in(fName);
	if (!in.is_open()) {
		throw RepoException("Nu s-a putut deschide fisierul");
	}
	while (in >> id >> name >> type >> wheels) {
		if (in.eof())
			break;
		Tractor tr{ id,name,type,wheels };
		store(tr);
	}
	in.close();
}

void RepoFile::writeToFile() {
	ofstream out(fName);
	if (!out.is_open()) {
		throw RepoException("Nu s-a putut deschide fisierul");
	}
	for (auto& tr : tractoare) {
		out << tr.getId() << " " << tr.getName() << " " << tr.getType() << " " << tr.getWheels() << endl;
	}
	out.close();
}