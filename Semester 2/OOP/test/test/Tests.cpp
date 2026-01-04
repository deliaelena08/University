#include "Tests.h"
#include<assert.h>

void testDomain(){
	Elev e{ 12345,"maria","nr2" };
	assert(e.getNrMatricol() == 12345);
	assert(e.getnume() == "maria");
	assert(e.getScoala() == "nr2");
}

void testRepo() {
	RepoFile repo("testrepo.txt");
	Elev e{ 12345,"maria","nr2" };
	repo.storeEl(e);
	assert(repo.getAll().size() == 1);
	assert(repo.exists(12345) == true);
	
}

void testService() {
	RepoFile repo("testservice.txt");
	Service srv(repo);
	vector<Elev> elevi = srv.getAllElevi();
	assert(srv.getAllElevi().size() == 2);
	Atelier at( "fotografie" );
	at.addElev(elevi[0]);
	assert(at.getELevi().size() == 1);
	at.addElev(elevi[1]);
	assert(at.getELevi().size() == 2);
	vector<Elev> sorted = srv.sortNyName(elevi);
	assert(sorted[0].getnume() == elevi[1].getnume());
}

void testall() {
	testDomain();
	testRepo();
	testService();
}