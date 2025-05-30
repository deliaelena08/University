%se dau n puncte in plan(prin coordonatele lor)
%Se cere sa se determine submultimile de puncte coliniare
%formula de verificare a coliniaritatii pentru A(xA,yA,B(xB,yB),C(xC,yC)
coliniare((X1, Y1), (X2, Y2), (X3, Y3)) :-
    (X2 - X1) * (Y3 - Y1) =:= (Y2 - Y1) * (X3 - X1).
% Alege nedeterminist un element X din listă,
alege_un_punct(X, [X|T], T).
alege_un_punct(X, [H|T], [H|RT]) :-
    alege_un_punct(X, T, RT).

/*
 alege_pereche_ordonat(+Lista, -P1, -P2, -Remainder)
   - Alege primul punct P1 ca fiind capul listei.
   - Alege al doilea punct P2 din restul listei, menținând ordinea.
   - Rest reprezintă ce rămâne DUPĂ extragerea lui P2.
   Astfel (P1,P2) este mereu în ordinea apariției, i < j.
*/
alege_pereche_ordonat([P1|T], P1, P2, Rest) :-
    alege_un_punct(P2, T, Rest).

/*
 colecteaza_coliniare_ordonat(A, B, Lista, Rez)
   - din Lista (ordonată), colectează toate punctele H care sunt coliniare cu (A,B),
     păstrându-le în ordinea apariției.
*/
colecteaza_coliniare_ordonat(_, _, [], []).
colecteaza_coliniare_ordonat(A, B, [H|T], [H|R]) :-
    coliniare(A,B,H),!,
    colecteaza_coliniare_ordonat(A,B,T,R).
colecteaza_coliniare_ordonat(A, B, [_|T], R) :-
    colecteaza_coliniare_ordonat(A,B,T,R).

my_count([], 0).
my_count([_|T], N) :-
    my_count(T, N1),
    N is N1 + 1.


subset_3plus(Lista, Sub) :-
    subsubset(Lista, Sub),
    my_count(Sub, C),
    C >= 3.
%   generează, prin backtracking, *toate* subseturile (de orice mărime)
%   menținând ordinea elementelor.
subsubset([], []).
subsubset([H|T], [H|R]) :-
    subsubset(T, R).
subsubset([_|T], R) :-
    subsubset(T, R).

/*
 linie_coliniare_ordonat(+Lista, -Submultime)
   - alege o pereche (A,B) în ordinea apariției,
   - adună punctele coliniare cu (A,B) aflate DUPĂ B,
   - submulțimea [A,B|Col] trebuie să aibă cel puțin 3 puncte.
*/

linie_coliniare_ordonat(Lista, Sub) :-
    % 1. Alege pereche (A,B)
    alege_pereche_ordonat(Lista, A, B, Rest),
    % 2. Adună toate punctele coliniare
    colecteaza_coliniare_ordonat(A, B, Rest, Col),
    % 3. Formăm mulțimea completă:
    Complet = [A, B | Col],
    % 4. Din "Complet", extragem sub-submulțimi de >=3
    subset_3plus(Complet, Sub).

toate_liniile_coliniare(Puncte, Rez) :-
    findall(Sol, linie_coliniare_ordonat(Puncte, Sol), Rez).
