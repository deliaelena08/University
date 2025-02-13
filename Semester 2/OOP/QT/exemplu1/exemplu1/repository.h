#pragma once
#include<fstream>
#include<vector>
#include"domain.h"
using std::vector;

class RepoException {
private:
	string message;
public:
	RepoException(string message) :message{ message } {};
	string getErrorMessage() {
		return this->message;
	}
	friend ostream& operator<<(ostream& stream, const RepoException& exception);
};

class RepoFile {
private:
	vector<Tractor> tractoare;
	string fName;
	void loadFromFile();
	void writeToFile();
public:
	RepoFile(string fName) : fName{ fName } {
		loadFromFile();
	}

	void store(const Tractor& tr);
	void modifyTractor(Tractor& tr, int wheels);
	bool exist(int id);
	vector<Tractor>& getAll();

};