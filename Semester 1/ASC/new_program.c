#include<stdio.h>
int main(){

    int sum=0,n=0;
    printf("%s","Introduceti un numar de numere");
    scanf("%d",n);
    for (int i=1;i<=n,i++)
    {
        int numar=0;
        scanf("%d",numar);
        sum=sum+numar;
    }
    printf("%d",sum);

}