#include<stdio.h>
int main()
{
int numar;
printf("Introduceti numarul");
scanf ("%d",&numar);
int coins=0;
while(numar>=5)
	numar=numar-5,coins++;
while(numar>=2)
	numar=numar-2,coins++;
while(numar>0)
	numar=numar-1,coins++;
printf("Numarul de monede minime este %d\n",coins);
return 0;
}

