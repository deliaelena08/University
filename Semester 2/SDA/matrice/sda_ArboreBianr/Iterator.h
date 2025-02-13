#pragma once
#include "Matrice.h"
//
class Iterator{
    friend class Matrice;
private:
    const Matrice& matrice;
    int linie;
    int index=0;
public:
    Iterator(const Matrice& matrice,int linie1): matrice{matrice},linie(linie1){}
    void first();
    void next();
    bool valid() const;
    TElem  getCurrent() const;
};