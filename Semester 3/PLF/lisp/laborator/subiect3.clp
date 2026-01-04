
(DEFUN dublare (L N POZ)
	(COND ((null L) nil)
		   ((= (mod POZ N) 0) (cons (car L) (cons (car L) (dublare (cdr L) N (+ POZ 1)))))
		   (t (cons (car L) (dublare (cdr L) N (+ POZ 1))))
	)
)

(DEFUN apeldublare(L N)
	(dublare L N 1)
)