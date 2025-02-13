#include"Test.h"
#include<assert.h>

void testDomain() {
	Masina m1{ "Fiat","Bravo",2007,"red" };
	Masina m2{ "Audi","A5",2007,"blue" };
	assert(m1.getFirma() == "Fiat");
	assert(m1.getModel() == "Bravo");
	assert(m1.getAn() == 2007);
	assert(m1.getCuloARE() == "red");
}
void testRepo() {
	RepoFile repo("test1.txt");
	Masina m1{ "Fiat","Bravo",2007,"red" };
	Masina m2{ "Audi","A5",2007,"blue" };
	repo.store(m1);
	assert(repo.getAll().size() == 1);
	repo.store(m2);
	assert(repo.getAll().size() == 2);
}

void testService() {
	RepoFile repo("test2.txt");
	Service srv(repo);
	srv.storeCar("Fiat", "Bravo", 2007, "red");
	srv.storeCar("Audi", "A5", 2007, "blue");
	srv.storeCar("Fiat", "Idea", 2003, "black");
	assert(srv.getAllCars().size() == 3);
	vector<Masina> sorted = srv.sortByFirma();
	assert(sorted[0].getFirma() == "Audi");
	vector<Masina> filtred = srv.filtreByFirma("Fiat");
	assert(filtred.size() == 2);
}

void testAll() {
	testDomain();
	testRepo();
	testService();
}