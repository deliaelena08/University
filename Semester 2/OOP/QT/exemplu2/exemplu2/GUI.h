#pragma once
#include"Service.h"
#include <QtWidgets/qwidget.h>
#include <QtWidgets/qlabel.h>
#include <QtWidgets/qboxlayout.h>
#include <QtWidgets/qpushbutton.h>
#include <QtWidgets/qtablewidget.h>
#include <QtWidgets/qmessagebox.h>
#include <QtWidgets/qheaderview.h>
#include <QtWidgets/qlineedit.h>
#include <QtWidgets/qcombobox.h>
#include <QtWidgets/qformlayout.h>

class Interfata_GUI : public QWidget {
public:
	Interfata_GUI(Service& service) :service{ service } {
		initGui();
		loadData(tabel, service.sorted());
		initConnect();
	}

private:
	Service& service;
	QTableWidget* tabel;
	QPushButton* btnAdd = new QPushButton{ "Add" };
	QPushButton* btnDelete = new QPushButton{ "Delete" };
	QPushButton* btnUpdate = new QPushButton{ "Update" };
	QPushButton* btnExit = new QPushButton{ "Exit" };
	QLineEdit* txtId = new  QLineEdit;
	QLineEdit* txtTitle = new  QLineEdit;
	QLineEdit* txtArtist = new  QLineEdit;
	QLineEdit* txtRank = new  QLineEdit;

	void initGui() {
		QHBoxLayout* lay = new QHBoxLayout{};
		auto leftlay = new QVBoxLayout{};

		auto forml = new QFormLayout;
		forml->addRow("Id",txtId);
		forml->addRow("Titlu", txtTitle);
		forml->addRow("Artist", txtArtist);
		forml->addRow("Rank", txtRank);

		setLayout(lay);
		leftlay->addLayout(forml);

		auto lyBtn = new QHBoxLayout{};
		lyBtn->addWidget(btnAdd);
		lyBtn->addWidget(btnDelete);
		lyBtn->addWidget(btnUpdate);
		lyBtn->addWidget(btnExit);

		int nr_colums = 5;
		int nr_lines = 3;
		this->tabel = new QTableWidget(nr_lines, nr_colums);
		QStringList tableHeader;
		tableHeader << "Id" << "Titlu" << "Artist" << "Rank" << "Numar melodii";
		this->tabel->setHorizontalHeaderLabels(tableHeader);
		this->tabel->horizontalHeader()->setSectionResizeMode(QHeaderView::ResizeToContents);
		
		leftlay->addLayout(lyBtn);
		lay->addLayout(leftlay);
		lay->addWidget(tabel);
	}

	void initConnect() {
		QObject::connect(btnAdd, &QPushButton::clicked, [&]() {
			auto id = txtId->text();
			auto title = txtTitle->text();
			auto artist = txtArtist->text();
			auto rank = txtRank->text();
			try {
				service.addMelody(id.split(" ")[0].toInt(), artist.toStdString(), title.toStdString(), rank.split(" ")[0].toInt());
				loadData(tabel, service.sorted());
			}
			catch (ExceptionValid& msg) {
				QMessageBox::warning(this, "Info", QString::fromStdString(msg.getErrorMsg()));
			}
			});
		QObject::connect(btnExit, &QPushButton::clicked, [&]() {
			QMessageBox::StandardButton reply = QMessageBox::question(this, "Exit", "Are you sure?", QMessageBox::Yes | QMessageBox::No);
			if (reply == QMessageBox::Yes)
				close();
			});
		QObject::connect(btnDelete, &QPushButton::clicked, [&]() {
			auto id = txtId->text();
			auto title = txtTitle->text();
			auto artist = txtArtist->text();
			auto rank = txtRank->text();
			int id1 = id.split(" ")[0].toInt();
			int rank1 = rank.split(" ")[0].toInt();
			string title1 = title.toStdString();
			string artist1 = artist.toStdString();
			vector<Melodie> ranks = service.filtre(artist1);
			if (ranks.size() == 1)
				QMessageBox::warning(this, "Info", "Ultima melodie a artistului nu poate fi stearsa");
			else {
				try {
					service.deleteMelody(id1);
					loadData(tabel, service.sorted());
				}
				catch (RepoException& msg) {
					QMessageBox::warning(this, "Info", QString::fromStdString(msg.getErrorMessage()));
				}
			}
			});
		QObject::connect(btnUpdate, &QPushButton::clicked, [&]() {
			auto id = txtId->text();
			auto title = txtTitle->text();
			auto artist = txtArtist->text();
			auto rank = txtRank->text();
			try {
				service.modifyMelody(id.split(" ")[0].toInt(), title.toStdString(), rank.split(" ")[0].toInt());
				loadData(tabel, service.sorted());
			}
			catch (RepoException& msg) {
				QMessageBox::warning(this, "Info", QString::fromStdString(msg.getErrorMessage()));
			}
			});
		QTableWidget::connect(tabel, &QTableWidget::itemSelectionChanged, [&]() {
			auto item = tabel->currentItem();
			QStringList parts;
			for (int i = 0; i < 5; i++)
				parts.push_back(tabel->item(item->row(), i)->text());
			txtId->setText(parts[0]);
			});
	}

	void loadData(QTableWidget* tabel, vector<Melodie> melodii) {
		tabel->clearContents();
		this->tabel->setRowCount(melodii.size());
		int linii = 0;
		for (auto& mel : melodii) {
			vector<Melodie> acelairank = service.filtre2(mel.getRank());
			int nr = acelairank.size();
			this->tabel->setItem(linii, 0, new QTableWidgetItem(QString::number(mel.getId())));
			this->tabel->setItem(linii, 1, new QTableWidgetItem(QString::fromStdString(mel.getTitle())));
			this->tabel->setItem(linii, 2, new QTableWidgetItem(QString::fromStdString(mel.getArtist())));
			this->tabel->setItem(linii, 3, new QTableWidgetItem(QString::number(mel.getRank())));
			this->tabel->setItem(linii, 4, new QTableWidgetItem(QString::number(nr)));
			linii++;
		}
	}
};