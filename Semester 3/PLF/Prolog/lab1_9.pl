%exista(L:list,El:list)(i,i)
exista([El|_],El):-!.
exista([H|T],El):-
	H \=El,
	exista(T,El).

%intersectia(L1:list,L2:list,Rez:list)
intersectia([],_,[]):-!.
intersectia([H|T],L2,[H|Rez]):-
	exista(L2,H),!,
	intersectia(T,L2,Rez).
intersectia([_|T],L2,Rez):-
	intersectia(T,L2,Rez).

%construire_lista(M:integer,N:integer,Rez:list)(i,i,o)
construire_lista(N,N,[N]):-!.
construire_lista(M,N,[M|Rez]):-
	M1 is M+1,
	construire_lista(M1,N,Rez).
