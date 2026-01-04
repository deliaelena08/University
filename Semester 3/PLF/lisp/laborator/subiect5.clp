;un arbore n-ar, sas se inlocuiasca nodurile de pe nivelul k din arbore
;cu o valoare e data
;(a (b (g))(c(d (e)) (f))), e=h,k=2 =>(a(b(h))(c(h(e))(h)))
;(my-load "D:/facultate/Semestrul 3/PLF/lisp/laborator/subiect5.clp")

(defun inlocuieste-noduri (arb el nivel)
  (cond
    ((null arb) nil) 
    ((AND (eq nivel 0)(atom (car arb))) (cons el (inlocuieste-noduri (cdr arb) el nivel)))
	((atom (car arb)) (cons (car arb) (inlocuieste-noduri (cdr arb) el nivel)))
    ((listp (car arb)) 
     (cons (inlocuieste-noduri (car arb) el (1- nivel))  
           (inlocuieste-noduri (cdr arb) el  nivel))
		) 
    )
)	
;nr de subliste de la orice nivel al carui prim atom numeric este par


;  (solutie_para '(A 3(B 2)(1 C 4)(D 2(6 F))((G 4) 6)))

(defun liniarizeaza (l)
	(cond 
	((numberp l) (list l))
	((atom l) nil)
	(t (mapcan #' liniarizeaza l))
	)
)

(defun ultimnrimpar (l flag)
	(cond 
		((and (null l) (= flag 0)) nil)
		((and (null l) (= flag 1)) t)
		((= (mod (car l) 2) 0) (ultimnrimpar (cdr l) 0))
		(t (ultimnrimpar (cdr l) 1))
	)
)

(defun solutie(l)
	(cond ((atom l) 0)
		  ((ultimnrimpar (liniarizeaza l) 0) (+ 1 (apply #'+ (mapcar #'solutie l))))
			(t (apply #'+ (mapcar #'solutie l)))
	)
)

(defun solutie_para (l)
(cond ((atom l) 0)
		((evenp (car (liniarizeaza l))) (+ 1 (apply #'+ (mapcar #'solutie_para l))))
		(t (apply #'+ (mapcar #'solutie_para l)))
)
)
