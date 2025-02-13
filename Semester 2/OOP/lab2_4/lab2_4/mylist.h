#pragma once
#include "player.h"

typedef struct {
	player* array;
	int lg;
	int cp;
}Mylist;

Mylist create_vid();
void destroy(Mylist* l);
int find_player(Mylist* l, char* f_n, char* l_n);
int size(Mylist* l);
void add(Mylist* l, char* n, char* pr, float scores[], int* ok);
void delete(Mylist* l, char* first_name, char* last_name, int* ok);
void testCreateList();
void test_size();
void test_find_player();
void test_delete();
void test_destroy();