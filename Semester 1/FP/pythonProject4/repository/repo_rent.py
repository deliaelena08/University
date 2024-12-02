from domain.domain import Rent


class InMemoryRepoRent:

    def __init__(self):
        self.__list = []

    def add(self, rent):
        '''
        Aduagraea unei inchirieri
        :param rent: obiectul inchiriere
        :return:-
        '''
        if rent in self.__list:
            raise ValueError("Filmul este deja inchiriat de aceiasi persoana")
        self.__list.append(rent)

    def delete(self, client, film):
        '''
        Stergerea unui film
        :param client: id-ul clientului din inchiriere
        :param film: id-ul filmului din inchiriere
        :return: -
        '''
        rent = Rent(client, film)
        if rent not in self.__list:
            raise ValueError("Filmul nu a fost inchiriat de aceasta persoana")
        self.__list.remove(rent)

    def get_all(self):
        '''
        Obtinerea listei de inchirieri
        :return: Toata lista
        '''
        return self.__list

    def get_rent(self,id1,id2):
        '''
        Obtinerea unei inchirieri
        :param id1: id-ul clientului
        :param id2: id-ul filmului
        :return: inchirierea daca exista
        '''
        for r in self.__list:
            if r.get_film_id()==id2 and r.get_client_id()==id1:
                return r
        return None

class FileRepoRent(InMemoryRepoRent):
    def __init__(self, filename):
        InMemoryRepoRent.__init__(self)
        self.__filename = filename
        self.load_in_memory()

    def load_in_memory(self):
        '''
        Incarcarea in memorie
        :return: -
        '''
        with open(self.__filename, mode='r', encoding='utf-8') as rents_file:
            lines = rents_file.readlines()
            lines = [line.strip() for line in lines if line.strip() != '']
            for line in lines:
                id1,id2 = line.split(',')
                InMemoryRepoRent.add(self,Rent(int(id1),int(id2)))

    def write_to_file(self):
        '''
        Scrierea in fisier
        :return: -
        '''
        rents = InMemoryRepoRent.get_all(self)
        rents = [str(str(rent.get_client_id())) + ',' + str(rent.get_film_id()) for rent in rents]
        with open(self.__filename, mode='w', encoding='utf-8') as rents_file:
            text_to_write = '\n'.join(rents)
            rents_file.write(text_to_write)

    def add(self, rent):
        '''
        Adaugraea in fisier
        :param rent: Obiectul inchiriere
        :return: inchirerea adaugata
        '''
        rent = InMemoryRepoRent.add(self, rent)
        self.write_to_file()
        return rent

    def delete(self, id_client,id_film):
        '''
        Stergerea unei inchirieri
        :param id_client: id-ul clientului din inchiriere
        :param id_film: id-ul filmului din inchiriere
        :return:inchirierea stearsa
        '''
        rent = InMemoryRepoRent.delete(self, id_client,id_film)
        self.write_to_file()
        return rent