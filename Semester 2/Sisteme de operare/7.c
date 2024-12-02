#include<stdio.h>
int main()
{
int d1,d2;
printf("Introduceti lungimile diagonalelor:");
scanf("%d %d",&d1,&d2);
printf("Aria rombului este egala cu %d\n",(d1*d2)/2);
int cateta1=d1/2;
cateta1=cateta1*cateta1;
int cateta2=d2/2;
cateta2=cateta2*cateta2;
int l=1;
while(l*l<(cateta1+cateta2))
	l++;
printf("Perimetrul rombului este %d\n",4*l);
return 0;
}
