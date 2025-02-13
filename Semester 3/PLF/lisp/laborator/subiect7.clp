
(DEFUN modifica(L nivel K)
( cond ((null L) nil)
		((AND (atom (car L)) (= nivel k)) (cons 0 (modifica (cdr L) nivel K)))
		((atom (car L)) (cons (car L) (modifica (cdr L) nivel K)))
		(t(cons (modifica (car L) (+ 1 nivel) K) (modifica (cdr L) nivel k)))
)
)

(DEFUN obtine (L K)
(modifica L 1 K)
)
;  (obtine '(a (1 (2 b)) (c (d))) '2)
; (gaseste '(A 1 (B 2) (1 C 4) (D 1(6 F))((G 4) 6)))
; nr sublistelor de la nivel impare in care suma lor este para si nivelul superfical e 1
(Defun suma(L)
(COND
	((null L) 0)
	((numberp (car L) ) (+ (car L) (suma (cdr L))))
	(t (+ 0 (suma (cdr L)))) 
)
)

(DEFUN nr_subliste(L Nivel)
( COND ((null L) 0)
		((atom L) 0)
		((AND (oddp Nivel) (listp L) (evenp (suma L))) (+ 1 (apply #'+ (MAPCAR (lambda (x) (nr_subliste x (1+ Nivel))) L))))
		(t(+ 0 (Apply #'+ (MAPCAR (lambda (x)(nr_subliste x  (+ 1 Nivel))) L))))
)
)

(DEFUN gaseste(L)
(nr_subliste L 1)
)

(DEFUN F(L)
	(funcall #'(lambda (v)
	(COND 
		  ((NULL L)0)
		  ((> v 2) (+ (CAR L) (F(CDR L))))
		  (T v)))
	(F(CAR L)))
)