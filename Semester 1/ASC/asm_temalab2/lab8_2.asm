bits 32 
;Se dau un nume de fisier si un text (definite in segmentul de date). Textul contine litere mici, litere mari, cifre si caractere speciale. Sa se inlocuiasca toate caracterele speciale din textul dat cu caracterul 'X'. Sa se creeze un fisier cu numele dat si sa se scrie textul obtinut in fisier.
global start        
extern exit,fprintf,fopen,fclose             
import exit msvcrt.dll    
import fopen msvcrt.dll
import fprintf msvcrt.dll 
import fclose msvcrt.dll

segment data use32 class=data
    mod_acces_fisier db "w",0
    nume_fisier db "fisier_problema.txt",0
    text db "MM<oir58 ei3075$^:l'",0
    len equ $-text-1
    descriptor_fis dd -1
segment code use32 class=code
    start:
        push mod_acces_fisier
        push nume_fisier
        call [fopen]; eax-adresa fisierului
        mov [descriptor_fis], eax
        
        ; verificam daca functia fopen a creat cu succes fisierul (daca EAX != 0)
        cmp eax, 0
        je final
        
        mov ecx,len;pregatim sirul
        mov esi,0
        
        repeta:
        mov al,[text+esi]
        
        mov bl,122
        cmp al,bl
        ja schimbare;AL>'z'
        
        mov bl,97
        cmp al,bl
        jae skip;AL>='a'
        
        mov bl,90
        cmp al,bl
        ja schimbare;AL>'Z'
        
        mov bl,65
        cmp al,bl
        jae skip;AL>='A'
        
        mov bl,57
        cmp al,bl
        ja schimbare;AL>'10'
        
        mov bl,48
        cmp al,bl
        jae skip;AL>='0'
        
        mov bl,32
        cmp al,bl
        je skip;AL==' '
        ;AL < '0'    
        
        schimbare: 
        mov byte[text+esi],88;schimbam cu caracterul 'X'
        
        skip:
        inc esi
        
        loop repeta
        
        push dword text
        push dword [descriptor_fis]
        call [fprintf];afisam sirul
        add esp, 4*2
        
        push dword [descriptor_fis]
        call [fclose];inchidem fisierul
        add esp, 4
        
        final:
        ; exit(0)
        push    dword 0      
        call    [exit]       