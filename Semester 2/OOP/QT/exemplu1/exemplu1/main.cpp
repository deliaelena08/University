#include "exemplu1.h"
#include <QtWidgets/QApplication>
#include"teste.h"
#include"Ui.h"
RepoFile repo{ "fisier.txt" };
Validator valid;
Service service{ repo,valid };

int main(int argc, char *argv[])
{
    testall();
    QApplication a(argc, argv);
    Interfata_GUI gui{ service };
    QPalette pal = QPalette();
    pal.setColor(QPalette::Window, Qt::cyan);
    pal.setColor(QPalette::WindowText, Qt::black);
    gui.setAutoFillBackground(true);
    gui.setPalette(pal);
    gui.show();
    return a.exec();
}
