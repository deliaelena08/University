#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <errno.h>
int main(int argc, char* argv[]) {
  int c;
  struct sockaddr_in server;
  int div[100]={0};
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
  printf("Introduceti un numar:\n");
  int nr=0;
  scanf("%d",&nr);
  send(c,&nr,sizeof(nr),0);
  recv(c, div, sizeof(int)*sizeof(div), 0);
  printf("Divizorii numarului %d sunt: \n",nr);
  int i=0;
  while(div[i]!=0){
	printf("%d ",div[i++]);
  }
  printf("\n");
  close(c);
  return 0;
}
