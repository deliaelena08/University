bits 32 
;Sa se citeasca de la tastatura doua numere a si b (in baza 10) si sa se calculeze a+b. Sa se afiseze rezultatul adunarii in baza 16.

global start
        
extern exit, printf, scanf  ; adaugam printf si scanf ca functii externe           
import exit msvcrt.dll     
import printf msvcrt.dll     ; indicam asamblorului ca functia printf se gaseste in libraria msvcrt.dll
import scanf msvcrt.dll      ; similar pentru scanf
   
segment data use32 class=data

        a dd 0
        b dd 0
        
        message1 db "Primul numar este: ",0
        format1 db "%d",0
        
        message2 db "Al doilea numar este: ",0
        format2 db "%d",0
        
        message3  db "Rezultatul adunarii este: %x", 0
        
segment code use32 class=code
    start:
        push dword message1
        call [printf]
        add esp,4*1;apelam functia de afisare pentru citirea primului numar
    
        push dword a       ; punem parametrii pe stiva de la dreapta la stanga
		push dword format1
		call [scanf]       ; apelam functia scanf pentru citirea primului numar
        add esp, 4 * 2
        
        push dword message2
        call [printf]
        add esp,4*1;apelam functia de afisare mesaj pentru al doilea numar
        
        push dword b       ; punem parametrii pe stiva de la dreapta la stanga
		push dword format2
		call [scanf]       ; apelam functia scanf pentru citirea al doilea numar
        add esp, 4 * 2
        
        mov eax,[a]
        mov ebx,[b]
        
        add eax,ebx
        
        push eax  ; punem pe stiva valoarea lui n
	    push dword message3  
		call [printf]       ; apelam functia printf oentru afisarea sumei
		add esp, 4 * 2
        
        ; exit(0)
        push    dword 0      
        call    [exit]       
