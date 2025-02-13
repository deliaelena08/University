#include "ui.h"
#include <iostream>
using namespace std;

void UI::printmenu() {
	cout << "\nMenu:\n";
	cout << "0.Inchidere aplicatie\n";
	cout << "1.Adaugare produs\n";
	cout << "2.Modificare produs\n";
	cout << "3.Stergere produs\n";
	cout << "4.Filtrare produse\n";
	cout << "5.Sortare produse\n";
	cout << "6.Afisare produse\n";
	cout << "7.Gestionare cos cumparaturi\n";
	cout << "8.Undo\n";
}

void UI::printmenufiltre() {
	cout << "\nOptiuni:\n";
	cout << "1.Filtrare dupa nume(egal cu unul dat)\n";
	cout << "2.Filtrare dupa pret(mai mic decat unul dat)\n";
	cout << "3.Filtrare dupa producatore(egal cu unul dat)\n";
}

void UI::printmenusort() {
	cout << "\nOptiuni:\n";
	cout << "1.Sortare crescator dupa nume\n";
	cout << "2.Sortare crescator dupa pret\n";
	cout << "3.Sortare crescator dupa nume si pret\n";
}

void UI::printmenucos() {
	cout << "\nOptiuni:\n";
	cout << "1.Goleste cos\n";
	cout << "2.Adauga in cos\n";
	cout << "3.Genereaza cos\n";
	cout << "4.Export\n";
}

void UI::uiprintall(vector<Product>& list) {
	for (auto& p : list) {
		cout << "Nume: " << p.getname() << " | Tipul: " << p.gettype() << " | Pretul: " << p.getprice() << " | Producatorul: " << p.getproductor() << '\n';
	}
}


void UI::uiprintlist() {
	vector<Product> list = service.getAll();
	this->uiprintall(list);
}

void UI::uiundo() {
	try {
		service.undo();
		cout << "Undo realizat cu succes!";
	}
	catch (RepoException& msg) {
		cout << msg.getErrorMessage() << endl;
	}
}

void UI::uiadd() {
	string name, type, productor;
	int price;
	cout << "Introduceti un nume de produs: ";
	getline(cin >> ws, name);

	cout << "Introduceti un tip pentru produs: ";
	getline(cin >> ws, type);

	cout << "Introduceti un pret(intreg) pentru produs: ";
	cin >> price;

	cout << "Introduceti producatorul pentru produs: ";
	getline(cin >> ws, productor);
	try {
		service.addProduct(name, type, price, productor);
		cout << "Adaugare cu succes!<3\n";
	}
	catch (ExceptionValidator& msg) {
		cout << "Datele produsului nu unt valide" << '\n';
		cout << msg.getErrorMessages();
	}
}

void UI::uimodify() {
	string name, productor, new_name, new_type;
	int price1;
	cout << "Introduceti un nume de produs existent: ";
	getline(cin >> ws, name);
	cout << "Introduceti un nume de producator existent: ";
	getline(cin >> ws, productor);
	cout << "Introduceti un nume nou de produs: ";
	getline(cin >> ws, new_name);
	cout << "Introduceti un tip nou : ";
	getline(cin >> ws, new_type);
	cout << "Introduceti un pret nou : ";
	cin >> price1;
	try {
		service.modifyProduct(name, productor, new_name, new_type, price1);
		cout << "Modificare realizata cu succes!-_-" << endl;
	}
	catch (RepoException& msg) {
		cout << msg.getErrorMessage() << endl;
	}
}

void UI::uidelete() {
	string name, productor;
	cout << "Introduceti un nume de produs existent: ";
	getline(cin >> ws, name);
	cout << "Introduceti un nume de producator existent: ";
	getline(cin >> ws, productor);
	try {
		service.deleteProduct(name, productor);
		cout << "Stergere realizata cu succes!:P";
	}
	catch (RepoException& msg) {
		cout << msg.getErrorMessage();
	}
}

void UI::uifiltrebyname() {
	string name;
	cout << "Introduceti un nume de produs existent: ";
	getline(cin >> ws, name);
	vector<Product> filtred_product = service.filtre_by_name(name);
	this->uiprintall(filtred_product);
}

void UI::uifiltrebyproductor() {
	string productor;
	cout << "Introduceti un producator existent: ";
	getline(cin >> ws, productor);
	vector<Product> filtred_product1 = service.filtre_by_productor(productor);
	this->uiprintall(filtred_product1);
}

void UI::uifiltrebyprice() {
	int price;
	cout << "Introduceti un pret de produs existent: ";
	cin >> price;
	vector<Product> filtred_product2 = service.filtre_by_price(price);
	this->uiprintall(filtred_product2);
}

void UI::uisortbyname() {
	vector<Product> list1 = service.sort_by_name();
	this->uiprintall(list1);
}

void UI::uisortbyprice() {
	vector<Product> list2 = service.sort_by_price();
	this->uiprintall(list2);
}

void UI::uisortbynameandprice() {
	vector<Product> list3 = service.sort_by_name_and_price();
	this->uiprintall(list3);
}

void UI::uiaddDefault() {
	this->service.addProduct("margele de branza", "lactate", 7, "Covalact");
	this->service.addProduct("Chipsuri cu pui", "snacksuri", 5, "Viva");
	this->service.addProduct("Sunca de porc", "mezeluri", 7, "Praga");
	this->service.addProduct("Cascaval feliat", "lactate", 8, "Hochland");
	this->service.addProduct("Inghetata vegana", "desert", 12, "Kaufland");
	this->service.addProduct("Servetele umede", "igiena", 6, "Igienol");
}

void UI::uiemptybasket() {
	this->service.emptyBasket();
	cout << "Suma actualizata este: " << this->service.getSumBasket() << '\n';
}

void UI::uiaddbasket() {
	string name, productor;
	cout << "Introduceti numele unui produs existent: ";
	getline(cin >> ws, name);
	cout << "Introduceti producatorul produsului:  ";
	getline(cin >> ws, productor);
	this->service.addToBasket(name, productor);
	cout << "Suma actualizata este: " << this->service.getSumBasket() << '\n';
}

void UI::uiexportbasket() {
	string name;
	cout << "Introduceti numele fisierului in care vreti sa introducem date: ";
	getline(cin >> ws, name);
	this->service.exportBasket(name);
}

void UI::uigeneratebasket() {
	cout << "Introduceti numarul de produse dorite de generat: ";
	int nr;
	cin >> nr;
	this->service.generate1(nr);
	cout << "Suma actualizata este: " << this->service.getSumBasket() << '\n';
}

void UI::uifiltrerun() {
	this->printmenufiltre();
	int runner = 0;
	cout << "Introduceti optiunea: ";
	cin >> runner;
	switch (runner)
	{
	case 1:
		uifiltrebyname();
		break;
	case 2:
		uifiltrebyprice();
		break;
	case 3:
		uifiltrebyproductor();
		break;
	default:
		break;
	}
}

void UI::uisortrun() {
	this->printmenusort();
	int runner = 0;
	cout << "Introduceti optiunea: ";
	cin >> runner;
	switch (runner)
	{
	case 1:
		uisortbyname();
		break;
	case 2:
		uisortbyprice();
		break;
	case 3:
		uisortbynameandprice();
		break;
	default:
		break;
	}
}

void UI::uibasketrun() {
	this->printmenucos();
	int runner = 0;
	cout << "Introduceti optiunea: ";
	cin >> runner;
	switch (runner)
	{
	case 1:
		uiemptybasket();
		break;
	case 2:
		uiaddbasket();
		break;
	case 3:
		uigeneratebasket();
		break;
	case 4:
		uiexportbasket();
		break;
	default:
		break;
	}
}
void UI::run() {
	int cmd;
	//uiaddDefault();
	while (true) {
		printmenu();
		cout << "Introduceti optiunea: ";
		cin >> cmd;
		switch (cmd) {
		case 0:
			exit(0);
		case 1:
			uiadd();
			break;
		case 2:
			uimodify();
			break;
		case 3:
			uidelete();
			break;
		case 4:
			uifiltrerun();
			break;
		case 5:
			uisortrun();
			break;
		case 6:
			uiprintlist();
			break;
		case 7:
			uibasketrun();
			break;
		case 8:
			uiundo();
			break;
		default:
			break;
		}
	}
}
