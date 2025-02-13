#include "Colectie.h"
#include "IteratorColectie.h"
#include <exception>
#include <iostream>

using namespace std;


Colectie::Colectie() {
	cp=10;
    array=new TElem[cp];
    frecventa=new int[cp];
    urm=new int[cp];
    prim=-1;
    size=0;
    initSpatiuLiber(0);
}

void Colectie::initSpatiuLiber(int index) {
    //Theta(cp)
    for(int i=index;i<cp-1;i++){
        urm[i]=i+1;
        frecventa[i]=0;
    }
    frecventa[cp-1]=0;
    urm[cp-1]=-1;
    primSpLiber=index;
}

int Colectie::aloca() {
    //Theta(1)
    int i=primSpLiber;
    primSpLiber=urm[primSpLiber];
    return i;
}

void Colectie::dealoca(int i) {
    //Theta(1)
    urm[i]=primSpLiber;
    primSpLiber=i;
}

int Colectie::creeazaNod(TElem e) {
    //O(n)
    if(primSpLiber==-1){
        TElem* aux;
        int *l1,*l2;
        l1=new int[cp*2];
        l2=new int[cp*2];
        aux=new TElem [cp*2];
        for(int i=0;i<cp;i++){
            aux[i]=array[i];
            l1[i]=urm[i];
            l2[i]=frecventa[i];
        }
        free(frecventa);
        free(urm);
        free(array);
        int cpv=cp;
        cp=cp*2;
        frecventa=l2;
        urm=l1;
        array=aux;
        initSpatiuLiber(cpv);
    }
    int i=aloca();
    array[i]=e;
    frecventa[i]=1;
    urm[i]=-1;
    return i;
}

int Colectie::verif(TElem e) {
    int i=prim;
    while(i!=-1){
        if (array[i]==e)
            return i;
        i=urm[i];
    }
    return -1;
}

void Colectie::adauga(TElem elem) {
    //Theta(n)
    int i=verif(elem);
    if(i!=-1)
        frecventa[i]++;
    else
    {
        int nou= creeazaNod(elem);
        if(size==0)
            prim=nou;
        else
        {
            urm[nou]=prim;
            prim=nou;
        }
    }
    size++;
}


bool Colectie::sterge(TElem elem) {
    //Theta(n)
    int i=prim;
    int j=-1;
    while(i!=-1) {
        if (array[i] == elem){
            frecventa[i]--;
            if(frecventa[i]==0){
                if (i == prim) {
                    prim = urm[i];
                } else {
                    urm[j]=urm[i];
                }
                dealoca(i);
            }
            size--;
            return true;
        }
        j=i;
        i=urm[i];
    }
	return false;
}


bool Colectie::cauta(TElem elem) const {
    //Theta(n)
    int i=prim;
    while(i!=-1){
        if(array[i]==elem)
            return true;
        i=urm[i];
    }
	return false;
}

int Colectie::nrAparitii(TElem elem) const {
    //Theta(n)
    int i=prim;
    while(i!=-1){
        if(array[i]==elem){
            return frecventa[i];
        }
        i=urm[i];
    }
    return 0;
}


int Colectie::dim() const {
    //Theta(1)
	return size;
}


bool Colectie::vida() const {
    //Theta(1)
	return size==0;
}

IteratorColectie Colectie::iterator() const {
	return  IteratorColectie(*this);
}


Colectie::~Colectie() {
    //Theta(1)
	free(array);
    free(urm);
    free(frecventa);
}


