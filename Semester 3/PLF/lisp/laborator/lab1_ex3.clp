;Definiti o functie care intoarce produsul a doi vectori.

(defun produs-scalar (v1 v2)
  (cond ((or (null v1) (null v2)) 0)
        (t (+ (* (car v1) (car v2))  
             (produs-scalar (cdr v1) (cdr v2)))))) 

;Sa se construiasca o functie care intoarce adancimea unei liste.
;(adancime '(1 2 3 (A) (B) (C D(F E))))  =>3
(DEFUN adancime(L)
(COND
	((null L) 0)
	((atom (car L)) (adancime(cdr L)))
	(t (+ 1 (adancime (car L)) (adancime (cdr L))))))
	
;Definiti o functie care sorteaza fara pastrarea dublurilor o lista liniara.
;   (apel '(3 1 2 3 4 5 1 4 2))

(defun elimina_dubluri (L)
  (cond
    ((null L) nil)
	((null (cadr L)) (cons (car L) (elimina_dubluri (cdr L))))
    ((= (car L) (cadr L)) (elimina_dubluri (cdr L)))  
    (t (cons (car L) (elimina_dubluri (cdr L))))))

;SORTARE PRIN INSERTIE 
(defun inserare (e L)
  (cond
    ((null L) (list e))  
    ((<= e (car L)) (cons e L)) 
    (t (cons (car L) (inserare e (cdr L))))))

(defun sortare (L)
  (cond
    ((null L) nil) 
    ((null (cdr L)) L)  
    (t (inserare (car L) (sortare (cdr L))))))


(defun apel(L)
(elimina_dubluri (sortare L))
)

; Sa se scrie o functie care intoarce intersectia a doua multimi.
;   (intersectia '(1 2 3 4 5 6 7 8 9) '(6 7 8 9 10 11 12)) 
(DEFUN exista(L el)
(cond 
((null L) nil)
((equal (car L) el) t)
(t (exista (cdr L) el))))

(Defun intersectia(L1 L2)
(cond ((null L1) nil)
	  ((null L2) nil)
	  ((exista L2 (car L1)) (cons (car L1) (intersectia (cdr L1) L2)))
	  (t (intersectia (cdr L1) L2))))