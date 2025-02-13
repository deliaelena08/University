;Sa se construiasca o functie care intoarce maximul atomilor numerici
; dintr-o lista, de la orice nivel.
;(cd "D:/facultate/Semestrul 3/PLF/lisp/laborator")
;(maxim '(1 2 (3 4 (5 6)))) =>6
;(maxim '(1 a (3 b (5 6 c)) d)) =>6
;(maxim '((10) 20 5)) =>20
;(maxim '(()))=>NIL
;(maxim '(a () b (c d (e) (()))))=> NIL

(defun removenil (L)
  (cond
    ((null L) nil) 
    ((null (car L)) (removenil (cdr L)))
    (t (cons (car L) (removenil (cdr L))))
	)
) 

(defun gaseste_maxim (L)
  (cond
    ((null L) nil)
    ((numberp (car L))
     (cond
		((null (cdr L)) (car L)) 
       ((> (car L) (gaseste_maxim (cdr L))) (car L))
       (t (gaseste_maxim (cdr L)))))
    (t (gaseste_maxim (cdr L)))
	)
)	

;maxim(L:list)
(defun maxim (L)
  (cond
    ((null L) nil)  
    ((numberp L) L) 
    ((listp L)      
     (gaseste_maxim (removenil (mapcar #'maxim L))))
    (t nil)
	)
)
