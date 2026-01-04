%{
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define MAX_TS 100
#define MAX_LEN_TOKEN 64

#define TIP_ID 1
#define TIP_CONST_NUMERICA 2
#define TIP_CONST_STRING 3

struct IntrareTS {
    char nume[MAX_LEN_TOKEN];
    int index;
    int tip_element;
};

struct IntrareTS ts[MAX_TS];
int index_ts = 0;
int nr_linie = 1;


void eroare(const char* msg, const char* token) {
    printf("Eroare lexicala la linia %d: %s ('%s')\n", nr_linie, msg, token);
}

int cauta_token(const char* token) {
    for (int i = 0; i < index_ts; i++) {
        if (strcmp(ts[i].nume, token) == 0) {
            return ts[i].index;
        }
    }
    return -1;
}

int adauga_token(const char* token, int tip) {
    int index_gasit = cauta_token(token);
    if (index_gasit != -1) {
        return index_gasit;
    }

    if (index_ts < MAX_TS) {
        strcpy(ts[index_ts].nume, token);
        ts[index_ts].tip_element = tip;
        ts[index_ts].index = index_ts + 1;
        return ts[index_ts++].index;
    }
    return -99;
}

void afiseaza_ts() {
    printf("\n");
    printf("Index | Tip        | Nume Token\n");
    for (int i = 0; i < index_ts; i++) {
        const char* tip_str;
        if (ts[i].tip_element == TIP_ID) tip_str = "Identificator";
        else if (ts[i].tip_element == TIP_CONST_NUMERICA) tip_str = "Const. Num.";
        else if (ts[i].tip_element == TIP_CONST_STRING) tip_str = "Const. String";
        else tip_str = "Necunoscut";

        printf("%5d | %10s | %s\n", ts[i].index, tip_str, ts[i].nume);
    }
}
%}

%option noyywrap

%%
^\xEF\xBB\xBF         ;

"main"|"if"|"else"|"while"|"struct"|"cattimp"|"executa"|"sfcattimp" {
    printf("FIP: (0, 0) | TOKEN: CUVANT_CHEIE '%s' (linia %d)\n", yytext, nr_linie);
}

"integer"|"float"|"char"|"string"|"bool" {
    printf("FIP: (1, 0) | TOKEN: TIP '%s' (linia %d)\n", yytext, nr_linie);
}
"read"|"write"      {
    printf("FIP: (2, 0) | TOKEN: IO '%s' (linia %d)\n", yytext, nr_linie);
}

"<-"|"**"|">"|"<"|"=/"|"="|"!"|"+"|"-"|"*"|"/"|"%"  {
    printf("FIP: (3, 0) | TOKEN: OPERATOR '%s' (linia %d)\n", yytext, nr_linie);
}

"{"|"}"|"("|")"|"," {
    printf("FIP: (4, 0) | TOKEN: SEPARATOR '%s' (linia %d)\n", yytext, nr_linie);
}

[0-9]+(\.[0-9]+)?   {
    int index_const = adauga_token(yytext, TIP_CONST_NUMERICA);
    printf("FIP: (5, %d) | TOKEN: CONST_NUMERICA '%s' (linia %d) -> Index TS: %d\n",
           index_const, yytext, nr_linie, index_const);
}

\'[^']*\'|\"[^\"]*\" {
    int index_const = adauga_token(yytext, TIP_CONST_STRING);
    printf("FIP: (5, %d) | TOKEN: CONST_STRING '%s' (linia %d) -> Index TS: %d\n",
           index_const, yytext, nr_linie, index_const);
}

[a-zA-Z][a-zA-Z0-9_#$]* {
    if (strlen(yytext) > 8) {
        eroare("Identificator prea lung (max 8 caractere)", yytext);
    } else {
        int index_id = adauga_token(yytext, TIP_ID);
        printf("FIP: (6, %d) | TOKEN: IDENTIFICATOR '%s' (linia %d) -> Index TS: %d\n",
               index_id, yytext, nr_linie, index_id);
    }
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

    printf("Format FIP: (Cod Token, Index TS/Constanta)\n");
    printf("Coduri Token (Simulate): 0=KW, 1=TIP, 2=IO, 3=OP, 4=SEP, 5=CONST, 6=ID\n");
    printf("\n");

    yylex();

    afiseaza_ts();

    fclose(f);
    return 0;
}