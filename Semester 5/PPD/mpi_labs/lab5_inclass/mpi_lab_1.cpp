#include <mpi.h>
#include <stdio.h>
#include <string>
#include <cassert>

void mpi_hello(int rank, int size);

using namespace std;

void init(int* vec, int len){
    for(int i = 0; i<len;i++){
        vec[i]=rand()%270;
    }
}

void validate(int* a, int* b, int* c, int len){
    for(int i=0;i<len;i++){
        assert(c[i] == a[i]+b[i]);
    }
}

void mpi_sum(int rank,int size){
    const int N=100;
    if(rank == 0){
        int* a= new int[N]
        int* a= new int[N]
        int* a= new int[N]
        init(a,N);
        init(b,N);
        int slice = N/(size-1);

        int r = N%(size-1);
        int start = 0;
        int end = slice;

        MPI_Request* reqs = new MPI_Request[size-1];

        for(int pid = 1; pid < size; pid++){
            if(r > 0){
                end+=1;
                r-=1;
            }
            start = end;
            end += slice;
        }
      
        int r = N%(size-1);
        int start = 0;
        int end = slice;
        for(int pid = 1; pid < size; pid++){
            if(r > 0){
                end+=1;
                r-=1;
            }
            int len = end-start;
            MPI_Send(&len, 1, MPI_INT, pid, 0, MPI_COMM_WORLD);
            MPI_Send(a + start, len, MPI_INT, pid, 0 , MPI_COMM_WORLD);
            MPI_Send(b + start, len, MPI_INT, pid, 0 , MPI_COMM_WORLD);

            MPI_Irecv(c+start,len,MPI_INT,pid,0,MPI_COMM_WORLD,&reqs[pid-1]);
      
            MPI_Recv(c+start,end-start,MPI_INT,pid,0,MPI_COMM_WORLD,MPI_STATUSES_IGNORE);
            start = end;
            end += slice;
        }
    
        validate(a,b,c,N);
        delete[] a;
        delete[] b;
        delete[] c;
    } else {
        int  len;
        MPI_Recv(&len,1,MPI_INT,0,0,MPI_COMM_WORLD, MPI_STATUSES_IGNORE);

        int* a = new int[len];
        int* b = new int[len];
        int* c = new int[len];

        MPI_Recv(a, len, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUSES_IGNORE);
        MPI_Recv(b, len, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUSES_IGNORE);

        for( int i = 0; i < len, i++){
            c[i] = a[i] + b[i];
        }
        MPI_Send(c,len,MPI_INT,0,0,MPI_COMM_WORLD);

        delete[] a;
        delete[] b;
        delete[] c;
    }
}

// recomandare la tema --o3
void mpi_hello_asink(int rank, int size) {
    const char fromat[] ="Hello From: %10d\n";
    const int STRING_SIZE = sizeof(fromat) - 1 + 10 - 4;
    if(rank == 0){
        //main
        char* buf = new char[STRING_SIZE*(size - 1)+1];

        buf[STRING_SIZE*(size - 1)] = '\0';
        MPI_Request *reqs = new MPI_Request [size-1];
        for (int pid = 1; pid< size; pid++){
            MPI_Irecv(buf + STRING_SIZE*(pid -1), STRING_SIZE,
                      MPI_CHAR, pid, 0, MPI_COMM_WORLD, &reqs[pid-1]);
        }

        bool ok = false;

//        while(!ok){
//            ok = true;
//            for(int pid=1; pid< size; pid++){
//                int flag;
//                MPI_Test(&reqs[pid-1],&flag,MPI_STATUSES_IGNORE);
//                if(flag == 0 )
//                    ok = false;
//                else
//                    printf("%d arrived\n", pid);
//            }
//        }

//        MPI_Waitall(size -1, reqs, MPI_STATUSES_IGNORE);

        for(int i=0;i<size-1; i++){
            int index;
            MPI_Waitany(size -1, reqs, &index, MPI_STATUS_IGNORE);
        }

        printf("%s\n", buf);
        //ii prindem in ordinea 1,2,3 si ii punem la o locatie fixa
        delete[] buf;
        delete[] reqs;
    }else{
        //worker
        char * buffer = new char[STRING_SIZE + 1];
        MPI_Request req;
        snprintf(buffer, STRING_SIZE+1, fromat,rank);
        MPI_Isend(buffer,STRING_SIZE,MPI_CHAR,0, 0, MPI_COMM_WORLD, &req);

        MPI_Wait(&req, MPI_STATUS_IGNORE);
        delete[] buffer; //bufferul nu trebuie modificat pana se trimite
    }
}

void mpi_sum_scatter(int rank, int size){
    int* a = nullptr;
    int* b = nullptr;
    int* c = nullptr;

    const int N = 1000;

    if(rank==0){
        a = new int[N];
        b = new int[N];
        c = new int[N];

        init(a,N);
        init(b,N);
        init(c,N);
    } 

    //la tema presupunem ca se impart exact
    int dim = N / size;
    int* aloc = new int[dim];
    int* bloc = new int[dim];
    int* cloc = new int[dim];

    MPI_Scatter(a,dim,MPI_INT,aloc,dim,MPI_INT,0,MPI_COMM_WORLD);
    MPI_Scatter(b,dim,MPI_INT,bloc,dim,MPI_INT,0,MPI_COMM_WORLD);

    for(int = 0;i<dim;i++){
        cloc[i] = aloc[i]  + bloc[i];
    }

    MPI_Gather(cloc,dim,MPI_INT,c,dim,MPI_INT,0,MPI_COMM_WORLD);

    if(rank == 0){
        validate(a,b,c,dim*size);
    }

    delete[] aloc;
    delete[] bloc;
    delete[] cloc;
    if(rank==0){
        delete[] a;
        delete[] b;
        delete[] c;
    }
}

int main(int argc, char** argv) {
    printf("Hello!");
    MPI_Init(&argc, &argv); // un fel de bariera

    MPI_Barrier(MPI_COMM_WORLD);
    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    printf("Salut din procesul %d din %d!\n", rank, size);

    int selected = stoi(argv[1]); //argv[1] e numele programului

    switch(selected){
        case 1:
            mpi_hello(rank,size);
            break;
        case 2:
            mpi_hello_asink(rank,size);
            break;
        case 3:
            mpi_sum(rank,size);
            break;
        case 4:
            mpi_sum_scatter(rank,size);
            break;
        default:
            printf("Option unkown: %d", selected);
            break;
    }
    MPI_Finalize();// acesta trebuie pus
    return 0;
}

void mpi_hello(int rank, int size) {
    const char fromat[] ="Hello From: %10d\n";
    const int STRING_SIZE = sizeof(fromat) - 1 + 10 - 4;
    if(rank == 0){
        //main
        char* buf = new char[STRING_SIZE*(size - 1)+1];

        buf[STRING_SIZE*(size - 1)] = '\0';

        for (int pid = 1; pid< size; pid++){
            MPI_Recv(buf + STRING_SIZE*(pid -1), STRING_SIZE,
                     MPI_CHAR, pid, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        }

        printf("%s\n", buf);
        //ii prindem in ordinea 1,2,3 si ii punem la o locatie fixa
        delete[] buf;
    }else{
        //worker
        char * buffer = new char[STRING_SIZE + 1];
        snprintf(buffer, STRING_SIZE+1, fromat,rank);
        MPI_Send(buffer,STRING_SIZE,MPI_CHAR,0, 0, MPI_COMM_WORLD);
        //mpi_com_world - contine toate threadurile
        //tag-ul determina canalul de comunicare
        delete[] buffer;
    }

}