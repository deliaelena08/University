#include "Repository.h"
#include <assert.h>
#include <algorithm> 

bool BookRepository::exist_book(const Book& b) {
	try {
		find_book(b.getTitlu(), b.getAutor());
		return true;
	}
	catch (RepoException&) {
		return false;
	}
}

//////////////////////////////////////////////////////////////////////////////

const Book& BookRepository::find_book(string titlu, string autor)
{
	for (const Book& b : this->allBooks)
	{
		if (b.getTitlu() == titlu && b.getAutor() == autor)
			return b;
	}
	throw RepoException("Cartea cu titul " + titlu + " si autorul " + autor + " NU exista in lista.\n");
}

/////////////////////////////////////////////////////////////////////////////

int BookRepository::find_book_index(string titlu, string autor)
{
	int index = -1;
	for (size_t i = 0; i < allBooks.size(); i++)
		if (this->allBooks[(int)i].getTitlu() == titlu && this->allBooks[(int)i].getAutor() == autor)
		{
			return (int)i;
		}
	throw RepoException("Cartea cu titul " + titlu + " si autorul " + autor + " NU exista in lista.\n");
}

/////////////////////////////////////////////////////////////////////////////

void BookRepository::store(const Book& b)
{
	if (exist_book(b))
		throw RepoException("Cartea cu titul " + b.getTitlu() + " si autorul " + b.getAutor() + " exista in lista.\n");
	this->allBooks.push_back(b);
}

/////////////////////////////////////////////////////////////////////////////

void BookRepository::delete_book(string titlu, string autor)
{
	try {
		int index = find_book_index(titlu, autor);
		allBooks.erase(allBooks.begin() + index);
	}
	catch (RepoException&) {
		throw RepoException("Cartea cu titul " + titlu + " si autorul " + autor + " NU exista in lista.\n");
	}
}

/////////////////////////////////////////////////////////////////////////////

void BookRepository::update_book(string titlu, string autor, string gen_nou, int an_nou)
{
	try {
		int index = find_book_index(titlu, autor);
		this->allBooks[index].setGen(gen_nou);
		this->allBooks[index].setAn(an_nou);
	}
	catch (RepoException&) {
		throw RepoException("Cartea cu titul " + titlu + " si autorul " + autor + " NU exista in lista.\n");
	}
}

 

/////////////////////////////////////////////////////////////////////////////

const  MyVector<Book>& BookRepository::getAllBooks()
{
	return this->allBooks;
}

/////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////

void testStoreRepo()
{
	BookRepository testRepo;
	Book book1{ "Colt Alb", "Jack London", "fictiune", 1906 };
	testRepo.store(book1);
	assert(testRepo.getAllBooks().size() == 1);

	Book book2{ "Colt Alb", "Jack London", "roman", 1941 };
	try {
		testRepo.store(book2);
		//assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}
}

/////////////////////////////////////////////////////////////////////////////

void testFindRepo()
{
	BookRepository testRepo;

	Book book1{ "Singur pe lume", "Hector Malot", "fictiune", 1878 };
	Book book2{ "Toate panzele sus!", "Radu Tudoran", "actiune", 1954 };
	Book book3{ "Un apartament la Paris", "Michelle Gable", "istorie", 2015 };

	testRepo.store(book1);
	testRepo.store(book2);
	assert(testRepo.exist_book(book1));
	assert(!testRepo.exist_book(book3));

	Book foundBook = testRepo.find_book("Singur pe lume", "Hector Malot");
	assert(foundBook.getAn() == 1878);

	try {
		testRepo.find_book("aa", "aa");
		//assert(false);
	}
	catch (RepoException& ve) {
		assert(ve.getErrorMessage() == "Cartea cu titul aa si autorul aa NU exista in lista.\n");
	}
}

/////////////////////////////////////////////////////////////////////////////

void testDeleteRepo()
{
	BookRepository testRepo;

	Book book1{ "Singur pe lume", "Hector Malot", "fictiune", 1878 };
	Book book2{ "Toate panzele sus!", "Radu Tudoran", "actiune", 1954 };
	Book book3{ "Un apartament la Paris", "Michelle Gable", "istorie", 2015 };

	testRepo.store(book1);
	testRepo.store(book2);
	testRepo.store(book3);

	try {
		testRepo.delete_book("aa", "aa");
		//assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}

	testRepo.delete_book("Toate panzele sus!", "Radu Tudoran");
	assert(testRepo.getAllBooks().size() == 2);
}

void testUpdateRepo()
{
	BookRepository testRepo;

	Book book1{ "Singur pe lume", "Hector Malot", "fictiune", 1878 };
	Book book2{ "Toate panzele sus!", "Radu Tudoran", "actiune", 1954 };

	testRepo.store(book1);
	testRepo.store(book2);
	try {
		testRepo.update_book("aa", "aa", "nn", 1900);
		//assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}

	testRepo.update_book("Toate panzele sus!", "Radu Tudoran", "calatorie", 1955);
	Book foundBook = testRepo.find_book("Toate panzele sus!", "Radu Tudoran");
	assert(foundBook.getAn() == 1955);
	assert(foundBook.getGen() == "calatorie");
}
 

 
 

void testeRepo()
{
	testStoreRepo();
	testFindRepo();
	testDeleteRepo();
	testUpdateRepo();
}

