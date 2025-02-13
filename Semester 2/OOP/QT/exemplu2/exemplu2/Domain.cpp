#include"Domain.h"

int Melodie::getId() const {
	return this->id;
}

string Melodie::getArtist() const {
	return this->artist;
}

string Melodie::getTitle() const {
	return this->title;
}

int Melodie::getRank() const {
	return this->rank;
}

void Melodie::setId(int id) {
	this->id = id;
}

void Melodie::setTitle(string title) {
	this->title = title;
}

void Melodie::setArtist(string artist) {
	this->artist = artist;
}

void Melodie::setRank(int rank) {
	this->rank = rank;
}
