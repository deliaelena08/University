#include "Matrice.h"

#include <exception>
#include <iostream>

using namespace std;


//teta(dim)
void Matrice::redim() {
	//alocam un spatiu de capacitate dubla
	Triplet* eNou = new Triplet[2 * cp];

	//copiem vechile valori in noua zona
	for (int i = 0; i < dim; i++)
	{
		eNou[i] = v[i];
	}

	//dublam capacitatea
	cp = 2 * cp;

	//eliberam vechea zona
	delete[] v;

	//vectorul indica spre noua zona
	v = eNou;

}

//teta(1)
Matrice::Matrice(int m, int n)  {
	//se arunca exceptie daca nr de linii sau de coloane este invalid
	if (m <= 0 || n <= 0) {
		throw "Matricea nu poate fi vida";
	}

	nr_Linii = m;
	nr_Coloane = n;


	cp = 5;
	dim = 0;
	v = new Triplet[cp];
	
}

//teta(1)
Matrice::~Matrice() {
	//eliberam zona de memorie alocata vectorului
	delete[] v;
}


//teta(1)
int Matrice::nrLinii() const {
	/*
	returneaza numarul de linii al matricei
	return: int
	*/
	return nr_Linii;
}


//teta(1)
int Matrice::nrColoane() const {
	/*
	returneaza numarul de coloane al matricei
	return: int
	*/
	return nr_Coloane;
}



/*
Caz Favorabil: teta(1)
Caz Defavorabil: teta(dim)
Caz Mediu: teta(dim)
Overall: O(dim)
*/
TElem Matrice::element(int i, int j) const {
	//se arunca exceptie daca (i,j) nu e pozitie valida in Matrice
	if (i >= nrLinii() || i < 0 || j >= nrColoane() || j < 0) {
		throw exception();
		return NULL_TELEMENT;
	}
		
	//Cauta elemntul printre triplete
	for (int k = 0; k < dim; k++) {
		//const Triplet& triplet = v[k];
		if (v[k].linie == i && v[k].coloana == j)
			return v[k].valoare;
	}

	//daca nu se gaseste printre triplete atunci elemtul este 0 
	return NULL_TELEMENT;
}



/*
Caz Favorabil: teta(1)
Caz Defavorabil: teta(dim)
Caz Mediu: teta(dim)
Overall: O(dim)
*/
TElem Matrice::modifica(int i, int j, TElem e) {
	//se arunca exceptie daca (i,j) nu e pozitie valida in Matrice
	 
	
	if (i >= nrLinii() || i < 0 || j >= nrColoane() || j < 0) {
		throw exception();
		return NULL_TELEMENT;
	}

	//daca valoarea este 0, trebuie sa eliminam tripletul din vector
	if (e == NULL_TELEMENT) {
		for (int k = 0; k < dim; k++)
		{
			if (v[k].linie == i && v[k].coloana == j)
			{
				TElem vecheaValoare = v[k].valoare;

				for (int m = k; m < dim - 1; m++)
					v[m] = v[m + 1];
				dim--;
				return vecheaValoare;
			}
		}
		//daca nu l-am gasit in triplete, atunci elementul este 0
		return NULL_TELEMENT;
	}
	
	//daca valoarea este diferita de 0
	//cautam printre triplete 
	for (int k = 0; k < dim; k++)
		if (v[k].linie == i && v[k].coloana == j) {
			TElem vecheaValoare = v[k].valoare;
			v[k].valoare = e;  //daca il gasim ii schimbam valoarea
			return vecheaValoare;
		}


	//daca nu il gasim, adaugam un nou triplet
	//daca s-a atins capacitatea maxima, redimensionam
    if (dim >= cp)
			redim();

	int k = dim;
	while (k > 0 && (i < v[k - 1].linie || (i == v[k - 1].linie && j < v[k - 1].coloana)))
	{
		v[k] = v[k - 1];
		k--;
	}
	 
	//punem noul element pe pozitia corecta in vector si cresc dimensiunea
    v[k] = { i, j, e };
	dim++;
	return NULL_TELEMENT;
}


