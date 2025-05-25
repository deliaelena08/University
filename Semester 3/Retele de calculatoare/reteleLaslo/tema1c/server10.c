#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <netdb.h>
#include <arpa/inet.h>

int main() {
    int s;
    struct sockaddr_in server, client;
    socklen_t l;
    uint32_t ip_address;
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
            printf("Eroare la primirea numelui de calculator\n");
            continue;
        }
        struct hostent *ent=gethostbyname(buffer);
	if(ent == NULL){
		printf("Numele calculatorului nu a putut fi gasit\n");
		ip_address=0;
	} else {
		struct in_addr **addr_list=(struct in_addr **) ent-> h_addr_list;
		ip_address=addr_list[0]->s_addr;//prima adresa IP gasita
        	if (sendto(s, &ip_address, sizeof(ip_address), 0, (struct sockaddr*)&client, l) < 0) {
            	  printf("Eroare la trimiterea Ip-ului\n");
        	}
        	else if(ip_address != 0) {
        	  printf("Adresa IP %s a fost trimisa cu succes\n",inet_ntoa(*(struct in_addr *)&ip_address));
        	} else {
		  printf("Eroare: Ip-ul nu a fost gasit pentr %s\n",buffer);
		}
	}
    }

    close(s);
    return 0;
}
