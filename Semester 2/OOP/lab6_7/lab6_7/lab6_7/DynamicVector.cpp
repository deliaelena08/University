
#include "DynamicVector.h"
#include <iostream>

using std::exception;

template <class Element>
DynamicVector<Element>:: DynamicVector() noexcept{
    lg = 0;
    cp = 10;
    elements = new Element[cp];
}

template <class Element>
DynamicVector<Element>::DynamicVector(const DynamicVector<Element>& ot) {
    lg = ot.lg;
    cp = ot.cp;
    elements = new Element[cp];
    for (int i = 0; i < lg; i++) {
        elements[i] = ot.elements[i];
    }
}

template <class Element>
DynamicVector<Element>::~DynamicVector() {
    if (elements != nullptr) {
        delete[] elements;
    }
}

template <class Element>
void DynamicVector<Element>::push_back(const Element& elm) {
    ensure_cap();
    elements[lg++] = elm;
}

template <class Element>
void DynamicVector<Element>::ensure_cap() noexcept{
    if (lg < cp) {
        return;
    }
    cp *= 2;
    Element* temp =elements;
    //for (int i = 0; i < lg; ++i) {
      //  temp[i] = elements[i];
    //}
    if (elements != nullptr) {
        delete[] elements;
    }
    elements = temp;
}

template <class Element>
bool DynamicVector<Element>::empty() const {
    if (lg == 0) {
        return true;
    }
    return false;
}

template <class Element>
MyIterator<Element> DynamicVector<Element>::begin() const {
    return elements;
}

template <class Element>
MyIterator<Element> DynamicVector<Element>::end() const {
    return elements + lg - 1;
}

template <class Element>
void DynamicVector<Element>::erase(const int& pos) {
    for (int i = pos; i < lg - 1; ++i) {
        elements[i] = elements[i + 1];
    }
    --lg;
}

template <class Element>
int DynamicVector<Element>::size() const noexcept {
    return lg;
}

template<class Element>
Element& DynamicVector<Element>::operator[](int poz) {
    if (poz<0 || poz>lg) {
        throw exception();
    }
    return elements[poz];
}

template<class Element>
void DynamicVector<Element>::copy(DynamicVector<Element>& ot) {
    ot.lg = 0;
    for (int i = 0; i < lg; ++i) {
        ot.push_back(elements[i]);
    }
}

template<class Element>
void DynamicVector<Element>::operator=(const DynamicVector& ot) {
    if (this == &ot) return *this;
    cp = ot.cp;
    lg = 0;
    for (int i = 0; i < ot.lg; ++i) {
        elements[lg++] = ot.elements[i];
    }
    return *this;
}

