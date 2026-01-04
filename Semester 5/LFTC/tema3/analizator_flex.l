%{
#include <stdio.h>
#include <string.h>

int nr_linie = 1;

void eroare(const char* msg, const char* token) {
    printf("Eroare lexicala la linia %d: %s ('%s')\n", nr_linie, msg, token);
}
%}

%option noyywrap

%%
^\xEF\xBB\xBF         ;  /* ignora BOM-ul UTF-8 */
"main"              { printf("TOKEN: CUVANT_CHEIE '%s' (linia %d)\n", yytext, nr_linie); }
"integer"|"float"|"char"|"string"|"bool" { printf("TOKEN: TIP '%s' (linia %d)\n", yytext, nr_linie); }
"read"|"write"      { printf("TOKEN: IO '%s' (linia %d)\n", yytext, nr_linie); }
"if"|"else"|"while"|"struct"|"cattimp"|"executa"|"sfcattimp"{ printf("TOKEN: CUVANT_CHEIE '%s' (linia %d)\n", yytext, nr_linie); }

"<-"|"**"|">"|"<"|"=/"|"="|"!"|"+"|"-"|"*"|"/"|"%"  {
    printf("TOKEN: OPERATOR '%s' (linia %d)\n", yytext, nr_linie);
}

"{"|"}"|"("|")"|"," {
    printf("TOKEN: SEPARATOR '%s' (linia %d)\n", yytext, nr_linie);
}

[0-9]+(\.[0-9]+)?   { printf("TOKEN: CONST_NUMERICA '%s' (linia %d)\n", yytext, nr_linie); }

\'[^']*\'|\"[^\"]*\" { printf("TOKEN: CONST_STRING '%s' (linia %d)\n", yytext, nr_linie); }

[a-zA-Z][a-zA-Z0-9_#$]* {
    if (strlen(yytext) > 8)
        eroare("Identificator prea lung", yytext);
    else
        printf("TOKEN: IDENTIFICATOR '%s' (linia %d)\n", yytext, nr_linie);
}

[ \t\r]+                    ;
\n                        { nr_linie++; }
.                         { eroare("Simbol necunoscut", yytext); }

%%

int main(int argc, char **argv) {
    if (argc < 2) {
        printf("Utilizare: %s fisier.mlp\n", argv[0]);
        return 1;
    }

    FILE *f = fopen(argv[1], "r");
    if (!f) {
        perror("Eroare la deschiderea fisierului");
        return 1;
    }
    yyin = f;
    yylex();
    fclose(f);
    return 0;
}