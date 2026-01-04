#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <stdlib.h>

int main() {
    int c;
    struct sockaddr_in server;

    c = socket(AF_INET, SOCK_DGRAM, 0);
    if (c < 0) {
        printf("Eroare la crearea socketului client\n");
        return 1;
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(4321);  
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr("127.0.0.1");  

    int request = 1;  
    sendto(c, &request, sizeof(request), 0, (struct sockaddr*)&server, sizeof(server));

    int max_min;
    socklen_t server_len = sizeof(server);
    if (recvfrom(c, &max_min, sizeof(max_min), MSG_WAITALL, (struct sockaddr*)&server, &server_len) < 0) {
        perror("Eroare la primirea răspunsului de la server");
        close(c);
        return 1;
    }

    max_min = ntohl(max_min);  
    printf("Maximul minimelor primit de la server este: %d\n", max_min);
    close(c);
    return 0;
}
