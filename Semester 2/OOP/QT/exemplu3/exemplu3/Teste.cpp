#include"Teste.h"
#include<assert.h>

void testDomain() {
	Apartament ap1{ "50m^2","Hasdeu",3000 };
	Apartament ap2{ "200m^2","Buna ziua",200000 };
	assert(ap1.getPret() == 3000);
	assert(ap1.getStrada() == "Hasdeu");
	assert(ap1.getSuprafata() == "50m^2");
	ap1.setPret(ap2.getPret());
	ap1.setStrada(ap2.getStrada());
	ap1.setSuprafata(ap2.getSuprafata());
	assert(ap1.getPret() == 200000);
	assert(ap1.getStrada() == "Buna ziua");
	assert(ap1.getSuprafata() == "200m^2");
}

void testAddRepo() {
	Apartament ap1{ "5000m^2","Hasdeu",3000 };
	Apartament ap2{ "2000m^2","Buna ziua",200000 };
	RepoFile repo("test1.txt");
	repo.store(ap1);
	repo.store(ap2);
	assert(repo.getAll().size() == 2);
	Apartament ap3{ "5000m^2","Hasdeu",30 };
	try {
		repo.store(ap3);
		assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}
	repo.deleted(ap1);
	repo.deleted(ap2);

}

void testDeleteRepo() {
	Apartament ap1{ "50m^2","Hasdeu",3000 };
	Apartament ap2{ "20m^2","Buna ziua",200000 };
	RepoFile repo("test1.txt");
	repo.store(ap1);
	repo.store(ap2);
	repo.deleted(ap1);
	assert(repo.getAll().size() == 1);
	repo.deleted(ap2);
	assert(repo.getAll().size() == 0);
	Apartament ap3{ "500m","Hash",30 };
	try {
		repo.deleted(ap3);
		assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}
}

void testExistsRepo() {
	std::fstream file("test1.txt");
	file.close();
	Apartament ap1{ "50m^2","Hasdeu",3000 };
	Apartament ap2{ "200m^2","Buna ziua",200000 };
	RepoFile repo("test1.txt");
	repo.store(ap1);
	repo.store(ap2);
	assert(repo.exists(ap1.getStrada(), ap1.getSuprafata()) == true);
	assert(repo.exists(ap2.getStrada(), ap1.getSuprafata()) == false);
	assert(repo.exists(ap2.getStrada(), ap2.getSuprafata()) == true);
	repo.deleted(ap1);
	repo.deleted(ap2);
}

void testRepo() {
	testAddRepo();
	testDeleteRepo();
	testExistsRepo();
}

void tetsAddService() {
	RepoFile repo1("test2.txt");
	Service srv(repo1);
	srv.storeAp("suprafata1", "strada2", 300);
	srv.storeAp("suprafata2", "strada2", 200);
	assert(srv.getALlAp().size() == 2);
	try {
		srv.storeAp("suprafata1", "strada2", 1000);
		assert(false);
	}
	catch(RepoException&) {
		assert(true);
	}
}

void testDeleteService() {
	RepoFile repo1("test2.txt");
	Service srv(repo1);
	srv.delteAp("suprafata1", "strada2", 300);
	assert(srv.getALlAp().size() == 1);
	srv.delteAp("suprafata2", "strada2", 200);
	assert(srv.getALlAp().size() == 0);
	try {
		srv.delteAp("ceva1", "ceva2", 30);
		assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}
}

void testFiltresService() {
	RepoFile repo1("test2.txt");
	Service srv(repo1);
	srv.storeAp("50m^2", "Andrei Muresan", 200);
	srv.storeAp("60m^2", "Chintenii de sus", 500);
	srv.storeAp("55m^2", "Apuseni", 10000);
	vector<Apartament> filtre1 = srv.filtreByPrice(srv.getALlAp(), 200, 600);
	assert(filtre1.size() == 2);
	vector<Apartament> filtre2 = srv.filtreBySpace(srv.getALlAp(),"20m^2", "59m^2");
	assert(filtre2.size() == 2);
}

void tetsService() {
	tetsAddService();
	testDeleteService();
	testFiltresService();
}

void testall() {
	testDomain();
	testRepo();
	tetsService();
}