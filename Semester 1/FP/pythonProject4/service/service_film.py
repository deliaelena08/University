from validare.valid import *
from repository.repo_film import *


class ServiceFilm:
    def __init__(self, repo: InMemoryRepoFilm, validator: ValidFilms):
        self.__repo=repo
        self.__validator=validator

    def add_film(self, id, title,description,type):
        '''
        Adaugarea unui film in memorie
        :param id: un numar intreg
        :param title: un string sugestiv
        :param description: un string sugestiv
        :param type: un string sugestiv
        :return: obiectul film
        '''
        self.__validator.valid_film(id,title,description,type)
        film=Film(int(id),title,description,type)
        self.__repo.add(film)

    def get_film(self, id):
        '''
        Gasirea unui film dupa id
        :param id: un numar intreg
        :return: Filmul gasit
        '''
        id = int(id)
        return self.__repo.get(id)

    def update_film(self,id, title,description,type):
        '''
        Modificarea unui film existent
        :param id: un numar intreg,un id existent
        :param title: un string sugestiv
        :param description: un string sugestiv
        :param type: un string sugestiv
        :return: filmul modificat
        '''
        self.__validator.valid_film(id, title, description, type)
        film = Film(int(id), title, description, type)
        return self.__repo.update(film)

    def delete_film(self, id):
        '''
        Stergerea unui film
        :param id: un numar intreg
        :return: filmul sters
        '''
        id = int(id)
        return self.__repo.delete(id)

    def get_all_films(self):
        '''
        Obtinerea tuturor filmelor
        :return: lista de filme
        '''
        return self.__repo.get_all()

    def gnome_sort(self,list):
        '''
        Sortearea prin algoritmul gnome
        :param list: lista
        :return: lista sortata crescator
        '''
        n=len(list)
        i=0
        while i<n:
            if i==0:
                i=i+1
            if list[i]>=list[i-1]:
                i=i+1
            else:
                aux=list[i]
                list[i]=list[i-1]
                list[i-1]=aux
                i=i-1
        return list

    def order_films_by_id(self):
        '''
        Ordonarea filmelor dupa id
        :return: Lista cu filme sortate
        '''
        id_list=[]
        for f in self.__repo.get_all():
            id_list.append(f.get_id())
        self.gnome_sort(id_list)
        films_sorted=[]
        for id in id_list:
            film=self.__repo.get(id)
            films_sorted.append(film)
        return films_sorted
