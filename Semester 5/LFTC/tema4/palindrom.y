%{
#include <stdio.h>
#include <stdlib.h>

extern int yylex();
extern FILE *yyin;

void yyerror(const char *s);
%}

%start propozitie

%%

propozitie: palindrom
          ;

palindrom: '#'
         | 'a' palindrom 'a'
         | 'b' palindrom 'b'
         | 'c' palindrom 'c'
         | 'd' palindrom 'd'
         | 'e' palindrom 'e'
         | 'f' palindrom 'f'
         | 'g' palindrom 'g'
         | 'h' palindrom 'h'
         | 'i' palindrom 'i'
         | 'j' palindrom 'j'
         | 'k' palindrom 'k'
         | 'l' palindrom 'l'
         | 'm' palindrom 'm'
         | 'n' palindrom 'n'
         | 'o' palindrom 'o'
         | 'p' palindrom 'p'
         | 'q' palindrom 'q'
         | 'r' palindrom 'r'
         | 's' palindrom 's'
         | 't' palindrom 't'
         | 'u' palindrom 'u'
         | 'v' palindrom 'v'
         | 'w' palindrom 'w'
         | 'x' palindrom 'x'
         | 'y' palindrom 'y'
         | 'z' palindrom 'z'
         ;

%%

void yyerror(const char *s) {
    fprintf(stderr, "Propozitia nu contine un # la mijloc.\n");
}

int main(int argc, char **argv) {
    printf("Introduceti o propozitie cu caracterul # la mijlocul acesteia:\n");
    yyin = stdin;

    if (yyparse() == 0) {
        printf("Propozitia este un palindrom.\n");
    } else {
        printf("Propozitia nu este palindrom.\n");
    }
    return 0;
}