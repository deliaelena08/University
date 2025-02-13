bits 32 ; 

;Se dau cuvintele A si B. Se cere dublucuvantul C:
;bitii 0-2 ai lui C au valoarea 0
;bitii 3-5 ai lui C au valoarea 1
;bitii 6-9 ai lui C coincid cu bitii 11-14 ai lui A
;bitii 10-15 ai lui C coincid cu bitii 1-6 ai lui B
;bitii 16-31 ai lui C au valoarea 1

global start        
extern exit              
import exit msvcrt.dll    
segment data use32 class=data
    a dw 5678h
    b dw 1234h
    c dd 0
segment code use32 class=code
    start:
        mov eax,0;
        and eax,1111111111111111111111111111000b;izolam bitii 0-2

        or eax,0000000000000000000000000111000b;bitii 3-5 a lui c vor fi 1
        mov ebx,eax;ebx vom avea pe bitii 3-5 valoarea 1 si bitii 0-2 valoarea 0
        
        mov eax,0
        mov ax,[a]
        and ax,0111100000000000b;punem in eax bitii 11-14 ai lui A
        ror ax,5;rotim 5 pozitii spre dreapta
        or ebx,eax; in ebx pe bitii 6-9 coincid cu bitii 11-14 ai lui A
        
        mov eax,0
        mov ax,[b]
        and ax,0000000001111110b;izolam bitii 1-6 din B 
        rol ax,9;rotim 9 pozitii spre stanga
        or ebx,eax; in ebx pe bitii 10-15 coincid cu bitii 1-6 ai lui B
        
        or ebx,11111111111111110000000000000000b;biti 31-16 a lui ebx vor avea valoarea 1
        
        mov [c],ebx;punem valoarea din registru in variabila rezultat
    
        ; exit(0)
        push    dword 0      
        call    [exit]       
