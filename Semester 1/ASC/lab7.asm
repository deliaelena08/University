bits 32 
global start    
;           Lab 8
    
;    esp c   _________
;            ox4000....
;    esp+8   _________
;            4
;    esp+4   _________
;    esp     2  

;push dword2
;push dword4
;call aduna rezultatul e in eax
;add esp,8-golim stiva

extern exit              
import exit msvcrt.dll    
segment data use32 class=data
    format db "Ana are %d mere",0
segment code use32 class=code
func:
            inc eax
            ret
    start:
        
        mov eax,0
        call[func]
        mov eax,2;aici se va intoarce dupa functie
        ;printf("Ana are mere");
        ;printf("Ana are %d mere",10);
        ;printf("Ana are %s","10 mere");
        push dword format
        call[printf]
        add esp,4
        fopen("aNa.txt","r");
        ;eax=0 nu e deschis bine
        ;eax !=0 merge fisierul
        ;eax ecx edx se pot folosi in fisierul
        ;a resb 100
        ;fread(a,100,1,eax);
        ;eax-descriptor fisier
        
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
