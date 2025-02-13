#pragma once
#include"Service.h"
#include<QtWidgets/qwidget.h>
#include<QtWidgets/qlineedit.h>
#include<QtWidgets/qpushbutton.h>
#include<QtWidgets/qboxlayout.h>
#include<QtWidgets/qformlayout.h>
#include<QtWidgets/qlistwidget.h>
#include<QtWidgets/qmessagebox.h>

class Interfata_Gui :public QWidget {
public:
	Interfata_Gui(Service& service) :service{ service } {
		init_gui();
		loadData(list,service.getALlAp());
		init_connect();
	}
private:
	Service& service;
	QListWidget* list = new QListWidget;
	QPushButton* btnDelete = new QPushButton{ "&Delete" };
	QPushButton* btnExit = new QPushButton{ "&Exit" };
	QPushButton* btnFiltre1 = new QPushButton{ "&Filtrare dupa pret" };
	QPushButton* btnFiltre2 = new QPushButton{ "&Filtrare dupa suprafata" };
	QLineEdit* txtFiltre1Pret1 = new QLineEdit;
	QLineEdit* txtFiltre1Pret2 = new QLineEdit;
	QLineEdit* txtFiltre2Suprafata1 = new QLineEdit;
	QLineEdit* txtFiltre2Suprafata2 = new QLineEdit;

	void init_gui() {
		QHBoxLayout* lay1 = new QHBoxLayout{};
		auto rightlay = new QVBoxLayout{};
		setLayout(lay1);
		auto lybtn1 = new QHBoxLayout{};
		lybtn1->addWidget(btnDelete);
		lybtn1->addWidget(btnExit);
		rightlay->addLayout(lybtn1);

		auto lytxtfield1 = new QFormLayout;
		lytxtfield1->addRow("Pret1:", txtFiltre1Pret1);
		lytxtfield1->addRow("Pret2:", txtFiltre1Pret2);
		auto filtre1 = new QVBoxLayout{};
		filtre1->addWidget(btnFiltre1);
		filtre1->addLayout(lytxtfield1);
		rightlay->addLayout(filtre1);

		auto lytxtfield2 = new QFormLayout;
		lytxtfield2->addRow("Suprafata1:", txtFiltre2Suprafata1);
		lytxtfield2->addRow("Suprafat2:", txtFiltre2Suprafata2);
		auto filtre2 = new QVBoxLayout{};
		filtre2->addWidget(btnFiltre2);
		filtre2->addLayout(lytxtfield2);
		rightlay->addLayout(filtre2);

		lay1->addLayout(rightlay);
		lay1->addWidget(list);
	}

	void init_connect(){
		QObject::connect(btnExit, &QPushButton::clicked, [&]() {
			QMessageBox::StandardButton reply = QMessageBox::question(this, "Exit", "Are you sure?", QMessageBox::Yes | QMessageBox::No);
			if (reply == QMessageBox::Yes)
				close();
			});
		QObject::connect(btnDelete, &QPushButton::clicked, [&]() {
			auto item = list->currentItem();
			QStringList parts = item->text().split(" ");
			string strada = parts[0].toStdString();
			string suprafata = parts[1].toStdString();
			int pret = parts[2].toInt();
			try {
				service.delteAp(suprafata, strada, pret);
				loadData(list, service.getALlAp());
			}
			catch (RepoException& msg) {
				QMessageBox::warning(this, "Info", QString::fromStdString(msg.getErrorMsg()));
			}
			});
		QObject::connect(btnFiltre1, &QPushButton::clicked, [&]() {
			auto pret1 = txtFiltre1Pret1->text();
			auto pret2 = txtFiltre1Pret2->text();
			vector<Apartament> apart = service.filtreByPrice(service.getALlAp(),pret1.toInt(), pret2.toInt());
			loadData(list, apart);
			});
		QObject::connect(btnFiltre2, &QPushButton::clicked, [&]() {
			auto pret1 = txtFiltre1Pret1->text();
			auto pret2 = txtFiltre1Pret2->text();
			auto suprafata1 = txtFiltre2Suprafata1->text();
			auto suprafata2 = txtFiltre2Suprafata2->text();
			vector<Apartament> apart; 
			vector<Apartament> apart1;
			if(!pret1.isEmpty() && !pret2.isEmpty()){
				 apart= service.filtreByPrice(service.getALlAp(),pret1.toInt(), pret2.toInt());
				 apart1= service.filtreBySpace(apart,suprafata1.toStdString(), suprafata2.toStdString());
			}
			else {
				 apart1 = service.filtreBySpace(service.getALlAp(), suprafata1.toStdString(), suprafata2.toStdString());
			}
			loadData(list, apart1);
			});
	}

	void loadData(QListWidget* list,vector<Apartament> apartamante) {
		list->clear();
		QListWidgetItem* firstline = new QListWidgetItem;
		firstline->setText("Strada    Suprafata    Pret ");
		list->addItem(firstline);
		for (auto& ap : apartamante) {
			QListWidgetItem* apart = new QListWidgetItem;
			apart->setText(QString::fromStdString(ap.getStrada()) + ' ' + QString::fromStdString(ap.getSuprafata()) + ' ' + QString::number(ap.getPret()));
			list->addItem(apart);
		}
	}
};