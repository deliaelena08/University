#include <iostream>
#include <fstream>
#include<vector>
#include<map>
using namespace std;
int n;
int get_min(vector<int> codare_prufer)
{
    vector<bool> fr(codare_prufer.size()+1,false);
    for(auto &i:codare_prufer)
    {
        fr[i]=true;
    }
    for(int i=0;i<=codare_prufer.size();i++)
        if(fr[i]== false)
            return i;
    return -1;

}
vector<int> decodare_prufer(vector<int> codare_prufer){
    map<int,int> parents;
    for(int i=0;i<n;i++){
        int x=codare_prufer.front();
        int nr= get_min(codare_prufer);
        parents[nr]=x;
        codare_prufer.erase(codare_prufer.begin());
        codare_prufer.push_back(nr);
    }
    int nr= get_min(codare_prufer);
    parents[nr]=-1;
    vector<int> par;
    for(pair<int,int> pr :parents){
        par.push_back(pr.second);
    }
    return par;
}
int main(int argc,char* argv[]) {
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);
    fin>>n;
    vector<int> codare_prufer(n);
    for(int i=0;i<n;i++){
        fin>>codare_prufer[i];
    }
    vector<int> parents= decodare_prufer(codare_prufer);
    fout<<parents.size()<<endl;
    for(int i:parents){
        fout<<i<<' ';
    }
    fin.close();
    fout.close();
    return 0;
}
