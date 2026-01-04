#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <errno.h>
int main(int argc, char* argv[]) {
  int c;
  struct sockaddr_in server;
  char input[1024];  // Buffer pentru a citi șirul de la tastatură
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
  int i=0;
  int l=0;
  printf("Introduceti un sir de caractere:\n");
  fgets(input, sizeof(input), stdin);
  printf("Introduceti pozitia:\n");
  scanf("%d",&i);
  printf("Introduceti lungimea:\n");
  scanf("%d",&l);
  if( (i>=strlen(input)) || (l>=strlen(input)-i+1) ){
	printf("Date invalide!");
	return -1;
  }
  input[strlen(input)-1]='\0';
  send(c,input,sizeof(char) * sizeof(input), 0);
  send(c,&i,sizeof(int),0);
  send(c,&l,sizeof(int),0);
  char subsir[1024];
  recv(c, subsir, sizeof(subsir), 0);
  printf("Subsirul cerut este: %s \n",subsir);
  close(c);
  return 0;
}
