#include "ui.h"
#include<iostream>
#include "teste.h"
#include <crtdbg.h>
#include <cstring>
#include "domain.h"


void startApp() {
	RepositoryProducts repo;
	ProductValidator valid;
	ServiceProduct service{ repo, valid };
	UI ui{ service };

	ui.run();
}

int main()
{
	_CrtDumpMemoryLeaks();
	testall();
	//startApp();
}