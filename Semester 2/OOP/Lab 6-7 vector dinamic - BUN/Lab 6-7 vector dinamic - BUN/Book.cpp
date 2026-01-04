#include "Book.h"
#include <assert.h>

string Book::getTitlu() const {
	return this->titlu;
}
string Book::getAutor() const {
	return this->autor;
}
string Book::getGen() const {
	return this->gen;
}
int Book::getAn() const {
	return this->an;
}

void Book::setTitlu(string titluNou) {
	this->titlu = titluNou;
}
void Book::setAutor(string autorNou) {
	this->autor = autorNou;
}
void Book::setGen(string genNou) {
	this->gen = genNou;
}
void Book::setAn(int anNou) {
	this->an = anNou;
}

std::ostream& operator<<(std::ostream& stream, const Book& b) {
	stream << "Titlu: " << b.titlu << "  |  Autor: " << b.autor << "  |  Gen: " << b.gen << "  |  An: " << b.an << "\n";

	return stream;
}

 

/////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////

void testGetSet()
{
	Book book{ "Un apartament la Paris", "Michelle Gable", "istorie", 2015 };
	assert(book.getTitlu() == "Un apartament la Paris");
	assert(book.getAutor() == "Michelle Gable");
	assert(book.getGen() == "istorie");
	assert(book.getAn() == 2015);

	book.setTitlu("Trandafirii pierduti");
	book.setAutor("Martha Hall Kelly");
	book.setGen("razboi");
	book.setAn(1914);

	assert(book.getTitlu() == "Trandafirii pierduti");
	assert(book.getAutor() == "Martha Hall Kelly");
	assert(book.getGen() == "razboi");
	assert(book.getAn() == 1914);
}

void testeDomain()
{
	testGetSet();
}
