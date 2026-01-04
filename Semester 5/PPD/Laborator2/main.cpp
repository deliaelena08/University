#include <iostream>
#include <thread>
#include <cassert>
#include <chrono>
#include<cmath>
#include<mem.h>

using namespace std;
using namespace std::chrono;

const int N = 100000;
const int P = 8;
const int MAX_VAL = 6000;
int a[N], b[N], c[N], c_secv[N];

void init(int* v, int len){
    for(int i = 0; i < len; i++)
        v[i] = rand() % 6000;
}

void sum(const int* a, const int* b, int* c, int len){
    for(int i = 0; i < len; i++){
        // c[i] = a[i] + b[i];
        c[i] = sqrt(a[i] * a[i] + b[i] * b[i]) - pow(3, a[i]);
    }
}

void work(const int* a, const int* b, int* c, int start, int end){
    for(int i=start; i < end; i++){
        // c[i] = a[i] + b[i];
        c[i] = sqrt(a[i] * a[i] + b[i] * b[i]) - pow(3, a[i]);
    }
}

void functie_noua(const int *a,const int *b, int *c) {
    //sa putem vedea ce se intampla daca alocam dinamic nu static
    int elems_slice = N/P;
    int r = N%P;

    int start_idx = 0;
    int end_idx = elems_slice;

    thread t[P];

    auto start_p = high_resolution_clock::now();
    for(int tid = 0; tid < P; tid++){
        if(r > 0){
            end_idx ++;
            r --;
        }

        //        cout<< "start:" << start_idx<< " end:" << end_idx<< endl;
        t[tid] = thread(work, a, b, c, start_idx, end_idx);

        start_idx = end_idx;
        end_idx += elems_slice;
    }

    for(auto & th : t)
        th.join();

    auto end_p = high_resolution_clock::now();
    duration<double, milli> delta_p = end_p - start_p;

    for(int i=0; i<N; i++)
        assert(c[i] == c_secv[i]);
    cout<<"Paralel: "<<delta_p.count()<<" ms" << endl;

}

int main() {
    init(a,N);
    init(b,N);

    auto start_secv = high_resolution_clock::now();
    sum(a,b,c_secv, N);
    auto end_secv = high_resolution_clock::now();
    duration<double, milli> delta_secv = end_secv - start_secv;
    cout << "Secv: " << delta_secv.count()<<endl;
    //    for(int i = 0; i< 3; i++)
    //            cout<<"a"<<a[i]<< " b:"<<b[i]<<" c:"<<c_secv[i]<<endl;

    // int elems_slice = N/P;
    // int r = N%P;
    //
    // int start_idx = 0;
    // int end_idx = elems_slice;
    //
    // thread t[P];
    //
    // auto start_p = high_resolution_clock::now();
    // for(int tid = 0; tid < P; tid++){
    //     if(r > 0){
    //         end_idx ++;
    //         r --;
    //     }
    //
    //     //        cout<< "start:" << start_idx<< " end:" << end_idx<< endl;
    //     t[tid] = thread(work, a, b, c, start_idx, end_idx);
    //
    //     start_idx = end_idx;
    //     end_idx += elems_slice;
    // }
    //
    // for(auto & th : t)
    //     th.join();
    //
    // auto end_p = high_resolution_clock::now();
    // duration<double, milli> delta_p = end_p - start_p;
    //
    // for(int i=0; i<N; i++)
    //     assert(c[i] == c_secv[i]);
    // cout<<"SUCCES"<<endl;
    // cout<<"Secv: "<<delta_secv.count()<<" ms"<<endl;
    // cout<<"Paralel: "<<delta_p.count()<<" ms";

    //functie_noua(a,b,c);
    int *a_dyn = new int[N];
    int *b_dyn = new int[N];
    int *c_dyn = new int[N];
    int *all = new int[3 * N];

    memcpy(a_dyn, a, N * sizeof(int));
    memcpy(b_dyn, b,  N * sizeof(int));

    memcpy(all, a, N * sizeof(int));
    memcpy(all + N , b,  N * sizeof(int));
    functie_noua( a_dyn, b_dyn, c_dyn);
    functie_noua(all, all + N, all + N * 2);
    //alocarea matrice [][] sau 1 lung,
    //alocarea matricei int* [] si apoi[] putem pierde din perfomanta
    //ideal ar fi sa fie liniarizata matricea in zigzag i*n+j pentru alocarea valorilor diferit distribuite
    /*
     * n linii 0 <= i < n , altfel i = clamp(i, 0, n-1)
     * m coloane 0 <= j < m, altfel j = clamp(j, 0, m-1)
     * a_ij = i * m + j = i + j * n
     * i
     * i = a_ij / m = a_ij % n
     * j = a_ij / n = a_ij % m
     * aplicarea unui kernel/  mergini se duplica elementele de langa ele adica se repeta toata marginea
     * nr pentru linii, nr pentru coloane
     * 
     */
    delete[] a_dyn;
    delete[] b_dyn;
    delete[] c_dyn;
    delete[] all;
    return 0;
}