#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

int main() {
    int sock;
    struct sockaddr_in server;
    char input[255];  // Buffer pentru a citi șirul de la tastatură
    sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        printf("Eroare la crearea socketului client\n");
        return 1;
    }
    memset(&server, 0, sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr("127.0.0.1");

    if (connect(sock, (struct sockaddr *)&server, sizeof(server)) < 0) {
        printf("Eroare la conectarea la server\n");
        return 1;
    }

    // Citim un șir de la utilizator
    printf("Introduceti un sir de caractere: \n");
    fgets(input, sizeof(input), stdin);
    input[strcspn(input, "\n")] = 0;
    printf("Sirul citit este: %s\nlungimea: %ld\n",input,strlen(input));
    int length = strlen(input);
    //length=htonl(length);
    send(sock, &length, sizeof(int), 0);
    send(sock, input, length*sizeof(char), 0);

    // Primim lungimea șirului inversat
    int reversedLength;
    recv(sock, &reversedLength, sizeof(int), 0);
    reversedLength = ntohl(reversedLength);
    // Primim șirul inversat
    char reversedString[reversedLength + 1];
    recv(sock, reversedString, reversedLength*sizeof(char), 0);
    reversedString[reversedLength] = '\0';

    printf("Sirul inversat primit de la server: %s\n", reversedString);

    close(sock);
    return 0;
}
