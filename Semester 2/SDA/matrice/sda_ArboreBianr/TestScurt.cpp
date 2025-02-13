#include "TestScurt.h"
#include <assert.h>
#include "Matrice.h"
#include "Iterator.h"
#include <iostream>

using namespace std;

void testAll() { //apelam fiecare functie sa vedem daca exista
	Matrice m(4,4);
	assert(m.nrLinii() == 4);
	assert(m.nrColoane() == 4);
	//adaug niste elemente
	m.modifica(1,1,5);
	assert(m.element(1,1) == 5);
	m.modifica(1,1,6);
	assert(m.element(1,2) == NULL_TELEMENT);
}

void testIterator(){
    Matrice m(4,4);
    m.modifica(1,1,17);
    m.modifica(1,2,27);
    m.modifica(2,1,10);
    m.modifica(2,2,15);
    m.modifica(3,3,11);
    Iterator it(m,1);
    it.next();
    assert(it.valid()==1);
    it.next();
    assert(it.getCurrent()==27);
    it.next();
    it.next();
    assert(it.valid()==0);
    Iterator it2(m,3);
    it2.next();
    it2.next();
    it2.next();
    assert(it2.getCurrent()==11);
}
