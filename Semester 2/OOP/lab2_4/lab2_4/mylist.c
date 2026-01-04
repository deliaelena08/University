#include "mylist.h"
#include <assert.h>
#include<stdlib.h>
#include <string.h>

Mylist create_vid()
{
	Mylist rez;
	rez.cp = 5;
	rez.array = malloc(sizeof(player) * ((rez.cp+1)));
	rez.lg = 0;
	return rez;
}

int size(Mylist* l)
{
	/*
	Aceasta functionalitate transmite marirea
	*/
	return l->lg;
}
void destroy(Mylist* l)
{
	/*
	Aceasta functionalitate distruge lista
	*/
	for (int i = 1; i <= l->lg; i++)
	{
		player p;
		p = destroy_player(&l->array[i]);
	}
	l->lg = 0;
	free(l->array);
}

int find_player(Mylist* l, char* f_n, char* l_n)
{
	/*
	 Aceasta functionalitate cauta un player in lista
	 Parametrii:lista de typ mylist,un nume,un prenume de tip string
	 Preconditii:datele sa fie valide si lista nenula
	 Postconditii:sa existe player-ul
	*/
	for (int i = 1; i <= l->lg; i++)
	{
		if (strcmp(l->array[i].first_name, f_n) == 0)
			if (strcmp(l->array[i].last_name, l_n) == 0)
				return i;
	}
	return -1;
}

void add(Mylist* l, char* n, char* pr, float scores[], int* ok)
{
	/*
	 Aceasta functionalitate face adaugarea unui participant in lista
	 Parametrii:lista de typ mylist,un nume,un prenume si scorurile introduse de utilizator
	 Preconditii:datele sa fie valide
	 */
	if (l->cp <= l->lg)
	{
		player* aux = malloc(sizeof(player) * (l->cp+6));
		for (int i = 1; i <= l->lg; i++)
		{
			aux[i] = l->array[i];
		}
		free(l->array);
		l->array = aux;
		l->cp = l->cp + 6;
	}
	int val = valid(n, pr, scores);
	if (val == 0)
	{
		player p;
		strcpy(p.first_name, n);
		strcpy(p.last_name, pr);
		for (int i = 0; i < 10; i++)
			p.scores[i] = scores[i];
		l->lg += 1;
		l->array[l->lg] = p;
		*ok = 1;
	}
}

void delete(Mylist* l, char* first_name, char* last_name, int* ok)
{
	/*
	 Aceasta functionalitate sterge un jucatori
	 Parametrii:lista de tip mylist,numele si prenumele unui jucator existent,o variabila
	 Preconditii:datele sa fie existente
	 Postconditii:sa se gaseasca acel jucator
	 */

	for (int i = 1; i <= l->lg; i++)
	{
		if (strcmp(l->array[i].first_name, first_name) == 0)
			if (strcmp(l->array[i].last_name, last_name) == 0)
			{
				for (int j = i + 1; j <= l->lg; j++)
					l->array[j - 1] = l->array[j];
				l->lg--;
				*ok = 1;
				break;}}
}



void testCreateList()
{
	Mylist l = create_vid();
	float scores[] = { 1,2,3,4,5,6,7,8,9,1 };
	int ok = 0;
	add(&l, "ana", "maria", scores, &ok);
	assert(size(&l) == 1);
	destroy(&l);
}

void test_size()
{
	Mylist l = create_vid();
	int ok = 0;
	float scores[] = { 1,2,3,4,5,6,7,8,9,1 };
	add(&l, "ana", "maria", scores, &ok);
	float scores2[] = { 1,1,1,1,1,1,1,1,1,1 };
	ok = 0;
	add(&l, "ovidiu", "mihai", scores2, &ok);
	float scores3[] = { 1,1,1,1,1,1,1,1,1,1 };
	ok = 0;
	add(&l, "mama", "ana", scores3, &ok);
	float scores4[] = { 1,1,1,1,1,1,1,1,1,1 };
	ok = 0;
	add(&l, "andreea", "laura", scores4, &ok);
	float scores5[] = { 1,1,1,1,1,1,1,1,1,1 };
	ok = 0;
	add(&l, "adrian", "george", scores5, &ok);
	float scores6[] = { 1,1,1,1,1,1,1,1,1,1 };
	ok = 0;
	add(&l, "cristian", "popovici", scores6, &ok);
	assert(size(&l) == 6);
	destroy(&l);
}

void test_destroy()
{
	Mylist l = create_vid();
	int ok = 0;
	float scores[] = { 1,2,3,4,5,6,7,8,9,1 };
	add(&l, "ana", "maria", scores, &ok);
	float scores2[] = { 1,1,1,1,1,1,1,1,1,1 };
	ok = 0;
	add(&l, "ovidiu", "mihai", scores2, &ok);
	destroy(&l);
	assert(l.lg == 0);
}


void test_find_player()
{
	Mylist l = create_vid();
	int ok = 0;
	float scores[] = { 1,2,3,4,5,6,7,8,9,1 };
	add(&l, "ana", "maria", scores, &ok);
	float scores2[] = { 1,1,1,1,1,1,1,1,1,1 };
	ok = 0;
	add(&l, "ovidiu", "mihai", scores2, &ok);
	assert(find_player(&l, "ovidiu", "mihai") == 2);
	assert(find_player(&l, "ovi", "mihai") == -1);
	destroy(&l);
}

void test_delete()
{
	Mylist l = create_vid();
	int ok = 0;
	float scores[] = { 1,2,3,4,5,6,7,8,9,1 };
	add(&l, "ana", "maria", scores, &ok);
	float scores2[] = { 1,1,1,1,1,1,1,1,1,1 };
	ok = 0;
	add(&l, "ovidiu", "mihai", scores2, &ok);
	ok = 0;
	delete(&l, "ana", "maria", &ok);
	assert(l.lg == 1);
	destroy(&l);
}