%{
#include <stdio.h>
#include "palindrom.tab.h"
#include <ctype.h>
%}

%option noyywrap

%%

[a-zA-Z]    {
                return tolower(yytext[0]);
            }

"#"         {
                return '#';
            }

.|\n        ;

%%