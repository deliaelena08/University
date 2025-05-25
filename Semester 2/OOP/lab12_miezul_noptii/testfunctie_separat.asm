bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    format dd "%s",0
    n dd 0
    cuvant times 101 db 0
    testi dd 3
    testprop db "Bag pl in morti.",0

; our code starts here
segment code use32 class=code
    start:
        mov ecx,[testi]; i
        mov ebx,testprop;`adresa sirului din care extrag cuvinte
               
        repeta:
        cmp ecx,1
        jz foundword
            
            cautarespatiu: ;cuvintele sunt separate prin spatiu
            inc ebx
            cmp [ebx],byte ' '
            jnz cautarespatiu
            
        dec ecx
        jmp repeta
        
        foundword:
        cld
        inc ebx;pentru ca eram la space.
        mov esi,ebx
        mov edi,cuvant
        stocare:
            movsb
            cmp [esi],byte ' '
            jz final
            cmp [esi],byte '.'
            jz final
        jmp stocare
        
        ; exit(0)
        final:
        mov [edi+1],byte 0
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
