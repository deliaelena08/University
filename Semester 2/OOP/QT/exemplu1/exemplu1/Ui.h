#pragma once
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
#include "service.h"

class Interfata_GUI : public QWidget {
public:
    Interfata_GUI(Service& service) : service{ service } {
        initGUI();
        loadData(tabel, service.sorted());
        initConnect();
        comboBox(service.getAllTractors());
    }
private:
    Service& service;
    QTableWidget* tabel;
    QPalette pal = QPalette();
    QPushButton* btnAdd = new QPushButton{ "Adauga" };
    QLineEdit* txtId = new QLineEdit;
    QLineEdit* txtName = new QLineEdit;
    QLineEdit* txtType = new QLineEdit;
    QLineEdit* txtWheels = new QLineEdit;
    QPushButton* btnExit = new QPushButton{ "Exit" };
    QComboBox* box = new QComboBox;

    void initGUI() {
        QHBoxLayout* lay1 = new QHBoxLayout{};
        auto rightlay = new QVBoxLayout{};
        pal.setColor(QPalette::Window, Qt::blue);

        auto forml = new QFormLayout;
        forml->addRow("Id", txtId);
        forml->addRow("Denumire", txtName);
        forml->addRow("Tip", txtType);
        forml->addRow("Roti", txtWheels);

        setLayout(lay1);
        rightlay->addLayout(forml);
        rightlay->addWidget(box);

        auto lyBtn = new QHBoxLayout{};
        lyBtn->addWidget(btnAdd);
        lyBtn->addWidget(btnExit);

        int nr_colums = 5;
        int nr_lines = 3;
        this->tabel = new QTableWidget(nr_lines, nr_colums);
        QStringList tableHeader;
        tableHeader << "ID" << "Denumire" << "Tip" << "Numar roti" << "Acelasi tip";
        this->tabel->setHorizontalHeaderLabels(tableHeader);
        this->tabel->horizontalHeader()->setSectionResizeMode(QHeaderView::ResizeToContents);

        rightlay->addLayout(lyBtn);
        lay1->addLayout(rightlay);
        lay1->addWidget(tabel);
    }

    void loadData(QTableWidget* tabel, vector<Tractor> tractoare) {
        tabel->clearContents();
        this->tabel->setRowCount(tractoare.size());
        int line = 0;
        for (auto& tr : tractoare) {
            vector<Tractor> ceva = service.filtred(tr.getType());
            int tipuri = ceva.size();
            this->tabel->setItem(line, 0, new QTableWidgetItem(QString::number(tr.getId())));
            this->tabel->setItem(line, 1, new QTableWidgetItem(QString::fromStdString(tr.getName())));
            this->tabel->setItem(line, 2, new QTableWidgetItem(QString::fromStdString(tr.getType())));
            this->tabel->setItem(line, 3, new QTableWidgetItem(QString::number(tr.getWheels())));
            this->tabel->setItem(line, 4, new QTableWidgetItem(QString::number(tipuri)));
            line++;
        }
    }

    void initConnect() {
        QObject::connect(btnAdd, &QPushButton::clicked, [&]() {
            auto id = txtId->text();
            auto nume = txtName->text();
            auto tip = txtType->text();
            auto roti = txtWheels->text();
            try {
                service.addTractor(id.split(" ")[0].toInt(), nume.toStdString(), tip.toStdString(), roti.split(" ")[0].toInt());
                loadData(tabel, service.sorted());
            }
            catch (RepoException& msg) {
                QMessageBox::warning(this, "Info", QString::fromStdString(msg.getErrorMessage()));
            }
            });

        QObject::connect(btnExit, &QPushButton::clicked, [&]() {
            QMessageBox::StandardButton reply = QMessageBox::question(this, "Exit", "Are you sure?", QMessageBox::Yes | QMessageBox::No);
            if (reply == QMessageBox::Yes)
                close();
            });

        QTableWidget::connect(tabel, &QTableWidget::itemSelectionChanged, [&]() {
            auto item = tabel->currentItem();
            QStringList parts;
            for (int i = 0; i < 5; i++)
                parts.push_back(tabel->item(item->row(), i)->text());
            txtId->setText(parts[0]);
            txtName->setText(parts[1]);
            txtType->setText(parts[2]);
            txtWheels->setText(parts[3]);
            });

        QComboBox::connect(box, &QComboBox::activated, this, [&]() {
            auto selectedItem = box->currentText();
            vector<Tractor> tractoare = service.sorted();

            // Iterate over all items in the table to reset their background color
            for (int row = 0; row < tabel->rowCount(); ++row) {
                for (int col = 0; col < tabel->columnCount(); ++col) {
                    auto item = tabel->item(row, col);
                    if (item) {
                        item->setBackground(Qt::NoBrush);
                    }
                }
            }

            // Iterate over all tractors and update the background color of the matching items
            for (int row = 0; row < tractoare.size(); ++row) {
                if (tractoare[row].getType() == selectedItem.toStdString()) {
                    for (int col = 0; col < tabel->columnCount(); ++col) {
                        auto item = tabel->item(row, col);
                        if (item) {
                            item->setBackground(QBrush(QColor( 255,0, 0)));
                        }
                    }
                }
            }
            });
    }

    void comboBox(vector<Tractor> tractoare) {
        map<string, int> tipuri;
        for (auto& tr : tractoare)
            tipuri[tr.getType()]++;
        for (auto& el : tipuri) {
            box->addItem(QString::fromStdString(el.first));
        }
    }
};
