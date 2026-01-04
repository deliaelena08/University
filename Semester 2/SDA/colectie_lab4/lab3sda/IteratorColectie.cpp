#include "IteratorColectie.h"
#include "Colectie.h"
#include <exception>


IteratorColectie::IteratorColectie(const Colectie& c): col(c) {
	index=c.prim;
}

void IteratorColectie::prim() {
	//Theta(1)
    index=col.prim;
}


void IteratorColectie::urmator() {
    //Theta(1)
	if(valid()) {
        if(frecventa<col.frecventa[index])
            frecventa++;
        else{
            index = col.urm[index];
            frecventa=1;
        }

    }
    else
        throw std::exception();
}


bool IteratorColectie::valid() const {
	//Theta(1)
	return index!=-1;
}



TElem IteratorColectie::element() const {
	if(!valid())
        throw std::exception();
    return col.array[index];
}
