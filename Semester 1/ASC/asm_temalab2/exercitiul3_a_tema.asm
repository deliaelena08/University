bits 32 ; assembling for the 32 bits architecture
;Cerinta problemei:(a*2)+2*(b-3)-d-2*c
;a-byte
;b-byte
;c-byte
;d-word
global start        

extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

segment data use32 class=data
     a db 2
     b db 4
     c db 1
     d dw 3
segment code use32 class=code
    start:
        mov AL,[a];AX=a
        mov DL, 2; DL=2
        mul DL;AX=AL*DL=a*2
        MOV BX, AX; BX = AX
        
        mov AL,[b];AL=b
        sub AL,3;AL=AL-3=b-3
        MOV DL, 2; DL =2
        mul DL;AX=AL*DL=2*(b-3)
        
        add BX,AX;BX=BX+AX=(a*2)+2*(b-3)
        sub BX,[d];BX=BX-d=(a*2)+2*(b-3)-d
        
        mov AL,[c];AL=c
        MOV DL, 2; DL=2
        mul DL;AX=AL*DL=c*2
        
        sub BX,AX;BX=BX-AX=(a*2)+2*(b-3)-d-2*c
        MOV AX, BX; AX = (a*2)+2*(b-3)-d-2*c
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
