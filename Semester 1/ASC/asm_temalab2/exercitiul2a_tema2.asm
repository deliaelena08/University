bits 32 
;Cerinta problema:x-(a*b*25+c*3)/(a+b)+e(fara semn)
;a,b,c-byte
;e-doubleword
;x-qword
global start        

extern exit               
import exit msvcrt.dll    
segment data use32 class=data
    a db 2
    b db 1
    c db 5
    e dd 1
    x dq 100
segment code use32 class=code
    start:
        mov AL,[a];
        mul byte [b];AX=a*b
        mov BX,25;BX=25
        mul BX;DX:AX=a*b*25
        push DX
        push AX
        pop EBX;EBX=a*b*25
        mov EAX,0
        mov AL,[c];
        mov CL,3;CL=3
        mul CL;AX=c*3=EAX
        add EAX,EBX;EAX=a*b*25+c*3 
        push EAX
        pop AX
        pop DX;DX:AX=a*b*25+c*3 
        mov BX,0
        mov BL,[a];BL=BX=a
        add BL,[b];BL=a+b
        div BX;AX=(a*b*25+c*3)/(a+b)
              ;DX=(a*b*25+c*3)%(a+b)
        mov ECX,0
        mov CX,AX;CX=ECX=(a*b*25+c*3)/(a+b)
        mov EAX,dword [x+0]
        mov EDX,dword [x+4];EDX:EAX=x
        sub EAX,ECX
        sbb EDX,0;EDX:EAX=x-(a*b*25+c*3)/(a+b)
        mov EBX,[e]
        add EAX,EBX;
        adc EDX,0;EDX:EAX=x-(a*b*25+c*3)/(a+b)+e
    
        ; exit(0)
        push    dword 0      
        call    [exit]       