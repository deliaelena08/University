; Sa se construiasca lista nodurilor unui arbore de tipul (2) parcurs in
; inordine.
;(inordine '(A (B) (C (D) (E))))  SRD => B A D C E
	
(defun inordine (Arb)
  (cond
    ((null Arb) nil) 
    ((atom (car Arb)) (append (inordine (cadr Arb)) (list (car Arb)) (inordine (cddr Arb))))
    (t (append (inordine (car Arb)) 
               (inordine (cdr Arb))))))