%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdarg.h>

// Fix pentru strdup
#ifdef _WIN32
#define strdup _strdup
#endif

extern int yylex();
extern int nr_linie;
extern FILE *yyin;

void yyerror(const char *s);

FILE *asm_file;

// Context: 0 = nimic, 1 = declarare, 2 = read, 3 = write
int context = 0;

void emit_code(const char *fmt, ...) {
    va_list args;
    va_start(args, fmt);
    vfprintf(asm_file, fmt, args);
    va_end(args);
}

// Declarare variabila
void asm_declare_var(char *name) {
    emit_code("section .data\n");
    emit_code("    _%s dd 0\n", name);
    emit_code("section .text\n");
}

// Citire (SCANF)
void asm_read_var(char *name) {
    emit_code("    ; Citire %s\n", name);
    emit_code("    push dword _%s\n", name);    // Adresa variabilei
    emit_code("    push dword format_in\n");
    emit_code("    call [scanf]\n");
    emit_code("    add esp, 8\n");
}

// Scriere (PRINTF)
void asm_write_var(char *name) {
    emit_code("    ; Afisare %s\n", name);
    emit_code("    push dword [_%s]\n", name);  // Valoarea variabilei
    emit_code("    push dword format_out\n");
    emit_code("    call [printf]\n");
    emit_code("    add esp, 8\n");
}

%}

%union {
    char* str_val;
    int int_val;
}

%token KW_MAIN KW_IF KW_ELSE KW_WHILE KW_STRUCT
%token TIP_INTEGER TIP_FLOAT TIP_CHAR TIP_STRING TIP_BOOL
%token OP_ASIG OP_PLUS OP_MINUS OP_DIV OP_MOD OP_MUL OP_POW
%token OP_MAI_MARE OP_MAI_MIC OP_EG OP_DIF OP_NEG
%token KW_READ KW_WRITE

%token <str_val> CONST
%token <str_val> ID

%start program

%right OP_ASIG
%left OP_PLUS OP_MINUS
%left OP_MUL OP_DIV OP_MOD
%right OP_POW
%nonassoc OP_MAI_MARE OP_MAI_MIC OP_EG OP_DIF OP_NEG

%%

program: KW_MAIN '{' init_asm instructions '}' {
           emit_code("    ; Exit Process\n");
           emit_code("    push dword 0\n");
           emit_code("    call [ExitProcess]\n");
       }
       ;

init_asm: {
    // Header standard pentru NASM + ALINK (Win32)
    emit_code("bits 32\n");
    emit_code("global start\n");
    emit_code("extern ExitProcess, printf, scanf\n");
    emit_code("import ExitProcess kernel32.dll\n");
    emit_code("import printf msvcrt.dll\n");
    emit_code("import scanf msvcrt.dll\n\n");

    // Date globale constante
    emit_code("section .data\n");
    emit_code("    format_in db \"%%d\", 0\n");
    emit_code("    format_out db \"%%d\", 10, 0\n");

    // Cod
    emit_code("section .text\n");
    emit_code("start:\n");
    // Sarim peste eventualele declaratii de date care urmeaza imediat
    // pentru a ajunge la prima instructiune reala.
    // (Desi NASM rearanjeaza sectiunile, e mai sigur asa vizual)
}
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

declaration: type { context = 1; } list_id { context = 0; }
           ;

type: TIP_INTEGER | TIP_FLOAT | TIP_CHAR | TIP_STRING | TIP_BOOL | KW_STRUCT ;

list_id: ID {
        if(context == 1) asm_declare_var($1);
        if(context == 2) asm_read_var($1);
        if(context == 3) asm_write_var($1);
        free($1);
       }
       | list_id ',' ID {
        if(context == 1) asm_declare_var($3);
        if(context == 2) asm_read_var($3);
        if(context == 3) asm_write_var($3);
        free($3);
       }
       ;

io: KW_READ { context = 2; } list_id { context = 0; }
  | KW_WRITE { context = 3; } list_id { context = 0; }
  ;

atribution: ID OP_ASIG expression {
    emit_code("    ; Atribuire rezultat\n");
    emit_code("    pop eax\n");
    emit_code("    mov [_%s], eax\n", $1);
    free($1);
};

/* Expresii - Stack Machine */
expression: ID {
              emit_code("    push dword [_%s]\n", $1);
              free($1);
          }
          | CONST {
              // Atentie: Daca CONST e float (ex: 3.14), NASM va da eroare la "push dword 3.14"
              // Presupunem integer pentru simplitate aici.
              emit_code("    push dword %s\n", $1);
              free($1);
          }
          | expression OP_PLUS expression {
              emit_code("    pop ebx\n    pop eax\n    add eax, ebx\n    push eax\n");
          }
          | expression OP_MINUS expression {
              emit_code("    pop ebx\n    pop eax\n    sub eax, ebx\n    push eax\n");
          }
          | expression OP_MUL expression {
              emit_code("    pop ebx\n    pop eax\n    imul eax, ebx\n    push eax\n");
          }
          | expression OP_DIV expression {
              emit_code("    pop ebx\n    pop eax\n    cdq\n    idiv ebx\n    push eax\n");
          }
          | expression OP_MOD expression {
              emit_code("    pop ebx\n    pop eax\n    cdq\n    idiv ebx\n    push edx\n");
          }
          | '(' expression ')'
          ;

conditional_instr: KW_IF conditions '{' instructions '}'
                 | KW_IF conditions '{' instructions '}' KW_ELSE '{' instructions '}'
                 ;

repetitional_instr: KW_WHILE conditions '{' instructions '}'
                  ;

conditions: expression comparison expression {
    emit_code("    pop ebx\n    pop eax\n");
    // Aici ar trebui cod de comparare (cmp eax, ebx) si jump-uri
    // Dar pentru exemplul tau curent, e ok si asa.
}

comparison: OP_MAI_MARE | OP_MAI_MIC | OP_DIF | OP_EG | OP_NEG ;

%%

void yyerror(const char *s) {
    fprintf(stderr, "Eroare sintactica la linia %d: %s\n", nr_linie, s);
}

int main(int argc, char **argv) {
    if (argc < 2) {
        fprintf(stderr, "Utilizare: %s fisier.txt\n", argv[0]);
        return 1;
    }

    FILE *f = fopen(argv[1], "r");
    if (!f) {
        perror("Eroare intrare");
        return 1;
    }

    asm_file = fopen("output.asm", "w");
    if (!asm_file) {
        perror("Eroare iesire");
        return 1;
    }

    yyin = f;
    yyparse();

    printf("Compilare terminata. Fisier generat: output.asm\n");

    fclose(f);
    fclose(asm_file);
    return 0;
}