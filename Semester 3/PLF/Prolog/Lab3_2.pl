%se da un numar n>0,toate descompunerile sale in suma de numere prime d.

%prim(Nr:Intrg,Div:Intreg) (i,i) det
%verifica daca un numar dat este prim sau nu
prim(Nr,Div):-
	Patrat is Div*Div,
	Patrat > Nr,!.
prim(Nr,Div):-
	Rest is Nr mod Div,
	Rest > 0,
	Divnou is Div+1,
	prim(Nr,Divnou).

%is_prim(Nr:integer) (i)
is_prim(Nr):-Nr>1,prim(Nr,2).

%genereaza_prime(Max:integer,Nr:integer,Rez:list) (i,o)
%functia returneaza o lista de numere prime pana la un max
genereaza_prime(Max,Nr,[]):-Nr>Max,!.
genereaza_prime(Max,Nr,[Nr|Rez]):-
	is_prim(Nr),!,
	Nrnou is Nr+1,
	genereaza_prime(Max,Nrnou,Rez).
genereaza_prime(Max,Nr,Rez):-
	Nrnou is Nr+1,
	genereaza_prime(Max,Nrnou,Rez).

%lista_numere_prime(Nr:intreg,Rez:lista) (i,o) det.
lista_numere_prime(Nr,Rez):-genereaza_prime(Nr,2,Rez).

%combSuma(L:lista,Nr:integer,Rez:lista) (i,i,o) nedet
%Nr reprezinta numarul care trebuie descompus
combSuma1([H|_],H,[H]).
combSuma1([_|T],Nr,Rez):-
	combSuma1(T,Nr,Rez).
combSuma1([H|T],Nr,[H|Rez]):-
	S1 is Nr-H,
	S1>0,
	combSuma1(T,S1,Rez).

%toatecombSuma(Nr:integer,LL:list of list)(i,o) det
toatecombSuma(Nr,LL):-
	lista_numere_prime(Nr,L),
	findall(Rez,combSuma1(L,Nr,Rez),LL).




