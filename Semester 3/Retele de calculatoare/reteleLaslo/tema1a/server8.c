#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
int main() {
  int s;
  struct sockaddr_in server, client;
  socklen_t l;

  s = socket(AF_INET, SOCK_STREAM, 0);
  if (s < 0) {
    printf("Eroare la crearea socketului server\n");
    return 1;
  }

  memset(&server, 0, sizeof(server));
  server.sin_port = htons(1234);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = INADDR_ANY;

  if (bind(s, (struct sockaddr *) &server, sizeof(server)) < 0) {
    printf("Eroare la bind\n");
    return 1;
  }
  listen(s,5);
  l=sizeof(client);
  memset(&client,0,l);
  int sir1[100],sir2[100],comune[100];
  int c,dim1,dim2;
  while(1){
	int dim=0;
	dim1=0;
	dim2=0;
	c=accept(s,(struct sockaddr *) &client,&l);
	printf("S-a conectat un client.\n");
	recv(c,&dim1,sizeof(int),0);
	//printf("%d\n",dim1);
	recv(c,sir1,sizeof(sir1)*sizeof(int),0);
	recv(c,&dim2,sizeof(int),0);
	recv(c,sir2,sizeof(sir2)*sizeof(int),0);
	//printf("%d\n",dim2);
	//printf("Dimensiuniile celor doua siruri sunt %d - %d",dim1,dim2);
	for(int i=0;i<dim1;i++){
	  for(int j=0;j<dim2;j++){
		if(sir1[i]==sir2[j]){
		  comune[dim++]=sir1[i];
		  break;
		}
	  }
	}
	//printf("Sirurile au %d numere comune",dim);
	send(c,&dim,sizeof(int),0);
	send(c,comune,sizeof(int)*dim,0);
        close(c);
  }
}
