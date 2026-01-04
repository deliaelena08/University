#include"Tests.h"
#include<assert.h>

void testDomain(){
	Melodie m1{ 1,"love","justin timberlake",6 };
	assert(m1.getArtist() == "justin timberlake");
	assert(m1.getId() == 1);
	assert(m1.getTitle() == "love");
	assert(m1.getRank() == 6);

	m1.setArtist("Ariana Grande");
	assert(m1.getArtist() == "Ariana Grande");

	m1.setId(2);
	assert(m1.getId() == 2);

	m1.setTitle("love me harder");
	assert(m1.getTitle() == "love me harder");

	m1.setRank(4);
	assert(m1.getRank() == 4);
}

void storeRepo() {
	RepoFile repo{ "test1.txt" };
	Melodie m1{ 1,"love","justin timberlake",6 };
	Melodie m2{ 2,"simfonia 2","mozart",10 };
	Melodie m3{ 2,"shameless","Camila Cabelo",8 };
	repo.storeMel(m1);
	repo.storeMel(m2);
	assert(repo.getAll().size() == 2);
	try {
		repo.storeMel(m3);
		assert(false);
	}
	catch(RepoException&) {
		assert(true);
	}
}

void deleteRepo() {
	RepoFile repo{ "test1.txt" };
	Melodie m1{ 1,"love","justin timberlake",6 };
	Melodie m2{ 2,"simfonia 2","mozart",10 };
	Melodie m3{ 4,"shameless","Camila Cabelo",8 };
	repo.storeMel(m1);
	repo.storeMel(m2);
	repo.storeMel(m3);
	assert(repo.getAll().size() == 3);

	repo.deleteMel(m2);
	assert(repo.getAll().size() == 2);
	try {
		repo.deleteMel(m2);
		assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}
}

void modifyRepo() {
	RepoFile repo{ "test1.txt" };
	Melodie m1{ 1,"love","justin timberlake",6 };
	Melodie m2{ 2,"simfonia 2","mozart",10 };
	Melodie m3{ 4,"shameless","Camila Cabelo",8 };
	repo.storeMel(m1);
	repo.storeMel(m2);
	repo.modifyMel(m1, "river", 9);
	vector<Melodie> melodii = repo.getAll();
	assert(melodii[0].getRank() == 9);
	assert(melodii[0].getTitle() == "river");
	try {
		repo.modifyMel(m3, "ceva", 3);
		assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}
}

void existsRepo(){
	RepoFile repo{ "test1.txt" };
	Melodie m1{ 1,"love","justin timberlake",6 };
	Melodie m2{ 2,"simfonia 2","mozart",10 };
	Melodie m3{ 4,"shameless","Camila Cabelo",8 };
	repo.storeMel(m1);
	repo.storeMel(m2);
	assert(repo.exists(1) == true);
	assert(repo.exists(4) == false);
	assert(repo.exists(2) == true);
	assert(repo.exists(3) == false);

}

void testRepo() {
	storeRepo();
	deleteRepo();
	modifyRepo();
	existsRepo();
}

void storeService() {
	RepoFile repo{ "test2.txt" };
	Validator valid;
	Service service{ repo,valid };
	service.addMelody(1, "Ariana Grande", "Everyday", 8);
	service.addMelody(2, "The Weekend", "hills", 9);
	assert(service.getAllMelodies().size() == 2);

	try {
		service.addMelody(3, "", "ceva", 4);
		assert(false);
	}
	catch (ExceptionValid&) {
		assert(true);
	}

	try {
		service.addMelody(3, "ceva", "", 4);
		assert(false);
	}
	catch (ExceptionValid&) {
		assert(true);
	}

	try {
		service.addMelody(3, "ok", "ok", -1);
		assert(false);
	}
	catch (ExceptionValid&) {
		assert(true);
	}

	try {
		service.addMelody(2 ,"cev", "ceva", 4);
		assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}
}

void deleteService() {
	RepoFile repo{ "test2.txt" };
	Validator valid;
	Service service{ repo,valid };
	service.addMelody(1, "Ariana Grande", "Everyday", 8);
	service.addMelody(2, "The Weekend", "hills", 9);
	assert(service.getAllMelodies().size() == 2);
	service.deleteMelody(1);
	assert(service.getAllMelodies().size() == 1);

}

void modifyService() {
	RepoFile repo{ "test2.txt" };
	Validator valid;
	Service service{ repo,valid };
	service.addMelody(1, "Ariana Grande", "Everyday", 8);
	service.addMelody(2, "The Weekend", "hills", 9);
	service.modifyMelody(1, "Truth", 3);
	vector<Melodie> melodies = service.getAllMelodies();
	assert(melodies[0].getTitle() == "Truth");
	assert(melodies[0].getRank() == 3);
	

}

void sortedService(){
	RepoFile repo{ "test2.txt" };
	Validator valid;
	Service service{ repo,valid };
	service.addMelody(1, "Ariana Grande", "Everyday", 8);
	service.addMelody(2, "The Weekend", "hills", 9);
	service.addMelody(3, "The Weekend", "i feel it", 9);
	service.addMelody(6, "The Weekend", "mouth to flame", 9);
	vector<Melodie> melodii = service.sorted();
	assert(melodii[0].getArtist() == "Ariana Grande");
	assert(melodii[1].getRank() == 9);
	assert(melodii[2].getId() == 3);
	assert(melodii[3].getTitle() == "mouth to flame");
}

void filtredService() {
	RepoFile repo{ "test2.txt" };
	Validator valid;
	Service service{ repo,valid };
	service.addMelody(1, "Ariana Grande", "Everyday", 8);
	service.addMelody(2, "The Weekend", "hills", 9);
	service.addMelody(3, "The Weekend", "i feel it", 9);
	service.addMelody(6, "The Weekend", "mouth to flame", 9);
	vector<Melodie> melodii = service.filtre("Ariana Grande");
	assert(melodii.size() == 1);
	vector<Melodie> melodii2 = service.filtre("The Weekend");
	assert(melodii2.size() == 3);

}
void testService() {
	storeService();
	deleteService();
	modifyService();
	sortedService();
	filtredService();
}

void testAll() {
	testDomain();
	testRepo();
	testService();
}