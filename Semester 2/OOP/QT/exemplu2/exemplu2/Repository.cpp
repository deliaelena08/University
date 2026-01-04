#include"Repository.h"

void RepoFile::loadData() {
	ifstream fin(fName);
	if (!fin.is_open()) {
		throw RepoException("Fisierul nu este deschis");
	}
	string title, artist;
	int id, rank;
	while (fin >> id >> title >> artist >> rank) {
		if (fin.eof())
			break;
		Melodie mel{ id,title,artist,rank };
		storeMel(mel);
	}
	fin.close();
}

void RepoFile::writeToFile() {
	ofstream fout(fName);
	if (!fout.is_open())
		throw RepoException("Fisierul nu se poate deschide");
	for (auto& mel : melodii) {
		fout << mel.getId() << " " << mel.getTitle() << " " << mel.getArtist() << " " << mel.getRank() << endl;
	}
	fout.close();
}

void RepoFile::deleteMel(Melodie& mel) {
	if (exists(mel.getId()) == false)
		throw RepoException("Melodia nu exista");
	int poz = 0;
	for (int i = 0; i < melodii.size();i++) {
		if (melodii[i].getId() == mel.getId()) {
			poz = i; break;
		}
	}
	melodii.erase(melodii.begin() + poz);
	writeToFile();
}

void RepoFile::storeMel(Melodie& mel) {
	if (exists(mel.getId()) == true)
		throw RepoException("Melodia cu id-ul dat exista");
	melodii.push_back(mel);
	writeToFile();
}

bool RepoFile::exists(int id) {
	bool ok = false;
	for (auto& mel:melodii)
		if (mel.getId() == id) {
			ok = true;
			break;
		}
	return ok;
}

void RepoFile::modifyMel(Melodie& mel,string title,int rank) {
	if (exists(mel.getId()) == false)
		throw RepoException("Melodia pe care vreti sa o modificati nu exista");
	for(auto& m:melodii)
		if (m.getId() == mel.getId()) {
			m.setRank(rank);
			m.setTitle(title);
		}
	writeToFile();
}

vector<Melodie>& RepoFile::getAll() {
	return melodii;
}