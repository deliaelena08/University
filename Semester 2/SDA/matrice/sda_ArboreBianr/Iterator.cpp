#include"Iterator.h"
#include <exception>
 void Iterator::first() {
    index=0;
}

void Iterator::next(){
    if(!valid())
        throw std::exception();
    index++;
}

bool Iterator::valid() const {
    return index>=0 && index<matrice.nrColoane();
}

TElem Iterator::getCurrent() const {
    if(!valid()){
        throw std::exception();
    }
    return matrice.element(linie,index);
}