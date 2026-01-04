%{
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "analizator.tab.h"

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
    fprintf(stderr, "Eroare lexicala la linia %d: %s ('%s')\n", nr_linie, msg, token);
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

%%
^\xEF\xBB\xBF         ;

"main"          { return KW_MAIN; }
"if"            { return KW_IF; }
"else"          { return KW_ELSE; }
"while"         { return KW_WHILE; }
"struct"        { return KW_STRUCT; }

"integer"       { return TIP_INTEGER; }
"float"         { return TIP_FLOAT; }
"char"          { return TIP_CHAR; }
"string"        { return TIP_STRING; }
"bool"          { return TIP_BOOL; }

"read"          { return KW_READ; }
"write"         { return KW_WRITE; }

"<-"            { return OP_ASIG; }
"**"            { return OP_POW; }
">"             { return OP_MAI_MARE; }
"<"             { return OP_MAI_MIC; }
"=/"            { return OP_DIF; }
"="             { return OP_EG; }
"!"             { return OP_NEG; }
"+"             { return OP_PLUS; }
"-"             { return OP_MINUS; }
"*"             { return OP_MUL; }
"/"             { return OP_DIV; }
"%"             { return OP_MOD; }

"{"             { return '{'; }
"}"             { return '}'; }
"("             { return '('; }
")"             { return ')'; }
","             { return ','; }

([0-9]+(\.[0-9]+)?|\'[^']*\'|\"[^\"]*\") {
    int tip_const = (yytext[0] == '\'' || yytext[0] == '\"') ? TIP_CONST_STRING : TIP_CONST_NUMERICA;
    int index_const = adauga_token(yytext, tip_const);
    yylval.index_ts = index_const;
    return CONST;
}

[a-zA-Z][a-zA-Z0-9_$#]* {
    if (strlen(yytext) > 8) {
        eroare("Identificator prea lung (max 8 caractere)", yytext);
    }
    int index_id = adauga_token(yytext, TIP_ID);
    yylval.index_ts = index_id;
    return ID;
}


[ \t\r]+                    ;
\n                        { nr_linie++; }
.                         { eroare("Simbol necunoscut", yytext); }

%%

int yywrap() {
    return 1;
}