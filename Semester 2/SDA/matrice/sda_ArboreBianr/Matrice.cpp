#include "Matrice.h"
#include <exception>
#include <iostream>
#include "Iterator.h"

using namespace std;

Nod::Nod(Triple e, PNod st, PNod dr) {
    //Theta(1)
    //Constructor nod
    this->el = e;
    this->st = st;
    this->dr = dr;
}

Triple Nod::element() {
    //Theta(1)
    //Obtinem elementul unui nod
    return el;
}

PNod Nod::stanga() {
    //Theta(1)
    //Obtinem nodul stang din arbore
    return st;
}

PNod Nod::dreapta() {
    //Theta(1)
    //Obtinem nodul drept din arbore
    return dr;
}

int Matrice::comparisonFunction(const Triple& firstElement, const Triple& secondElement) const {
    //Theta(1)
    //Functia de comparare in functie de linie ,iar daca acestea sunt egale , se ia in functie de coloane
    int aux = firstElement.line - secondElement.line;
    if (aux < 0)
        return -1;
    else if (aux > 0)
        return 1;
    else {
        aux = firstElement.column - secondElement.column;
        if (aux < 0)
            return -1;
        else if (aux > 0)
            return 1;
        else
            return 0;
    }
}

PNod Matrice::search(int line, int column) const {
    //O(h), h-inaltimea arborelui
    //Cauta elemnetul dupa o linie si o coloana data
    if (line < 0 || line >= numLines || column < 0 || column >= numCols)
        return nullptr;
    Triple element;
    element.line = line;
    element.column = column;
    auto current = rad;
    while (current != nullptr) {
        if (comparisonFunction(current->el, element) == 0)
            return current;
        else if (comparisonFunction(current->el, element) < 0)
            current = current->dr;
        else
            current = current->st;
    }
    return nullptr;
}

Matrice::Matrice(int m, int n) {
    //Theta(1)
    //Constructor matrice
    numLines = m;
    numCols = n;
    rad = nullptr;
}

PNod Matrice::adaugaNod(PNod nod, Triple el) {
    //O(h), h-inaltimea arborelui
    //Adaugam un nod in arbore in functie de valoarea sa
    if (nod == nullptr) {
        nod = new Nod(el, nullptr, nullptr);
    } else {
        if ((comparisonFunction(nod->el, el)) > 0)
            nod->st = adaugaNod(nod->st, el);
        else
            nod->dr = adaugaNod(nod->dr, el);
    }
    return nod;
}

void Matrice::distrugeNod(PNod nod) {
    //O(h), h-inaltimea arborelui
    //Distruge un nod dat
    if (nod != nullptr) {
        distrugeNod(nod->dr);
        distrugeNod(nod->st);
        delete nod;
    }
}

int Matrice::nrLinii() const {
    //Theta(1)
    //Returneaza numarul de linii
    return numLines;
}

int Matrice::nrColoane() const {
    //Theta(1)
    //Returneaza numarul de coloane
    return numCols;
}

TElem Matrice::element(int i, int j) const {
    //O(h), h-inaltimea arborelui
    //Obtinem elementul de pe o linie si o coloana data
    if (i < 0 || i >= numLines || j < 0 || j >= numCols)
        throw exception();
    auto node = search(i, j);
    if (node != nullptr)
        return node->el.value;
    return NULL_TELEMENT;
}


TElem Matrice::modifica(int i, int j, TElem e) {
    //O(h), h-inaltimea arborelui
    //Modifica un element in functie de linia si coloana data
    if (i < 0 || i >= numLines || j < 0 || j >= numCols)
        throw exception();

    auto node = search(i, j);
    if (node != nullptr) {
        TElem oldValue = node->el.value;
        //daca adauga un element null
        if (e == NULL_TELEMENT) {
            sterge(i, j);
        } else {
            node->el.value = e;
        }
        return oldValue;
    } else if (e != NULL_TELEMENT) {
        Triple el{i, j, e};
        rad = adaugaNod(rad, el); //adaugam nodul de la radacina
    }
    return NULL_TELEMENT;
}


Iterator Matrice::iterator(int linie) const {
    return Iterator(*this,linie);
}

PNod Matrice::minValueNode(PNod node) {
    //Obtinem nodul minim din cauza functiei de comparatie
    PNod current = node;
    while (current && current->st != nullptr)
        current = current->st;
    return current;
}

PNod Matrice::stergeNod(PNod root, Triple key) {
    //Sterge nodul
    //O(h) in functie de cat de mic e elementul
    if (root == nullptr)
        return root;

    if (comparisonFunction(key, root->el) < 0)
        root->st = stergeNod(root->st, key);
    else if (comparisonFunction(key, root->el) > 0)
        root->dr = stergeNod(root->dr, key);
    else {
        if (root->st == nullptr) {
            PNod temp = root->dr;
            delete root;
            return temp;
        } else if (root->dr == nullptr) {
            PNod temp = root->st;
            delete root;
            return temp;
        }

        PNod temp = minValueNode(root->dr);
        root->el = temp->el;
        root->dr = stergeNod(root->dr, temp->el);
    }
    return root;
}

void Matrice::sterge(int i, int j) {
    Triple elementToDelete{i, j, 0};
    //sterge nodul
    rad = stergeNod(rad, elementToDelete);
}
