%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "translator.tab.h" // Include definitiile de tokeni generate de Bison

// Fix pentru strdup pe Windows (daca folosesti MSVC/anumite versiuni GCC)
#ifdef _WIN32
#define strdup _strdup
#endif

int nr_linie = 1;

void eroare_lexicala(const char* msg, const char* token) {
    fprintf(stderr, "Eroare lexicala la linia %d: %s ('%s')\n", nr_linie, msg, token);
}
%}

%option noyywrap

%%
    /* Ignoram BOM-ul daca exista */
^\xEF\xBB\xBF         ;

    /* Cuvinte cheie */
"main"          { return KW_MAIN; }
"if"            { return KW_IF; }
"else"          { return KW_ELSE; }
"while"         { return KW_WHILE; }
"struct"        { return KW_STRUCT; }

    /* Tipuri de date */
"integer"       { return TIP_INTEGER; }
"float"         { return TIP_FLOAT; }
"char"          { return TIP_CHAR; }
"string"        { return TIP_STRING; }
"bool"          { return TIP_BOOL; }

    /* IO */
"read"          { return KW_READ; }
"write"         { return KW_WRITE; }

    /* Operatori */
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

    /* Separatori */
"{"             { return '{'; }
"}"             { return '}'; }
"("             { return '('; }
")"             { return ')'; }
","             { return ','; }

    /* Constante numerice */
[0-9]+(\.[0-9]+)? {
    yylval.str_val = strdup(yytext);
    return CONST;
}

    /* Identificatori */
[a-zA-Z][a-zA-Z0-9_$#]* {
    yylval.str_val = strdup(yytext);
    return ID;
}

    /* Spatii si linii noi */
[ \t\r]+        ;
\n              { nr_linie++; }

    /* Orice altceva e eroare */
.               { eroare_lexicala("Simbol necunoscut", yytext); }

%%