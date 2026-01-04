%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

extern int yylex();
extern int nr_linie;
extern FILE *yyin;

void yyerror(const char *s);
%}


%union {
    int index_ts;
}

%token KW_MAIN KW_IF KW_ELSE KW_WHILE KW_STRUCT
%token TIP_INTEGER TIP_FLOAT TIP_CHAR TIP_STRING TIP_BOOL
%token OP_ASIG OP_PLUS OP_MINUS OP_DIV OP_MOD OP_MUL OP_POW
%token OP_MAI_MARE OP_MAI_MIC OP_EG OP_DIF OP_NEG
%token KW_READ KW_WRITE

%token <index_ts> CONST
%token <index_ts> ID

%start program

%right OP_ASIG
%left OP_PLUS OP_MINUS
%left OP_MUL OP_DIV OP_MOD
%right OP_POW
%nonassoc OP_MAI_MARE OP_MAI_MIC OP_EG OP_DIF OP_NEG

%%

program: KW_MAIN '{' instructions '}'
       ;

instructions: instruction
            | instructions instruction
            ;

instruction: declarations
           | io
           | atribution
           | conditional_instr
           | repetitional_instr
           ;

declarations: declaration
            | declarations declaration
            ;

declaration: type list_id
           ;

type: TIP_INTEGER
    | TIP_FLOAT
    | TIP_CHAR
    | TIP_STRING
    | TIP_BOOL
    | struct_def
    ;

struct_def: KW_STRUCT '{' declarations '}'
          ;

list_id: ID
       | list_id ',' ID
       ;

io: KW_READ list_id
  | KW_WRITE list_id
  ;

atribution: ID OP_ASIG expression
          ;

expression: ID
          | CONST
          | expression operant ID
          | expression operant CONST
          | '(' expression operant ID ')'
          | '(' expression operant CONST ')'
          ;

operant: OP_PLUS
       | OP_MINUS
       | OP_DIV
       | OP_MOD
       | OP_MUL
       | OP_POW
       ;

conditional_instr: KW_IF conditions '{' instructions '}'
                 | KW_IF conditions '{' instructions '}' KW_ELSE '{' instructions '}'
                 ;

conditions: ID
          | CONST
          | conditions comparison ID
          | conditions comparison CONST
          ;

comparison: OP_MAI_MARE
          | OP_MAI_MIC
          | OP_DIF
          | OP_EG
          | OP_NEG
          ;

repetitional_instr: KW_WHILE conditions '{' instructions '}'
                  ;

%%

void yyerror(const char *s) {
    fprintf(stderr, "Eroare sintactica la linia %d: %s\n", nr_linie, s);
}

int main(int argc, char **argv) {
    if (argc < 2) {
        fprintf(stderr, "Utilizare: %s fisier.mlp\n", argv[0]);
        return 1;
    }

    FILE *f = fopen(argv[1], "r");
    if (!f) {
        perror("Eroare la deschiderea fisierului");
        return 1;
    }
    yyin = f;

    int result = yyparse();

    if (result == 0) {
        printf("Analiza Sintactica a reusit, programul este corect sintactic!\n");
    } else {
        printf("Analiza Sintactica a esuat\n");
    }

    fclose(f);
    return 0;
}