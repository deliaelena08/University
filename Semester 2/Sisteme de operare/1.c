#include<stdio.h>
int main()
{
float r,pi=3.14159;
printf("Introduceti lungimea razei:");
scanf("%f",&r);
float aria1=r*r*pi;
printf("Aria cercului este %f \n",aria1);
double aria2= aria1;
printf("Aria cercului este %lf \n",aria2);
if (aria2>aria1)
	printf("%f>%f\n",aria2,aria1);
else
	printf("%f<%f\n",aria2,aria1);
}
