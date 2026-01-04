%cmmdc_test(Nr1:Integer,Nr2:Integer,Rez:Integer)(i,i,o)
%Nr1 primul numar
%Nr2 al doilea numar
%Rez cel mai mare divizor comun
cmmdc_test(Nr1,0,Nr1):-!.
cmmdc_test(Nr1,Nr2,Rez):-
	Rest is Nr1 mod Nr2,
	cmmdc_test(Nr2,Rest,Rez).

%cmmmc_test(Nr1:Integer,Nr2:Integer,Rez:integer)(i,i,o)
%Nr1 primul numar
%Nr2 al doilea numar
%Rez cel mai mic multiplu comun
cmmmc_test(Nr1,Nr2,Rez):-
	cmmdc_test(Nr1,Nr2,Div),
	Rez is abs(Nr1*Nr2)//Div.

%cmmmc_lista(L:list,Rez:Integer)(i,o)
%L lista pe care o parcurgem
%Rez cel mai mare multiplu comun din lista
cmmmc_lista([],1):-!.
cmmmc_lista([H|T],Rez):-
	cmmmc_lista(T,Rez1),
	cmmmc_test(H,Rez1,Rez).
