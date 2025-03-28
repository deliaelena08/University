%aspect_vale(L:lista,Flag:integer)(i)
aspect_vale([_],1):-!.
aspect_vale([A,B|T],F):-
	F=0,
	A>B,!,
	aspect_vale([B|T],0).
aspect_vale([A,B|T],F):-
	A<B,
	F=1,!,
	aspect_vale([B|T],1).
aspect_vale([A,B|T],F):-
	A<B,
	F=0,
	aspect_vale([B|T],1).

verificavale(L):-aspect_vale(L,0).

%suma_alternata(L:list,Poz:Integer,Rez:integer)
suma_alternata([],_,0):-!.
suma_alternata([H|T],Poz,Rez):-
	Rest is Poz mod 2,
	Rest = 1,!,
	Poz1 is Poz+1,
	suma_alternata(T,Poz1,Rez1),
	Rez is Rez1+H.
suma_alternata([H|T],Poz,Rez):-
	Poz1 is Poz + 1,
	suma_alternata(T,Poz1,Rez1),
	Rez is Rez1-H.
suma(L,Rez):- suma_alternata(L,1,Rez).
