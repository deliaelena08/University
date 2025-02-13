#pragma once
#include"Domain.h"
#include<fstream>
#include<vector>
using namespace std;

using std::vector;

class RepoException {
private:
	string message;
public:
	RepoException(string message) :message{ message } {};
	string getErrorMessage() {
		return this->message;
	}
};

class RepoFile {
private:
	vector<Elev> elevi;
	string fName;
	//Incarca din fisier
	void loadData();
	//Scrie in fisier
	void writeToFile();

public:
	//Constructor
	RepoFile(string fName) :fName{ fName } {
		loadData();
	}
	//Distrcuctor
	RepoFile(const RepoFile& ot) = delete;
	//Incarca un elev
	void storeEl(Elev& el);
	//Verifica daca exista
	bool exists(int nrMatricol);
	//obtine toti elevii
	vector<Elev> getAll() {
		return this->elevi;
	}
};