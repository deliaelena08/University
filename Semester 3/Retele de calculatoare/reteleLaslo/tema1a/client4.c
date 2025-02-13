#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <errno.h>
int main(int argc, char* argv[]) {
  int c;
  struct sockaddr_in server;
  char input1[1024],input2[1024],input[1024];
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

  printf("Introduceti primul sir de caractere:\n");
  fgets(input1, sizeof(input1), stdin);
  input1[strlen(input1)-1]='\0';
  printf("Introduceti al doilea sir de caractere:\n");
  fgets(input2,sizeof(input2),stdin);
  input2[strlen(input2)-1]='\0';

  uint16_t lung1=strlen(input1);
  uint16_t lung2=strlen(input2);
  send(c, &lung1, sizeof(lung1), 0);
  send(c, input1, sizeof(char) * lung1, 0);
  send(c, &lung2, sizeof(lung2), 0);
  send(c, input2, sizeof(char) * lung2, 0);
  int lung=lung1+lung2;
  recv(c, input, sizeof(char)*lung, 0);
  input[lung]='\0';
  printf("Sirul interclasat este: %s\n", input);
  close(c);
  return 0;
}
