bits 32

global start
extern exit
import exit msvcrt.dll

        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...

; our code starts here
segment code use32 class=code
    start:
    
    a dw 2h        ; a - word (16-bit signed)
    b db 1h        ; b - byte (8-bit signed)
    c dd 2h        ; c - dword (32-bit signed)
    x dq 1h        ; x - qword (64-bit signed)

segment code use32 class=code
    start:
        mov ax, word[a]          
        imul ax              
        mov bx, ax           
        mov cx, dx           

        movsx ax, byte [b]  
        add bx, ax           
        adc cx, 0            

        mov ax, cx
        cwd
        mov edx,eax
        mov eax,0
        mov ax,dx
        cwd
        add eax, dword [x]  
        adc edx, dword [x+4] 

        movsx ebx, byte [b] 
        add ebx, ebx         
        idiv ebx             

        mov ebx, eax        
        mov eax, [c]         
        imul eax, dword [c] 

        add eax, ebx         
        adc edx, 0          

        push eax
        push edx
        call exit
