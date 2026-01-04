#include<stdio.h>
int main()
{
int nr1,nr2;
printf("Introduceti 2 numere intregi:");
scanf("%d %d",&nr1,&nr2);
int sum=nr1+nr2;
printf("\n Suma numerelor este: %d",sum);
float media_aritmetica=1.0*(nr1+nr2)/2;
printf("\n Media aritmetica este: %f",media_aritmetica);
int suma_patratelor=nr1*nr1+nr2*nr2;
printf("\n Suma patratelor numerelor este: %d \n",suma_patratelor);
return 0;
}
