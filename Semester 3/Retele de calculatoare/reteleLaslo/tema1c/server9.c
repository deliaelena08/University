#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <time.h>

int main() {
    int s;
    struct sockaddr_in server, client;
    socklen_t l;
    uint32_t current_time;
    char buffer[100];

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

        if (recvfrom(s, buffer, sizeof(buffer), MSG_WAITALL, (struct sockaddr*)&client, &l) < 0) {
            printf("Eroare la primirea cererii\n");
            continue;
        }
        current_time=(uint32_t) time(NULL);// inceputul epocii Unix 1 ianuarie 1970
	uint32_t days=(current_time)/(24*3600);
	uint32_t seconds=current_time%(24*3600);
	days=ntohl(days);
	seconds=ntohl(seconds);
        if (sendto(s, &days, sizeof(days), 0, (struct sockaddr*)&client, l) < 0) {
            printf("Eroare la trimiterea zilelor\n");
        }
        else {
            printf("Numarul zilelor a fost trimis cu succes\n");
        }
	if(sendto(s,&seconds,sizeof(seconds),0,(struct sockaddr*)&client,l)<0){
		printf("Eroare la trimiterea secundelor");
	} else {
		printf("Numarul de secunde a fost trimis\n");
	}
    }

    close(s);
    return 0;
}
