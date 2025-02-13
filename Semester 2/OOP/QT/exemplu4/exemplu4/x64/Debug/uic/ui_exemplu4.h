/********************************************************************************
** Form generated from reading UI file 'exemplu4.ui'
**
** Created by: Qt User Interface Compiler version 6.7.1
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_EXEMPLU4_H
#define UI_EXEMPLU4_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_exemplu4Class
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *exemplu4Class)
    {
        if (exemplu4Class->objectName().isEmpty())
            exemplu4Class->setObjectName("exemplu4Class");
        exemplu4Class->resize(600, 400);
        menuBar = new QMenuBar(exemplu4Class);
        menuBar->setObjectName("menuBar");
        exemplu4Class->setMenuBar(menuBar);
        mainToolBar = new QToolBar(exemplu4Class);
        mainToolBar->setObjectName("mainToolBar");
        exemplu4Class->addToolBar(mainToolBar);
        centralWidget = new QWidget(exemplu4Class);
        centralWidget->setObjectName("centralWidget");
        exemplu4Class->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(exemplu4Class);
        statusBar->setObjectName("statusBar");
        exemplu4Class->setStatusBar(statusBar);

        retranslateUi(exemplu4Class);

        QMetaObject::connectSlotsByName(exemplu4Class);
    } // setupUi

    void retranslateUi(QMainWindow *exemplu4Class)
    {
        exemplu4Class->setWindowTitle(QCoreApplication::translate("exemplu4Class", "exemplu4", nullptr));
    } // retranslateUi

};

namespace Ui {
    class exemplu4Class: public Ui_exemplu4Class {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_EXEMPLU4_H
