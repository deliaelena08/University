#include <iostream>
#include <vector>
#include <algorithm>
#include<fstream>
using namespace std;
struct muchie{
    int i,j,cost;
};
int n , m , t[105],ma[101][2];
muchie x[5000];
int v[5000];

void sort(){
    for(int i = 0 ; i < m - 1; i ++)
        for(int j = i + 1 ; j < m ; ++j)
            if(x[i].cost > x[j].cost)
            {
                muchie aux = x[i];
                x[i] = x[j];
                x[j] = aux;
            }
    for(int i =1 ; i <= n ; ++i)
        t[i] = i;
}

void kruskal(int S,int cnt){
    for(int i = 0 ; i < m && cnt < n ; i ++)
        if(t[x[i].i] != t[x[i].j])
        {
            v[i] = 1;
            S += x[i].cost;
            int ai = t[x[i].i], aj = t[x[i].j];
            for(int j =1 ; j <= n ; ++j)
                if(t[j] == aj)
                    t[j] = ai;
        }
}

int main(int argc, char * argv[]) {
    std::ifstream fin(argv[1]);
    std::ofstream fout(argv[2]);
    fin >> n >> m;

    for(int i = 0 ; i < m ; ++i)
        fin >> x[i].i >> x[i].j >> x[i].cost;
    sort();
    int S = 0, cnt = 0;
    kruskal(S,cnt);
    fout <<"Costul minim: "<< S << endl;
    int nr=0;
    for(int i = 0 ; i < m ; ++i)
        if(v[i] == 1){
            ma[nr][1]=x[i].i;
            ma[nr][2]=x[i].j;
            nr++;
        }
    fout<<"Numarul de muchii al arborelui: "<<nr<<endl;
    for(int i=0;i<nr;i++)
        fout<<ma[i][1]<<' '<<ma[i][2]<<endl;
    return 0;
}
