;sa se scrie un program lisp care sa elimine toate aparitiile unui element dat
;(1 (2 A (3 A)) (A)) ,el =A =>( 1(2(3)) (NIL))
(DEFUN elimin (L el)
	(COND ((null L) nil)
		  ((equal (car L) el) (elimin (cdr L) el))
		  ((listp (car L)) (cons (elimin (car L) el) (elimin (cdr L) el)))
		  (t( cons (car L) (elimin (cdr L) el)))
	)
)

;numarul de subliste de la orice nivek care suma atomilor numerici de la
;niveluri impare este numar par,nivelul superficial al listei se considera 1
;(A 1 (B 2) (1 C 4) (D 1 (6 F)) ((G 4) 6)) => 4 subliste

(defun sumanivel (L NIVEL)
  (cond
    ((null L) 0) 
    ((and (numberp L) (oddp NIVEL)) 1 ) 
    ((listp L)
     (apply #'+ (mapcar #'(lambda (x) (sumanivel x (1+ NIVEL))) L)))
    (t 0))
)
