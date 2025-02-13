
#include<assert.h>
#include "DynamicVector.h"
#include "teste.h"

void ::tests_domain()
{
	Product p1{ "ciocolata cu capsuni","dulciuri",4,"milka" };
	assert(p1.getname() == "ciocolata cu capsuni");
	assert(p1.getprice() == 4);
	assert(p1.gettype() == "dulciuri");
	assert(p1.getproductor() == "milka");

	p1.setname("ciocolata cu oreo");
	assert(p1.getname() == "ciocolata cu oreo");

	p1.setprice(5);
	assert(p1.getprice() == 5);

	p1.setproductor("Kandia");
	assert(p1.getproductor() == "Kandia");

	p1.settype("desert");
	assert(p1.gettype() == "desert");
}


void ::testaddrepo()
{

	RepositoryProducts testrepo;
	Product p1{ "biscuiti cu ciocolata", "dulciuri", 2, "Eugenia" };
	testrepo.store(p1);
	assert(testrepo.getallproducts().size() == 1);

	Product p2{ "chipsuri cu sare","snacks",7,"Lays" };
	testrepo.store(p2);
	assert(testrepo.getallproducts().size() == 2);

	Product p3{ "chipsuri cu sare", "snacks", 9, "Lays" };
	try {
		testrepo.store(p3);
		assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}
}

void ::testfindrepo()
{
	RepositoryProducts testrepo;
	Product p1{ "biscuiti cu ciocolata","dulciuri",2,"Eugenia" };
	testrepo.store(p1);
	Product p2{ "chipsuri cu sare","snacks",7,"Lays" };
	testrepo.store(p2);
	Product p3("chipsuri cu sare", "snacksuri", 9, "Chio");
	assert(testrepo.exists(p1));
	assert(!testrepo.exists(p3));
	Product p = testrepo.find("chipsuri cu sare", "Lays");
	assert(p.getprice() == 7);
	assert(p.getproductor() == "Lays");
	try {
		testrepo.find("ceva", "altceva");
		assert(false);
	}
	catch (RepoException& m)
	{
		assert(m.getErrorMessage() == "Produsul cu numele ceva si cu producatorul altceva nu exista");
	}
}

void ::testdeleterepo()
{
	RepositoryProducts testrepo;
	Product p1{ "biscuiti cu ciocolata","dulciuri",2,"Eugenia" };
	testrepo.store(p1);
	Product p2{ "chipsuri cu sare","snacks",7,"Lays" };
	testrepo.store(p2);
	Product p3("chipsuri cu sare", "snacksuri", 9, "Chio");
	testrepo.store(p3);
	Product p4("ciocolata", "dulciuri", 5, "poiana");
	assert(testrepo.findpoz(p4) == -1);
	testrepo.delete_product(p4);
	assert(testrepo.getallproducts().size() == 3);
	testrepo.delete_product(p2);
	assert(testrepo.getallproducts().size() == 2);
	assert(testrepo.findpoz(p3) == 1);
}

void ::testmodifyrepo() {
	RepositoryProducts testrepo;
	Product p1{ "biscuiti cu ciocolata","dulciuri",2,"Eugenia" };
	testrepo.store(p1);
	Product p2{ "chipsuri cu sare","snacks",7,"Lays" };
	testrepo.store(p2);
	Product p3("chipsuri cu sare", "snacksuri", 9, "Chio");
	testrepo.store(p3);
	Product p4("ciocolata", "dulciuri", 5, "poiana");
	try {
		testrepo.modify_product(p4, "cioco", "dulce", 7);
		assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}
	testrepo.modify_product(p2, "chipsuri cu branza", "snacksuri", 8);
	assert(p2.getname() == "chipsuri cu branza");
	assert(p2.gettype() == "snacksuri");
	assert(p2.getprice() == 8);
}

void ::testrepo()
{
	::testaddrepo();
	::testdeleterepo();
	::testfindrepo();
	::testmodifyrepo();
}



void ::testAddService() {
	RepositoryProducts repo;
	ProductValidator valid;
	ServiceProduct service{ repo,valid };

	service.addProduct("ciocolata cu visine", "dulciuri", 6, "milka");
	service.addProduct("ciocolata neagra cu caramel sarat", "dulciuri", 9, "heidi");
	service.addProduct("socitzi", "snacks", 7, "alka");

	assert(service.getAll().size() == 3);

	try {
		service.addProduct("socitzi", "snacks", 7, "alka");
		assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}

	try {
		service.addProduct("toortizi", "snacks", 4, "alka");
		assert(true);
	}
	catch (ExceptionValidator&) {
		assert(false);
	}

	try {
		service.addProduct("t", "chips", 8, "chio");
		assert(false);
	}
	catch (ExceptionValidator&) {
		assert(true);
	}

	try {
		service.addProduct("teddy bears", "", 5, "haribo");
		assert(false);
	}
	catch (ExceptionValidator&) {
		assert(true);
	}

	try {
		service.addProduct("teddy bears", "jeleuri", -6, "haribo");
		assert(false);
	}
	catch (ExceptionValidator&) {
		assert(true);
	}

	try {
		service.addProduct("paine graham", "paine", 2, "s");
		assert(false);
	}
	catch (ExceptionValidator&) {
		assert(true);
	}
}

void ::testDeleteService() {
	RepositoryProducts repo;
	ProductValidator valid;
	ServiceProduct service{ repo,valid };

	service.addProduct("ciocolata cu visine", "dulciuri", 6, "milka");
	service.addProduct("ciocolata neagra cu caramel sarat", "dulciuri", 9, "heidi");
	service.addProduct("socitzi", "snacks", 7, "alka");
	try {
		service.deleteProduct("socitzi", "lotto");
		assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}
	service.deleteProduct("socitzi", "alka");
	assert(repo.getallproducts().size() == 2);

}

void ::add_by_default_test() {
	RepositoryProducts repo;
	ProductValidator valid;
	ServiceProduct service{ repo,valid };

	service.addProduct("ciocolata cu visine", "dulciuri", 6, "milka");
	service.addProduct("ciocolata neagra cu caramel sarat", "dulciuri", 9, "heidi");
	service.addProduct("socitzi", "snacks", 7, "alka");
}

void ::testModifyService() {
	RepositoryProducts repo;
	ProductValidator valid;
	ServiceProduct service{ repo,valid };

	service.addProduct("ciocolata cu visine", "dulciuri", 6, "milka");
	service.addProduct("ciocolata neagra cu caramel sarat", "dulciuri", 9, "heidi");
	service.addProduct("socitzi", "snacks", 7, "alka");
	service.modifyProduct("socitzi", "alka", "tortizi", "rontaieli", 4);
	Product p = repo.find("tortizi", "alka");
	assert(p.gettype() == "rontaieli");
	assert(p.getprice() == 4);

	try {
		service.modifyProduct("ciocolata", "milka", "ciocolata cu oreo", "dulce", 7);
		assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}

}

void ::testfindService() {
	RepositoryProducts repo;
	ProductValidator valid;
	ServiceProduct service{ repo,valid };

	service.addProduct("ciocolata cu visine", "dulciuri", 6, "milka");
	service.addProduct("ciocolata neagra cu caramel sarat", "dulciuri", 9, "heidi");
	service.addProduct("socitzi", "snacks", 7, "alka");
	Product p1{ "tortizi","snacks",5,"alka" };

	try {
		service.find_product(p1.getname(), p1.getproductor());
		assert(false);
	}
	catch (RepoException&) {
		assert(true);
	}

	try {
		service.find_product("socitzi", "alka");
		assert(true);
	}
	catch (RepoException&) {
		assert(false);
	}

}

void ::testfiltrebyname() {
	RepositoryProducts repo;
	ProductValidator valid;
	ServiceProduct service{ repo,valid };

	service.addProduct("ciocolata cu visine", "dulciuri", 6, "milka");
	service.addProduct("ciocolata cu visine", "dulciuri", 6, "poiana");
	service.addProduct("ciocolata neagra cu caramel sarat", "dulciuri", 9, "heidi");
	service.addProduct("socitzi", "snacks", 7, "alka");
	DynamicVector<Product> testlist = service.filtre_by_name("ciocolata cu visine");
	assert(testlist.size() == 2);
	assert(testlist[0].getproductor() == "milka");
	assert(testlist[1].getproductor() == "poiana");
}


void ::testfiltrebyprice() {
	RepositoryProducts repo;
	ProductValidator valid;
	ServiceProduct service{ repo,valid };

	service.addProduct("ciocolata cu visine", "dulciuri", 6, "milka");
	service.addProduct("ciocolata cu visine", "dulciuri", 6, "poiana");
	service.addProduct("ciocolata neagra cu caramel sarat", "dulciuri", 9, "heidi");
	service.addProduct("socitzi", "snacks", 7, "alka");
	DynamicVector<Product> testlist = service.filtre_by_price(7);
	assert(testlist.size() == 3);
	assert(testlist[0].getproductor() == "milka");
	assert(testlist[1].getproductor() == "poiana");
	assert(testlist[2].getname() == "socitzi");
}

void ::testfiltrebyproductor() {
	RepositoryProducts repo;
	ProductValidator valid;
	ServiceProduct service{ repo,valid };

	service.addProduct("ciocolata cu visine", "dulciuri", 6, "milka");
	service.addProduct("ciocolata cu visine", "dulciuri", 6, "poiana");
	service.addProduct("ciocolata neagra cu caramel sarat", "dulciuri", 9, "heidi");
	service.addProduct("ciocolata cu oreo", "dulciuri", 6, "milka");
	service.addProduct("ciocolata cu capsuni", "dulciuri", 6, "milka");
	service.addProduct("socitzi", "snacks", 7, "alka");
	DynamicVector<Product> testlist = service.filtre_by_productor("milka");
	assert(testlist.size() == 3);
	assert(testlist[0].getname() == "ciocolata cu visine");
	assert(testlist[1].getname() == "ciocolata cu oreo");
	assert(testlist[2].getname() == "ciocolata cu capsuni");
}

void ::testsortbyname() {
	RepositoryProducts repo;
	ProductValidator valid;
	ServiceProduct service{ repo,valid };

	service.addProduct("amandina", "prajitura", 10, "auchan");
	service.addProduct("biscuiti simpli", "dulciuri", 4, "croco");
	service.addProduct("mere", "fructe", 4, "pomme");
	service.addProduct("iaurt cu visine", "lactate", 2, "danone");
	service.addProduct("biscuiti cu ciocolata", "dulciuri", 2, "eugenia");

	DynamicVector<Product> testlist = service.sort_by_name();
	assert(testlist[0].getname() == "amandina");
	assert(testlist[2].getproductor() == "croco");
	assert(testlist[testlist.size() - 1].getname() == "mere");

}

void ::testsortbyprice() {
	RepositoryProducts repo;
	ProductValidator valid;
	ServiceProduct service{ repo,valid };

	service.addProduct("mere", "fructe", 3, "pomme");
	service.addProduct("amandina", "prajitura", 10, "auchan");
	service.addProduct("biscuiti simpli", "dulciuri", 5, "croco");
	service.addProduct("biscuiti cu ciocolata", "dulciuri", 2, "eugenia");
	service.addProduct("iaurt cu visine", "lactate", 3, "danone");


	DynamicVector<Product> testlist = service.sort_by_price();
	assert(testlist[0].getname() == "biscuiti cu ciocolata");
	assert(testlist[2].getproductor() == "danone");
	assert(testlist[testlist.size() - 1].getname() == "amandina");
	assert(testlist[3].getprice() == 5);
	assert(testlist[1].gettype() == "fructe");

}

void ::testsortbynameandprice() {
	RepositoryProducts repo;
	ProductValidator valid;
	ServiceProduct service{ repo,valid };

	service.addProduct("mere", "fructe", 3, "pomme");
	service.addProduct("amandina", "prajitura", 10, "auchan");
	service.addProduct("biscuiti simpli", "dulciuri", 5, "croco");
	service.addProduct("biscuiti cu ciocolata", "dulciuri", 2, "eugenia");
	service.addProduct("amandina", "prajitura", 7, "lidl");
	service.addProduct("iaurt cu visine", "lactate", 3, "danone");
	service.addProduct("mere", "fructe", 4, "homegarden");

	DynamicVector<Product> testlist = service.sort_by_name_and_price();
	assert(testlist[0].getname() == "amandina");
	assert(testlist[1].getprice() == 10);
	assert(testlist[2].getproductor() == "eugenia");
	assert(testlist[3].getprice() == 5);
	assert(testlist[testlist.size() - 1].getprice() == 4);
}

void ::testservice() {
	::testAddService();
	::testDeleteService();
	::testModifyService();
	::testfindService();
	::testfiltrebyname();
	::testfiltrebyprice();
	::testfiltrebyproductor();
	::testsortbyname();
	::testsortbyprice();
	::testsortbynameandprice();
}

void testall()
{
	::tests_domain();
	::testrepo();
	::testservice();
}