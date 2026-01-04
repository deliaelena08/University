#include <iostream>
#include<fstream>
using namespace std;
int E,V,ma[100][100],cnt,L[100];

void Euler(int k)
{
    for(int i = 0 ; i < V ; i ++)
        if(ma[k][i] == 1)
        {
            ma[k][i] = ma[i][k] = 0;
            Euler(i);
        }
    L[++cnt] = k;
}


int main(int argc,char* argv[]) {
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);
    fin>>V>>E;
    int x,y;
    while(fin>>x>>y)
        ma[x][y]=ma[y][x]=1;
    Euler(0);
    for(int i = 1 ; i < cnt ; i++)
        fout << L[i] << " ";
    return 0;
}
