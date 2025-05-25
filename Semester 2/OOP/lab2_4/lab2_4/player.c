#include "player.h"
#include <string.h>
#include <assert.h>

int valid(char* first_name, char* last_name, float scores[])
{/*
	 Aceasta functionalitate valideaza datele introduse de utilizator
	 Parametrii:nume,prenume si scorurile sale
	 Preconditii:sa fie de tipul lor
	 Postconditii:sa respecte conditiile
	 */
	int not_valid = 0;
	for (int i = 0; i < 10; i++)
		if (scores[i] < 0 || scores[i]>10)
		{
			not_valid = 1;
		}
	if (strlen(first_name) < 2)
	{
		not_valid = 1;
	}
	else
		if (strlen(last_name) < 2)
		{
			not_valid = 1;
		}
	return not_valid;
}


void set_last_name(player* p, char* last_n)
{
	/*
	Aceasta functionalitate seteaza prenumele
	Parametrii: p de tip player,numele de tip string
	Preconditii: sa existe player
	*/
	strcpy(p->last_name, &(*last_n));
}

void set_name(player* p, char* first_n)
{
	/*
	Aceasta functionalitate seteaza numele
	Parametrii: p de tip player,numele de tip string
	Preconditii: sa existe player
	*/
	strcpy(p->first_name, first_n);
}

void set_scor(player* p, float scor, int nr)
{
	/*
	Aceasta functionalitate seteaza scorul
	Parametrii: p de tip player, scorul pe care sa-l modifice si pozitia
	Preconditii: sa existe player
	*/
	p->scores[nr] = scor;
}



player creare(char* first_name, char* last_name, float scores[])
{
	/*
	 Aceasta functionalitate creaza un jucator
	 Parametrii:numele,prenumele si scorurile sale
	 Preconditii:sa fie de tipul lor
	 */
	player p;
	strcpy(p.first_name, first_name);
	strcpy(p.last_name, last_name);
	for (int i = 0; i < 10; i++)
		p.scores[i] = scores[i];
	return p;
}

player destroy_player(player* p)
{
	p->first_name[0] = '\0';
	p->last_name[0] = '\0';
	for (int i = 0; i < 10; i++)
		p->scores[i] = 0;
	return *p;
}

void testsetname()
{
	float scores[] = { 1,2,3,4,5,6,7,8,9,1 };
	player p = creare("ana", "maria", scores);
	set_name(&p, "cristina");
	assert(strcmp(p.first_name, "cristina") == 0);

}
void testdestroy()
{
	float scores[] = { 1,2,3,4,5,6,7,8,9,1 };
	player p = creare("ana", "maria", scores);
	destroy_player(&p);
	assert(strlen(p.first_name) == 0);
	assert(strlen(p.last_name) == 0);
	assert(p.scores[2] == 0);
}

void testsetscot()
{
	float scores[] = { 1,2,3,4,5,6,7,8,9,1 };
	player p = creare("ana", "maria", scores);
	set_scor(&p, 2, 0);
	assert(p.scores[0] == 2);
}

void testsetname1()
{
	float scores[] = { 1,2,3,4,5,6,7,8,9,1 };
	player p = creare("ana", "maria", scores);
	set_last_name(&p, "cristina");
	assert(strcmp(p.last_name, "cristina") == 0);
}

void testcreate()
{
	float scores[] = { 1,2,3,4,5,6,7,8,9,1 };
	player p = creare("ana", "maria", scores);
	assert(strcmp(p.first_name, "ana") == 0);
	assert(strcmp(p.last_name, "maria") == 0);
	p.scores[3] = 4;
	assert(p.scores[3] == 4);
	float scores1[] = { 1,2,3,4,5,6,7,8,9,11 };
	int v1 = valid("ana", "maria", scores1);
	assert(v1 == 1);
	float scores2[] = { 1,2,3,4,5,6,7,8,9,1 };
	int v2 = valid("a", "maria", scores2);
	assert(v2 == 1);
	float scores3[] = { 1,2,3,4,5,6,7,8,9,1 };
	int v3 = valid("ana", "m", scores3);
	assert(v3 == 1);
}