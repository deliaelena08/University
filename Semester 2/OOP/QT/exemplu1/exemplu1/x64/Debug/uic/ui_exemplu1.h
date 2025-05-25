/********************************************************************************
** Form generated from reading UI file 'exemplu1.ui'
**
** Created by: Qt User Interface Compiler version 6.7.1
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_EXEMPLU1_H
#define UI_EXEMPLU1_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_exemplu1Class
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *exemplu1Class)
    {
        if (exemplu1Class->objectName().isEmpty())
            exemplu1Class->setObjectName("exemplu1Class");
        exemplu1Class->resize(600, 400);
        menuBar = new QMenuBar(exemplu1Class);
        menuBar->setObjectName("menuBar");
        exemplu1Class->setMenuBar(menuBar);
        mainToolBar = new QToolBar(exemplu1Class);
        mainToolBar->setObjectName("mainToolBar");
        exemplu1Class->addToolBar(mainToolBar);
        centralWidget = new QWidget(exemplu1Class);
        centralWidget->setObjectName("centralWidget");
        exemplu1Class->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(exemplu1Class);
        statusBar->setObjectName("statusBar");
        exemplu1Class->setStatusBar(statusBar);

        retranslateUi(exemplu1Class);

        QMetaObject::connectSlotsByName(exemplu1Class);
    } // setupUi

    void retranslateUi(QMainWindow *exemplu1Class)
    {
        exemplu1Class->setWindowTitle(QCoreApplication::translate("exemplu1Class", "exemplu1", nullptr));
    } // retranslateUi

};

namespace Ui {
    class exemplu1Class: public Ui_exemplu1Class {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_EXEMPLU1_H
