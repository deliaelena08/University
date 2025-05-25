#include "exemplu4.h"
#include <QtWidgets/QApplication>
#include"Test.h"
#include"Gui.h"

RepoFile repo{ "fisier.txt" };
Service service{repo};
int main(int argc, char *argv[])
{
    testAll();
    QApplication a(argc, argv);
    Intergata_GUI gui(service);
    gui.show();
    return a.exec();
}
