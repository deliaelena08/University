#include<assert.h>
#include "teste.h"
void testDomain() {
	Tractor t1{ 1,"tractor1","masina mare",8 };
	assert(t1.getId()==1);
	assert(t1.getName() == "tractor1");
	assert(t1.getType() == "masina mare");
	assert(t1.getWheels() == 8);
	t1.setId(0);
	assert(t1.getId() == 0);

	t1.setName("tractoras");
	assert(t1.getName() == "tractoras");

	t1.setType("tractor");
	assert(t1.getType() == "tractor");

	t1.setWheels(4);
	assert(t1.getWheels() == 4);
}

void testAddRepo() {
	RepoFile testrepo("test.txt");
	Tractor t1{ 1,"tractor1","masina mare",8 };
	testrepo.store(t1);
	assert(testrepo.getAll().size() == 1);

	Tractor t2{ 2,"tractor2","masina",4 };
	testrepo.store(t2);
	assert(testrepo.getAll().size() == 2);

	Tractor t3{ 1,"tractor3","utilaj",6 };
	try {
		testrepo.store(t3);
		assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}

}

void testExistRepo() {
	RepoFile testrepo("test.txt");
	Tractor t1{ 1,"tractor1","masina mare",8 };
	testrepo.store(t1);
	assert(testrepo.exist(1)==true);
	Tractor t3{ 2,"tractor3","utilaj",6 };
	assert(testrepo.exist(2) == false);
}

void testModifyRepo() {
	RepoFile testrepo("test.txt");
	Tractor t1{ 1,"tractor1","masina mare",8 };
	testrepo.store(t1);
	testrepo.modifyTractor(t1, 6);
	assert(t1.getWheels() == 6);

	Tractor t3{ 2,"tractor3","utilaj",6 };
	try {
		testrepo.modifyTractor(t3, 7);
		assert(false);
	} 
	catch(RepoException&){
		assert(true);
	}

}

void testAddService() {
	RepoFile repo{ "test2.txt" };
	Validator valid;
	Service service{ repo,valid };
	service.addTractor(1, "tractor1", "tip1", 4);
	service.addTractor(2, "tractor2", "tip2", 8);
	vector<Tractor> tractors = service.getAllTractors();
	assert(tractors[1].getId() == 2);
	try {
		service.addTractor(4, "tractor1", "tip1", 7);
		assert(false);
	}
	catch (ExceptionValid&) {
		assert(true);
	}
	try {
		service.addTractor(4, "", "tip1", 4);
		assert(false);
	}
	catch (ExceptionValid&) {
		assert(true);
	}
	try {
		service.addTractor(4, "tractor1", "", 6);
		assert(false);
	}
	catch (ExceptionValid&) {
		assert(true);
	}
	try {
		service.addTractor(2, "tractor1", "tip1", 4);
		assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}
}

void testModifyService() {
	RepoFile repo{ "test2.txt" };
	Validator valid;
	Service service{ repo,valid };
	service.addTractor(1, "tractor1", "tip1", 4);
	service.addTractor(2, "tractor2", "tip2", 8);
	service.modifyTractors(1, 6);
	vector<Tractor> tractors = service.getAllTractors();
	assert(tractors[0].getWheels() == 6);
	try {
		service.modifyTractors(3, 7);
		assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}
}

void testSorted() {
	RepoFile repo{ "test2.txt" };
	Validator valid;
	Service service{ repo,valid };
	service.addTractor(1, "masina", "tip1", 4);
	service.addTractor(2, "audi", "tip2", 8);
	service.addTractor(3, "bormasina", "tip3", 6);
	vector<Tractor> tractors = service.sorted();
	assert(tractors[0].getId() == 2);
	assert(tractors[2].getWheels() == 4);
}

void testFiltred() {
	RepoFile repo{ "test2.txt" };
	Validator valid;
	Service service{ repo,valid };
	service.addTractor(1, "masina", "rosie", 4);
	service.addTractor(2, "audi", "verde", 8);
	service.addTractor(3, "bormasina", "rosie", 6);
	vector<Tractor> tractors = service.filtred("rosie");
	assert(tractors[0].getWheels() == 4);
}

void testRepo() {
	testAddRepo();
	testExistRepo();
	testModifyRepo();
}

void testService() {
	testAddService();
	testModifyService();
	testSorted();
	testFiltred();
}

void testall() {
	testDomain();
	testRepo();
}