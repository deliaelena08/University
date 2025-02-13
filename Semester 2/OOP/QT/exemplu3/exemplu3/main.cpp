#include "exemplu3.h"
#include <QtWidgets/QApplication>
#include"Teste.h"
#include"GUI.h"
RepoFile repo("fisier.txt");
Service service(repo);
int main(int argc, char* argv[])
{
    testall();
    QApplication a(argc, argv);
    Interfata_Gui gui{ service };
    gui.show();
    return a.exec();
}
