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
  char buffer[1024],subsir[1024];
  int c;
  while(1){
	int i=0;
	int lg=0;
	for(int j=0;j<strlen(buffer);j++){
		buffer[j]='\0';
		subsir[j]='\0';
	}
	c=accept(s,(struct sockaddr *) &client,&l);
	printf("S-a conectat un client.\n");
	recv(c,buffer,sizeof(buffer)*sizeof(char),0);
	recv(c,&i,sizeof(int),0);
	recv(c,&lg,sizeof(int),0);
	i=i-1;
	int max=i+lg;
	int k=0;
	while(i<max)
	  subsir[k++]=buffer[i++];
	subsir[k]='\0';
	printf("(SERVER)Subsirul este: %s\n",subsir);
	send(c,subsir,sizeof(subsir)*sizeof(char),0);
        close(c);
  }
}
