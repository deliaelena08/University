#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_exemplu4.h"

class exemplu4 : public QMainWindow
{
    Q_OBJECT

public:
    exemplu4(QWidget *parent = nullptr);
    ~exemplu4();

private:
    Ui::exemplu4Class ui;
};
