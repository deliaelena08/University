#include "test.h"
#include <QtWidgets/QApplication>

#include"Tests.h"
#include"GUI.h"
RepoFile repo{ "fisier.txt" };
Service service{ repo };

void populate() {
    vector<Elev> elevi = service.getAllElevi();
    Atelier at1( "Desen");
    Atelier at2( "Jurnalism" );
    Atelier at3( "Informatica" );
    Atelier at4( "Fotografie" );
    Atelier at5( "Muzica" );
    int nr = 0;
    for (auto& el : elevi) {
        if (nr % 2 == 0) {
            at1.addElev(el);
            at3.addElev(el);
        }
        else {
            at2.addElev(el);
            at4.addElev(el);
        }
        nr++;
    }
    at5.addElev(elevi[0]);
}
int main(int argc, char *argv[])
{
    testall();
    populate();
    QApplication a(argc, argv);
	Intergata_GUI gui(service);
	gui.show();
    return a.exec();
}
