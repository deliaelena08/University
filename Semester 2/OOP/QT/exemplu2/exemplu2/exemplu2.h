#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_exemplu2.h"

class exemplu2 : public QMainWindow
{
    Q_OBJECT

public:
    exemplu2(QWidget *parent = nullptr);
    ~exemplu2();

private:
    Ui::exemplu2Class ui;
};
