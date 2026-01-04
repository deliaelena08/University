#pragma once
#include"Domain.h"
#include<vector>
using std::vector;
using std::ostream;

class RepoException {
private:
	string msg;
public:
	RepoException(string msg) :msg{ msg } {};
	string getErrMsg() {
		return this->msg;
	}
	friend ostream& operator<<(ostream& stream, const RepoException& exception);
};
class RepoFile {
private:
	vector<Masina> masini;
	string fName = "";
	void loadFromFile();
	void writeToFile();
public:
	RepoFile(string fName):fName{fName}{
		loadFromFile();
	}
	RepoFile(const RepoFile& ot) = delete;

	void store(Masina& mas);
	vector<Masina> getAll() {
		return masini;
	}
};