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
    int s;
    struct sockaddr_in server, client;
    socklen_t l;
    char buffer[100];
    uint16_t lung,spaces;

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
        spaces=0;
        l = sizeof(client);
        memset(&client, 0, sizeof(client));

        if (recvfrom(s, &lung, sizeof(lung), MSG_WAITALL, (struct sockaddr*)&client, &l) < 0) {
            printf("Eroare la primirea lungimii numar\n");
            continue;
        }
        if (recvfrom(s,buffer,sizeof(buffer), MSG_WAITALL, (struct sockaddr *) &client, &l) < 0){
 	    printf("Eroare la primirea sirului\n");
	    continue;
	}
        lung = ntohs(lung);
        printf("Numarul de caractere este: %hu\n", lung);
	printf("Sirul de caractere este %s\n",buffer);
        for(int i=0;i<lung;i++){
	  if(buffer[i] == ' '){
	     spaces++;
	   }
	}
        printf("Numar de spatii trimise: %hu\n", spaces);
        spaces = htons(spaces);

        if (sendto(s, &spaces, sizeof(spaces), 0, (struct sockaddr*)&client, l) < 0) {
            printf("Eroare la trimiterea numarului\n");
        }
        else {
            printf("Numarul a fost trimis cu succes\n");
        }
    }

    close(s);
    return 0;
}
