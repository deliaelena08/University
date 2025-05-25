%Turbo(X:INTEGER,N:INTEGER,REZ:INTEGER)
%x este numarul iar n este exponentialul
%in rez se va intoarce rezultatul
%(i,i,o)-determinism
turbo(_,0,1):-!.
turbo(X,N,Rez):-
	N>0,
	N1 is N-1,
        turbo(X,N1,Rez1),
	Rez is Rez1*X.

%Turbo2(X:intreg,N:intreg,N1:intreg,Rez:integ)
%X este numarul ridicat la puterea N
%N1 este indexul comparat cu exponentialul
%Rez este variabila in care se transmite rezultatul
%(i,i,i,o)-determinism
turbo2(_,N,N,1):-!.
turbo2(X,N,N1,Rez):-
	N1<N,
	N2 is N1+1,
	turbo2(X,N,N2,Rez1),
	Rez is Rez1 * X.
exponential(X,N,Rez):-N>0,
	turbo2(X,N,0,Rez).

%lista permutarilor ca valoarea absoluta a diferentei dintre
%doua elemente consecutive din permutare este <=3
%(i,o)-Nedeterminism
check(A,[B|_]) :-
    Val is abs(A - B),
    Val =< 3.

inserare([],E,[E]).
inserare(L,E,[E|L]):-
	check(E,L).
inserare([H|T],E,[H|R]):-
	inserare(T,E,R),
	check(H,R).

permutari([],[]).
permutari([H|T],R):-
	permutari(T,RP),
	inserare(RP,H,R).

allsolutions(L,R):-findall(RP,permutari(L,RP),R).

