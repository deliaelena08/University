% multimi_egale(M1:list,M2:list)(i,i)
multimi_egale(M1, M2) :-
    subset(M1, M2),
    subset(M2, M1).

% subset(L:list,M:list)(i,i)
subset([], _).
subset([H|T], M) :-
    member1(H, M),
    subset(T, M).

%member1(El:integer,L:list)(i,i)
member1(H,[El|T]):-
	H \= El,
	member1(H,T).
member1(H,[H|_]):-!.

%n_membru(L:list,N:integer,Rez:integer)(i,i,o)
n_membru([],_,_):-!.
n_membru([_|T],N,Rez):-
	N>1,!,
	N1 is N-1,
	n_membru(T,N1,Rez).
n_membru([H|_],1,H):-!.
