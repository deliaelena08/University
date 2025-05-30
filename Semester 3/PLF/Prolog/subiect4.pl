%aranjamentele unei liste L luate cate k care sa aiba produsul P

insereaza4([],E,[E]).
insereaza4([H|T],E,[E,H|T]).
insereaza4([H|T],E,[H|Rez]):-
	insereaza4(T,E,Rez).

permutari4([],[]).
permutari4([H|T],Rez):-
	permutari4(T,Rez1),
	insereaza4(Rez1,H,Rez).

aranjamente4([],0,_,[]).
aranjamente4([H|_],1,P,[H]):-
	H=:=P.
aranjamente4([H|T],K,P,[H|Rez]):-
	K>1,
        P mod H =:=0,
	P1 is P//H,
	K1 is K-1,
	P1>1,
	aranjamente4(T,K1,P1,Rez).
aranjamente4([_|T],K,P,Rez):-
	aranjamente4(T,K,P,Rez).

onesolution(L,K,P,R):-
	aranjamente4(L,K,P,Rez),
	permutari4(Rez,R).

allsolutions(L,K,P,R):-findall(Aranjamente,onesolution(L,K,P,Aranjamente),R).

