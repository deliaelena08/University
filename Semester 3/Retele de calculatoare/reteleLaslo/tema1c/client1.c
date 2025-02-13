#include <sys/types.h>
#include <sys/socket.h>
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
  uint16_t suma,a,b;
  printf("a=");
  scanf("%hu",&a);
  printf("b=");
  scanf("%hu",&b);
  a=htons(a);
  b=htons(b);

  memset(&server, 0, sizeof(server));
  server.sin_port = htons(1234);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr("127.0.0.1");

  sendto(c, &a, sizeof(a), 0, (struct sockaddr *) &server, sizeof(server));
  sendto(c, &b, sizeof(b), 0, (struct sockaddr *) &server,sizeof(server));
  socklen_t server_len = sizeof(server);
  recvfrom(c, &suma, sizeof(suma), MSG_WAITALL, (struct sockaddr*)&server, &server_len);
  suma = ntohs(suma);
  printf("a+b=%hu\n", suma);
  close(c);
}
