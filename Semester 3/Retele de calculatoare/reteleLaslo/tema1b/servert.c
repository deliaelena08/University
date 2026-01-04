#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>


int *global_min;

void handle_client(int client_socket) {
    int count = 0;

    if (recv(client_socket, &count, sizeof(int), MSG_WAITALL) <= 0) {
        perror("Error primire date");
        close(client_socket);
        exit(1);
    }

    printf("Am primit: %d numere\n", count);
    int buffer[count];

    if (recv(client_socket, buffer, count * sizeof(int), MSG_WAITALL) <= 0) {
        perror("Eroare primire sirul de date");
        close(client_socket);
        exit(1);
    } 
    int min = buffer[0];
    for (int i = 1; i < count; i++) {
        if (buffer[i] < min) {
            min = buffer[i];
        }
    }

    printf("Minimul este : %d \n",min);
  
    if (*global_min > min) {
            *global_min = min;
    }
    close(client_socket);
    exit(0);
}

void start_server(int port) {
    int s;
    struct sockaddr_in server, client;
    socklen_t l = sizeof(client);

    s = socket(AF_INET, SOCK_STREAM, 0);
    if (s < 0) {
        perror("Error creare server socket");
        exit(1);
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(port);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;

    if (bind(s, (struct sockaddr*)&server, sizeof(server)) < 0) {
        perror("Error conectare server socket");
        close(s);
        exit(1);
    }

    listen(s, 5);
    printf("Serverul asculta pe portul %d\n", port);

    while (1) {
        int client_socket = accept(s, (struct sockaddr*)&client, &l);
        if (client_socket < 0) {
            perror("Error accepta conectarea");
            continue;
        }

        pid_t pid = fork();
        if (pid < 0) {
            perror("Fork error");
            close(client_socket);
            continue;
        }
        else if (pid == 0) {  
            close(s); 
            handle_client(client_socket);
        }
        else {  
            close(client_socket);  
        }
    }

    close(s);
}

void start_udp_server(int udp_port) {
    int s;
    struct sockaddr_in server, client;
    socklen_t l = sizeof(client);

    s = socket(AF_INET, SOCK_DGRAM, 0);
    if (s < 0) {
        perror("Error creare server UDP");
        exit(1);
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(udp_port);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;

    if (bind(s, (struct sockaddr*)&server, sizeof(server)) < 0) {
        perror("Error bind UDP");
        close(s);
        exit(1);
    }

    printf("Serverul UDP asculta pe portul %d\n", udp_port);

    while (1) {
        l = sizeof(client);
        int request;
        //asteptam o  cerere
        if (recvfrom(s, &request, sizeof(request), MSG_WAITALL, (struct sockaddr*)&client, &l) < 0) {
            perror("Error primire cerere UDP");
            continue;
        }

        printf("Cerere primita de la client UDP\n");
        int min_to_send = htonl(*global_min);  
        if (sendto(s, &min_to_send, sizeof(min_to_send), 0, (struct sockaddr*)&client, l) < 0) {
            perror("Error trimitere minim prin UDP");
        }
        else {
            printf("Minimul global %d trimis catre clientul UDP\n", *global_min);
        }
    }

    close(s);
}

int main() {
    pid_t pid = fork();

    *global_min = 100;
    if (pid < 0) {
        perror("Fork error");
        return 1;
    }
    else if (pid == 0) {
        pid_t tcp_pid = fork();
        if (tcp_pid < 0) {
            perror("Fork error");
            return 1;
        }
        else if (tcp_pid == 0) {
            start_server(1234);
        }
        else {
            start_server(5678);
        }
    }
    else {
        start_udp_server(4321);
    }\
    return 0;
}
