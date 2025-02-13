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
    uint8_t a;
    uint16_t divizori[100], nrd;

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
        nrd = 0;
        l = sizeof(client);
        memset(&client, 0, sizeof(client));

        if (recvfrom(s, &a, sizeof(a), MSG_WAITALL, (struct sockaddr*)&client, &l) < 0) {
            printf("Eroare la primirea numarului\n");
            continue;
        }

        printf("Numarul primit este: %hhu\n", a);
        for (int i = 1; i <= a; i++) {
            if (a % i == 0) {
                divizori[nrd++] = i;
            }
        }

        printf("Numarul de divizori este: %d\n", nrd);
        nrd = htons(nrd);

        if (sendto(s, &nrd, sizeof(nrd), 0, (struct sockaddr*)&client, l) < 0) {
            printf("Eroare la trimiterea numarului de divizori\n");
        }
        else {
            printf("Numarul de divizori trimis cu succes\n");
        }

        for (int i = 0; i < nrd; i++) {
            divizori[i] = htons(divizori[i]);
        }
        if (sendto(s, divizori,sizeof(divizori), 0, (struct sockaddr*)&client, l) < 0) {
            printf("Eroare la trimiterea sirului de divizori\n");
        }
        else {
            printf("Sirul a fost trimis cu succes \n");
        }
    }
    close(s);
    return 0;
}
