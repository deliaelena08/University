#pragma once
#include "Service.h"
#include <assert.h>
#include <algorithm>

void BookService::addBook(string titlu, string autor, string gen, int an)
{
	Book b{ titlu, autor, gen, an };
	val.valideaza(b);
	repo.store(b);
}

/////////////////////////////////////////////////////////////////////////////////////

void BookService::deleteBook(string titlu, string autor)
{
	repo.delete_book(titlu, autor);
}

/////////////////////////////////////////////////////////////////////////////////////

void BookService::modifyBook(string titlu, string autor, string gen_nou, int an_nou)
{
	repo.update_book(titlu, autor, gen_nou, an_nou);
}

/////////////////////////////////////////////////////////////////////////////////////

const Book& BookService::findBook(string titlu, string autor)
{
	return repo.find_book(titlu, autor);
}

/////////////////////////////////////////////////////////////////////////////////////

const  MyVector<Book> BookService::filter_by_title(const string titlu)
{
	MyVector<Book> filteredBooks;
	MyVector<Book> all_books = repo.getAllBooks();

	for (size_t i = 0; i < all_books.size(); i++)
		if (all_books[i].getTitlu() == titlu) {
			filteredBooks.push_back(all_books[i]);
		}
	return filteredBooks;
}

/////////////////////////////////////////////////////////////////////////////////////

const  MyVector<Book> BookService::filter_by_year(const int an)
{
	MyVector<Book> filteredBooks;
	MyVector<Book> all_books = repo.getAllBooks();

	for (size_t i = 0; i < all_books.size(); i++)
		if (all_books[i].getAn() == an) {
			filteredBooks.push_back(all_books[i]);
		}
	return filteredBooks;
}

/////////////////////////////////////////////////////////////////////////////////////

const  MyVector<Book> BookService::sort_books(const string& criteriu) {

	//se ordoneaza dupa titlu, autor sau an, iar daca toua carti sunt egale dpdv al acestui criteriu, ele se ordoneaza dupa gen

	auto compareFunction = [&criteriu](const Book& a, const Book& b) {
		if (criteriu == "titlu") {
			if (a.getTitlu() == b.getTitlu())
				return a.getGen() < b.getGen();
			else
				return a.getTitlu() < b.getTitlu();
		}

		else if (criteriu == "autor") {
			if (a.getAutor() == b.getAutor())
				return a.getGen() < b.getGen();
			else
				return a.getAutor() < b.getAutor();
		}

		else if (criteriu == "an") {
			if (a.getAn() == b.getAn())
				return a.getGen() < b.getGen();
			else
				return a.getAn() < b.getAn();
		}
		};


	MyVector<Book> sortBooks = repo.getAllBooks();

	sort(sortBooks.begin(), sortBooks.end(), compareFunction);

	return sortBooks;
};

/////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////

void testAddService()
{
	BookRepository testRepo;
	BookValidator testValid;
	BookService testService{ testRepo, testValid };

	testService.addBook("Singur pe lume", "Hector Malot", "fictiune", 1878);
	testService.addBook("Toate panzele sus!", "Radu Tudoran", "actiune", 1954);
	testService.addBook("Un apartament la Paris", "Michelle Gable", "istorie", 2015);

	assert(testService.getAllBooks().size() == 3);
	try {
		testService.addBook("Toate panzele sus!", "Radu Tudoran", "actiune", 1954);
	}
	catch (RepoException&) {
		assert(true);
	}

	try {
		testService.addBook("Carte", "", "actiune", 1954);
	}
	catch (ValidationException&) {
		assert(true);
	}

	try {
		testService.addBook("CARTE", "Radu Tudoran", "a", 1954);
	}
	catch (ValidationException&) {
		assert(true);
	}

	try {
		testService.addBook("", "Radu Tudoran", "actiune", 1954);
	}
	catch (ValidationException&) {
		assert(true);
	}

	try {
		testService.addBook("Carte", "Radu Tudoran", "actiune", 2025);
	}
	catch (ValidationException& ve) {
		assert(ve.getErrorMessages() == "Anul aparitiei nu poate fi peste 2024. \n");
	}
}

void testDeleteService()
{
	BookRepository testRepo;
	BookValidator testValid;
	BookService testService{ testRepo, testValid };

	testService.addBook("Singur pe lume", "Hector Malot", "fictiune", 1878);
	testService.addBook("Toate panzele sus!", "Radu Tudoran", "actiune", 1954);
	testService.addBook("Un apartament la Paris", "Michelle Gable", "istorie", 2015);

	testService.deleteBook("Toate panzele sus!", "Radu Tudoran");
	assert(testService.getAllBooks().size() == 2);
}

void testFindService()
{
	BookRepository testRepo;
	BookValidator testValid;
	BookService testService{ testRepo, testValid };

	testService.addBook("Singur pe lume", "Hector Malot", "fictiune", 1878);
	testService.addBook("Toate panzele sus!", "Radu Tudoran", "actiune", 1954);
	testService.addBook("Un apartament la Paris", "Michelle Gable", "istorie", 2015);

	assert(testService.findBook("Singur pe lume", "Hector Malot").getAn() == 1878);
}

void testModifyService()
{
	BookRepository testRepo;
	BookValidator testValid;
	BookService testService{ testRepo, testValid };

	testService.addBook("Singur pe lume", "Hector Malot", "fictiune", 1878);
	testService.addBook("Toate panzele sus!", "Radu Tudoran", "actiune", 1954);
	testService.addBook("Un apartament la Paris", "Michelle Gable", "istorie", 2015);

	testService.modifyBook("Un apartament la Paris", "Michelle Gable", "favorit", 1950);

	assert(testService.findBook("Un apartament la Paris", "Michelle Gable").getAn() == 1950);
	assert(testService.findBook("Un apartament la Paris", "Michelle Gable").getGen() == "favorit");
}

void testFilterService()
{
	BookRepository testRepo;
	BookValidator testValid;
	BookService testService{ testRepo, testValid };

	testService.addBook("Singur pe lume", "Hector Malot", "fictiune", 1878);
	testService.addBook("Toate panzele sus!", "Radu Tudoran", "actiune", 1954);
	testService.addBook("Singur pe lume", "Michelle Gable", "istorie", 2015);

	const  MyVector<Book>& filtered_books = testService.filter_by_title("Singur pe lume");
	assert(filtered_books.size() == 2);

	const  MyVector<Book>& filtered_books_2 = testService.filter_by_year(2000);
	assert(filtered_books_2.size() == 0);
	const  MyVector<Book>& filtered_books_3 = testService.filter_by_year(1878);
	assert(filtered_books_3.size() == 1);
}

void testSortService()
{
	BookRepository testRepo;
	BookValidator testValid;
	BookService testService{ testRepo, testValid };

	testService.addBook("Singur pe lume", "Hector Malot", "fictiune", 2023);
	testService.addBook("Toate panzele sus!", "Radu Tudoran", "actiune", 1954);
	testService.addBook("In ape adanci", "Paula Hawkins", "thriller", 2017);


	const  MyVector<Book>& sorted_books_title = testService.sort_books("titlu");
	assert(sorted_books_title[1].getTitlu() == "Singur pe lume");

	const  MyVector<Book>& sorted_books_author = testService.sort_books("autor");
	assert(sorted_books_author[2].getGen() == "actiune");

	const  MyVector<Book>& sorted_books_year = testService.sort_books("an");
	assert(sorted_books_year[1].getTitlu() == "In ape adanci");

	Book book4{ "Trandafirii pierduti", "Paula Hawkins", "istorie", 2017 };
	testRepo.store(book4);

	const  MyVector<Book>& sorted_books_year_gen = testService.sort_books("an");
	assert(sorted_books_year_gen[1].getTitlu() == "Trandafirii pierduti");
	assert(sorted_books_year_gen[2].getTitlu() == "In ape adanci");

	Book book5{ "Trandafirii pierduti", "Autor", "arta", 2015 };
	testRepo.store(book5);

    const  MyVector<Book>& sorted_books_title_gen = testService.sort_books("titlu");
	assert(sorted_books_title_gen[3].getAutor() == "Autor");
	assert(sorted_books_title_gen[4].getAutor() == "Paula Hawkins");

	const  MyVector<Book>& sorted_books_author_gen = testService.sort_books("autor");
	assert(sorted_books_author_gen[2].getGen() == "istorie");
	assert(sorted_books_author_gen[3].getGen() == "thriller");
}

void testeService()
{
	testAddService();
	testDeleteService();
	testFindService();
	testModifyService();
	testFilterService();
	testSortService();
}

