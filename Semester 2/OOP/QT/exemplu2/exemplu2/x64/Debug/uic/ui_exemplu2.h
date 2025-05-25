/********************************************************************************
** Form generated from reading UI file 'exemplu2.ui'
**
** Created by: Qt User Interface Compiler version 6.7.1
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_EXEMPLU2_H
#define UI_EXEMPLU2_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_exemplu2Class
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *exemplu2Class)
    {
        if (exemplu2Class->objectName().isEmpty())
            exemplu2Class->setObjectName("exemplu2Class");
        exemplu2Class->resize(600, 400);
        menuBar = new QMenuBar(exemplu2Class);
        menuBar->setObjectName("menuBar");
        exemplu2Class->setMenuBar(menuBar);
        mainToolBar = new QToolBar(exemplu2Class);
        mainToolBar->setObjectName("mainToolBar");
        exemplu2Class->addToolBar(mainToolBar);
        centralWidget = new QWidget(exemplu2Class);
        centralWidget->setObjectName("centralWidget");
        exemplu2Class->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(exemplu2Class);
        statusBar->setObjectName("statusBar");
        exemplu2Class->setStatusBar(statusBar);

        retranslateUi(exemplu2Class);

        QMetaObject::connectSlotsByName(exemplu2Class);
    } // setupUi

    void retranslateUi(QMainWindow *exemplu2Class)
    {
        exemplu2Class->setWindowTitle(QCoreApplication::translate("exemplu2Class", "exemplu2", nullptr));
    } // retranslateUi

};

namespace Ui {
    class exemplu2Class: public Ui_exemplu2Class {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_EXEMPLU2_H
