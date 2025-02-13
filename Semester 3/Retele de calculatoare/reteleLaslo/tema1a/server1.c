#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>

int main() {
  int s;
  struct sockaddr_in server, client;
  socklen_t l;

  s = socket(AF_INET, SOCK_STREAM, 0);
  if (s < 0) {
    printf("Eroare la crearea socketului server\n");
    return 1;
  }

  memset(&server, 0, sizeof(server));
  server.sin_port = htons(1234);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = INADDR_ANY;

  if (bind(s, (struct sockaddr *) &server, sizeof(server)) < 0) {
    printf("Eroare la bind\n");
    return 1;
  }
  listen(s,5);
  l=sizeof(client);
  memset(&client,0,l);

  int c;
  while(1){
	c=accept(s,(struct sockaddr *) &client,&l);
	printf("S-a conectat un client.\n");
	if(fork()==0){
	  int count=0;
	  uint16_t suma=0;
	  int c1=c;
	  recv(c1,&count,sizeof(int),0);
	  count = ntohl(count);
      
	  int buffer[count];
	  recv(c1,buffer,count*sizeof(int),0);
  	  for(int i=0;i<count;i++){
		buffer[i] = ntohl(buffer[i]);
	  	suma+=buffer[i];
	  }
  	  printf("Serverul a privit %d numere iar suma este %d \n",count,suma);
	  suma=htonl(suma);
	  if (send(c1, &suma, sizeof(suma), 0) < 0) {
		  printf("Eroare la trimitere");
	  }
	  else {
		  printf("Trimis cu success");
	  }
	  for(int i=0;i<count;i++){
		buffer[i]=0;
	  }
          close(c1);
	  return 0;
      }
  }
}
