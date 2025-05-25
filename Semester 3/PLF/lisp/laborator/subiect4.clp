;inlocuirea numerelor pare de la orice nivel cu succesorul lor
;(my-load "D:/facultate/Semestrul 3/PLF/lisp/laborator/subiect4.clp")
(DEFUN inlocuire1(L)
	(COND ((null L) nil)
		  ((and (numberp (car L)) (= (mod (car L) 2) 0)) (cons (+ 1 (car L)) (inlocuire1 (cdr L))))
		  ((listp (car L)) (cons (inlocuire1 (car L)) (inlocuire1 (cdr L))))
		  (t (cons (car L) (inlocuire1 (cdr L))))
	)
)

(defun inlocuire2 (L)
  (cond
    ((null L) nil)
    (t
     (funcall
      (lambda (v)
        (cond
          ((and (numberp v) (zerop (mod v 2)))
           (cons (1+ v)
                 (inlocuire2 (cdr L))))
          ((listp v)
           (cons (inlocuire2 v) 
                 (inlocuire2 (cdr L))))
          (t (cons v
                   (inlocuire2 (cdr L))))))
      (car L)))))  ;; <--- argumentul pentru lambda este (car L)

(DEFUN maxim_superficial(L)
	(COND
		((NULL L) -1)
		((AND (numberp (car L)) (> (car L) (maxim_superficial (cdr L)))) (car L))
		(t (maxim_superficial (cdr L)))
	)
)

(DEFUN nr_subliste(L Nivel)
	(COND
		((NULL L) 0)
		((not (listp L)) 0)
		(T(+
			(COND
				((AND (oddp Nivel)(zerop (mod (maxim_superficial L) 2))) 1)
				(T 0) 
			)
			(apply #'+
				(mapcar #'(lambda (x)
                          (nr_subliste x (1+ Nivel)))
             L)))
		)
	)
)

(DEFUN determina_nivele(L)
	(nr_subliste L 1)
)

