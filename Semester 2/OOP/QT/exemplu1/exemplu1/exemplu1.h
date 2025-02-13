#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_exemplu1.h"

class exemplu1 : public QMainWindow
{
    Q_OBJECT

public:
    exemplu1(QWidget *parent = nullptr);
    ~exemplu1();

private:
    Ui::exemplu1Class ui;
};
