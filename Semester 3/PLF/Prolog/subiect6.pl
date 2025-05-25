

solutie1(1,3):-!.
solutie1(N,Rez):-
	N1 is N-1,
	solutie1(N1,Rez1),
	Rez is Rez1+ N*3.
solutie2(0,_,0):-!.
solutie2(N,Poz,Rez):-
	N1 is N-1,
	Poz1 is Poz+3,
	solutie2(N1,Poz1,Rez1),
	Rez is Rez1+Poz1.
sol2(N,Rez):-solutie2(N,0,Rez).


insereaza6([],E,[E]):-!.
insereaza6([H|T],E,[E,H|T]).
insereaza6([H|T],E,[H|Rez]):-
	insereaza6(T,E,Rez).

permutari([],[]).
permutari([H|T],Rez):-
	permutari(T,Rez1),
	insereaza6(Rez1,H,Rez).
aranjamente6([],0,_,[]).
aranjamente6([H|_],1,S,[H]):-
	H =:= S.
aranjamente6([H|T],K,S,[H|Rez]):-
	K>1,
	S1 is S-H,
	S1>0,
	K1 is K-1,
	aranjamente6(T,K1,S1,Rez).
aranjamente6([_|T],K,S,Rez):-
	aranjamente6(T,K,S,Rez).

onesolution(L,K,P,Rez):-
	aranjamente6(L,K,P,Rez1),
	permutari(Rez1,Rez).
allsolutions(L,K,S,R):-findall(Ar,onesolution(L,K,S,Ar),R).


