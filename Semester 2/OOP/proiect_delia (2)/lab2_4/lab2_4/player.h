#pragma once

typedef struct
{
	char last_name[50];
	char first_name[50];
	float scores[10];

}player;

int valid(char* first_name, char* last_name, float scores[]);
player creare(char* first_name, char* last_name, float scores[]);
player destroy_player(player* p);
void set_name(player* p, char* first_n);
void set_scor(player* p, float scor, int nr);
void set_last_name(player* p, char* last_n);
void testcreate();
void testdestroy();
void testsetname1();
void testsetname();
void testsetscot();