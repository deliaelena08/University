%substituie(L:lista,El:integer,S:lista,Rez:lista) (i,i,i,o)
substituie([],_,_,[]):-!.
substituie([H|T],El,S,[S|Rez]):-
	El =:= H,!,
	substituie(T,El,S,Rez).
substituie([H|T],El,S,[H|Rez]):-
	substituie(T,El,S,Rez).

%elimina(L:list,Poz:integer:Rez:list) (i,i,o)
elimina([],_,[]):-!.
elimina([H|T],Poz,[H|Rez]):-
	Poz \= 1,!,
	Poz1 is Poz-1,
	elimina(T,Poz1,Rez).
elimina([_|T],Poz,Rez):-
	Poz =:= 1,
	Poz1 is Poz-1,
	elimina(T,Poz1,Rez).
