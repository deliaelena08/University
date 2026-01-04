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
#include "Histograma.h"
#include"Abstract.h"


class Interfata_GUI:public QWidget, public Observable,public Observer{
public:
    Interfata_GUI(ServiceProduct& service, Basket& basket) :service{ service }, basket{ basket } {
       
         list= new ProductListModel;
         lst= new QListView;
         lstBasket = new QListView;
         lst->setModel(list);
         lstBasket->setModel(list);
        initGUI();
        loadData(lst,service.getAll());
        tableData(tabel, service.getAll());
        initConnect();
    }
private:
    ProductListModel* list;
    QListView* lst;
    map<string,int>::iterator it;
    ServiceProduct& service;
    QListWidget* lst2 = new QListWidget;
    QListView* lstBasket ;
    QPalette pal = QPalette();
    QPushButton* btnExit = new QPushButton{ "&Iesire" };
    QPushButton* btnAdd=new QPushButton{ "&Adauga" };
    QPushButton* btnDelete = new QPushButton{ "&Sterge" };
    QPushButton* btnModify = new QPushButton{ "&Modifica" };
    QPushButton* btnUndo = new QPushButton{ "&Undo" };
    QPushButton* btnMore = new QPushButton{ "&Mai mult" };
    QPushButton* btnBasket1 = new QPushButton{ "&CosCRUDGUI" };
    QPushButton* btnBasket2 = new QPushButton{ "&CosReadOnlyGui" };
    QPushButton* btnSort1 = new QPushButton{ "&Sortare dupa nume" };
    QPushButton* btnSort2 = new QPushButton{ "&Sortare dupa pret" };
    QPushButton* btnFiltre1 = new QPushButton{ "&Filtrare dupa nume" };
    QPushButton* btnFiltre2 = new QPushButton{ "&Filtrare dupa pret" };
    QPushButton* btnAddBasket = new QPushButton{ "Adauga cos" };
    QPushButton* btnGenerateBasket = new QPushButton{ "Genereaza aleatoriu" };
    QPushButton* btnGenerateBasket2 = new QPushButton{ "Genereaza aleatoriu" };
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
    QLineEdit* txtGenerateBasket2 = new QLineEdit;
    QLineEdit* txtExportBasket = new QLineEdit;
    vector<QPushButton*> butoane;
    Basket& basket;
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
        auto lyBtn2 = new QHBoxLayout{};
        //rightlay->addLayout(lyBtn1);
        lyBtn2->addWidget(btnAddBasket);
        lyBtn2->addWidget(btnDeleteBasket);
        lyBtn2->addWidget(btnGenerateBasket2);
        rightlay->addLayout(lyBtn2);

        auto linelaybun = new QFormLayout;
        auto linelayrau = new QFormLayout;
        linelayrau->addRow("Nume produs ", txtAddBasketProduct);
        linelayrau->addRow("Nume producator", txtAddBasketProductor);
        linelaybun->addRow("Adaugare", linelayrau);
        linelaybun->addRow("Generator", txtGenerateBasket2);
        rightlay->addLayout(linelaybun);

        rightlay->addWidget(btnBasket1);
        rightlay->addWidget(btnBasket2);
        rightlay->addWidget(btnMore);

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
        rightlay3->addWidget(btnGenerateBasket);
        rightlay3->addWidget(btnExportBasket);
        linelay->addRow("Generator", txtGenerateBasket);
        linelay->addRow("Export", txtExportBasket);
        rightlay3->addLayout(linelay);
        
        lay3->addLayout(rightlay3);
        lay3->addWidget(lstBasket);
        wdg->setLayout(lay3);

    }

    void initConnect() {
        addObservcer(this);
        QObject::connect(lst, &QListView::clicked, [&]() {
            QModelIndex currentIndex = lst->currentIndex();
            if (currentIndex.isValid()) {
                QVariant data = currentIndex.data();
                QStringList parts = data.toString().split(" | ");
                if (parts.size() >= 4) {
                    txtNume->setText(parts[0]);
                    txtTip->setText(parts[1]);
                    txtPret->setText(parts[2]);
                    txtProducator->setText(parts[3]);
                    txtAddBasketProduct->setText(parts[0]);
                    txtAddBasketProductor->setText(parts[3]);
                }
            }
            else {
                txtNume->clear();
                txtTip->clear();
                txtPret->clear();
                txtProducator->clear();
                txtAddBasketProduct->clear();
                txtAddBasketProductor->clear();
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
        QObject::connect(btnBasket1, &QPushButton::clicked, [&]() {
            basketButton();
            });
        QObject::connect(btnBasket2, &QPushButton::clicked, [&]() {
            histogram();
            });
        QObject::connect(btnAddBasket, &QPushButton::clicked, [&]() {
            auto nume = txtAddBasketProduct->text();
            auto prdt = txtAddBasketProductor->text();
            service.addToBasket(nume.toStdString(), prdt.toStdString());
            loadData(lstBasket, service.getAllBasket());
            QListWidgetItem* lastline = new QListWidgetItem;
            lastline->setText("Suma curenta : " + QString::number(service.getSumBasket()));
            QMessageBox::warning(this, "Info", "Suma curenta este : " + QString::number(service.getSumBasket()));
            notify();
            });
        QObject::connect(btnGenerateBasket, &QPushButton::clicked, [&]() {
            auto number = txtGenerateBasket->text();
            service.generate1(number.toInt());
            loadData(lstBasket, service.getAllBasket());
            QListWidgetItem* lastline = new QListWidgetItem;
            QMessageBox::warning(this, "Info", "Suma curenta este : "+QString::number(service.getSumBasket()));
            notify();
            });
        QObject::connect(btnGenerateBasket2, &QPushButton::clicked, [&]() {
            auto number = txtGenerateBasket2->text();
            service.generate1(number.toInt());
            loadData(lstBasket, service.getAllBasket());
            QListWidgetItem* lastline = new QListWidgetItem;
            QMessageBox::warning(this, "Info", "Suma curenta este : " + QString::number(service.getSumBasket()));
            notify();
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
            QMessageBox::warning(this, "Info", "Suma curenta este : " + QString::number(service.getSumBasket()));
            notify();
            });
           
    }


    void histogram() {
        HistogramGUI* hgui=new HistogramGUI(basket);
        hgui->show();
    }

    void update() override {
        loadData(lst, service.getAll());
        tableData(tabel, service.getAll());
    }
    
    void loadData(QListView* lst, const vector<Product>& products) {
        list->clear(); 
        for (const auto& p : products) {
            list->addItem(p);
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

