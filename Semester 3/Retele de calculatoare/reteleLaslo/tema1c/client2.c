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
  int a;
  bool prim;
  printf("Introduceti un numar: ");
  scanf("%d",&a);
  if(a<1){
	printf("Input invalid (nr >0)\n");
	return 1;
  }
  a=htons(a);

  memset(&server, 0, sizeof(server));
  server.sin_port = htons(1234);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr("127.0.0.1");

  sendto(c, &a, sizeof(a), 0, (struct sockaddr *) &server, sizeof(server));
  socklen_t server_len = sizeof(server);
  recvfrom(c, &prim, sizeof(prim), MSG_WAITALL, (struct sockaddr*)&server, &server_len);
  prim = ntohs(prim);
//  printf("Boolean primit %d\n",prim);
  if(prim == 1){
  	printf("Numarul primit este prim\n");
  } else {
	printf("Numarul primit nu este prim\n");
  }
  close(c);
}
