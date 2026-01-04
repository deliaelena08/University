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
  int buffer[sizeof(input)];  // Vector pentru a stoca numerele convertite   
  int count = 0; // Număr de numere găsite
  uint16_t suma=0;
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
  printf("Introduceti un sir de numere separate prin spațiu: ");
  fgets(input, sizeof(input), stdin);
  char *token = strtok(input, " ");
  errno=0;
  while (token != NULL ) {
	char* endptr;
        long num = strtol(token, &endptr, 10);
        // Verificăm input
        if (errno != 0) {
            printf("Eroare: '%s' nu este un număr valid.\n", token);
            return -1;
        }
        //adaugam in sirul de numere intregi
        buffer[count++] = (int)num;
        token = strtok(NULL, " ");
  }
    // Trimitem numărul de elemente
  send(c, &count, sizeof(int), 0);
    // Trimitem șirul
  send(c, buffer, count * sizeof(int), 0);
  recv(c, &suma, sizeof(int), 0);
  suma = ntohs(suma);
  printf("Suma numerelor trimise este: %d\n", suma);
  close(c);
  return 0;
}
