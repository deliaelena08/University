#include<stdio.h>
#include<stdlib.h>
#include<pthread.h>
#include<unistd.h>
#include<fcntl.h>
int diferenta[30];
//int dif_max=0;
int cif;
int nr1,nr2;
FILE* fd;
int num,dif;
unsigned char elemente[3000];
pthread_barrier_t bar;
pthread_mutex_t mtx=PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t cond1=PTHREAD_COND_INITIALIZER;
int flag=0;//1 cand trebuie procesate datele 0 cand trebuie citite

void clear(){
  for(int i=0;i<3000;i++)
	elemente[i]=0;
}

int is_full(){
  if(flag==1)
	return 1;
  return 0;
}

int is_empty(){
  if(flag==0)
	return 1;
  return 0;
}

void* citire(void* a){
  free(a);
  pthread_mutex_lock(&mtx);
  while(is_full()){
	pthread_cond_wait(&cond1,&mtx);
  }
  //clear();
  for(int i=0;i<3000;i++){
	fscanf(fd,"%hhd",&elemente[i]);
  }
  flag=1;
  pthread_cond_signal(&cond1);
  pthread_mutex_unlock(&mtx);
  return NULL;
}

void* procesare(void* a){
  int id=*(int*)a;
  free(a);
  pthread_mutex_lock(&mtx);
  while(is_empty()){
	pthread_cond_wait(&cond1,&mtx);
  }

  for(int i=0;i<3000-1;i++){
//	printf("%d %d %d %d\n",dif,elemente[i],elemente[i+1],cif);
	if(elemente[i]%10==cif && elemente[i+1]%10==cif)
	{
	  if(abs(elemente[i]-elemente[i+1])>dif){
	    nr1=elemente[i];
	    nr2=elemente[i+1];
	    dif=abs(elemente[i]-elemente[i+1]);
	}
  }
  }
  diferenta[id]=dif;
  //printf("Dif %d\n",diferenta[id]);
  flag=0;
  pthread_cond_signal(&cond1);
  pthread_mutex_unlock(&mtx);
  pthread_barrier_wait(&bar);
  return NULL;
}

int main(int argc,char* argv[]){
  fd=fopen("s2.txt","r");
  printf("C:");
  scanf("%d",&cif);
  printf("N:");
  scanf("%d",&num);
  int creare=num/3000;
  pthread_barrier_init(&bar,NULL,creare);
  pthread_t tc[creare];
  pthread_t tp[creare];
  for(int i=0;i<creare;i++){
	int* id=malloc(sizeof(int));
	int* id2=malloc(sizeof(int));
	*id=i;*id2=i;
	pthread_create(&tc[i],NULL,citire,id);
	pthread_create(&tp[i],NULL,procesare,id2);
  }
  for(int i=0;i<creare;i++){
	pthread_join(tc[i],NULL);
	pthread_join(tp[i],NULL);
  }
  pthread_mutex_destroy(&mtx);
  pthread_cond_destroy(&cond1);
   printf("Diferenta maxima absoluta este: %d intre %d si %d",dif,nr1,nr2);
  fclose(fd);
  return 0;
}
