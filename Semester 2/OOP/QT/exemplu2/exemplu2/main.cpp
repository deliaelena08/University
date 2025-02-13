#include "exemplu2.h"
#include <QtWidgets/QApplication>
#include "GUI.h"
#include "Tests.h"
#include "Service.h"

RepoFile repo{ "fisier1.txt" };
Validator valid;
Service service{ repo,valid };

int main(int argc, char *argv[])
{
    testAll();
    QApplication a(argc, argv);
    Interfata_GUI gui{ service };
    gui.show();
    return a.exec();
}
