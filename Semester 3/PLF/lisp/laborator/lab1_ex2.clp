;Definiti o functie care selecteaza al n-lea element al unei liste, sau
;NIL, daca nu exista.
;    n_element '(1 2 3 4 5)
(DEFUN n_element(L n)
(COND ((null L) nil)
	  ((= n 1) (car L))
	  (t(n_element (cdr L) (- n 1)))
))

;Sa se construiasca o functie care verifica daca un atom e membru al unei
;liste nu neaparat liniara.
(DEFUN membru(L e)
	(COND
		((null L) nil)
		((equal (car L) e) t)
		((listp (car L)) (OR (membru (car L) e) (membru (cdr L) e)))
		(t( membru (cdr L) e))
	)
)
;(membru '(1 2 (A) (4 S(5)) D) '5)

;Sa se construiasca lista tuturor sublistelor unei liste. Prin sublista se
;intelege fie lista insasi, fie un element de pe orice nivel, care este
;lista. Exemplu: (1 2 (3 (4 5) (6 7)) 8 (9 10)) =>
;( (1 2 (3 (4 5) (6 7)) 8 (9 10)) (3 (4 5) (6 7)) (4 5) (6 7) (9 10) ).

(DEFUN construiasca(L)
(COND ((null L) nil)
	  ((atom (car L)) (cons (car L) (construiasca (cdr L)))) 
	  (t (cons L (append (construiasca (car L)) (construiasca (cdr L)))))
)
)
;Sa se scrie o functie care transforma o lista liniara intr-o multime.
; (gaseste '(1 2 A S 9 10 C 8 T R) 0)
(defun gaseste(L a)
(COND ((null L) t)
	   ((equal (car L) a) NIL)
	   (t (gaseste (cdr L) a))
)
)

(DEFUN multime (L)
(COND ((null L) nil)
	  ((gaseste (cdr L) (car L)) (cons (car L) (multime (cdr L))))
	  (t (multime (cdr L)))
))