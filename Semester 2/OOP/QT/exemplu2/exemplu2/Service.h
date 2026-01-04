#pragma once
#include"Repository.h"

class ExceptionValid {
private:
	vector<string> errors;
public:
	ExceptionValid(vector<string> errors) :errors{ errors } {}
	string getErrorMsg() {
		string msg = "";
		for (const string& err : errors) {
			msg += err;
		}
		return msg;
	}
};

class Validator {
public:
	void valid(const Melodie& mel) {
		vector<string> errors;
		if (mel.getArtist() == "")
			errors.push_back("Nume de artist invalid");
		if (mel.getTitle() == "")
			errors.push_back("Nume de piesa invalid");
		if (mel.getRank() < 0 or mel.getRank() > 10)
			errors.push_back("Rank invalid");
		if (errors.size() > 0)
			throw ExceptionValid(errors);
	}
};

class Service {
private:
	RepoFile& repo;
	Validator& valid;
public:
	Service(RepoFile& repo, Validator& valid) : repo{ repo }, valid{ valid }{}
	Service(const Service& ot) = delete;
	Melodie getMelody(int id);
	void addMelody(int id, string artist, string title, int rank);
	void deleteMelody(int id);
	void modifyMelody(int id, string title, int rank);
	vector<Melodie> filtre(string artist);
	vector<Melodie> filtre2(int rank);
	vector<Melodie> sorted();
	const vector<Melodie>& getAllMelodies() noexcept {
		return this->repo.getAll();
	}
};