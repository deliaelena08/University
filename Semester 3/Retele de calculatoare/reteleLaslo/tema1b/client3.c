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
  int spaces=0;
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
  input[strcspn(input, "\n")] = '\0';
  int lung=strlen(input);
  send(c,&lung,sizeof(int),0);
  send(c, input,  sizeof(input) * sizeof(char), 0);
  recv(c, &spaces, sizeof(int), 0);
  spaces = ntohl(spaces);
  printf("Numarul de spatii este: %d\n", spaces);
  close(c);
  return 0;
}
