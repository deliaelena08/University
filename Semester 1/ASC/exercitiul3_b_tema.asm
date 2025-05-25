bits 32 ; assembling for the 32 bits architecture
;Cerinta problema:f*(e-2)/[3*(d-5)]
;d-byte
;e-word
;f-word
global start        

extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

segment data use32 class=data
    d db 6
    e dw 4
    f dw 3

segment code use32 class=code
    start:
        
        MOV AL,[d];AL=d
        SUB AL,5;AL=AL-5
        MOV BL,3;BL=3
        MUL BL;AX=AL*BL
        MOV BX,AX;BX=[3*(d-5)]

        MOV CX,[e];CX=e
        SUB CX,2;CX=CX-2=e-2
        MOV AX,[f];AX=f
        MUL CX;DX:AX=f*(e-2)
        
        DIV BX;DX:AX=DX:AX/BX=f*(e-2)/[3*(d-5)]
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
