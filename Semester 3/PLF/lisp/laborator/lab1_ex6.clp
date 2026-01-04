;Sa se scrie de doua ori elementul de pe pozitia a n-a a unei liste 
;(load "lab1_ex6.clp")
;(sub_a '(1 2 3 4) 3)
;(sub_b '(A B C) '(X Y Z))
;(sub_c '(1 2 (3 (4 5) (6 7)) 8 (9 10)))
;(sub_d '(1 2 (a b c) 3 d (e f 6)))


;Sa se scrie de doua ori elementul de pe pozitia a n-a in ll
; De exemplu, pentru (10 20 30 40 50) si n=3 se va produce (10 20 
;30 30 40 50).
;sub_a(L:list n:integer) 

( DEFUN sub_a(L n)
(COND ((null L) nil)
      ((= n  1)(cons (car L) L))
      (t(cons (car L) (sub_a (cdr L) (- n 1))))
)
)

;Sa se scrie o functie care realizeaza o lista de asociere cu cele doua 
;liste pe care le primeste. De ex: (A B C) (X Y Z) --> ((A.X) (B.Y) 
;(C.Z)). 
; sub_b(L1:list L2:list)

( DEFUN sub_b (L1 L2)
  (COND ((or (null L1) (null L2)) nil)
        (t (cons (cons (car L1) (car L2)) (sub_b (cdr L1) (cdr L2))))
   )
) 

;Sa se determine numarul tuturor sublistelor unei liste date, pe orice 
;nivel. Prin sublista se intelege fie lista insasi, fie un element de pe 
;orice nivel, care este lista. Exemplu: (1 2 (3 (4 5) (6 7)) 8 (9 10)) => 
;5 (lista insasi, (3 ...), (4 5), (6 7), (9 10)). 
;sub_c(L:list)

( DEFUN sub_c (L)
    (COND ((null L) 1)
            ((atom (car L)) (sub_c (cdr L)))
             (t (+ (sub_c (car L)) (sub_c (cdr L))))
    )
)

;Sa se construiasca o functie care intoarce numarul atomilor dintr-o lista, 
;de la nivel superficial.
;sub_d(L:list)

( DEFUN sub_d (L)
 (COND ((null L) 0)  
        ((atom (car L)) (+ 1 (sub_d (cdr L))))  
        (t (+ (sub_d (car L))(sub_d (cdr L)))))
)
