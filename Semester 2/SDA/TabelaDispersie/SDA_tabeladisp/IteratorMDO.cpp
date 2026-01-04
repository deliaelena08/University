#include "IteratorMDO.h"
#include "MDO.h"
#include <algorithm>
#include <iostream>
#include <stdexcept>

IteratorMDO::IteratorMDO(const MDO& d) : dict(d), indexK(0), indexH(0) {
    chei = getChei();
    std::sort(chei.begin(), chei.end(), [this](const TCheie& a, const TCheie& b) {
        return dict.rel(a, b);
    });
    prim();
}

void IteratorMDO::prim() {
    //O(n)
    indexK = 0;
    indexH = 0;
    if (valid()) {
        isSetToNext();
    }
}

void IteratorMDO::urmator() {
    //O(n)
    if (!valid()) {
        throw std::runtime_error("Iterator is not valid");
    }
    indexH++;
    isSetToNext();
}


bool IteratorMDO::valid() const {
    //Theta(1)
    return indexK < chei.size();
}

TElem IteratorMDO::element() const {
    //Theta(1)
    if (!valid()) {
        throw std::runtime_error("Iterator is not valid");
    }
    int pos = dict.d(chei[indexK], indexH);
    return dict.e[pos];
}


void IteratorMDO::isSetToNext() {
    //O(n)
    while (indexK < chei.size()) {
        while (indexH < dict.m) {
            int pos = dict.d(chei[indexK], indexH);
            if (dict.e[pos].first == chei[indexK] && dict.e[pos].second != -1) {
                return;
            }
            indexH++;
        }
        indexK++;
        indexH = 0;
    }
}

std::vector<TCheie> IteratorMDO::getChei() {
    //Theta(m)
    std::vector<TCheie> keys;
    for (int i = 0; i < dict.m; i++) {
        if (dict.e[i].first != -1 && std::find(keys.begin(), keys.end(), dict.e[i].first) == keys.end()) {
            keys.push_back(dict.e[i].first);
        }
    }
    return keys;
}






/*#include "IteratorMDO.h"
#include "MDO.h"
#include <algorithm>
#include<iostream>


IteratorMDO::IteratorMDO(const MDO& d) : dict(d) {
    chei=getChei();
    cout<<chei.size()<<endl;
    prim();
}

void IteratorMDO::prim() {
    indexH=0;
    indexK=0;
    if (valid())
        isSetToNext();
}

void IteratorMDO::urmator() {
    if (!valid()) {
        throw exception();
    }
    indexH++;
    if (isSetToNext())
        return;

    indexK++;
    if (!valid()) {
        return;
    }
    indexH=0;
    isSetToNext();
}

bool IteratorMDO::isSetToNext() {
    TElem e=getElem();
    while (e.first != -1) {
        if (e.first == chei[indexK])
            return true; //am gasit urmatorul element
        indexH++;
        e=getElem();
    }
    return false;
}

bool IteratorMDO::valid() const {
    return indexK<chei.size();
}

TElem IteratorMDO::element() const {
    if (!valid()) {
        throw exception();
    }
    return getElem();
}

vector<TCheie> IteratorMDO::getChei(){
    vector<TCheie> list;
    for(int i=0;i<dict.m;i++){
        bool ok=true;
        if(dict.e[i].first==-1)
            continue;
        for(auto& e:list)
            if(e==dict.e[i].first){
                ok=false;
                break;
            }
        if(ok)
            list.push_back(dict.e[i].first);
    }
    for(int i=0;i<list.size()-1;i++){
        for(int j=i+1;j<list.size();j++){
            if(!rel(list[i],list[j])){
                TCheie aux=list[i];
                list[i]=list[j];
                list[j]=aux;
            }
        }
    }
    return list;
}

TElem IteratorMDO::getElem() const{
    int d=dict.d(chei[indexK],indexH);
    return dict.e[d];
}
*/

