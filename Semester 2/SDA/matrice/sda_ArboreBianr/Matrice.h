#pragma once

typedef int TElem;

class Iterator;

#define NULL_TELEMENT 0
struct Triple {
    int line;
    int column;
    TElem value;
};

class Nod;
typedef Nod *PNod;

class Nod
{
public:
    friend class Matrice;
    //constructor
    Nod(Triple el, PNod st, PNod dr);
    Triple element();
    PNod stanga();
    PNod dreapta();

private:
    Triple el;
    PNod st, dr;
};

class Matrice {
    friend class Iterator;
private:
    PNod rad;
    PNod adaugaNod(PNod nod,Triple el);
    void distrugeNod(PNod nod);
    PNod search(int line, int column) const;
    int comparisonFunction(const Triple& firstElement, const Triple& secondElement) const;
    PNod minValueNode(PNod node);
    PNod stergeNod(PNod root, Triple key);
    void sterge(int i, int j);
    int numLines;
    int numCols;
public:

	//constructor
	//se arunca exceptie daca nrLinii<=0 sau nrColoane<=0
	Matrice(int nrLinii, int nrColoane);


	//destructor
	~Matrice(){
        distrugeNod(rad);
    };

	//returnare element de pe o linie si o coloana
	//se arunca exceptie daca (i,j) nu e pozitie valida in Matrice
	//indicii se considera incepand de la 0
	TElem element(int i, int j) const;


	// returnare numar linii
	int nrLinii() const;

	// returnare numar coloane
	int nrColoane() const;


	// modificare element de pe o linie si o coloana si returnarea vechii valori
	// se arunca exceptie daca (i,j) nu e o pozitie valida in Matrice
	TElem modifica(int i, int j, TElem);

    Iterator iterator(int linie) const;

};







