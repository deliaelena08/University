#pragma once
#include "UI.h"

#include <string>
#include <iostream>

using namespace std;

void ConsoleUI::printMenu()
{
	cout << "Meniul:\n";
	cout << "1. Adauga carte\n";
	cout << "2. Strege carte\n";
	cout << "3. Modifica carte\n";
	cout << "4. Cauta carte\n";
	cout << "5. Filtrare carti\n";
	cout << "6. Sortare carti\n";
	cout << "7. Afiseaza toate cartile\n";
	cout << "8. Exit\n";
}

void ConsoleUI::uiAdd() {
	string titlu, autor, gen;
	int an;

	cout << "Titul cartii este: ";
	getline(cin >> ws, titlu);

	cout << "Autorul cartii este: ";
	getline(cin >> ws, autor);

	cout << "Genull cartii este: ";
	getline(cin >> ws, gen);

	cout << "Anul aparitiei: ";
	cin >> an;
	try {
		service.addBook(titlu, autor, gen, an);
	}
	catch (RepoException& re) {
		cout << re.getErrorMessage();
	}
	catch (ValidationException& ve) {
		cout << "Cartea nu este valida!\n";
		cout << ve.getErrorMessages();
	}
}

void ConsoleUI::uiDelete()
{
	string titlu, autor;

	cout << "Titul cartii pe care doriti sa o stregeti este: ";
	getline(cin >> ws, titlu);

	cout << "Autorul cartii este: ";
	getline(cin >> ws, autor);

	try {
		service.deleteBook(titlu, autor);
	}
	catch (RepoException& re) {
		cout << re.getErrorMessage();
	}

}

void ConsoleUI::uiModify()
{
	string titlu, autor, gen_nou;
	int an_nou;


	cout << "Titul cartii pe care doriti sa o modificati este: ";
	getline(cin >> ws, titlu);

	cout << "Autorul cartii este: ";
	getline(cin >> ws, autor);

	cout << "Noul gen al cartii este: ";
	getline(cin >> ws, gen_nou);

	cout << "Noul an al aparitiei: ";
	cin >> an_nou;

	try {
		service.modifyBook(titlu, autor, gen_nou, an_nou);
	}
	catch (RepoException& re) {
		cout << re.getErrorMessage();
	}

}

void ConsoleUI::uiFind()
{
	string titlu, autor;

	cout << "Titul cartii pe care doriti sa o cautati este: ";
	getline(cin >> ws, titlu);

	cout << "Autorul cartii este: ";
	getline(cin >> ws, autor);

	try {
		const Book& b = service.findBook(titlu, autor);
		cout << b;
	}
	catch (RepoException& re) {
		cout << re.getErrorMessage();
	}

}

void ConsoleUI::uiFilter()
{
	string optiune_filtrare;

	cout << "Introduceti crieteriul dupa care se face filtrarea (titlu/ an): ";
	getline(cin >> ws, optiune_filtrare);

	if (optiune_filtrare == "titlu")
	{
		string titlu;
		cout << "Introduceti titlul dupa care se face filtrarea: ";
		getline(cin >> ws, titlu);
		const  MyVector<Book>& filtered_tile_books = service.filter_by_title(titlu);
		if (filtered_tile_books.size() == 0)
			cout << "Nu exista carti cu acest titlu.\n";
		else
			for (const auto& book : filtered_tile_books)
				cout << book;
	}
	else if (optiune_filtrare == "an")
	{
		int an;
		cout << "Introduceti anul dupa care se face filtrarea: ";
		cin >> an;
		const  MyVector<Book>& filtered_year_books = service.filter_by_year(an);
		if (filtered_year_books.size() == 0)
			cout << "Nu exista carti din acest an.\n";
		else
			for (const auto& book : filtered_year_books)
				cout << book;
	}
	else cout << "Ati introdus criteriul gresit";
}

void ConsoleUI::uiSort()
{
	string optiune_sortare;

	cout << "Introduceti crieteriul dupa care se face filtrarea (titlu/ autor / an): ";
	getline(cin >> ws, optiune_sortare);

	if ((optiune_sortare != "titlu") && (optiune_sortare != "autor") && (optiune_sortare != "an"))
		cout << "Ati introuds un criteriu gresit\n";
	else
	{
		const  MyVector<Book>& sort_books = service.sort_books(optiune_sortare);
		for (const auto& book : sort_books)
			cout << book;
	}


}

void ConsoleUI::printAllBooks()
{
	const  MyVector<Book>& allBooks = service.getAllBooks();
	if (allBooks.size() == 0)
		cout << "Nu exista carti.\n";
	else
		for (const auto& book : allBooks)
			cout << book;
}

void ConsoleUI::run()
{
	int ok = 1;
	int optiune;
	this->addDefaultBooks();
	while (ok)
	{
		printMenu();
		cout << "Introduceti o comanda: ";
		//altfel ar intra in loop infinit cand introducem o optiune gresita 
		if (!(cin >> optiune)) {
			cin.clear();
			cin.ignore(numeric_limits<streamsize>::max(), '\n');
			cout << "Ai introdus o optiune invalida\n";
			continue;
		}
		switch (optiune)
		{
		case 1:
			uiAdd();
			break;
		case 2:
			uiDelete();
			break;
		case 3:
			uiModify();
			break;
		case 4:
			uiFind();
			break;
		case 5:
			uiFilter();
			break;
		case 6:
			uiSort();
			break;
		case 7:
			printAllBooks();
			break;
		case 8:
			ok = 0;
			break;
		default:
			printf("Ai introdus o optiune invalida \n");
			break;
		}

	}
}

void ConsoleUI::addDefaultBooks()
{
	this->service.addBook("Singur pe lume", "Hector Malot", "fictiune", 1878);
	this->service.addBook("Toate panzele sus!", "Radu Tudoran", "actiune", 1954);
	this->service.addBook("Un apartament la Paris", "Michelle Gable", "istorie", 2015);
	this->service.addBook("In ape adanci", "Paula Hawkins", "thriller", 2017);
	this->service.addBook("Mostenirea Kremlinului", "Ion Mihai Pacepa", "istorie", 1993);
}