%in_list(El:integer,L:list)(i,i)
in_list(El,[H|T]):-
	H \= El,!,
	in_list(El,T).
in_list(El,[El|_]):-!.

%reuniune(L1:list,L2:list,Rez:list) (i,i,o)
reuniune([],_,[]):-!.
reuniune([H|T],L2,[H|Rez]):-
	in_list(H,L2),!,
	reuniune(T,L2,Rez).
reuniune([_|T],L2,Rez):-
	reuniune(T,L2,Rez).

combinari1([],_,[]):-!.
combinari1([H|T],El,[[El,H]|Rez]):-
	combinari1(T,El,Rez).

combinari2([],[]):-!.
combinari2([H|T],[Rez1|Rez]):-
	combinari1(T,H,Rez1),
	combinari2(T,Rez).

