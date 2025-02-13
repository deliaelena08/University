#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <errno.h>
int main(int argc, char* argv[]) {
  int c;
  struct sockaddr_in server;
  char input[1024],ch;  // Buffer pentru a citi șirul de la tastatură
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
  printf("Introduceti un sir de caractere:\n");
  fgets(input, sizeof(input), stdin);
  printf("Introduceti un caracter:\n");
  scanf("%c",&ch);
  input[strlen(input)-1]='\0';
  uint16_t lung=strlen(input);
  send(c,&lung,sizeof(lung),0);
  send(c, input,  sizeof(char) * lung, 0);
  send(c,&ch,sizeof(char),0);
  int poz[100]={0};
  recv(c, poz, sizeof(poz), 0);
  printf("Pozitiile pe care caracterul '%c' se afla sunt: \n",ch);
  int j=0;
  while(poz[j]!=0){
	printf("%d ",poz[j++]);
  }
  printf("\n");
  close(c);
  return 0;
}
