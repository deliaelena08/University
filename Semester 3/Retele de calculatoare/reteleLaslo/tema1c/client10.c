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
    char buffer[100];
    uint32_t ip_address;

    c = socket(AF_INET, SOCK_DGRAM, 0);
    if (c < 0) {
        printf("Eroare la crearea socketului client\n");
        return 1;
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr("172.18.41.62");

    if (sendto(c, buffer, sizeof(buffer), 0, (struct sockaddr*)&server, sizeof(server)) < 0) {
        printf("Eroare la trimiterea cererii\n");
        close(c);
        return 1;
    }
    printf("Introduceti un nume de calculator: \n");
    fgets(buffer,sizeof(buffer),stdin);
    buffer[strcspn(buffer,"\n")]='\0';
    if(sendto(c,buffer,sizeof(buffer),0,(struct sockaddr*)&server,sizeof(server))<0){
	printf("Eroare la trimiterea numelui de calculator\n");
	close(c);
	return 1;
    }
    socklen_t server_len = sizeof(server);
    if (recvfrom(c, &ip_address, sizeof(ip_address), MSG_WAITALL, (struct sockaddr*)&server, &server_len) < 0) {
        printf("Eroare la primirea adresei IP\n");
        close(c);
        return 1;
    }
    if(ip_address == 0){
	printf("Numele calculatorului nu a putut fi gasit \n");
    } else {
	struct in_addr ip_addr;
	ip_addr.s_addr = ip_address;
	printf("Adresa IP este: %s\n",inet_ntoa(ip_addr));
    }
    close(c);
    return 0;
}
