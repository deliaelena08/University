#pragma once

#include "MDO.h"


class IteratorMDO {
    friend class MDO;
private:

    //constructorul primeste o referinta catre Container
    //iteratorul va referi primul element din container
    IteratorMDO(const MDO& dictionar);

    //contine o referinta catre containerul pe care il itereaza
    const MDO& dict;
    int indexH;
    int indexK;
    Relatie rel;

    vector<TCheie> chei;
    TElem getElem() const;
    vector<TCheie> getChei();
    void isSetToNext();

public:

    //reseteaza pozitia iteratorului la inceputul containerului
    void prim();

    //muta iteratorul in container
    // arunca exceptie daca iteratorul nu e valid
    void urmator();

    //verifica daca iteratorul e valid (indica un element al containerului)
    bool valid() const;

    //returneaza valoarea elementului din container referit de iterator
    //arunca exceptie daca iteratorul nu e valid
    TElem element() const;
};

