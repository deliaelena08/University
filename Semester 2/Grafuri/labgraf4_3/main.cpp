#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <queue>
#include <vector>

using namespace std;

class Nod{
public:
    int frecventa;
    char caracter;
    Nod *st,*dr;
};

class compNodP{
    //bool reverse;
public:
    //compNodP(const bool& reverse0=false):reverse{reverse0}{}
    bool operator()(const Nod* nod_st, const Nod* nod_dr) const{
        bool ret= nod_st->frecventa > nod_dr->frecventa;
        if(nod_st->frecventa == nod_dr->frecventa)
            ret= nod_st->caracter > nod_dr->caracter;
        return ret;

    }

};

void genereaza_cod(map<char,string>& rez,string s,Nod* arb){
    if(arb->st!=nullptr)
        genereaza_cod(rez,s+"0",arb->st);
    if(arb->dr!=nullptr)
        genereaza_cod(rez,s+"1",arb->dr);
    if(arb->st==nullptr && arb->dr==nullptr)
        rez[arb->caracter]=s;

}

map<char,string> codare_Huffman(map<char,int> dict){
    priority_queue<Nod*,vector<Nod*>,compNodP> arb;
    for(auto elem:dict){
        Nod* n=new Nod;
        n->caracter=elem.first;
        n->frecventa=elem.second;
        n->st=nullptr;
        n->dr=nullptr;
        arb.push(n);
    }
    for(int i=0;i<dict.size()-1;i++){
        Nod* z=new Nod;
        z->st=arb.top();
        arb.pop();
        z->dr=arb.top();
        arb.pop();
        z->frecventa=z->st->frecventa+z->dr->frecventa;
        z->caracter=min(z->st->caracter,z->dr->caracter);
        arb.push(z);
    }
    map<char,string> rez;

    genereaza_cod(rez,"",arb.top());

    return rez;
}

int main(int argc, char** argv)
{
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);
    string text;
    map<char,int> dict;
    getline(fin,text);

    for(int i=0;i<text.length();i++){
        if(dict.find(text[i])==dict.end())
            dict[text[i]]=0;
        dict[text[i]]++;
    }

    map<char,string> rez=codare_Huffman(dict);

    fout<<dict.size()<<"\n";
    for(auto& elem:dict)
        fout<<elem.first<<" "<<elem.second<<"\n";

    for(int i=0;i<text.length();i++){
        fout<<rez[text[i]];
    }


    fin.close();
    fout.close();
    return 0;
}