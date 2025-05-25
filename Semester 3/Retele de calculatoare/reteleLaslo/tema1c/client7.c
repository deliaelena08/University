#include <sys/types.h>
#include <sys/socket.h>
#include <stdbool.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

int main() {
  int c;
  struct sockaddr_in server;

  c = socket(AF_INET, SOCK_DGRAM, 0);
  if (c < 0) {
    printf("Eroare la crearea socketului client\n");
    return 1;
  }
  char input[100],buffer[100];
  int lg,poz;

  printf("Introduceti un sir de caractere: ");
  fgets(input, sizeof(input), stdin);
  input[strlen(input)-1]='\0';
  printf("Introduceti pozitia pentru subsir: ");
  scanf("%d",&poz);
  printf("Introduceti lungimea subsirului: ");
  scanf("%d",&lg);
  uint16_t lung=strlen(input);

  if(lung <1 || lg<=0 || poz<0){
	  printf("Input invalid\n");
	return 1;
  }
  if(lg+poz>lung){
	printf("Subsir de dimensiuni invalide\n");
	return 1;
  }

  memset(&server, 0, sizeof(server));
  server.sin_port = htons(1234);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr("172.18.41.62");

  sendto(c, input, sizeof(input), 0, (struct sockaddr *) &server, sizeof(server));
  poz=ntohs(poz);
  lg=ntohs(lg);
  sendto(c, &poz, sizeof(poz), 0, (struct sockaddr*) &server, sizeof(server));
  sendto(c, &lg, sizeof(lg), 0, (struct sockaddr*) &server,sizeof(server));
  socklen_t server_len = sizeof(server);
  recvfrom(c, buffer, sizeof(buffer), MSG_WAITALL, (struct sockaddr*)&server, &server_len);
  int ls=strlen(buffer);
  printf("Subsirul de %d caractere este: %s\n",ls,buffer);
  //printf("\n");
  close(c);
}
