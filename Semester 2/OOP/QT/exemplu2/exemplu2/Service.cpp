#include"Service.h"

void Service::addMelody(int id, string artist, string title, int rank) {
	Melodie m1{ id,title,artist,rank };
	valid.valid(m1);
	repo.storeMel(m1);
}

Melodie Service::getMelody(int id) {
	vector<Melodie> melodies = repo.getAll();
	for (auto& m : melodies)
		if (m.getId() == id)
			return m;
}

void Service::deleteMelody(int id) {
	Melodie mel = getMelody(id);
	repo.deleteMel(mel);
}

void Service::modifyMelody(int id, string title, int rank) {
	Melodie mel = getMelody(id);
	repo.modifyMel(mel, title, rank);
}
vector<Melodie> Service::filtre(string artist) {
	vector<Melodie> melodies = repo.getAll();
	vector<Melodie> filtred;
	for (auto& mel : melodies) {
		if (mel.getArtist() == artist)
			filtred.push_back(mel);
	}
	return filtred;

}
vector<Melodie> Service::filtre2(int rank) {
	vector<Melodie> melodies = repo.getAll();
	vector<Melodie> filtred;
	for (auto& mel : melodies) {
		if (mel.getRank() == rank)
			filtred.push_back(mel);
	}
	return filtred;
}

vector<Melodie> Service::sorted() {
	vector<Melodie> melodies = repo.getAll();
	for (int i = 0; i < melodies.size() - 1; i++) {
		for(int j=i+1;j<melodies.size();j++)
			if (melodies[i].getTitle() > melodies[j].getTitle()) {
				Melodie aux = melodies[i];
				melodies[i] = melodies[j];
				melodies[j] = aux;
			}
	}
	return melodies;
}