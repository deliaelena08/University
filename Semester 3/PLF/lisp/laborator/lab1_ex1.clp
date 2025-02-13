;Sa se insereze intr-o lista liniara un atom a dat dupa al 2-lea, al 4-lea,
;al 6-lea,....element.
;(insereaza '(A B C D E F G H) 1 0)

(Defun insereaza (L a poz)
	(COND  ((null L) nil)
		((oddp poz) (cons (car L) (cons a (insereaza (cdr L) a (1+ poz)))))
		(t (cons (car L)(insereaza (cdr L) a (+ 1 poz))))
	)
)

(DEFUN apel_1 (L a)
	(insereaza L a 0)
)
 
;Definiti o functie care obtine dintr-o lista data lista tuturor atomilor
;care apar, pe orice nivel, dar in ordine inversa. De exemplu: (((A B) C)
;(D E)) --> (E D C B A)
;(atomi '(((A B) C)(D E)))
(Defun atomi(L)
	(COND
	((null L) nil)
	((atom (car L)) (append (atomi (cdr L)) (list (car L))))
	(t (append (atomi (cdr L)) (atomi (car L))))
	)
)

;Definiti o functie care intoarce cel mai mare divizor comun al numerelor
;dintr-o lista neliniara.

(DEFUN cmmdc(A B)
	(COND ((= A 0) B)
		  ((= B 0) A)
		  ((< A B) (cmmdc (- B A) A))
		  (t (cmmdc (- A B) A))))
(Defun liniarizeaza(L)
(COND ((null L) nil)
	  ((numberp (car L)) (append  (list (car L)) (liniarizeaza (cdr L))))
	  ((atom (car L)) (liniarizeaza (cdr L)))
	  (t (append (liniarizeaza (car L)) (liniarizeaza (cdr L))))
)
)

(defun cmmdc_lista (L)
  (cond ((null L) nil)  
        ((null (cdr L)) (car L)) 
        (t (cmmdc (car L) (cmmdc_lista (cdr L))))))
		
; (apel2 '(4 (16) 12 ((8) 2))) =>2

(DEFUN apel2(L)
( cmmdc_lista(liniarizeaza L)))