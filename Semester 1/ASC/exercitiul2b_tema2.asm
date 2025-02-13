bits 32 
;Cerinta problema:x-(a*b*25+c*3)/(a+b)+e(cu semn)
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
    x dq 10
segment code use32 class=code
    start:
        mov AL,[a];
        imul byte [b];AX=a*b
        
        mov BX,25;BX=25
        imul BX;DX:AX=a*b*25
        push DX
        push AX
        pop EBX;EBX=a*b*25
        
        mov AL,[c];
        mov CL,3;CL=3
        imul CL;AX=3*c
        cwde;EAX=3*c
        
        add EAX,EBX;EAX=a*b*25+c*3 
        mov ECX,EAX;ECX=a*b*25+c*3 
        
        mov AL,[a];AL=BX=a
        add AL,[b];AL=a+b
        cbw;AX=a+b
        mov BX,AX;BX=a+b
        
        push ECX
        pop AX
        pop DX
        
        idiv BX;AX=(a*b*25+c*3)/(a+b)
              ;DX=(a*b*25+c*3)%(a+b)
              
        cwde;EAX=(a*b*25+c*3)/(a+b)
        mov ECX,EAX;ECX=(a*b*25+c*3)/(a+b)
        
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
