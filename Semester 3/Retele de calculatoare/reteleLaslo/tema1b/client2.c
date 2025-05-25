#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <errno.h>
#include <ctype.h>

int is_number(const char* str){
  //printf("\n ---- %s ---- \n",str);
  if(str == NULL || *str == '\0'){
	return 0;
  }
  for(int i=0;str[i] != '\0' && str[i] != '\n';i++){
	//printf("intra cu %d",str[i]);
	if(!isdigit(str[i])){
		return 0;
	}
  }
  return 1;
}

int main(int argc, char* argv[]) {
  int c;
  struct sockaddr_in server;
  char input[1024];  // Buffer pentru a citi șirul de la tastatură
  int buffer[1024];  // Vector pentru a stoca numerele convertite   
  c = socket(AF_INET, SOCK_STREAM, 0);
  if (c < 0) {
    printf("Eroare la crearea socketului client\n");
    return 1;
  }

  memset(&server, 0, sizeof(server));
  server.sin_port = htons(8080);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr("172.30.106.247");
  if (connect(c, (struct sockaddr *) &server, sizeof(server)) < 0) {
	printf("Eroare la conectarea la server\n");
	return 1;
  }
  printf("Introduceti un sir de numere separate prin spațiu: \n");
  fgets(input, sizeof(input), stdin);
  int buffer_index=0;
  char *token = strtok(input, " ");
  while (token != NULL ) {
	if(is_number(token)){
	  buffer[buffer_index++]=atoi(token);
	} else {
	  printf("Input invalid %s . Se asteapta numere \n",token);
	  return 1;
	}
        token = strtok(NULL, " ");
  }
   // Trimitem numărul de elemente
  printf("Trimitem %d numere \n",buffer_index);
  send(c, &buffer_index, sizeof(int), 0);
  send(c, buffer,buffer_index * sizeof(int), 0);
  int suma=0;
  recv(c, &suma, sizeof(int), 0);
  printf("Suma numerelor trimise este: %d\n", suma);
  close(c);
  return 0;
}

