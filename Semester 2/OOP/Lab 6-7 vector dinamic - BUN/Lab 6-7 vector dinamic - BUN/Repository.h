#pragma once
#include "Book.h"
#include "VectorDinamic.h"

class RepoException {
private:
	string errorMsg;

public:
	RepoException(string errorMsg) :errorMsg{ errorMsg } {};
	string getErrorMessage() {
		return this->errorMsg;
	}
};

class BookRepository {
private:
	MyVector<Book> allBooks;

public:

	BookRepository() = default;


	//constructor de copiere
	//punem delete fiindca nu vrem sa se faca nicio copie la Repository
	//(in aplicatie avem 1 singur Repo)
	BookRepository(const BookRepository& ot) = delete;

	/*
	Adauga o carte in lista
	@param b: carte care se adauga (Book)
	@return: -
	@throws: RepoException daca o carte cu acelasi titlu si acelasi autor exista deja
	*/
	void store(const Book& b);

	/**
	 * Sterge o carte din lista
	 * @param titlu: titlu cartii ce dorim sa fie stearsa (string)
	 * @param autor: autorul cartii ce dorim sa fie stearsa (string)
	 * @throws: RepoException daca nu exista o carte de sters
	 */
	void delete_book(string titlu, string autor);


	/*
	Verififca daca o carte data exista n lista
	@param b: cartea care se cauta in lista
	@return: true daca cartea exista, false altfel
	*/
	bool exist_book(const Book& b);


	/*
	Cauta o carte cu titlul si autorul dat

	@param titlu: titlul dupa care se cauta
	@param autor: autorul dupa care se cauta
	@returns: cartea cu titlul si autorul dat (daca exista)
	@throws: RepoException daca nu exista carte cu titlul si autorul dat*/
	const Book& find_book(string titlu, string autor);


	/**
	 * @brief: cauta o anumita carte dupa titlu si autor
	 * @param titlu: titlul cartii cautate (string)
	 * @param autor: autorul cartii cautate (string)
	 * @return: indexul pe care se afla cartea in vector sau -1 daca nu exista
	 */
	int find_book_index(string titlu, string autor);


	/**
	 * @brief modifica genul si anul unei carti cu titlul si autorul dat (apeleaza  update_book din repo)
	 * @param titlu: titlul cartii care trebuie modificata (string)
	 * @param autor: autorul cartii care trebuie modificata (string)
	 * @param gen_nou: noul gen (string)
	 * @param an_nou: noul an (int)
	 * @return: -
	 * @throws:  RepoException daca nu  exista carte cu titlu si autorul dat
	 */
	void update_book(string titlu, string autor, string gen_nou, int an_nou);


	/*
	Returneaza o lista cu toate cartile
	@return: lista cu cartile (MyVector<Book>&)
	*/
	const  MyVector<Book>& getAllBooks();

};

void testeRepo();

