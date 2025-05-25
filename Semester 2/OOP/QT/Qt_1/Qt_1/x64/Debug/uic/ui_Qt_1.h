/********************************************************************************
** Form generated from reading UI file 'Qt_1.ui'
**
** Created by: Qt User Interface Compiler version 6.7.1
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_QT_1_H
#define UI_QT_1_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_Qt_1Class
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *Qt_1Class)
    {
        if (Qt_1Class->objectName().isEmpty())
            Qt_1Class->setObjectName("Qt_1Class");
        Qt_1Class->resize(600, 400);
        menuBar = new QMenuBar(Qt_1Class);
        menuBar->setObjectName("menuBar");
        Qt_1Class->setMenuBar(menuBar);
        mainToolBar = new QToolBar(Qt_1Class);
        mainToolBar->setObjectName("mainToolBar");
        Qt_1Class->addToolBar(mainToolBar);
        centralWidget = new QWidget(Qt_1Class);
        centralWidget->setObjectName("centralWidget");
        Qt_1Class->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(Qt_1Class);
        statusBar->setObjectName("statusBar");
        Qt_1Class->setStatusBar(statusBar);

        retranslateUi(Qt_1Class);

        QMetaObject::connectSlotsByName(Qt_1Class);
    } // setupUi

    void retranslateUi(QMainWindow *Qt_1Class)
    {
        Qt_1Class->setWindowTitle(QCoreApplication::translate("Qt_1Class", "Qt_1", nullptr));
    } // retranslateUi

};

namespace Ui {
    class Qt_1Class: public Ui_Qt_1Class {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_QT_1_H
