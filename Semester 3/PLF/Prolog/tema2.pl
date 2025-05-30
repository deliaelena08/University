%1a.suma a doua numere scrise in reprezentare de lista

%se calculeaza lungimea unei liste date
%lungime(L:lista,N lungimea finala)
%Model de flux:(i,o)

%lista goala are lungimea 0
lungime([], 0).

%parcurgem in continuare lista
lungime([H|Tail], N) :-
    Rest is H mod 2 ,
    Rest = 0,
    lungime(Tail, N1),
    N is N1 + 1.
lungime([H|Tail],N):-
	Rest is H mod 2,
	Rest = 1 ,
	lungime(Tail,N).

%transformare lista in numar: [1,2,3] => 321, l1*10^n-1+l2*10^n-2+...+ln
%transformare_in_numar(L:lista de intregi,N:dimensiunea listei,Rez:rezultatul)
%model de flux(i,i,o)

%daca am parcurs toata lista ne oprim
transformare_in_numar([],_,0):-!.

%daca a mai ramas doar un element se returneaza acesta
transformare_in_numar([H],_,H):-!.

%daca mai sunt elemente in lista atunci adaugam cifra pe pozitia 10^n-1
transformare_in_numar([H|T], N, Rez) :-
    N1 is N - 1,
    transformare_in_numar(T, N1, RezRest),
    H1 is H * 10 ** N1,
    Rez is H1 + RezRest.

%Predicatul transforma un numar intr-o lista
%numar_in_lista(El:numarul intreg,L:lista rezultat)
%Model de flux:(i,o)

%când numărul este mai mic decât 10, returnăm lista doar cu acel număr
numar_in_lista(El, [El]) :-
    El < 10, !.

%daca numarul ramas e mai mare ca 10 atunci obtinem cifra
%si o adaugam in lista, impartind numarul ramas la 10 pt apel
numar_in_lista(El, Lista) :-
    Cifra is El mod 10,
    NumarNou is El // 10,
    numar_in_lista(NumarNou, ListaParțială),
    append(ListaParțială, [Cifra], Lista).


%calculam suma a doua numere reprezentate in liste
%suma_liste(L1:lista cifre,L2:lista cifre,Rez rezultatul sumei)
%Model de flux:(i,i,o)
suma_liste(L1,L2,Rez):-
	lungime(L1,N1),
	transformare_in_numar(L1,N1,Rez1),
	lungime(L2,N2),
	transformare_in_numar(L2,N2,Rez2),
	Rez is Rez1+Rez2.

% 1b se da o lista formata din numere intregi si liste de cifre, se
% calculeaza suma tuturor numerelor din subliste si returneaza suma in l
% suma_subliste(L:lista cu subliste,Rez:suma reprezentata in lista
% Model de flux: (i,o)

%daca s-a parcurs toata lista predicatul se opreste
suma_subliste([],[0]):-!.

%daca am gasit o sublista ,facem suma dintre sublista si rezultat si il
%transformam intr-o lista
suma_subliste([H|T],Rez):-
	is_list(H),!,
	suma_subliste(T,Rez1),
	suma_liste(H,Rez1,Suma),
	numar_in_lista(Suma,Rez).

%daca nu este o sublista atunci parcurgem in continuare
suma_subliste([_|T],Rez):-
	suma_subliste(T,Rez).














