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
  uint16_t count;
  while(1){
	count=0;
	for(int j=0;j<strlen(buffer);j++){
		buffer[j]='\0';
	}
	c=accept(s,(struct sockaddr *) &client,&l);
	printf("S-a conectat un client.\n");
	recv(c,&count,sizeof(count),0);
	recv(c,buffer,count*sizeof(char),0);
	//printf("Sirul primit de lungime %d este: %s\n",count,buffer);
	for(int i=0;2*i<strlen(buffer)-1;i++){
		char aux=buffer[i];
		buffer[i]=buffer[strlen(buffer)-i-1];
		buffer[strlen(buffer)-i-1]=aux;
	}
	//printf("(SERVER) Oglinditul este : %s\n",buffer);
	send(c,buffer,sizeof(char)*strlen(buffer),0);
        close(c);
  }
}
