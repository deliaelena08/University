bits 32 
;Cerinta problemei:a-b-d+2+c+(10-b)
;a-byte
;b-byte
;c=byte
;d-byte
global start        

extern exit               
import exit msvcrt.dll    

segment data use32 class=data
    a db 1
    b db 2
    c db 3
    d db 4

segment code use32 class=code
    start:
     mov AL,[a];AL=a
     sub AL,[b];AL=AL-b=a-b
     sub AL,[d];AL=AL-d=a-b-d
     add AL,2;AL=AL+2=a-b-d+2
     add AL,[c];AL=AL+c=a-b-d+2+c
     mov BL,10;BL=10
     sub BL,[b];BL=BL-b=10-b;
     add AL,BL;AL=AL+BL=a-b-d+2+c+(10-b)
  
    
        ; exit(0)
        push    dword 0      
        call    [exit]       
