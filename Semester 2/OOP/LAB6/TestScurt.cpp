#include <assert.h>

#include "MDO.h"
#include "IteratorMDO.h"

#include <exception>
#include <vector>
#include<iostream>
using namespace std;

bool relatie1(TCheie cheie1, TCheie cheie2) {
    if (cheie1 <= cheie2) {
        return true;
    }
    else {
        return false;
    }
}

void testAll() {
    MDO dictOrd = MDO(relatie1);
    assert(dictOrd.dim() == 0);
    assert(dictOrd.vid());
    dictOrd.adauga(1, 2);
    dictOrd.adauga(1, 3);
    assert(dictOrd.dim() == 2);
    assert(!dictOrd.vid());
    vector<TValoare> v = dictOrd.cauta(1);
    assert(v.size() == 2);
    v = dictOrd.cauta(3);
    assert(v.size() == 0);
    IteratorMDO it = dictOrd.iterator();
    it.prim();
    while (it.valid()) {
        TElem e = it.element();
        it.urmator();
    }
    assert(dictOrd.sterge(1, 2) == true);
    assert(dictOrd.sterge(1, 3) == true);
    assert(dictOrd.sterge(2, 1) == false);
    assert(dictOrd.vid());
}
int adaugaInexistent( const MDO& mdo){
    //Theta(n*m)
    MDO dictOrd = MDO(relatie1);
    IteratorMDO it1 = mdo.iterator();
    it1.prim();
    while (it1.valid()) {
        bool ok = false;
        TElem el1 = it1.element();
        IteratorMDO it2 = dictOrd.iterator();
        it2.prim();
        while (it2.valid()) {
            TElem e = it2.element();
            if (e.first == el1.first && el1.second == e.second) {
                ok = true;
                break;
            }
            it2.urmator();
        }
        if (!ok) {
            dictOrd.adauga(el1.first, el1.second);
        }
        it1.urmator();
    }
    return dictOrd.dim();
}

void testFunctionalitate(){
    MDO dictOrd = MDO(relatie1);
    assert(dictOrd.dim()==0);
    for (int i = 0; i <50 ; i++) {
        dictOrd.adauga(i, i+1);
    }
    MDO d(relatie1);
    for (int i = 0; i <50 ; i++) {
        d.adauga(i, i);
        d.adauga(i-1, i+1);
    }
    assert(adaugaInexistent(d)==99);
}