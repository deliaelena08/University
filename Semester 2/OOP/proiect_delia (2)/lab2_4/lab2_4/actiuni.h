#pragma once
#include "mylist.h"

void sort_by_scor(Mylist* l);
void sort_by_name(Mylist* l);
int verif_begin_name(Mylist* l, int i, char ch);
int less(player* p, float scor);
void delete(Mylist* l, char* first_name, char* last_name, int* ok);
void test_sort_by_name();
void test_by_scor();
void test_less();
void test_begin_name();