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
  char input[100],ch;
  int buffer[100];

  printf("Introduceti un sir de caractere: ");
  fgets(input, sizeof(input), stdin);
  input[strlen(input)-1]='\0';
  printf("Introduceti un caracter : ");
  scanf("%c",&ch);
  uint16_t lung=strlen(input);

  if(lung <1){
	  printf("Input invalid, accept siruri cu mai mult de un caracter\n");
	return 1;
  }
  lung=ntohs(lung);
  memset(&server, 0, sizeof(server));
  server.sin_port = htons(1234);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr("172.18.41.62");

  sendto(c, &lung, sizeof(lung), 0, (struct sockaddr *) &server, sizeof(server));
  sendto(c, input, sizeof(input), 0, (struct sockaddr *) &server, sizeof(server));
  sendto(c, &ch, sizeof(ch), 0, (struct sockaddr*) &server, sizeof(server));
  socklen_t server_len = sizeof(server);
  int lungime=0;
  recvfrom(c, &lungime, sizeof(lungime), MSG_WAITALL,  (struct sockaddr *) &server,&server_len);
  recvfrom(c, buffer, sizeof(buffer), MSG_WAITALL, (struct sockaddr*)&server, &server_len);
  printf("Pozitiile pe care %c se afla sunt:\n",ch);
  lungime=htons(lungime);
  for(int i=0;i<lungime;i++){
	printf("%d ",buffer[i]);
  }
  printf("\n");
  close(c);
}
