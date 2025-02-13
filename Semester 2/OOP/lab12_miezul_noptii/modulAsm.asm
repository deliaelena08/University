bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)

global _cuvantasmWord

; our data is declared here (the variables needed by our program)
segment data public data use32
    cuvant times 101 db 0
    testi dd 3,0
    testprop db 'test.',0
; our code starts here
segment code public use32
    _cuvantasmWord:
            ; creare cadru de stiva pentru programul apelat
        push ebp
        mov ebp, esp
    
        mov ecx,[ebp+8]; i
        mov ebx,[ebp+12];adresa sirului din care extrag cuvinte
               
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
        mov ecx,[ebp+8]
        cmp ecx,1
        jz firstiteration
        inc ebx;pentru ca eram la space.
        firstiteration:
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
        
        mov [edi], byte 0
        
        mov esp, ebp
        pop ebp
        
        mov eax,cuvant;intoarcem adresa sirului rezultat
        
        ret