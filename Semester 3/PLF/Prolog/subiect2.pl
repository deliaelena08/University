%suma primelor n numere
suma1(1,1):-!.
suma1(N,Rez):-
	N1 is N-1,
	suma1(N1,Rez1),
	Rez is Rez1+N.
suma2(N,N,N):-!.
suma2(N,N1,Rez):-
	N1<N,
	N2 is N1+1,
	suma2(N,N2,Rez1),
	Rez is Rez1+N1.
calculeaza(N,Rez):-suma2(N,1,Rez).
%lista de aranjamente cu n elemente avand suma s data
%L=[2,7,4,5,3],N=2,S=7=>[[2,5],[5,2],[3,4],[4,3]]

inserare1([],E,[E]).
inserare1([H|T],E,[E,H|T]).
inserare1([H|T],E,[H|R]):-
	inserare1(T,E,R).

permutari1([],[]).
permutari1([H|T],R):-
	permutari1(T,RP),
	inserare1(RP,H,R).

aranjamente([],0,_,[]).
aranjamente([H|_],1,S,[H]):-
	S =:= H.
aranjamente([H|T],N,S,[H|Rez]):-
	N>1,
	Suma is S-H,
	N1 is N-1,
	Suma >= 0,
	aranjamente(T,N1,Suma,Rez).
aranjamente([_|T], N, S, Rez) :-
    aranjamente(T, N, S, Rez).

onesolution(L,N,S,R):-
	aranjamente(L,N,S,Rez),
	permutari1(Rez,R).
all_aranjamente(L, N, S, Rez) :-
    findall(Aranjament, onesolution(L, N, S, Aranjament), Rez).
