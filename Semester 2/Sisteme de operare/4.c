#include<stdio.h>
int main()
{
float nr;
printf("Introduceti numarul de centrimetrii:\n");
scanf("%f",&nr);
float inch=2.57*nr;
printf("Numarul de centrimetrii convertit in ich este %f\n",inch);
int feet=inch*12;
printf("Numarul de centrimetrii convertiti in feet este %d\n",feet);
return 0;
}
