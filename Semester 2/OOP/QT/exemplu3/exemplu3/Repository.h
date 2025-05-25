#pragma once
#include"Domain.h"
#include<vector>
#include<fstream>
#include<iostream>
using std::vector;
using std::ostream;

class RepoException {
private:
	string msg;
public:
	RepoException(string msg) :msg{ msg } {}
	string getErrorMsg() {
		return this->msg;
	}
	//friend ostream& operator<<(ostream& stream, const RepoException& exception);
};

class RepoFile {
private:
	vector<Apartament> apartamente;
	string fName;
	void loadFromFile();
	void writeToFile();
public:
	RepoFile(string fName) :fName{ fName }{
		loadFromFile();
	}
	RepoFile(const RepoFile& ot) = delete;

	void store(Apartament& ap);
	void deleted(Apartament& ap);
	bool exists(string strada,string suprafata);
	vector<Apartament> getAll() {
		return apartamente;
	}
};