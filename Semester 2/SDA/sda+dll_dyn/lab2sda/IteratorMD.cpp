#include "IteratorMD.h"
#include "MD.h"
#include<exception>

using namespace std;

IteratorMD::IteratorMD(const MD& _md): md(_md) {
    //Theta(1)
	index=md.prim;
}

TElem IteratorMD::element() const{
    //Theta(1)
    if(!valid())
        throw exception();
	return md.array[index];
}

bool IteratorMD::valid() const {
    //Theta(1)
	return index!=-1;
}

void IteratorMD::urmator() {
    //Theta(1)
	if(valid())
        index=md.urm[index];
    else
        throw exception();
}

void IteratorMD::prim() {
    //Theta(1)
	index=md.prim;
}

