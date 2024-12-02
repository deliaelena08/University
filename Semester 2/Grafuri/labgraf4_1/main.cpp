#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

vector<int> codare_prufer(vector<int> parents,vector<bool> leafs,vector<bool> exists) {
    vector<int> prufer_code;
    while(prufer_code.size()<parents.size()-1){
        int min_leaf = -1;
        for (int i = 0; i < parents.size(); ++i) {
            if (leafs[i]==true && exists[i]==true&& parents[i] < parents[min_leaf]) {
                min_leaf = i;
            }
        }
            prufer_code.push_back(parents[min_leaf]);
            leafs[parents[min_leaf]]=true;
            exists[min_leaf]=false;
            for(int j=0;j<parents.size();j++){
                if(exists[j] && parents[j]==parents[min_leaf])
                {
                    leafs[parents[min_leaf]]=false;
                    break;
                }
            }

    }
    return prufer_code;
}

int main(int argc, char *argv[]) {
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);
    int n;
    fin >> n;
    vector<bool> exists(n);
    vector<bool> leafs(n);
    vector<int> parents(n);
    int rad = -1;
    for (int i = 0; i < n; i++) {
        exists[i]=true;
        leafs[i]=true;
        fin >> parents[i];
        if (parents[i] == -1){
            rad = i;
            leafs[parents[i]]=false;
        }
        else
            leafs[parents[i]]=false;
    }

    vector<int> prufer_code = codare_prufer(parents,leafs,exists);
    fout << prufer_code.size() << endl;
    for (int i : prufer_code) {
        fout << i << " ";
    }

    fin.close();
    fout.close();
    return 0;
}