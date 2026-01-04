;Se citesc de la tastatura un numar natural n si n propozitii care contin cel putin n cuvinte (nu se fac validari).
;Sa se afiseze sirul format prin concatenarea cuvintelor de pe pozitia i din propozitia i, i=1,n (separate prin spatiu).
;Exemplu: Se da: n=5
;Ana are mere si pere.
;Pe birou se gaseste un cos cu fructe.
;Cartea mea preferata se afla pe masa.
;Afara a nins si este destul de frig.
;Maine o sa merg la cumparaturi.
;Se va afisa:
;Ana birou preferata si la

bits 32 

extern exit               
import exit msvcrt.dll    
segment data use32 class=data
    
    a dd 0
    message1 db "Introduceti numarul de propozitii : ",0
    format1 db "%d",0
    
    
segment code use32 class=code
    start:
        push dword message1
        call [printf]
        add esp,4*1;apelam functia de afisare pentru citirea primului numar
        
        push dword a       ; punem parametrii pe stiva de la dreapta la stanga
		push dword format1
		call [scanf]       ; apelam functia scanf pentru citirea primului numar
        add esp, 4 * 2
        
        mov ecx,[a]
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
