#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

int main() {
    int c;
    struct sockaddr_in server;
    char buffer[10] = "request";
    uint32_t days_since_1970, seconds_today;

    c = socket(AF_INET, SOCK_DGRAM, 0);
    if (c < 0) {
        printf("Eroare la crearea socketului client\n");
        return 1;
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr("127.0.0.1");

    if (sendto(c, buffer, sizeof(buffer), 0, (struct sockaddr*)&server, sizeof(server)) < 0) {
        printf("Eroare la trimiterea cererii\n");
        close(c);
        return 1;
    }

    socklen_t server_len = sizeof(server);
    if (recvfrom(c, &days_since_1970, sizeof(days_since_1970), MSG_WAITALL, (struct sockaddr*)&server, &server_len) < 0) {
        printf("Eroare la primirea numarului de zile\n");
        close(c);
        return 1;
    }
    days_since_1970 = ntohl(days_since_1970); 

    if (recvfrom(c, &seconds_today, sizeof(seconds_today), MSG_WAITALL, (struct sockaddr*)&server, &server_len) < 0) {
        printf("Eroare la primirea numarului de secunde\n");
        close(c);
        return 1;
    }
    seconds_today = ntohl(seconds_today);
    printf("Numarul de zile de la 1 ianuarie 1970: %u\n", days_since_1970);
    printf("Numarul de secunde din ziua curenta: %u\n", seconds_today);

    close(c);
    return 0;
}
