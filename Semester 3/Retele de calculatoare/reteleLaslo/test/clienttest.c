#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <errno.h>
#include <ctype.h>

int is_number(const char* str) {
    if (str == NULL || *str == '\0') {
        return 0;
    }
    for (int i = 0; str[i] != '\0'; i++) {
        if (!isdigit(str[i])) {
            return 0;
        }
    }
    return 1;
}

int main(int argc, char* argv[]) {
    if (argc < 3) {
        printf("Usage: %s <server_ip> <port>\n", argv[0]);
        return 1;
    }

    int port = atoi(argv[2]);
    if (port <= 0 || port > 65535) {
        printf("Invalid port number.\n");
        return 1;
    }

    int c;
    struct sockaddr_in server;
    char input[1024];  
    int buffer[1024];  

    c = socket(AF_INET, SOCK_STREAM, 0);
    if (c < 0) {
        printf("Error creating client socket\n");
        return 1;
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(port);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr(argv[1]);

    if (connect(c, (struct sockaddr*)&server, sizeof(server)) < 0) {
        printf("Error connecting to server\n");
        return 1;
    }

    printf("Introduceti un sir de numere: \n");
    fgets(input, sizeof(input), stdin);

    size_t len = strlen(input);
    if (len > 0 && input[len - 1] == '\n') {
        input[len - 1] = '\0';
    }

    int buffer_index = 0;
    char* token = strtok(input, " ");
    while (token != NULL) {
        if (is_number(token)) {
            buffer[buffer_index++] = atoi(token);
        }
        else {
            printf("Input invalid %s . Se asteapta numere \n", token);
            close(c);
            return 1;
        }
        token = strtok(NULL, " ");
    }

    printf("Trimitem %d numere\n", buffer_index);
    send(c, &buffer_index, sizeof(int), 0);
    send(c, buffer, buffer_index * sizeof(int), 0);
    close(c);
    return 0;
}
