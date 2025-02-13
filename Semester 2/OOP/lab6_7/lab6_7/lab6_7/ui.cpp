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
}

void UI::printmenufiltre() {
	cout << "Optiuni:\n";
	cout << "1.Filtrare dupa nume(egal cu unul dat)\n";
	cout << "2.Filtrare dupa pret(mai mic decat unul dat)\n";
	cout << "3.Filtrare dupa producatore(egal cu unul dat)\n";
}

void UI::printmenusort() {
	cout << "Optiuni:\n";
	cout << "1.Sortare crescator dupa nume\n";
	cout << "2.Sortare crescator dupa pret\n";
	cout << "3.Sortare crescator dupa nume si pret\n";
}

void UI::printmenucos() {
	cout << "Optiuni:\n";
	cout << "1.Goleste cos";
	cout << "2.Adauga in cos";
	cout << "3.Genereaza cos";
	cout << "4.Export";
}

void UI::uiprintall(DynamicVector<Product>& list) {
	for (int i = 0; i < list.size(); i++) {
		cout << "Nume: " << list[i].getname() << " | Tipul: " << list[i].gettype() << " | Pretul: " << list[i].getprice() << " | Producatorul: " << list[i].getproductor()<<'\n';
	}
}

void UI::uiprintlist() {
	DynamicVector<Product> list = service.getAll();
	this->uiprintall(list);
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
		cout << "Datele produsului nu unt valide"<<'\n';
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
		cout << "Modificare realizata cu succes!-_-"<<endl;
	}
	catch (RepoException& msg) {
		cout << msg.getErrorMessage()<<endl;
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
	DynamicVector<Product> filtred_product=service.filtre_by_name(name);
	this->uiprintall(filtred_product);
}

void UI::uifiltrebyproductor() {
	string productor;
	cout << "Introduceti un producator existent: ";
	getline(cin >> ws, productor);
	DynamicVector<Product> filtred_product1 = service.filtre_by_productor(productor);
	this->uiprintall(filtred_product1);
}

void UI::uifiltrebyprice() {
	int price;
	cout << "Introduceti un pret de produs existent: ";
	cin >> price;
	DynamicVector<Product> filtred_product2 = service.filtre_by_price(price);
	this->uiprintall(filtred_product2);
}

void UI::uisortbyname() {
	DynamicVector<Product> list1 = service.sort_by_name();
	this->uiprintall(list1);
}

void UI::uisortbyprice() {
	DynamicVector<Product> list2 = service.sort_by_price();
	this->uiprintall(list2);
}

void UI::uisortbynameandprice() {
	DynamicVector<Product> list3 = service.sort_by_name_and_price();
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

void UI::uifiltrerun() {
	this->printmenufiltre();
	int runner=0;
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

void UI::run() {
	int cmd;
	uiaddDefault();
	while (true) {
		printmenu();
		cout << "Introduceti optiunea: ";
		cin >> cmd;
		switch (cmd){
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
		default:
			break;
		}
	}
}
