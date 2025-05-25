;multimea submultimelor listei date sub forma unei liste liiare 
;(1 2 3) --> (NIL (1) (2) (3) (1 2) (1 3) (2 3) (1 2 3))

;insereaza(el:integer,L:list)
;functia insereaza un element pe fiecare pozitie a unei liste
(defun insereaza(el L)
(cond  ((null L) (list (cons el NIL)))
	   (t (append  (list (cons  el  L)) (list (append  (list el) (list (car L)))) (insereaza el (cdr L))))
))


;Submultime (L:list)
;formeaza submultimile pentru toate listele date 
(defun submultime (L)
(COND ((null L) (list NIL))
	  (t (append  (insereaza (car L) (cdr L)) (submultime (cdr L))))
)
)