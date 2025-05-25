#pragma once
#include<QtWidgets/qwidget.h>
#include <QtWidgets/QApplication>
#include<QtWidgets/qpushbutton.h>
#include<QtWidgets/qlabel.h>
#include<QtWidgets/qboxlayout.h>
#include<QtWidgets/qlineedit.h>
#include<QtWidgets/qformlayout.h>
#include<QtWidgets/qlistwidget.h>
#include<QtWidgets/qtablewidget.h>
#include <QtWidgets/qmessagebox.h>
#include<QtWidgets/qheaderview.h>
#include<qdebug.h>
#include "service.h"

class Interfata_GUI: public QWidget{
public:
    Interfata_GUI(ServiceProduct& service):service{ service } {
        initGUI();
        loadData(lst,service.getAll());
        tableData(tabel, service.getAll());
        initConnect();
    }
private:
    map<string,int>::iterator it;
    ServiceProduct& service;
    QListWidget* lst = new QListWidget;
    QListWidget* lst2 = new QListWidget;
    QListWidget* lstBasket = new QListWidget;
    QPalette pal = QPalette();
    QPushButton* btnExit = new QPushButton{ "&Iesire" };
    QPushButton* btnAdd=new QPushButton{ "&Adauga" };
    QPushButton* btnDelete = new QPushButton{ "&Sterge" };
    QPushButton* btnModify = new QPushButton{ "&Modifica" };
    QPushButton* btnUndo = new QPushButton{ "&Undo" };
    QPushButton* btnMore = new QPushButton{ "&Mai mult" };
    QPushButton* btnBasket = new QPushButton{ "&Cos" };
    QPushButton* btnSort1 = new QPushButton{ "&Sortare dupa nume" };
    QPushButton* btnSort2 = new QPushButton{ "&Sortare dupa pret" };
    QPushButton* btnFiltre1 = new QPushButton{ "&Filtrare dupa nume" };
    QPushButton* btnFiltre2 = new QPushButton{ "&Filtrare dupa pret" };
    QPushButton* btnAddBasket = new QPushButton{ "Adauga cos" };
    QPushButton* btnGenerateBasket = new QPushButton{ "Genereaza aleatoriu" };
    QPushButton* btnExportBasket = new QPushButton{ "Exporta cos" };
    QPushButton* btnDeleteBasket = new QPushButton{ "Goleste cos" };
    QLineEdit* txtNume = new QLineEdit;
    QLineEdit* txtTip = new QLineEdit;
    QLineEdit* txtPret = new QLineEdit;
    QLineEdit* txtProducator = new QLineEdit;
    QLineEdit* txtInp = new QLineEdit;
    QLineEdit* txtAddBasketProduct = new QLineEdit;
    QLineEdit* txtAddBasketProductor = new QLineEdit;
    QLineEdit* txtGenerateBasket = new QLineEdit;
    QLineEdit* txtExportBasket = new QLineEdit;
    vector<QPushButton*> butoane;
    QTableWidget* tabel ;

	void initGUI() { 
        QHBoxLayout* lay1 = new QHBoxLayout{};
        auto rightlay = new QVBoxLayout{};
        pal.setColor(QPalette::Window, Qt::darkBlue);

        auto forml = new QFormLayout;
        forml->addRow("Nume produs", txtNume);
        forml->addRow("Tipul produsului", txtTip);
        forml->addRow("Pret", txtPret);
        forml->addRow("Nume producator", txtProducator);

        setLayout(lay1);
        rightlay->addLayout(forml);

        auto lyBtn = new QHBoxLayout{};
       // auto lyBtn1 = new QHBoxLayout{};
        lyBtn->addWidget(btnAdd);
        lyBtn->addWidget(btnDelete);
        lyBtn->addWidget(btnModify);
        lyBtn->addWidget(btnUndo);
        lyBtn->addWidget(btnExit);
       /* map<string, int> sample_map = service.createMap(service.getAll());

        for (it = sample_map.begin(); it != sample_map.end(); it++){
            string nume = it->first;
            QPushButton* buton = new QPushButton{QString::fromStdString(nume)};
            butoane.push_back(buton);
            QObject::connect(buton, &QPushButton::clicked, [this,nume]() {
                map<string, int> sample_map2 = service.createMap(service.getAll());
                int valoare = sample_map2[nume];
                QMessageBox::information(nullptr,"Map",QString::number(valoare));
                });
            //qDebug() << it->first<<" "<<it->second<<" ";
            lyBtn1->addWidget(buton);
        }*/
        int nr_colums = 4;
        int nr_lines = 3;
        this->tabel = new QTableWidget(nr_lines, nr_colums);
        QStringList tableHeader;
        tableHeader << "Nume" << "Tip" << "Pret" << "Producator";
        this->tabel->setHorizontalHeaderLabels(tableHeader);
        this->tabel->horizontalHeader()->setSectionResizeMode(QHeaderView::ResizeToContents);

        rightlay->addLayout(lyBtn);
        //rightlay->addLayout(lyBtn1);
        rightlay->addWidget(btnMore);
        rightlay->addWidget(btnBasket);

        lay1->addLayout(rightlay);
        lay1->addWidget(lst);
        lay1->addWidget(tabel);

	}

    void modifyButton() {
        loadData(lst2,service.getAll());
        QWidget* wdg = new QWidget;
        QPalette pal = QPalette();
        pal.setColor(QPalette::Window, Qt::lightGray);
        wdg->setAutoFillBackground(true);
        wdg->setPalette(pal);
        wdg->show();

        auto forml = new QFormLayout;
        QHBoxLayout* lay2 = new QHBoxLayout{};
        auto rightlay2 = new QVBoxLayout{};
        rightlay2->addWidget(btnSort1);
        rightlay2->addWidget(btnSort2);
        rightlay2->addWidget(btnFiltre1);
        rightlay2->addWidget(btnFiltre2);
        forml->addRow("Filtru", txtInp);
        rightlay2->addLayout(forml);

        lay2->addLayout(rightlay2);
        lay2->addWidget(lst2);
        wdg->setLayout(lay2);
    }

    void basketButton() {
        vector<Product> basket = service.getAllBasket();
        loadData(lstBasket, basket);
        QListWidgetItem* lastline = new QListWidgetItem;
        lastline->setText("Suma curenta : " + QString::number(service.getSumBasket()));
        lstBasket->addItem(lastline);

        QWidget* wdg = new QWidget;
        QPalette pal = QPalette();
        pal.setColor(QPalette::Window, Qt::lightGray);
        wdg->setAutoFillBackground(true);
        wdg->setPalette(pal);
        wdg->show();

        QHBoxLayout* lay3 = new QHBoxLayout{};
        auto rightlay3 = new QVBoxLayout{};
        auto linelay1 = new QFormLayout;
        auto linelay = new QFormLayout;
        rightlay3->addWidget(btnAddBasket);
        rightlay3->addWidget(btnGenerateBasket);
        rightlay3->addWidget(btnExportBasket);
        rightlay3->addWidget(btnDeleteBasket);
        linelay1->addRow("Nume produs ",txtAddBasketProduct);
        linelay1->addRow("Nume producator", txtAddBasketProductor);
        linelay->addRow("Adaugare", linelay1);
        linelay->addRow("Generator", txtGenerateBasket);
        linelay->addRow("Export", txtExportBasket);
        rightlay3->addLayout(linelay);
        
        lay3->addLayout(rightlay3);
        lay3->addWidget(lstBasket);
        wdg->setLayout(lay3);

    }

    void initConnect() {
        QListWidget::connect(lst, &QListWidget::clicked, [&]() {
            if (lst->selectedItems().isEmpty()) {
                txtNume->setText("");
                txtTip->setText("");
                txtPret->setText("");
                txtProducator->setText("");
            }
            else {
                auto item = lst->currentItem();
                QStringList parts = item->text().split(" | ");
                txtNume->setText(parts[0]);
                txtTip->setText(parts[1]);
                txtPret->setText(parts[2]);
                txtProducator->setText(parts[3]);
            }
            });

        QTableWidget::connect(tabel, &QTableWidget::itemSelectionChanged, [&]() {
            auto item = tabel->currentItem();
            QStringList parts;
            for (int i = 0; i < 4; i++) 
                parts.push_back(tabel->item(item->row(), i)->text());
            txtNume->setText(parts[0]);
            txtTip->setText(parts[1]);
            txtPret->setText(parts[2]);
            txtProducator->setText(parts[3]);
            
            });

        QObject::connect(btnExit, &QPushButton::clicked, [&]() {
            QMessageBox::StandardButton reply=QMessageBox::question(this, "Exit", "Are you sure?",QMessageBox::Yes|QMessageBox::No);
            if(reply==QMessageBox::Yes)
                close();
            });
        QObject::connect(btnAdd, &QPushButton::clicked, [&]() {
            auto nume = txtNume->text();
            auto tip = txtTip->text();
            auto pret = txtPret->text();
            auto producator = txtProducator->text();
            try {
                service.addProduct(nume.toStdString(), tip.toStdString(), pret.split(" ")[0].toInt(), producator.toStdString());
                loadData(lst, service.getAll());
                tableData(tabel, service.getAll());
            }
            catch(RepoException &msg) {
                QMessageBox::warning(this, "Info", QString::fromStdString(msg.getErrorMessage()));
            }
            });
        QObject::connect(btnDelete, &QPushButton::clicked, [&]() {
            auto nume = txtNume->text();
            auto tip = txtTip->text();
            auto pret = txtPret->text();
            auto producator = txtProducator->text();
            try {
                service.deleteProduct(nume.toStdString(), producator.toStdString());
                loadData(lst, service.getAll());
                tableData(tabel, service.getAll());
                QMessageBox::information(nullptr, "Stergere", "Stergere cu succes");
            }
            catch (RepoException& msg) {
                QMessageBox::warning(this, "Info", QString::fromStdString(msg.getErrorMessage()));
            }
            });
        QObject::connect(btnModify, &QPushButton::clicked, [&]() {
            auto nume = txtNume->text();
            auto tip = txtTip->text();
            auto pret = txtPret->text();
            auto producator = txtProducator->text();
            service.modifyProduct(nume.toStdString(), producator.toStdString(), nume.toStdString(), tip.toStdString(), pret.split(" ")[0].toInt());
            loadData(lst, service.getAll());
            tableData(tabel, service.getAll());
            });
        QObject::connect(btnUndo, &QPushButton::clicked, [&]() {
            try {
                service.undo();
                loadData(lst, service.getAll());
                tableData(tabel, service.getAll());
            }
            catch (RepoException& msg) {
                QMessageBox::warning(this, "Info", QString::fromStdString(msg.getErrorMessage()));
            }
            });
        QObject::connect(btnMore, &QPushButton::clicked, [&]() {
            modifyButton();
            });
        QObject::connect(btnSort1, &QPushButton::clicked, [&]() {
            loadData(lst2, service.sort_by_name());
            });
        QObject::connect(btnSort2, &QPushButton::clicked, [&]() {
            loadData(lst2, service.sort_by_price());
            });
        QObject::connect(btnFiltre1, &QPushButton::clicked, [&]() {
            auto nume = txtInp->text();
            loadData(lst2, service.filtre_by_name(nume.toStdString()));
            });
        QObject::connect(btnFiltre2, &QPushButton::clicked, [&]() {
            auto pret = txtInp->text();
            loadData(lst2, service.filtre_by_price(pret.toInt()));
            });
        QObject::connect(btnBasket, &QPushButton::clicked, [&]() {
            basketButton();
            });
        QObject::connect(btnAddBasket, &QPushButton::clicked, [&]() {
            auto nume = txtAddBasketProduct->text();
            auto prdt = txtAddBasketProductor->text();
            service.addToBasket(nume.toStdString(), prdt.toStdString());
            loadData(lstBasket, service.getAllBasket());
            QListWidgetItem* lastline = new QListWidgetItem;
            lastline->setText("Suma curenta : " + QString::number(service.getSumBasket()));
            lstBasket->addItem(lastline);
            });
        QObject::connect(btnGenerateBasket, &QPushButton::clicked, [&]() {
            auto number = txtGenerateBasket->text();
            service.generate1(number.toInt());
            loadData(lstBasket, service.getAllBasket());
            QListWidgetItem* lastline = new QListWidgetItem;
            lastline->setText("Suma curenta : " + QString::number(service.getSumBasket()));
            lstBasket->addItem(lastline);
            });
        QObject::connect(btnExportBasket, &QPushButton::clicked, [&]() {
            auto numeFis = txtExportBasket->text();
            service.exportBasket(numeFis.toStdString());
            QMessageBox::information(nullptr, "Export", "Export cu succes");
            });
        QObject::connect(btnDeleteBasket, &QPushButton::clicked, [&]() {
            service.emptyBasket();
            loadData(lstBasket,service.getAllBasket());
            QListWidgetItem* lastline = new QListWidgetItem;
            lastline->setText("Suma curenta : " + QString::number(service.getSumBasket()));
            lstBasket->addItem(lastline);
            });
           
    }

    void loadData(QListWidget* lst,vector<Product> products) {
        lst->clear();
        QListWidgetItem* firstline = new QListWidgetItem;
        firstline->setText("Nume    Tip    Pret    Producator");
        lst->addItem(firstline);
        for (auto& p : products) {
            QListWidgetItem* product = new QListWidgetItem;
            product->setText(QString::fromStdString(p.getname())+" | "+QString::fromStdString(p.gettype()) + " | " + QString::number(p.getprice())+ " | " + QString::fromStdString(p.getproductor()));
            product->setData(Qt::UserRole, QVariant::fromValue(&p));
            lst->addItem(product);
        }
    }
   

    void tableData(QTableWidget* tabel,vector<Product> products) {
        tabel->clearContents();
        this->tabel->setRowCount(products.size());
        int line_number = 0;
        for (auto& product : products) {
            this->tabel->setItem(line_number, 0, new QTableWidgetItem(QString::fromStdString(product.getname())));
            this->tabel->setItem(line_number, 1, new QTableWidgetItem(QString::fromStdString(product.gettype())));
            this->tabel->setItem(line_number, 2, new QTableWidgetItem(QString::number(product.getprice())));
            this->tabel->setItem(line_number, 3, new QTableWidgetItem(QString::fromStdString(product.getproductor())));
            line_number++;
        }
       
    }

};

