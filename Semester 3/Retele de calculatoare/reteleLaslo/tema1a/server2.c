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
  char buffer[1024];
  int c;
  uint16_t spaces,count;
  while(1){
	spaces=0;
	c=accept(s,(struct sockaddr *) &client,&l);
	printf("S-a conectat un client.\n");
	recv(c,&count,sizeof(count),0);
	recv(c,buffer,count*sizeof(char),0);
	for(int i=0;i<count;i++){
	  if(buffer[i] == ' ')
		spaces++;
	}
	spaces=htons(spaces);
	send(c,&spaces,sizeof(spaces),0);
        close(c);
  }
}

