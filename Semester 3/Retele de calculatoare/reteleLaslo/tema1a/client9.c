#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <errno.h>
int main(int argc, char* argv[]) {
  int c;
  struct sockaddr_in server;
  char inp1[100],inp2[100];
  int sir1[100],sir2[100];
  c = socket(AF_INET, SOCK_STREAM, 0);
  if (c < 0) {
    printf("Eroare la crearea socketului client\n");
    return 1;
  }

  memset(&server, 0, sizeof(server));
  server.sin_port = htons(1234);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr("127.0.0.1");
  if (connect(c, (struct sockaddr *) &server, sizeof(server)) < 0) {
	printf("Eroare la conectarea la server\n");
	return 1;
  }

  printf("Introduceti primul sir de numere:\n");
  fgets(inp1, sizeof(inp1), stdin);
  int dim1=0;
  char* token=strtok(inp1," ");
  while(token!=NULL){
	int numar=atoi(token);
	sir1[dim1++]=numar;
	token=strtok(NULL," ");
  }

  printf("Introduceti al doilea sir de numere:\n");
  fgets(inp2,sizeof(inp2),stdin);
  int dim2=0;
  char* token2=strtok(inp2," ");
  while(token2!=NULL){
	int nr=atoi(token2);
	sir2[dim2++]=nr;
	token2=strtok(NULL," ");
  }

  send(c,&dim1,sizeof(int),0);
  send(c,sir1,sizeof(sir1)*sizeof(int), 0);
  send(c,&dim2,sizeof(int),0);
  send(c,sir2,sizeof(sir2)*sizeof(int),0);

  int necomune[100],dim;
  recv(c,&dim,sizeof(int),0);
  recv(c, necomune, sizeof(int)*sizeof(necomune), 0);
  printf("Numerele care nu se regasesc in al doilea sir sunt: \n");
  for(int i=0;i<dim;i++)
	printf("%d ",necomune[i]);
  printf("\n");
  close(c);
  return 0;
}
