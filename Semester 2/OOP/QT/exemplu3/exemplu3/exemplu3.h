#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_exemplu3.h"

class exemplu3 : public QMainWindow
{
    Q_OBJECT

public:
    exemplu3(QWidget *parent = nullptr);
    ~exemplu3();

private:
    Ui::exemplu3Class ui;
};
