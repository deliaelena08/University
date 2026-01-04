#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_Qt_1.h"

class Qt_1 : public QMainWindow
{
    Q_OBJECT

public:
    Qt_1(QWidget *parent = nullptr);
    ~Qt_1();

private:
    Ui::Qt_1Class ui;
};
