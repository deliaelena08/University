#include "actiuni.h"
#include <string.h>
#include <assert.h>

void sort_by_scor(Mylist* l)
{
	/*
	 Aceasta functionalitate sorteaza lista dupa un scorurile jucatoriilor
	 Parametrii:lista de tip mystruct
	 Preconditii:toate scorurile sa fie valide
	 */
	for (int i = 1; i < l->lg; i++)
		for (int j = i + 1; j <= l->lg; j++)
		{
			float scor1 = 0;
			float scor2 = 0;
			for (int k = 0; k < 10; k++)
			{
				scor1 += l->array[i].scores[k];
				scor2 += l->array[j].scores[k];
			}
			if (scor1 < scor2)
			{
				player p;
				p = l->array[i];
				l->array[i] = l->array[j];
				l->array[j] = p;
			}
		}
}


void sort_by_name(Mylist* l)
{
	/*
	 Aceasta functionalitate sorteaza jucatorii dupa numele lor
	 Parametrii:lista de tip  mylist
	 Preconditii:sa existe lista de cel putin 1 element
	 */
	for (int i = 1; i < l->lg; i++)
		for (int j = i + 1; j <= l->lg; j++)
			if (strcmp(l->array[i].first_name, l->array[j].first_name) > 0)
			{
				player p;
				p = l->array[i];
				l->array[i] = l->array[j];
				l->array[j] = p;
			}
}

int verif_begin_name(Mylist* l, int i, char ch)
{
	/*
	Aceasta functionalitate verifica daca un player incepe cu un caracter
	 Parametrii:lista de tip  mylist,pozitia si char
	 Preconditii:sa existe lista de cel putin 1 element
	*/
	char name[100];
	strcpy(name, l->array[i].first_name);
	char ch1 = name[0];
	if (ch1 == ch)
		return 1;
	return 0;
}

int less(player* p, float scor)
{
	/*
	 Aceasta functionalitate verifica daca scorurile sunt mai mici decat cea data
	 Parametrii:lista de tip  mylist si un scor introdus de la tastatura
	 Preconditii:sa existe playerul si scorul
	 */
	int ok = 0;
	for (int j = 0; j < 10; j++)
		if (p->scores[j] >=scor)
			ok = 1;
	return ok;

}



void test_sort_by_name()
{
	Mylist l = create_vid();
	int ok = 0;
	float scores[] = { 1,2,3,4,5,6,7,8,9,1 };
	add(&l, "ana", "maria", scores, &ok);
	float scores2[] = { 1,1,1,1,1,1,1,1,1,1 };
	ok = 0; ok = 0;
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
	sort_by_name(&l);
	assert(strcmp(l.array[1].last_name, "george") == 0);
	destroy(&l);
}

void test_by_scor()
{
	Mylist l = create_vid();
	int ok = 0;
	float scores[] = { 1,2,3,4,5,6,0,0,0,1 };
	add(&l, "ana", "maria", scores, &ok);
	ok = 0;
	float scores2[] = { 1,1,1,1,1,1,1,1,1,1 };
	add(&l, "ovidiu", "mihai", scores2, &ok);
	ok = 0;
	float scores3[] = { 0,2,4,6,4,2,1,7,8,1 };
	add(&l, "marius", "pruna", scores3, &ok);
	ok = 0;
	sort_by_scor(&l);
	assert(strcmp(l.array[1].first_name, "marius") == 0);
	destroy(&l);
}

void test_less()
{
	Mylist l = create_vid();
	int ok = 0;
	float scores[] = { 1,2,3,4,5,6,0,0,0,1 };
	add(&l, "ana", "maria", scores, &ok);
	ok = 0;
	float scores2[] = { 1,1,1,1,1,1,1,1,1,1 };
	add(&l, "ovidiu", "mihai", scores2, &ok);
	ok = 0;
	float scores3[] = { 0,2,4,6,4,2,1,7,8,1 };
	add(&l, "marius", "pruna", scores3, &ok);
	ok = 0;
	assert(less(&(l.array[1]), (float)3) == 1);
	destroy(&l);
}


void test_begin_name()
{
	Mylist l = create_vid();
	int ok = 0;
	float scores[] = { 1,2,3,4,5,6,0,0,0,1 };
	add(&l, "ana", "maria", scores, &ok);
	ok = 0;
	float scores2[] = { 1,1,1,1,1,1,1,1,1,1 };
	add(&l, "oanea", "mihaita", scores2, &ok);
	assert(verif_begin_name(&l, 1, 'b') == 0);
	assert(verif_begin_name(&l, 2, 'o') == 1);
	destroy(&l);
}