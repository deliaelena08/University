#pragma once
#include<iostream>
#include<string.h>
using namespace std;
using std::string;

class Melodie {
private:
	int id = 0;
	string title = "";
	string artist = "";
	int rank = 0;
public:
	Melodie() = delete;
	Melodie(int id, string title, string artist, int rank) : id{ id }, title{ title }, artist{ artist }, rank{ rank } {};
	Melodie(const Melodie& ot) : id{ ot.id }, title{ ot.title }, artist{ ot.artist }, rank{ ot.rank } {}
	int getId() const;
	string getArtist() const;
	string getTitle() const;
	int getRank() const;

	void setId(int id);
	void setTitle(string title);
	void setArtist(string artist);
	void setRank(int rank);

};