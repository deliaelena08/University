;Se da un arbore de tipul (2). Sa se precizeze nivelul pe care apare un nod
; x in arbore. Nivelul radacii se considera a fi 0. 
;  (nivele '(A (B) (C (D) (E))) 'D 0) =>2

(defun nivele (Arb x nivel)
  (cond
    ((null Arb) nil) 
    ((and (atom (car Arb)) (equal (car Arb) x)) nivel) 
    ((listp (car Arb)) (or (nivele (car Arb) x (1+ nivel)) (nivele (cdr Arb) x nivel)))
    (t (nivele (cdr Arb) x nivel)))) 
 