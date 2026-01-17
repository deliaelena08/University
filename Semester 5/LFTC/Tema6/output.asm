bits 32
global start
extern ExitProcess, printf, scanf
import ExitProcess kernel32.dll
import printf msvcrt.dll
import scanf msvcrt.dll

section .data
    format_in db "%d", 0
    format_out db "%d", 10, 0
section .text
start:
section .data
    _a dd 0
section .text
section .data
    _b dd 0
section .text
section .data
    _c dd 0
section .text
    ; Citire a
    push dword _a
    push dword format_in
    call [scanf]
    add esp, 8
    ; Citire b
    push dword _b
    push dword format_in
    call [scanf]
    add esp, 8
    push dword [_a]
    push dword [_b]
    push dword 2
    pop ebx
    pop eax
    imul eax, ebx
    push eax
    pop ebx
    pop eax
    add eax, ebx
    push eax
    ; Atribuire rezultat
    pop eax
    mov [_c], eax
    ; Afisare c
    push dword [_c]
    push dword format_out
    call [printf]
    add esp, 8
    ; Exit Process
    push dword 0
    call [ExitProcess]
