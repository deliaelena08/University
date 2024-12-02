import random
import string

from validare.valid import *
from repository.repo_client import *


class ServiceClient:
    def __init__(self, repo: InMemoryRepoClient, validator: ValidClients):
        self.__repo=repo
        self.__validator=validator

    def add_client(self, id, name, cnp):
        '''
        Adaugarea unui client
        :param id: un numar intreg
        :param name: un string sugestiv
        :param cnp: un sir de numere intregi
        :return: obiectul client creat
        '''
        self.__validator.valid_client(id, name, cnp)
        client = Client(int(id), name, cnp)
        self.__repo.add(client)

    def get_client(self, id):
        '''
        Gasirea unui client dupa id
        :param id: id-ul clientului
        :return: clientul gasit
        '''
        id = int(id)
        return self.__repo.get(id)

    def update_client(self, id, name, cnp):
        '''
        Modificarea unui client
        :param id: un numar natural
        :param name: un string sugestiv
        :param cnp: un numar intreg
        :return: Clientul modificat
        '''
        self.__validator.valid_client(id, name, cnp)
        client = Client(int(id), name, cnp)
        return self.__repo.update(client)

    def gnome_sort(self,list):
        '''
        Sortrea unei liste prin metoda gnome
        :param list: o lista
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

    def order_clients_by_id(self):
        '''
        Ordonarea clientiilor dupa id
        :return: lista de clienti sortati
        '''
        id_list=[]
        for c in self.__repo.get_all():
            id_list.append(c.get_id())
        self.gnome_sort(id_list)
        clients_sorted=[]
        for id in id_list:
            clients_sorted.append(self.__repo.get(id))
        return clients_sorted

    def delete_client(self, id):
        '''
        Stergerea unui client
        :param id: id-ul clientului care sa fie sters
        :return: Clientul sters
        '''
        id = int(id)
        return self.__repo.delete(id)

    def get_all_clients(self):
        '''
        Obtinerea listei de clienti
        :return: toata lista
        '''
        return self.__repo.get_all()


    def random_client(self, n):
        '''
        Generarea unor clienti random
        :param n: numarul de clienti care doresc sa fie generati
        :return: -
        '''
        n = int(n)
        for i in range(n):
            caractere = string.ascii_lowercase + string.digits
            random_id = '' . join([str(random.randint(0, 9))for _ in range(3)])
            first_letter_name = random.choice(string.ascii_uppercase)
            letters_name = '' . join([str(random.choice(caractere)) for _ in range(7)])
            name = first_letter_name + letters_name
            cnp = '' . join([str(random.randint(0, 9)) for _ in range(10)])
            self.__validator.valid_client(id, name, cnp)
            self.__repo.add(Client(int(random_id), name, cnp))
