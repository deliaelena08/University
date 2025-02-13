
#pragma once
#include"Service.h"
#include<qwidget.h>
#include<QtWidgets/qboxlayout.h>
#include<QtWidgets/qpushbutton.h>
#include<QtWidgets/qlineedit.h>
#include<QtWidgets/qlistwidget.h>
#include<QtWidgets/qmessagebox.h>
#include<QtWidgets/qformlayout.h>

class Intergata_GUI :public QWidget {
public:
	Intergata_GUI(Service& service) :service{ service } {
		initGui();
		loadData(list, service.getAllElevi());
		initConnect();
	}
private:
	Service& service;
	QListWidget* list = new QListWidget;
	QListWidget* listFiltre = new QListWidget;
	QLineEdit* line = new QLineEdit;
	QLineEdit* txtFirma = new QLineEdit;
	QLineEdit* txtModel = new QLineEdit;
	QLineEdit* txtAn = new QLineEdit;
	QLineEdit* txtCuloare = new QLineEdit;
	QLineEdit* txtFiltre = new QLineEdit;
	QPushButton* btnExit = new QPushButton{ "&Exit" };
	QPushButton* btnShow = new QPushButton{ "&Sortare nume" };
	QPushButton* btnDesen = new QPushButton{ "&Desen+Pictura" };
	QPushButton* btnFotografie = new QPushButton{ "&Fotografie" };
	QPushButton* btnMuzica = new QPushButton{ "&Muzica" };
	QPushButton* btnInfo = new QPushButton{ "&Informatica" };
	QPushButton* btnJurnalism= new QPushButton{ "&Jurnalism" };

	void initGui() {
		QHBoxLayout* laymain = new QHBoxLayout{};
		QVBoxLayout* leftlay = new QVBoxLayout{};
		setLayout(laymain);

		QHBoxLayout* lybtn = new QHBoxLayout{};
		lybtn->addWidget(btnShow);
		lybtn->addWidget(btnExit);
		leftlay->addLayout(lybtn);
		QHBoxLayout* lybtn1 = new QHBoxLayout{};
		lybtn1->addWidget(btnDesen);
		lybtn1->addWidget(btnFotografie);
		lybtn1->addWidget(btnInfo);
		lybtn1->addWidget(btnJurnalism);
		lybtn1->addWidget(btnMuzica);
		leftlay->addLayout(lybtn1);

		leftlay->addWidget(txtFiltre);
		leftlay->addWidget(listFiltre);
		laymain->addLayout(leftlay);
		laymain->addWidget(list);
	}

	void initConnect() {
		QObject::connect(btnExit, &QPushButton::clicked, [&]() {
			QMessageBox::StandardButton reply = QMessageBox::question(this, "Exit", "Are you sure?", QMessageBox::Yes | QMessageBox::No);
			if (reply == QMessageBox::Yes)
				close();
			});
		
		QObject::connect(btnShow, &QPushButton::clicked, [&]() {
			vector<Elev> elevi = service.sortNyName(service.getAllElevi());
			loadData(list, elevi);
			});
		QObject::connect(btnDesen, &QPushButton::clicked, [&]() {
			Atelier at( "Desen" );
			vector<Elev> elevi = at.getELevi();
			loadData(listFiltre, elevi);
			});
		QObject::connect(btnFotografie, &QPushButton::clicked, [&]() {
			Atelier at("Fotografie");
			vector<Elev> elevi = at.getELevi();
			loadData(listFiltre, elevi);
			});
		QObject::connect(btnJurnalism, &QPushButton::clicked, [&]() {
			Atelier at( "Jurnalism" );
			vector<Elev> elevi = at.getELevi();
			loadData(listFiltre, elevi);
			});
		QObject::connect(btnMuzica, &QPushButton::clicked, [&]() {
			Atelier at("Muzica" );
			vector<Elev> elevi = at.getELevi();
			loadData(listFiltre, elevi);
			});
		QObject::connect(btnInfo, &QPushButton::clicked, [&]() {
			Atelier at( "Informatica" );
			vector<Elev> elevi = at.getELevi();
			loadData(listFiltre, elevi);
			});
	}

	void loadData(QListWidget* list, vector<Elev> elevi) {
		list->clear();
		QListWidgetItem* firstline = new QListWidgetItem;
		firstline->setText("Numele  Scoala");
		list->addItem(firstline);
		for (auto& el : elevi) {
			QListWidgetItem* elev = new QListWidgetItem;
			elev->setText(QString::fromStdString(el.getnume()) + ' ' + QString::fromStdString(el.getScoala()));
			list->addItem(elev);
		}
	}
};

