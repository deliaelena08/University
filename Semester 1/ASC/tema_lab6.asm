;Se da un sir S de dublucuvinte.
;Sa se obtina sirul D format din octetii dublucuvintelor din sirul D sortati in ordine descrescatoare in interpretarea fara semn.
;s DD 12345607h, 1A2B3C15h
;d DB 56h, 3Ch, 34h, 2Bh, 1Ah, 15h, 12h, 07h

bits 32 
global start        
extern exit              
import exit msvcrt.dll    
segment data use32 class=data
    s dd 12345607h, 1A2B3C15h
    ls equ ($-s)/4
    d times ls db 0

segment code use32 class=code
    start:
        mov ecx, ls;punem lungimea in ECX lungimea sirului B
        mov esi,s;variabila pentru incrementare
        
        jecxz sfarsit1
        cld
        repeta1:
            lodsd
            push eax
        loop repeta1
        
        mov ecx,ls*2
        mov esi,0
        
        jecxz sfarsit2
        repeta2:
            pop ax
            mov [d+esi],al
            inc esi
            mov [d+esi],ah
            inc esi
        loop repeta2
        
        mov ecx,ls*4
        mov esi,0
        for1:
            push ecx
            mov ebx,esi
            inc ebx
            for2:
                mov al,[d+esi]
                mov dl,[d+ebx]
                cmp dl,al
                jle done
                    mov dl,[d+esi]
                    mov al,[d+ebx]
                    mov [d+esi], al
                    mov [d+ebx], dl
                done:
                inc ebx
            loop for2
            inc esi
            pop ecx
        loop for1
        sfarsit1:
        sfarsit2:
        ; exit(0)
        push    dword 0      
        call    [exit]       
