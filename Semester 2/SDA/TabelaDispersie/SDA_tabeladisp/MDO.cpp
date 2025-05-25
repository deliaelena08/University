#include "MDO.h"
#include "IteratorMDO.h"
#include <iostream>
#include <vector>
#include<cmath>
#include <exception>
#include <algorithm>
using namespace std;
const TElem NIL= make_pair(-1,-1);
const TElem STERS= make_pair(-1,-2);

bool suntInRelatie(const TCheie& key1, const TCheie& key2, Relatie rel) {
    return rel(key1, key2);
}

int MDO::hashCode(TCheie key)const {
    //Theta(1)
    return key%m;
}

int MDO::d(TCheie key, int i)  const{
    //Theta(1)
    return (hashCode(key) + i*(i+1)/2) % m;
}

MDO::MDO(Relatie r) {
    rel=r;
    sz=0;
    m = MAX;
    for (int i = 0; i < m; i++) {
        e[i] = NIL;
    }
}

void MDO::adauga(TCheie c, TValoare v) {
    //O(n)
    TElem elem(c, v);
    if (e[d(c,0)] == NIL) {
        e[d(c,0)] = elem;
        sz=1;
        return;
    }
    int i = 0,j;
    while (i <m) {
        j = d(c, i);
        if ((e[j] == NIL or e[j]==STERS)) {
            e[j] = elem;
            sz++;
            return;
        }
        i++;
    }

}
vector<TValoare> MDO::cauta(TCheie c) const {
    //O(n)
    vector<TValoare> valori;
    int i=0;
    int pozitie=d(c,i);
    while(e[pozitie]!=NIL && i<m){
        if(e[pozitie].first==c  ) {
            valori.push_back(e[pozitie].second);
        }
        i++;
        pozitie=d(c,i);
    }
    return valori;
}

bool MDO::sterge(TCheie c, TValoare v) {
    //O(n)
    int position=d(c,0);
    int i=0;
    while(i<m && e[position]!=NIL){
        if(e[position]==TElem(c,v))
        {
            e[position]=STERS;
            sz--;
            return true;
        }
        i++;
        position= d(c,i);
    }
    return false;
}

int MDO::dim() const {
    return sz;
}

bool MDO::vid() const {
    return sz==0;
}

IteratorMDO MDO::iterator() const {
    return IteratorMDO(*this);
}

MDO::~MDO() {
    for(int i=0;i<m;i++){
        e[i]=NIL;
    }
}
