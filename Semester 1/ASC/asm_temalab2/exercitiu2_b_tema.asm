bits 32 ; assembling for the 32 bits architecture
global start        
;Cerinta problemei:(a-b+c)-(d+d)
;a-word
;b-word
;c-word
;d-word
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

segment data use32 class=data
    a dw 5
    b dw 2
    c dw 3
    d dw 1

; our code starts here
segment code use32 class=code
    start:
        mov AX,[a];AX=a
        sub AX,[b];AX=AX-b=a-b
        add AX,[c];AX=AX+c=a-b+c
        mov DX,[d];DX=d
        add DX,[d];DX=DX+d=d+d
        sub AX,DX;AX=AX-DX=(a-b+c)-(d+d)
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
