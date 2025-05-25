#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>

int main() {
    int s;
    struct sockaddr_in server, client;
    socklen_t l;

    s = socket(AF_INET, SOCK_STREAM, 0);
    if (s < 0) {
        perror("Eroare la crearea socketului server");
        return 1;
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;

    if (bind(s, (struct sockaddr*)&server, sizeof(server)) < 0) {
        perror("Eroare la bind");
        close(s);
        return 1;
    }

    listen(s, 5);
    l = sizeof(client);
    memset(&client, 0, l);

    int c;
    while (1) {
        c = accept(s, (struct sockaddr*)&client, &l);
        if (c < 0) {
            perror("Eroare la accept");
            continue;
        }
        printf("S-a conectat un client.\n");

        pid_t pid = fork();
        if (pid < 0) {
            perror("Eroare la fork");
            close(c);
            continue;
        }
        else if (pid == 0) {  // Proces copil
            close(s);
            int spaces=0;
	    int count=0;
            if (recv(c, &count, sizeof(int), MSG_WAITALL) <= 0) {
                perror("Eroare la recv");
                close(c);
                exit(1);
            }
	    count=ntohl(count);
            printf("%d\n", count);
            char buffer[count];
            if (recv(c, buffer, count * sizeof(char), MSG_WAITALL) <= 0) {
                perror("Eroare la recv pentru buffer");
                close(c);
                exit(1);
            }

            for (int i = 0; i < count; i++) {
		if(buffer[i] == ' ')
		  spaces++;
                //printf("Caracterul %d este %c \n", i + 1, buffer[i]);
            }

            printf("Serverul a primit %d caractere,\n iar numarul de spatii este %d \n", count, spaces);
            spaces = htonl(spaces);
            if (send(c, &spaces, sizeof(spaces), 0) <= 0) {
                perror("Eroare la trimiterea sumei");
            }

            close(c);
            exit(0);
        }
        else { // Proces parinte
            close(c);
        }
    }

    close(s);
    return 0;
}
