#pragma once
#include "repository.h"
#include <vector>
#include <string>

using std::vector;
using std::string;

class ExceptionValid {
	vector<string> errorMessages;
public:
	ExceptionValid(vector<string> errorMessages) : errorMessages{ errorMessages } {}
	string getErrorMessages() {
		string msg = "";
		for (const string& err : errorMessages) {
			msg += err;
		}
		return msg;
	}
};

class Validator {
public:
	void valid(const Tractor& tr) {
		vector<string> messages;
		if (tr.getName() == "")
			messages.push_back("Numele tractorului nu trebuie sa fie vid");
		if (tr.getType() == "")
			messages.push_back("Tipul tractorului nu trebuie sa fie vid");
		if (tr.getWheels() < 2 || tr.getWheels() > 16 || tr.getWheels() % 2 != 0)
			messages.push_back("Numarul de roti nu este valid");
		if (!messages.empty())
			throw ExceptionValid(messages);
	}
};

class Service {
private:
	RepoFile& repo;
	Validator& valid;
public:
	Service(RepoFile& repo, Validator& valid) noexcept : repo{ repo }, valid{ valid } {}  // Make constructor public
	Service(const Service& ot) = delete;

	void addTractor(int id, string name, string type, int wheels);
	void modifyTractors(int id, int wheels);
	const vector<Tractor> filtred(string type); 
	const vector<Tractor> sorted();
	const vector<Tractor>& getAllTractors() noexcept {
		return this->repo.getAll();
	}
};
