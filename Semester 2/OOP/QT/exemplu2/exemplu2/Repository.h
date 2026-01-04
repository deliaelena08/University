#pragma once
#include"Domain.h"
#include<fstream>
#include<vector>
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
	vector<Melodie> melodii;
	string fName;
	void loadData();
	void writeToFile();

public:
	RepoFile(string fName) :fName{ fName } {
		loadData();
	}
	void deleteMel(Melodie &mel);
	void storeMel(Melodie& mel);
	bool exists(int id);
	void modifyMel(Melodie& mel,string title,int rank);
	vector<Melodie>& getAll();
};