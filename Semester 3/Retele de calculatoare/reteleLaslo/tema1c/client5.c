#include <sys/types.h>
#include <sys/socket.h>
#include <stdbool.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

int main() {
    int c;
    struct sockaddr_in server;

    c = socket(AF_INET, SOCK_DGRAM, 0);
    if (c < 0) {
        printf("Eroare la crearea socketului client\n");
        return 1;
    }

    uint8_t a;
    uint16_t divizori[100], nrd;

    printf("Introduceti un numar reprezentat pe un octet(fara semn): ");
    scanf("%hhu", &a);

    if (a < 1) {
        printf("Input invalid, accept siruri cu mai mult de un caracter\n");
        return 1;
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr("127.0.0.1");
    sendto(c, &a, sizeof(a), 0, (struct sockaddr*)&server, sizeof(server));

    socklen_t server_len = sizeof(server);
    recvfrom(c, &nrd, sizeof(nrd), MSG_WAITALL, (struct sockaddr*)&server, &server_len);
    nrd = ntohs(nrd);
    recvfrom(c, divizori, sizeof(divizori), MSG_WAITALL, (struct sockaddr*)&server, &server_len);

    printf("Divizorii sunt: ");
    for (int i = 0; i < nrd; i++) {
        divizori[i] = ntohs(divizori[i]);
        printf("%hu ", divizori[i]);
    }
    printf("\n");

    close(c);
    return 0;
}
