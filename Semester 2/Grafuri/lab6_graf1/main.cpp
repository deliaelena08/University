#include <iostream>
#include <fstream>
#include<queue>
#include<vector>
using namespace std;
int V,E,ma[100][100];
#define MAX 100
int bfs(int  rG[MAX][MAX], int sursa, int dest, int prev[MAX]) {
    std::vector<bool> visit(MAX);//vizitat nevizitat
    for (int i=0;i<MAX;i++) {
        visit[i] = false;
    }
    queue<int> q;
    q.push(sursa);
    visit[sursa];
    while (!q.empty()){
        int u = q.front();
        q.pop();
        for (int v = 0; v < V; v++) {
            if (visit[v] == false && rG[u][v] > 0) {
                q.push(v);
                prev[v] = u;
                visit[v] = true;
            }
        }
    }
    return visit[dest] == true;
}

int min(int a, int b) {
    if (a < b) return a;
    return b;
}

int minCut(int c[MAX][MAX],int sursa,int dest) {
    int u, v, flux_max = 0, rG[MAX][MAX],prev[MAX];

    //copiez matricea de capacitati
    for (u = 0; u < V; u++)
        for (v = 0; v < V; v++)
            rG[u][v] = c[u][v];

    //cat timp exsita cale reziduala
    while (bfs(rG, sursa, dest, prev)) {
        int flux = INT_MAX;

        //determinam fluxul pe drumul gasit
        for (v = dest; v != sursa; v = prev[v]) {
            u = prev[v];
            flux = min(flux, rG[u][v]);
        }

        //stabimil cat ducem, cat primim
        for (v = dest; v != sursa; v = prev[v]) {
            u = prev[v];
            rG[u][v] -= flux;//de la u la v scadem
            rG[v][u] += flux;//ducem inapoi (adunam)
        }

        flux_max += flux;
    }
    //fluxul este maxim;
    return flux_max;
}
int main(int argc,char* argv[]) {

    ifstream fin(argv[1]);
    ofstream fout(argv[2]);
    fin>>V>>E;
    int x,y,c;
    for(int i=0;i<E;i++){
        fin>>x>>y>>c;
        ma[x][y]=c;
    }
    int source=0;
    int destination=V-1;
    fout<<minCut(ma,source,destination);
    return 0;
}
