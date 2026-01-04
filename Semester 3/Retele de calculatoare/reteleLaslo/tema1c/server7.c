#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdbool.h>

int main() {
    int s,lg,poz;
    struct sockaddr_in server, client;
    socklen_t l;
    char subsir[100],buffer[100];

    s = socket(AF_INET, SOCK_DGRAM, 0);
    if (s < 0) {
        printf("Eroare la crearea socketului server\n");
        return 1;
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;

    if (bind(s, (struct sockaddr*)&server, sizeof(server)) < 0) {
        printf("Eroare la bind\n");
        close(s);
        return 1;
    }

    while (1) {
        l = sizeof(client);
        memset(&client, 0, sizeof(client));

        if (recvfrom(s,buffer,sizeof(buffer), MSG_WAITALL, (struct sockaddr *) &client, &l) < 0){
 	    printf("Eroare la primirea sirului\n");
	    continue;
	}
	if (recvfrom(s, &poz, sizeof(poz), MSG_WAITALL, (struct sockaddr*)&client, &l)<0){
		printf("Eroare la primirea pozitiei de start\n");
		continue;
	}
  	if (recvfrom(s, &lg, sizeof(lg), MSG_WAITALL, (struct sockaddr*)&client,&l)<0){
		printf("Eroare la primirea lungimii subsirului\n");
		continue;
	}

	poz=ntohs(poz);
	lg=ntohs(lg);
	int i=0;
	int n=lg+poz;
	while(poz<n)
	  subsir[i++]=buffer[poz++];
	subsir[strlen(subsir)]='\0';
        printf("Subsirul este: %s\n", subsir);
	if(sendto(s, subsir, sizeof(subsir), 0, (struct sockaddr*) &client, l)<0){
	  printf("Eroare la trimiterea subsirului\n");
	} else {
		printf("Sirul a fost trimis\n");
	}
    }

    close(s);
    return 0;
}
