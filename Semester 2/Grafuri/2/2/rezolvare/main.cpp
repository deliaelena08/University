#include <iostream>
#include <vector>
#include <cmath>
#include <queue>
#include <utility>
#include <limits>
#include <fstream>

using namespace std;

ifstream in5("nota5.in");
ifstream in10("nota10.in");

ofstream out5("nota5.out");
ofstream out10("nota10.out");

int V, M;
bool bfs(int rGraph[100][100], int s, int t, int parent[])
{
    // presupun ca nici un varf a fost vizitat
    bool visited[V];
    for (int i = 0; i < V; i++)
        visited[i] = false;

    queue<int> q;
    q.push(s);
    visited[s] = true;
    parent[s] = -1;

    // apelez BFS
    while (!q.empty()) {
        int u = q.front();
        q.pop();

        for (int v = 0; v < V; v++) {
            if (!visited[v] && rGraph[u][v] > 0) {
                // daca am gasit o conexiune catre nodul destinatie, opresc bucla si returnez true
                if (v == t) {
                    parent[v] = u;
                    return true;
                }
                q.push(v);
                parent[v] = u;
                visited[v] = true;
            }
        }
    }

    // returnez false daca nu se gaseste

    return false;
}

// Returns the maximum flow from s to t in the given graph
int fordFulkerson(int graph[100][100], int s, int t)
{
    int u, v;
    int rGraph[100][100];
    for (u = 0; u < V; u++)
        for (v = 0; v < V; v++)
            rGraph[u][v] = graph[u][v];

    int parent[V]; // completat de BFS

    int max_flow = 0;

    while (bfs(rGraph, s, t, parent)) {
        // Caut capacitatea minima reziduala a nodurilor
        int path_flow = INT_MAX;
        for (v = t; v != s; v = parent[v]) {
            u = parent[v];
            path_flow = min(path_flow, rGraph[u][v]);
        }

        // update capacitati reziduale
        for (v = t; v != s; v = parent[v]) {
            u = parent[v];
            rGraph[u][v] -= path_flow;
            rGraph[v][u] += path_flow;
        }

        max_flow += path_flow;
    }

    // Return flow
    return max_flow;
}

// ------------------------------------------------------------ //

struct City {
    int x, y;
};

double calcDistance(const City& a, const City& b) {
    return sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
}

void mstPrim(const vector<City>& cities) {
    int N = cities.size();
    vector<double> key(N, numeric_limits<double>::max());
    vector<int> parent(N, -1);
    vector<bool> inMST(N, false);

    // Coada de priorități pentru a selecta nodul cu cea mai mică cheie
    priority_queue<pair<double, int>, vector<pair<double, int>>, greater<pair<double, int>>> pq;

    // Pornim de la nodul 0
    pq.push({0.0, 0});
    key[0] = 0.0;

    while (!pq.empty()) {
        int u = pq.top().second;
        pq.pop();

        if (inMST[u]) continue;

        inMST[u] = true;

        for (int v = 0; v < N; v++) {
            if (v != u && !inMST[v]) {
                double weight = calcDistance(cities[u], cities[v]);
                if (weight < key[v]) {
                    key[v] = weight;
                    pq.push({key[v], v});
                    parent[v] = u;
                }
            }
        }
    }

    // Afișăm MST și costul total
    double total_cost = 0.0;
    for (int i = 1; i < N; i++) {
        total_cost += calcDistance(cities[i], cities[parent[i]]);
    }
    out10 << "Total cost: " << total_cost << endl;
}

int main() {
    int graph[100][100], a, b, c;
    in5 >> V >> M;

    for (int i = 1; i <= M; i++){
        in5 >> a >> b >> c;
        graph[a - 1][b - 1] = c;
    }

    out5 << fordFulkerson(graph, 0, V - 1);

    in10 >> V;

    vector<City> cities(V);
    for (int i = 0; i < V; i++) {
        in10 >> cities[i].x >> cities[i].y;
    }

    mstPrim(cities);

}
