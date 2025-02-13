
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include "player.h"
#include "mylist.h"
#include "actiuni.h"
#include <limits.h>

#define _CRTDBG_MAP_ALLOC
#include <crtdbg.h>

void run_test()
{
	/*
	 Aceasta functionalitate ruleaza toate testele
	 */
	testCreateList();
	test_destroy();
	test_begin_name();
	testcreate();
	testsetname();
	testsetname1();
	test_delete();
	testsetscot();
	test_sort_by_name();
	test_by_scor();
	test_find_player();
	testdestroy();
	test_size();
	test_less();
}

void menu2(int* optiune)
{
	/*
	Afisarea meniului pentru sortare
	*/
	printf("\n1.Actualizare nume\n");
	printf("2.Actualizare prenume\n");
	printf("3.Actualizare scor\n");
	printf("Introduceti optiune: ");
	(void)scanf("%d", optiune);
}


void modify_name_ui(Mylist* l, int i)
{
	/*
	Aceasta functionalitate apeleaza modificarea unui jucator dupa nume
	Parametrii:o lista, o pozitie i din lista
	*/
	char first_n[50];
	printf("Introduceti numele pentru actualizare: ");
	(void)scanf("%s", &first_n);
	set_name(&(l->array[i]), first_n);
}

void modify_scor_ui(Mylist* l, int i)
{
	/*
	Aceasta functionalitate apeleaza modificarea unui jucator dupa scor
	Parametrii:o lista, o pozitie i din lista
	*/
	printf("Introduceti al-catelea scor vreti sa-l modificati: ");
	int nr;
	float scor;
	(void)scanf("%d", &nr);
	printf("Introduceti scorul: ");
	(void)scanf("%f", &scor);
	set_scor(&(l->array[i]), scor, nr - 1);
}

void modify_last_name_ui(Mylist* l, int i)
{
	/*
	Aceasta functionalitate apeleaza modificarea unui jucator dupa nume
	Parametrii:o lista, o pozitie i din lista
	*/
	char last_n[50];
	printf("Introduceti prenumele pentru actualizare: ");
	(void)scanf("%s", &last_n);
	set_last_name(&(l->array[i]), last_n);
}



void update(Mylist* l, char* first_name, char* last_name)
{
	/*
	 Aceasta functionalitate actualizeaza datele unui jucator dupa anumite criterii
	 Parametrii:lista de tip  mylist,numele si prenumele jucatorului
	 Preconditii:sa existe lista de cel putin 1 element si sa fie alese si introduse datele corect
	 */
	for (int i = 1; i <= l->lg; i++)
	{
		if (strcmp(l->array[i].first_name, first_name) == 0)
			if (strcmp(l->array[i].last_name, last_name) == 0)
			{
				int optiune = 0;
				menu2(&optiune);
				if (optiune == 1)
				{
					modify_name_ui(l, i);
				}
				else
				{
					if (optiune == 2)
					{
						modify_last_name_ui(l, i);
					}
					else
					{
						modify_scor_ui(l, i);
					}
				}
			}
	}
}

void begin_of_name(Mylist* l, char ch)
{
	/*
	 Aceasta functionalitate cauta jucatorii a carui nume incep cu o litera data
	 Parametrii:lista de tip  mylist,o litera data de utilizator
	 Preconditii:sa existe lista de cel putin 1 element cu un astfel de caracter
	 */

	for (int i = 1; i <= l->lg; i++)
		if (verif_begin_name(l, i, ch) == 1)
			printf("%s %s\n", l->array[i].first_name, l->array[i].last_name);

}

void afisare(Mylist* l)
{
	/*
	 Aceasta functionalitate afiseaza numele si prenumele participantilor din lista
	 Parametrii:lista de tip struct
	 Preconditii:lista sa nu fie vida
	 */
	for (int i = 1; i <= l->lg; i++)
		printf("%s %s\n", l->array[i].first_name, l->array[i].last_name);
}


void delete_ui(Mylist* l)
{
	/*
	 Aceasta functionalitate colecteaza si afiseaza datele pentru stergere
	 Parametrii:lista de tip struct
	 Preconditii:lista ,numele si prenumele sa fie existente
	 */
	int ok = 0;
	char first_name[50], last_name[50];
	printf("Introduceti numele utilizatorului pe care il cautati: ");
	(void)scanf("%s", &first_name);
	printf("Introduceti prenumele utilizatorului pe care il cautati: ");
	(void)scanf("%s", &last_name);
	delete(l, first_name, last_name, &ok);
	if (ok == 0)
		printf("Stergere nu s-a putut efectua\n");
	else
		printf("Stergere cu succes\n");
}


void less_ui(Mylist* l)
{
	/*
	 Aceasta functionalitate cauta jucatorii cu scoruri mai mici decat unul dat
	 Parametrii:lista de tip mylist
	 Preconditii:scorul sa fie float
	 Postconditii:sa existe astfel de jucatori
	 */
	float scor;
	printf("Introduceti un scor: ");
	(void)scanf("%f", &scor);
	for (int i = 1; i <= l->lg; i++)
		if (less(&(l->array[i]), scor) == 0)
			printf("%s %s\n", l->array[i].first_name, l->array[i].last_name);

}

void begin_of_name_ui(Mylist* l)
{
	/*
	 Aceasta functionalitate cauta jucatorii ai carui nume incep cu o litera data
	 Parametrii:lista de tip mylist
	 Preconditii:sa exista lista si sa fie un character valid
	 Postconditii:sa existe astfel de jucator
	 */
	char ch;
	printf("Introduceti o litera: ");
	(void)scanf(" %c", &ch);
	begin_of_name(l, ch);
}

void update_ui(Mylist* l)
{
	/*
	 Aceasta functionalitate configureaza datele pentru actualizare
	 Parametrii:lista de tip mylist
	 Preconditii:sa existe lista
	 Postconditii:sa existe jucatorul
	 */
	char first_name[50], last_name[50];
	printf("Introduceti numele utilizatorului pe care il cautati: ");
	(void)scanf("%s", &first_name);
	printf("Introduceti prenumele utilizatorului pe care il cautati: ");
	(void)scanf("%s", &last_name);
	update(l, first_name, last_name);
}

void add_on_list_ui(Mylist* l)
{
	/*
	 Aceasta functionalitate configureaza datele pentru crearea unui nou jucator
	 Parametrii:lista de tip mylist
	 Preconditii:sa existe lista iar datele sa fie valide
	 */
	int ok = 0;
	char first_name[50], last_name[50];
	float scores[10];
	printf("Introduceti numele: ");
	(void)scanf("%s", &first_name);
	printf("Introduceti prenumele: ");
	(void)scanf("%s", &last_name);
	for (int i = 0; i < 10; i++)
	{
		printf("Punctajul %d este :", i + 1);
		(void)scanf("%f", &scores[i]);
	}
	add(l, first_name, last_name, scores, &ok);
	if (ok == 1)
		printf("Adaugare cu succes!");
	else
		printf("Adaugare nereusita, mai incercati odata!");
}

void sort_by_name_ui(Mylist* l)
{
	/*
	Aceasta functionalitate apeleaza sortarea dupa nume
	*/
	sort_by_name(l);
	afisare(l);
}

void sort_by_scor_ui(Mylist* l)
{
	/*
	Aceasta functionalitate apeleaza sortarea dupa scor
	*/
	sort_by_scor(l);
	afisare(l);
}

void afisare_scor(Mylist* l)
{
	/*
	Aceasta functionalitate afiseaza scorurile unui jucator dat
	*/
	char first_name[50], last_name[50];
	printf("Introduceti numele utilizatorului pe care il cautati: ");
	(void)scanf("%s", &first_name);
	printf("Introduceti prenumele utilizatorului pe care il cautati: ");
	(void)scanf("%s", &last_name);
	int poz = find_player(l, first_name, last_name);
	if (poz == -1)
		printf("Nu exista acest player");
	else
	{
		printf("%s", l->array[poz].first_name);
		for (int i = 0; i < 10; i++)
			printf("Scorul de pe pozitia %d are valoarea %f\n", i + 1, l->array[poz].scores[i]);
	}
}

void menu1()
{
	/*
	Aceasta functionalitate afiseaza consola
	*/

	printf("\nMeniu\n");
	printf("0.Exit\n");
	printf("1.Adaugare participant\n");
	printf("2.Actualizare participant existent\n");
	printf("3.Stergere particicipant\n");
	printf("4.Afisarea participantilor care au un scor mai mic decat unul dat\n");
	printf("5.Afisarea participantilor a carui nume incep cu o litera data\n");
	printf("6.Sortarea participantilor dupa nume\n");
	printf("7.Sortarea participantilor dupa scor\n");
	printf("8.Afisare participanti\n");
	printf("9.Afisare scorurile participantilor\n");
	printf("Introduceti optiunea: ");
}

int main()
{
	run_test();
	_CrtDumpMemoryLeaks();
	/*
		Aceasta  gestioneaza meniul si este consola aplicatiei
		*/
	int optiune, ok = 1;
	Mylist list = create_vid();
	while (ok != 0)
	{
		menu1();
		(void)scanf("%d", &optiune);
		switch (optiune)
		{
		case 0:
			return 0;
		case 1:
			add_on_list_ui(&list);
			break;
		case 2:
			update_ui(&list);
			break;
		case 3:
			delete_ui(&list);
			break;
		case 4:
			less_ui(&list);
			break;
		case 5:
			begin_of_name_ui(&list);
			break;
		case 6:
			sort_by_name_ui(&list);
			break;
		case 7:
			sort_by_scor_ui(&list);
			break;
		case 8:
			afisare(&list);
			break;
		case 9:
			afisare_scor(&list);
			break;
		}
	}
	destroy(&list);
	return 0;
}