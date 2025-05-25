;(cd "C:/Users/tapuc/Desktop/facultate/Semestrul 3/PLF/lisp/laborator")
;(load "lab2_ex5.clp")
;(adancime '(A 2 B 1 F 0 C 2 D 0 E 0) 'F)
;(adancime '(A 2 B 1 F 0 C 2 D 0 E 0) 'A)
;(adancime '(A 2 B 0 C 2 D 0 E 0) 'D)
;(adancime '(A 2 B 0 C 2 D 0 E 0) 'B)


;Sa se intoarca adancimea la care se afla un nod intr-un arbore de tipul (1). 
;Reprezentarea 1: (A 2 B 0 C 2 D 0 E 0)

;Subarbore_st(ABS: list, NV:int, NM:int)
( DEFUN Subarbore_st (ABS NV NM)
    (cond
      ((null ABS) nil)
      ((= NV (+ 1 NM)) nil)
      (t (cons (car ABS) (cons (cadr ABS) ( Subarbore_st ( cddr ABS) (+ 1 NV) (+ (cadr ABS) NM)))))
    )
)
;stang(ARB: list)
( DEFUN stang (ARB)
  (Subarbore_st (cddr ARB) 0 0)
)

;Subarbore_dr(ABD:list, NV:int, NM:int)
( DEFUN Subarbore_dr (ABD NV NM)
    (cond
        ((null ABD) nil)
        ((= NV (+ 1 NM)) ABD)
        (t (Subarbore_dr (cddr ABD) (+ 1 NV) (+ (cadr ABD) NM)))
     )
)

;drept(ARB:list)
( DEFUN drept (ARB)
  (Subarbore_dr (cddr ARB) 0 0)
)

; gaseste_adancime(ARB: list, Nod: atom, Adancime: intreg)
(DEFUN gaseste_adancime (ARB Nod Adancime)
  (cond
    ((null ARB) nil) 
    ((eq (car ARB) Nod) Adancime) 
    (t 
    ;cautam recursiv in subarborele drept si stang
     (or 
      (gaseste_adancime (stang ARB) Nod (+ 1 Adancime)) 
      (gaseste_adancime (drept ARB) Nod (+ 1 Adancime)) 
     )
    )
  )
)

(DEFUN adancime (ARB Nod)
  (gaseste_adancime ARB Nod 0) 
)
