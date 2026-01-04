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
    int s,poz[100];
    struct sockaddr_in server, client;
    socklen_t l;
    char ch,buffer[100];
    uint16_t lung;

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
	lung=0;
        if (recvfrom(s, &lung, sizeof(lung), MSG_WAITALL, (struct sockaddr*)&client, &l) < 0) {
            printf("Eroare la primirea lungimii numar\n");
            continue;
        }
        if (recvfrom(s,buffer,sizeof(buffer), MSG_WAITALL, (struct sockaddr *) &client, &l) < 0){
 	    printf("Eroare la primirea sirului\n");
	    continue;
	}
	if (recvfrom(s, &ch, sizeof(ch), MSG_WAITALL, (struct sockaddr*)&client, &l)<0){
		printf("Eroare la primirea caracterului\n");
		continue;
	}
        lung = ntohs(lung);
	//ch=ntohs(ch);
        printf("Numarul de caractere este: %hu\n", lung);
	printf("Sirul de caractere este %s\n",buffer);
	printf("Caracterul este : %c\n",ch);
        int j=0;
	for(int i=0;i<lung;i++){
	  if(buffer[i] == ch){
		poz[j++]=i;
	  }
	}
        printf("Caracterul apare in %d locuri\n",j);
        j=htons(j);
	if (sendto(s, &j , sizeof(j), 0, (struct sockaddr*)&client, l) < 0) {
            printf("Eroare la trimiterea lungimii\n");
        }
        else {
            printf("Lungimea a fost trimisa cu succes\n");
        }
	if(sendto(s, poz, sizeof(poz), 0, (struct sockaddr*) &client, l)<0){
	  printf("Eroare la trimiterea listei\n");
	} else {
		printf("Sirul a fost trimis\n");
	}
    }

    close(s);
    return 0;
}
