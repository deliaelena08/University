bits 32 
;Cerinta problemei:6*3
;a=6
;b=3
global start        

extern exit               
import exit msvcrt.dll    

segment data use32 class=data
    a dw 6
    b dw 3

segment code use32 class=code
    start:
        mov AX, [a]
        mov BX, [b]
        mul BX ; DX:AX=a*b
        push DX
        push AX
        pop EAX ; EAX = DX:AX
        
        ; exit(0)
        push    dword 0      
        call    [exit]       

