;test 1
;reprezentare rad subarborele drept subarborele stang
;calea de la radacina catre un nod dat
;(a(b(g))(c(d(e))(f))) nod=e=> (a c e)
(defun parcurgere (arb n)
  (cond
    ((null arb) nil) ; Dacă arborele este gol, returnează NIL.
    ((eq (car arb) n) (list (car arb))) ; Dacă rădăcina este nodul căutat, returnează nodul.
    (t (cond
         ((parcurgere (cadr arb) n) ; Căutare în subarborele stâng.
          (cons (car arb) (parcurgere (cadr arb) n)))
         ((parcurgere (caddr arb) n) ; Căutare în subarborele drept.
          (cons (car arb) (parcurgere (caddr arb) n)))
         (t nil))))) 

;determinarea numarului de subliste de la orice nivel pentru care 
;primul atom numeric la orice nivel este impar	
;(a(B 2)(1 C 4)(D 1 (5 F))((G 4)6)) are 3
( defun subliste (L)
	(cond ((null L) 0)
		  ((and (numberp L) (oddp L)) 1)
		  ((listp L) (apply #'+ (mapcar #'subliste L)))
		  (t 0)
	)
)