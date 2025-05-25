#include <iostream>
#include <vector>
#include <fstream>
#include<map>

using namespace std;

struct Nod {
    int h ;
    int e ;
};

struct Edge {
    int c;
    int f;
    int cf;
};

int V, E;
vector<Nod> nodes;
vector<map<int,Edge>> lst;  // Adjacency list to store edges

void initPreflux(){
    nodes[0].h = nodes.size();
    for(auto &ucf: lst[0])
    {
        int u=ucf.first;
        ucf.second.f = ucf.second.c;
        nodes[u].e = ucf.second.c;
        nodes[0].e -= ucf.second.c;
    }
}


int get_cf(int u,int v)
{
    if(lst[u][v].cf==1) // u,v in E
    {
        return lst[u][v].c-lst[u][v].f;
    }
    if(lst[u][v].cf==2) // v,u in E
    {
        return lst[v][u].f;
    }
    return 0;
}



bool cond_pomp(int &u,int &v)
{
    for(u=1;u<nodes.size()-1;u++)
    {
        if(nodes[u].e<=0) continue;
        for(const auto& l:lst[u])
        {
            v=l.first;
            if(get_cf(u,v)<=0) continue;
            if(nodes[u].h == nodes[v].h+1)
            {
                return true;
            }
        }
    }
    return false;
}

void pompare(int u,int v)
{
    //cout<<"pompare "<<u<<' '<<v<<'\n';
    int df=min(nodes[u].e, get_cf(u,v));
    if(lst[u][v].cf==1)
        lst[u][v].f += df;
    else
        lst[v][u].f -= df;

    nodes[u].e-=df;
    nodes[v].e+=df;
}

bool cond_inaltare(int &u)
{
    int w=-1;
    for(u=1;u<nodes.size()-1;u++)
    {
        if(nodes[u].e<=0) continue;
        bool ulowest=true;
        int k=0;
        for(const auto &vcf: lst[u])
        {
            int v=vcf.first;
            if(get_cf(u,v)>0)
            {
                k++;
                if(nodes[u].h>nodes[v].h)
                {
                    ulowest=false;
                    break;
                }
            }
        }
        if(!ulowest) continue;
        if(w==-1)
        {
            w=u;
        }
        else
        {
            if(nodes[u].h<nodes[w].h)
                w=u;
        }
    }
    u=w;
    return u!=-1;
}

void inaltare(int u)
{
    int hmin=INT_MAX;
    for(const auto &vcf: lst[u])
    {
        int v=vcf.first;
        if(get_cf(u,v)>0)
            hmin=min(hmin, nodes[v].h);
    }
    nodes[u].h=hmin+1;

}

void pomp_preflux()
{
    int s=0;
    int t=nodes.size()-1;
    initPreflux();

    int u,v;
    while(true)
    {
        if(cond_pomp(u,v))
        {
            pompare(u,v);
            continue;
        }
        if(cond_inaltare(u))
        {
            inaltare(u);
            continue;
        }
        break;
    }
}

int main(int argc, char* argv[]) {
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);
    fin >> V >> E;
    nodes=vector<Nod>(V,{0,0});
    lst=vector<map<int,Edge>>(V);

    int x, y, c;
    for (int i = 0; i < E; ++i) {
        fin >> x >> y >> c;
        lst[x][y]={c,0,1};
        lst[y][x]={0,0,2};
    }


    initPreflux();
    pomp_preflux();

    int maxFlow = 0;
    for ( auto& edge : lst[0]) {
        maxFlow += edge.second.f;
    }
    fout << "Maximum Flow: " << maxFlow << endl;
    return 0;
}
