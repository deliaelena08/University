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
		loadData(list, service.sortByFirma());
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
	QPushButton* btnShow = new QPushButton{ "&Show cars" };
	QPushButton* btnAdd = new QPushButton{ "&Add car" };

	void initGui() {

		QHBoxLayout* laymain = new QHBoxLayout{};
		QVBoxLayout* leftlay = new QVBoxLayout{};
		setLayout(laymain);
		QFormLayout* texts = new QFormLayout;
		texts->addRow("Firma", txtFirma);
		texts->addRow("Model", txtModel);
		texts->addRow("An", txtAn);
		texts->addRow("Culoare", txtCuloare);
		leftlay->addLayout(texts);

		QHBoxLayout* lybtn = new QHBoxLayout{};
		lybtn->addWidget(btnAdd);
		lybtn->addWidget(btnShow);
		lybtn->addWidget(btnExit);
		leftlay->addLayout(lybtn);

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
		QObject::connect(btnAdd, &QPushButton::clicked, [&]() {
			string firma=txtFirma->text().toStdString();
			string model = txtModel->text().toStdString();
			int an = txtAn->text().split(" ")[0].toInt();
			string culoare = txtCuloare->text().toStdString();
			service.storeCar(firma, model, an, culoare);
			loadData(list, service.sortByFirma());
			});
		QObject::connect(btnShow, &QPushButton::clicked, [&]() {
			string firma = txtFiltre->text().toStdString();
			vector<Masina> masina = service.filtreByFirma(firma);
			listFiltre->clear();
			QListWidgetItem* firstline1 = new QListWidgetItem;
			firstline1->setText("Masini: " + QString::number(masina.size()));
			listFiltre->addItem(firstline1);
			for (auto& ma : masina) {
				QListWidgetItem* masinita = new QListWidgetItem;
				masinita->setText(QString::fromStdString(ma.getModel()) + " " + QString::number(ma.getAn()));
				listFiltre->addItem(masinita);
			}
			});

	}

	void loadData(QListWidget* list, vector<Masina> masini) {
		list->clear();
		QListWidgetItem* firstline = new QListWidgetItem;
		firstline->setText("Firma  Model");
		list->addItem(firstline);
		for (auto& mas : masini) {
			QListWidgetItem* masina = new QListWidgetItem;
			QString color = QString::fromStdString(mas.getCuloARE());
			masina->setText(QString::fromStdString(mas.getFirma()) + ' ' + QString::fromStdString(mas.getModel()));
			masina->setBackground(QColor(color));
			list->addItem(masina);
		}
	}
};