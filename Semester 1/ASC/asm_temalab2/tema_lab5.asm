;Se dau 2 siruri de octeti A si B. Sa se construiasca sirul R care sa contina elementele lui B in ordine inversa urmate de elementele impare ale lui A.
;Exemplu:
;A: 2, 1, 3, 3, 4, 2, 6
;B: 4, 5, 7, 6, 2, 1
;R: 1, 2, 6, 7, 5, 4, 1, 3, 3

bits 32
global start        
extern exit               
import exit msvcrt.dll    
segment data use32 class=data
    a db 2, 1, 3, 3, 4, 2, 6
    la equ $-a
    b db 4, 5, 7, 6, 2, 1
    lb equ $-b
    r times lb+la db 0
segment code use32 class=code
    start:
        mov ecx, lb;punem lungimea in ECX lungimea sirului B
        mov esi,0;variabila pentru incrementare
        
        jecxz Sfarsit
        Repeta1:
            mov al,[b+ecx-1]
            mov [r+esi],al;adaugam elementele sirului B in ordinea inversa
            inc esi
        loop Repeta1
        
        mov ecx, la ;punem lungimea in ECX lungimea sirului Al
        mov esi,0;variabila pentru incrementare
        mov ebx,lb;punem in ebx lungimea sirului B
        Repeta2:
            mov al,[a+esi];punem in al primul element din sirul A
            rcr al,1;rotim spre dreapta al, daca este par atunci cf=0 ,cf=1 altfel
            jae paritate;dcaa numarul e par nu l va adauga in r
                mov al,[a+esi]
                mov [r+ebx],al
                inc ebx
            paritate:
            inc esi
        loop Repeta2
        Sfarsit:
        push    dword 0     
        call    [exit]       
