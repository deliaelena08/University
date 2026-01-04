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
  char input[1024];
  uint16_t spaces=0;

  printf("Introduceti un sir de caractere: ");
  fgets(input, sizeof(input), stdin);
  input[strlen(input)-1]='\0';
  uint16_t lung=strlen(input);
  if(lung <1){
	printf("Input invalid\n");
	return 1;
  }
  lung=ntohs(lung);
  memset(&server, 0, sizeof(server));
  server.sin_port = htons(1234);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr("127.0.0.1");

  sendto(c, &lung, sizeof(lung), 0, (struct sockaddr *) &server, sizeof(server));
  sendto(c, input, sizeof(input), 0, (struct sockaddr *) &server, sizeof(server));
  socklen_t server_len = sizeof(server);
  recvfrom(c, &spaces, sizeof(spaces), MSG_WAITALL, (struct sockaddr*)&server, &server_len);
  spaces = ntohs(spaces);
//  printf("Boolean primit %d\n",prim);
  printf("Numarul de spatii este %hu\n",spaces);
  close(c);
}
