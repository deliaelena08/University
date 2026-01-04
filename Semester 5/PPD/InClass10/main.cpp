#include <iostream>
#include <omp.h>
using namespace std;
const int LENGTH = 100000000;
int a[LENGTH];
int b[LENGTH];
int c[LENGTH];

const int N = 5000;
int x[N][N];
int y[N][N];
int z[N][N];

void initialize() {
    for (int i = 0; i < LENGTH; i++) {
        a[i]=rand()%1000;
        b[i]=rand()%1000;
        c[i]=rand()%1000;
    }
}

void initializeMat() {
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            x[i][j]=rand()%1000;
            y[i][j]=rand()%1000;
            z[i][j]=rand()%1000;
        }
    }
}

void validateSumMat() {
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            if ( x[i][j] + y[i][j] != z[i][j]) {
                cout<<"Sum not valid"<<endl;
                exit(1);
            }
        }
    }
    cout<<"Sum valid"<<endl;
}



void sumMat() {
    double time = omp_get_wtime();
    #pragma omp parallel for collapse(2)
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            z[i][j] = x[i][j] + y[i][j];
        }
    }
    double time2 = omp_get_wtime();
    cout << "Sum mat time: "<<time2-time<<endl;
}

void validateSum() {
    for (int i = 0; i < LENGTH; i++) {
        if (a[i] + b[i] != c[i]) {
            //nu da tot timpul flush
            cout<<"Sum not valid"<<endl;
            exit(1);
        }
    }
    cout<<"Sum valid"<<endl;
}


void validateProdSum(int sum) {
    for (int i = 0; i < LENGTH; i++) {
        sum-= a[i] * b[i];
    }
    if (sum != 0) {
        cout<<"ProdSum not valid"<<endl;
        exit(1);
    }
    cout<<"Sum valid"<<endl;
}

void sum() {
    double time = omp_get_wtime();
    //default imparte length, cum faceam cu modulo - static
    //dinamic e ca si cum ai lucra cu o variabila atomica, implica mai multe sincronizari
    #pragma omp parallel for schedule(dynamic,100)
    for (int i = 0; i < LENGTH; i++) {
        c[i] = a[i] + b[i];
    }
    double time2 = omp_get_wtime();
    cout << "Sum time: "<<time2-time<<endl;
}

int prod_scalar() {
    double time = omp_get_wtime();
    int sum = 0;
    // #pragma omp parallel for
    // for (int i = 0; i < LENGTH; i++) {
    //     // #pragma omp critical
    //     // {
    //     //     sum += a[i] * b[i];
    //     // }
    //     #pragma omp atomic
    //     {
    //         sum += a[i] * b[i];
    //     }
    // }
    // int aux = 0;
    // //by defaulot variabilele la parallel sunt shared
    // //acum cu acest private aux e dublicat, cu firstprivate ia si valoarea lui initiala
    // #pragma omp parallel shared(sum) firstprivate(aux)
    // { // fork
    //     #pragma omp parallel for nowait
    //     for(int i = 0; i < LENGTH; i++) {//bariera fara no wait, prgama omp barrier pentru sincronizare manuala
    //         #pragma omp atomic
    //         aux += a[i] * b[i];
    //     }
    //     //este o bariela
    //     #pragma omp atomic
    //     sum+=aux;
    // } // join

    //cel mai rapid
    #pragma omp parallel for reduction(+:sum)
    for (int i = 0; i < LENGTH; i++) {
        sum += a[i] * b[i];
    }

    double time2 = omp_get_wtime();
    cout << "ProdSum time: "<<time2-time<<endl;
    return sum;
}

void omp_sections() {
#pragma omp parallel num_threads(16)
    {
        #pragma omp sections
        {
            #pragma omp section
            {
                cout << "Hello University! "
             << omp_get_thread_num() << " "
             <<omp_get_num_threads() << " "
             <<omp_get_max_threads() <<endl;
            }
            #pragma omp section
            {
                cout << omp_get_thread_num() << endl;
                for (int i = 0 ; i < LENGTH ; i++) {
                    c[i] = a[i] + b[i];
                }
                validateSum();
            }
            #pragma omp section
            {
                int res = 0;
                cout << omp_get_thread_num() << " " << res << endl;
            }
        }
    }
}

int main() {
    //comunicari manuale, un proces trimite la tot/unul, sau proces primeste tot
    // cout << "Hello University!" <<endl
    // << omp_get_thread_num()<<endl
    // <<omp_get_num_threads()<<endl
    // <<omp_get_max_threads() <<endl;
    // omp_set_num_threads(16);
    // #pragma omp parallel num_threads(10)
    // {//fork
    //     #pragma omp critical
    //     {
    //         cout << "Hello University!" <<endl
    //         << omp_get_thread_num()<<endl
    //         <<omp_get_num_threads()<<endl
    //         <<omp_get_max_threads() <<endl;
    //     }
    // }//join

    initialize();
    //sumMat();
    //validateSumMat();
    omp_sections();
    // sum();
    // validateSum();
    // validateProdSum(prod_scalar());
}