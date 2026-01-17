/* A Bison parser, made by GNU Bison 3.8.2.  */

/* Bison interface for Yacc-like parsers in C

   Copyright (C) 1984, 1989-1990, 2000-2015, 2018-2021 Free Software Foundation,
   Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* DO NOT RELY ON FEATURES THAT ARE NOT DOCUMENTED in the manual,
   especially those whose name start with YY_ or yy_.  They are
   private implementation details that can be changed or removed.  */

#ifndef YY_YY_TRANSLATOR_TAB_H_INCLUDED
# define YY_YY_TRANSLATOR_TAB_H_INCLUDED
/* Debug traces.  */
#ifndef YYDEBUG
# define YYDEBUG 0
#endif
#if YYDEBUG
extern int yydebug;
#endif

/* Token kinds.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
  enum yytokentype
  {
    YYEMPTY = -2,
    YYEOF = 0,                     /* "end of file"  */
    YYerror = 256,                 /* error  */
    YYUNDEF = 257,                 /* "invalid token"  */
    KW_MAIN = 258,                 /* KW_MAIN  */
    KW_IF = 259,                   /* KW_IF  */
    KW_ELSE = 260,                 /* KW_ELSE  */
    KW_WHILE = 261,                /* KW_WHILE  */
    KW_STRUCT = 262,               /* KW_STRUCT  */
    TIP_INTEGER = 263,             /* TIP_INTEGER  */
    TIP_FLOAT = 264,               /* TIP_FLOAT  */
    TIP_CHAR = 265,                /* TIP_CHAR  */
    TIP_STRING = 266,              /* TIP_STRING  */
    TIP_BOOL = 267,                /* TIP_BOOL  */
    OP_ASIG = 268,                 /* OP_ASIG  */
    OP_PLUS = 269,                 /* OP_PLUS  */
    OP_MINUS = 270,                /* OP_MINUS  */
    OP_DIV = 271,                  /* OP_DIV  */
    OP_MOD = 272,                  /* OP_MOD  */
    OP_MUL = 273,                  /* OP_MUL  */
    OP_POW = 274,                  /* OP_POW  */
    OP_MAI_MARE = 275,             /* OP_MAI_MARE  */
    OP_MAI_MIC = 276,              /* OP_MAI_MIC  */
    OP_EG = 277,                   /* OP_EG  */
    OP_DIF = 278,                  /* OP_DIF  */
    OP_NEG = 279,                  /* OP_NEG  */
    KW_READ = 280,                 /* KW_READ  */
    KW_WRITE = 281,                /* KW_WRITE  */
    CONST = 282,                   /* CONST  */
    ID = 283                       /* ID  */
  };
  typedef enum yytokentype yytoken_kind_t;
#endif

/* Value type.  */
#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
union YYSTYPE
{
#line 57 "translator.y"

    char* str_val;
    int int_val;

#line 97 "translator.tab.h"

};
typedef union YYSTYPE YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define YYSTYPE_IS_DECLARED 1
#endif


extern YYSTYPE yylval;


int yyparse (void);


#endif /* !YY_YY_TRANSLATOR_TAB_H_INCLUDED  */
