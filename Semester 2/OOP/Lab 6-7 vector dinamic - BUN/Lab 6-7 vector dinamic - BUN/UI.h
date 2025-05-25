#pragma once 
#include "Service.h"

class ConsoleUI {

private:
	BookService& service;

public:
	ConsoleUI(BookService& service) :service{ service } {};
	ConsoleUI(const ConsoleUI& ot) = delete;
	void printMenu();
	void uiAdd();
	void uiDelete();
	void uiModify();
	void uiFind();
	void uiFilter();
	void uiSort();
	void printAllBooks();
	void run();
	void addDefaultBooks();
};

