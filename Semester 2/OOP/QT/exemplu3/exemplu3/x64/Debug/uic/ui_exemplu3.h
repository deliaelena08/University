/********************************************************************************
** Form generated from reading UI file 'exemplu3.ui'
**
** Created by: Qt User Interface Compiler version 6.7.1
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_EXEMPLU3_H
#define UI_EXEMPLU3_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_exemplu3Class
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *exemplu3Class)
    {
        if (exemplu3Class->objectName().isEmpty())
            exemplu3Class->setObjectName("exemplu3Class");
        exemplu3Class->resize(600, 400);
        menuBar = new QMenuBar(exemplu3Class);
        menuBar->setObjectName("menuBar");
        exemplu3Class->setMenuBar(menuBar);
        mainToolBar = new QToolBar(exemplu3Class);
        mainToolBar->setObjectName("mainToolBar");
        exemplu3Class->addToolBar(mainToolBar);
        centralWidget = new QWidget(exemplu3Class);
        centralWidget->setObjectName("centralWidget");
        exemplu3Class->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(exemplu3Class);
        statusBar->setObjectName("statusBar");
        exemplu3Class->setStatusBar(statusBar);

        retranslateUi(exemplu3Class);

        QMetaObject::connectSlotsByName(exemplu3Class);
    } // setupUi

    void retranslateUi(QMainWindow *exemplu3Class)
    {
        exemplu3Class->setWindowTitle(QCoreApplication::translate("exemplu3Class", "exemplu3", nullptr));
    } // retranslateUi

};

namespace Ui {
    class exemplu3Class: public Ui_exemplu3Class {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_EXEMPLU3_H
