%cmmmc(nr1:integer,nr2:integer,Rez:integer) (i,i,o)
cmmdc(Nr1,0,Nr1):-!.
cmmdc(Nr1,Nr2,Rez):-
	Rest is Nr1 mod Nr2,
	cmmdc(Nr2,Rest,Rez).

%cmmmc(Nr1:intreg, Nr2:intreg, Rez: integ) (i,i,o)
cmmmc(Nr1,Nr2,Rez):-
	cmmdc(Nr1,Nr2,Rez1),
	Rez is abs(Nr1* Nr2)//Rez1.

%cmmmc_lista(L:lista,Rez:intreg) (i,o)
cmmmc_lista([A,B],Rez):-
	cmmmc(A,B,Rez),!.
cmmmc_lista([H|T],Rez):-
	cmmmc_lista(T,Rez1),
	cmmmc(H,Rez1,Rez).

%adauga(L:lista,el:integ,p1:intreg,p2:intreg,Rez:lita)(i,i,i,i,o)
% p1 reprezinta pozitia pe care se afla elementul curent in lista
% p2 reprezinta urmatoarea pozitie in care se poate adauga el

multiplu_doi(Nr1,Nr1):-!.
multiplu_doi(Nr1,Nr2):-
	Nr1<Nr2,
	Nr11 is Nr1*2,
	multiplu_doi(Nr11,Nr2).

adauga([],_,_,[]):-!.
adauga([H|T],El,P1,[H,El|Rez]):-
	multiplu_doi(1,P1),!,
	P2 is P1+1,
	adauga(T,El,P2,Rez).
adauga([H|T],El,P1,[H|Rez]):-
	P2 is P1+1,
	adauga(T,El,P2,Rez).


