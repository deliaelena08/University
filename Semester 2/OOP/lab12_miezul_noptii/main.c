/*
Se citesc de la tastatura un numar natural n si n propozitii care contin cel putin n cuvinte (nu se fac validari).
Sa se afiseze sirul format prin concatenarea cuvintelor de pe pozitia i din propozitia i, i=1,n (separate prin spatiu).
*/
#include <stdio.h>
#include <string.h>
//functia din fisierul modulAsm.asm
char* cuvantasmWord(int i, char sir[]);


int main() {
    char textfinal[101] = "";
    int i, n;

    printf("Introduceti numarul de propozitii: ");
    scanf("%d", &n);

    for (i = 1; i <= n; i++) {
        printf("Scrieti a %d propozitie: ", i);

        char sen[100];
        scanf(" %[^\n]s", sen);
        
        char *word = cuvantasmWord(i, sen);

        // Concatenam textfinal cu cuvantul obtinut la iteratia i
        strcat(textfinal, word);
        strcat(textfinal, " "); // Spatiu intre cuvinte
    }

    // Punem . la finalul propozitiei
    textfinal[strlen(textfinal) - 1] = '.';

    printf("Textul final este: %s\n", textfinal);

    return 0;
}