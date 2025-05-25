#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>

int main() {
    int s;
    struct sockaddr_in server, client;
    socklen_t l;
    uint16_t suma, a, b;

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

        if (recvfrom(s, &a, sizeof(a), MSG_WAITALL, (struct sockaddr*)&client, &l) < 0) {
            printf("Eroare la primirea primului număr\n");
            continue;
        }
        a = ntohs(a);
        printf("Primul număr este: %hu\n", a);

        if (recvfrom(s, &b, sizeof(b), MSG_WAITALL, (struct sockaddr*)&client, &l) < 0) {
            printf("Eroare la primirea celui de-al doilea număr\n");
            continue;
        }
        b = ntohs(b);  
        printf("Al doilea număr este: %hu\n", b);

        suma = a + b;
        printf("Suma trimisă va fi: %hu\n", suma);
        suma = htons(suma); 

        if (sendto(s, &suma, sizeof(suma), 0, (struct sockaddr*)&client, l) < 0) {
            printf("Eroare la trimiterea sumei\n");
        }
        else {
            printf("Suma a fost trimisă cu succes\n");
        }
    }

    close(s);
    return 0;
}
