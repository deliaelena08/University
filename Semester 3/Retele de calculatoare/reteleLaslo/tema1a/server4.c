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
  char buffer1[1024],buffer2[1024],sir[1024];
  int c;
  uint16_t count1,count2;
  while(1){
	count1=0;
	count2=0;
	for(int j=0;j<strlen(sir);j++){
		buffer1[j]='\0';
		buffer2[j]='\0';
		sir[j]='\0';
	}
	c=accept(s,(struct sockaddr *) &client,&l);
	printf("S-a conectat un client.\n");
	recv(c,&count1,sizeof(count1),0);
	recv(c,buffer1,count1*sizeof(char),0);
	recv(c,&count2,sizeof(count2),0);
	recv(c,buffer2,count2*sizeof(char),0);
	printf("Primul sir de lungime %d este: %s\n",count1,buffer1);
	printf("Al doilea sir de lungime %d este: %s\n",count2,buffer2);

	int i=0;
	int j=0;
	int k=0;
	while(i<count1 && j<count2){
	  if(buffer1[i]<buffer2[j]){
		sir[k++]=buffer1[i];
	  	i++;
	  }
	  else{
		sir[k++]=buffer2[j];
	  	j++;
	  }
	}
	while(i<count1)
	  sir[k++]=buffer1[i++];

	while(j<count2)
	  sir[k++]=buffer2[j++];
	sir[k]='\0';

	printf("(SERVER) Sirul este : %s\n",sir);
	send(c,sir,sizeof(char)*strlen(sir),0);
        close(c);
  }
}
