#pragma once
#include <string>
#include "Repository.h"
#include "Validators.h"

class BookService {

private:
	BookRepository& repo;
	BookValidator& val;

public:
	BookService(BookRepository& repo, BookValidator& val) :repo{ repo }, val{ val } {};


	//constructor de copiere
	//punem delete fiindca nu vrem sa se faca nicio copie la Service
	//(in aplicatie avem 1 singur Service)
	BookService(const BookService& ot) = delete;

	/*
	Adauga o carte.
	*@param titlu: titlul cartii care se adauga (string)
	*@param autor: autorul cartii care se adauga (string)
	*@param gen: genul cartii care se adauga (string)
	*@durata an: anul cartii care se adauga (int)
	*
	* @throws:
	*	RepoException daca mai exista carte cu titlu si autorul dat
	*	ValidationException daca cartea nu este valida
	*/

	void addBook(string titlu, string autor, string gen, int an);

	/*
	Sterge o carte.  
	*@param titlu: titlul cartii ce se doreste a fi stearsa (string)
	*@param autor: autorul cartii ce se doreste a fi stearsa (string)
	*@return: -
	*@throws:  RepoException daca nu  exista carte cu titlu si autorul dat
	*/
	void deleteBook(string titlu, string autor);


	/**
	 * @brief modifica genul si anul unei carti cu titlul si autorul dat (apeleaza  update_book din repo)
	 * @param titlu: titlul cartii care trebuie modificata (string)
	 * @param autor: autorul cartii care trebuie modificata (string)
	 * @param gen_nou: noul gen (string)
	 * @param an_nou: noul an (int)
	 * @return: -
	 * @throws:  RepoException daca nu  exista carte cu titlu si autorul dat
	 */
	void modifyBook(string titlu, string autor, string gen_nou, int an_nou);


	/**
	 * @brief: returneaza cartea ce are titlul si autorul dat sau exceptie daca nu exista
	 * @param titlu: titlul cartii cautate (string)
	 * @param autor: autorul cartii cautate (string)
	 * @return: cartea cautata (Book&)
	 * @throws: RepoException daca nu  exista carte cu titlu si autorul dat
	 */
	const Book& findBook(string titlu, string autor);

	/*
	 * @brief: Returneaza un vector cu toate cartile
	 *  @return:  vector cu toate cartile
	*/
	const  MyVector<Book>& getAllBooks() {
		return this->repo.getAllBooks();
	}


	/**
	 * @brief returneaza un vector in care apar cartile cu titlul dat
	 * @param titlu: titlul dupa care se face filtrarea (string)
	 * @return: MyVector<Book>
	 */
	const  MyVector<Book>  filter_by_title(const string titlu);


	/**
	 * @brief returneaza un vector in care apar cartile cu anul dat
	 * @param an: anul dupa care se face filtrarea (int)
	 * @return: MyVector<Book>
	 */
	const  MyVector<Book> filter_by_year(const int an);


	/**
	 * @brief: returneaza un nou vector in care apar cartile sortate dupa criteriul dat
	 *         !!!!se ordoneaza dupa titlu, autor sau an, iar daca toua carti sunt egale dpdv al acestui criteriu, 
	           ele se ordoneaza dupa gen
	 * @param criteriu: criteriul dupa care se face sortarea (string)
	 * @return: MyVector<Book>
	 */
	const  MyVector<Book> sort_books(const string& criteriu);
};

void testeService();

