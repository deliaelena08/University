bits 32 
;Cerinta problemei :c+a+b+b+a
;a - byte
;b - word
;c - double word
global start        
extern exit               
import exit msvcrt.dll    
segment data use32 class=data
    a db 0Ah
    b dw 0Bh
    c dd 0Ch
segment code use32 class=code
    start:
        mov ECX,[c];ECX=c
        mov AX,[b];AX=b
        cwde;EAX=b
        mov EBX,EAX;EBX=EAX
        mov AL,[a];AL=a
        cbw;AX=a
        cwde;EAX=AX=a
        mov EDX,EAX;EDX=a
        mov EAX,ECX;EAX=c
        add EAX,EDX;EAX=c+a
        add EAX,EBX;EAX=c+a+b
        add EAX,EBX;EAX=c+a+b+b
        add EAX,EDX;EAX=c+a+b+b+a
        ; exit(0)
        push    dword 0      
        call    [exit]       
