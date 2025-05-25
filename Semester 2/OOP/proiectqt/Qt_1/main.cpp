#include "ui.h"
#include<iostream>
#include "teste.h"
#include <crtdbg.h>
#include <cstring>
#include "domain.h"
#include <QtWidgets/qlistwidget.h>
#include "Interfata_GUI.h"

RepositoryProductsFile repo{ "Products.txt" };
ProductValidator valid;
Basket basket;
ServiceProduct service{ repo, valid, basket };

int main(int argc,char *argv[])
{
	QApplication a(argc, argv);
	Interfata_GUI gui{ service };
	QPalette pal = QPalette();
	// set black background
	// Qt::black / "#000000" / "black"
	pal.setColor(QPalette::Window, Qt::lightGray);
	gui.setAutoFillBackground(true);
	gui.setPalette(pal);
	gui.show();
	return a.exec();

	//_CrtDumpMemoryLeaks();
	//testall();
	//startApp();

}





/*void startApp() {
	RepositoryProductsFile repo{ "Products.txt" };
	ProductValidator valid;
	Basket basket;
	ServiceProduct service{ repo, valid, basket };
	UI ui{ service };

	ui.run();
}*/
