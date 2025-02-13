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
    int a;
    bool prim;

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
        prim=1;
        l = sizeof(client);
        memset(&client, 0, sizeof(client));

        if (recvfrom(s, &a, sizeof(a), MSG_WAITALL, (struct sockaddr*)&client, &l) < 0) {
            printf("Eroare la primirea primului numar\n");
            continue;
        }
        a = ntohs(a);
        printf("Numarul este: %hu\n", a);
        for(int i=2;i*i<=a;i++){
	  if(a%i==0){
	     prim=0;
	     break;
	   }
	}
        printf("Booleanul trimis va fi: %d\n", prim);
        prim = htons(prim);

        if (sendto(s, &prim, sizeof(prim), 0, (struct sockaddr*)&client, l) < 0) {
            printf("Eroare la trimiterea booleanului\n");
        }
        else {
            printf("Booleanul a fost trimis cu succes\n");
        }
    }

    close(s);
    return 0;
}
