%1b
% Cazul in care n = 0, am ajuns la pozitia in care trebuie pus elementul
intercaleaza(L, E, 0, [E|L]):-!.
%lista e vida inseamna ca dar n e mai mare ca 1 atunci returnam lista
intercaleaza([],_,_,[]):-!.
%altfel
intercaleaza([H|T], E, N, [H|R]) :-
	N1 is N-1,
	intercaleaza(T,E,N1,R).

%1a

%cel mai mare divizor dintre doua numere
%b=0 sau a=0 atunci returnam val != 0
cmmdc(A,0,A):-!.
cmmdc(0,B,B):-!.
%altfel calculam divizorul prin scaderi
cmmdc(A,B,R):-
	A>=B,
	A1 is A-B,
	cmmdc(A1,B,R).
cmmdc(A,B,R):-
	A<B,
	B1 is B-A,
	cmmdc(A,B1,R).

%lista e goala returnam -1
cmmdc_lista([],-1).
%am ramas doar cu cmmdc din lista
cmmdc_lista([H],H).
%altfel
cmmdc_lista([H|T],R):-
	%facem cmmdc din ce a ramas
	cmmdc_lista(T,R1),
	%rezultatul il comparam
	cmmdc(R1,H,R).

