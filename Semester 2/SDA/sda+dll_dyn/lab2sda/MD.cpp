#include "MD.h"
#include "IteratorMD.h"
#include <exception>
#include <iostream>

using namespace std;


MD::MD() {
    cp=10;
    array = new TElem[cp];
    urm=new int[cp];
    prec=new int[cp];
    prim=-1;
    size=0;
    ultim=-1;
    initSpatiuLiber(0);
}

void MD::initSpatiuLiber(int index) {
    //Theta (cp)
    for(int i=index;i<cp-1;i++)
    {
        urm[i]=i+1;
    }
    urm[cp-1]=-1;
    primSpLiber=index;
}

int MD::aloca() {
    //Theta(1)
    int i=primSpLiber;
    primSpLiber=urm[primSpLiber];
    return i;
}

void MD::dealoca(int i) {
    //Theta(1)
    if (size==1)
    {
        urm[i]=-1;
        prec[i]=-1;
        prim=ultim=-1;
    }
    else if(i==prim)
    {
        int u=urm[i];
        prec[u]=-1;
        prim=u;
    }
    else if(i==ultim){
        int p=prec[i];
        urm[p]=-1;
        ultim=p;
    }
    else{
        int u=urm[i];
        int p=prec[i];
        prec[u]=p;
        urm[p]=u;
    }
    urm[i]=primSpLiber;
    primSpLiber=i;
}

int MD::creeazaNod(TElem e) {
    //O(n)
    if(primSpLiber==-1){
        TElem* aux;
        int *l1,*l2;
        l1=new int[cp*2];
        l2=new int[cp*2];
        aux=new TElem [cp*2];
        for(int i=0;i<cp;i++)
        {
            aux[i]=array[i];
            l1[i]=prec[i];
            l2[i]=urm[i];
        }
        free(prec);
        free(urm);
        free(array);
        int cpv=cp;
        cp=cp*2;
        prec=l1;
        urm=l2;
        array=aux;
        initSpatiuLiber(cpv);
    }
    int i=aloca();
    array[i]=e;
    urm[i]=-1;
    prec[i]=-1;
    return i;
}

void MD::adauga(TCheie c, TValoare v) {
    //Theta(1)
    TElem e={c,v};
    int i= creeazaNod(e);
    if(size==0){
        prim=i;
        ultim=i;
    }
    else if(size==1){
        ultim=i;
        urm[prim]=ultim;
        prec[ultim]=prim;
    }
    else {
        urm[ultim] = i;
        prec[i] = ultim;
        ultim = i;
    }
    size++;
}


bool MD::sterge(TCheie c, TValoare v) {
    //Theta(n)
    int i=prim;
    while(i!=-1) {
        int u=urm[i];
        if (array[i].first == c && array[i].second == v) {
            dealoca(i);
            size--;
            return true;
        }
        i = u;
    }
	return false;
}


vector<TValoare> MD::cauta(TCheie c) const {
    //O(n)
	int i=prim;
    vector <TValoare> list;
    while(i!=-1){
        if(array[i].first==c)
            list.push_back(array[i].second);
        i=urm[i];
    }
	return list;
}


int MD::dim() const {
    //Theta(1)
	return size;
}


bool MD::vid() const {
    //Theta(1)
	return size==0;
}

IteratorMD MD::iterator() const {
    //Theta(1)
	return IteratorMD(*this);
}


MD::~MD() {
    //Theta(1)
    free(array);
    free(urm);
    free(prec);
}


void MD::filtreaza(Conditie cond){
    //Theta(n)
    for(int i=0;i<size;i++){
        if(!cond(array[i].second))
            sterge(array[i].first,array[i].second);
    }
}