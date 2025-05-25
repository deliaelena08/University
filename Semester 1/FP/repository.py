class FilmRepository:

    def __init__(self):
        self.__film_stocare = {}

    def add_film(self, film):
        """
        Adauga film
        :param film:obiect
        :return: -
        """
        id_film = film.get_id_film()
        if id_film in self.__film_stocare:
            raise Exception("id film existent")
        self.__film_stocare[id_film] = film

    def delete_film(self, id_film_delete):
        """
        Sterge film
        :param id_film_delete: int
        :return: -
        """
        if id_film_delete not in self.__film_stocare:
            raise Exception("id film inexistent")
        self.__film_stocare.pop(id_film_delete)

    def find_film(self, id_film_find):
        """
        Cauta film dupa id
        :param id_film_find: int
        :return: obiectul cu id ul introdus sau exceptie
        """
        if id_film_find not in self.__film_stocare:
            raise Exception("Nu exista un film cu acest id")
        return self.__film_stocare[id_film_find]

    def get_all(self):
        """
        Returneaza toate filmele
        :return: dictionar
        """
        return [self.__film_stocare[i] for i in self.__film_stocare.keys()]


class ClientRepository:

    def __init__(self):
        self.__client_stocare = {}

    def add_client(self, client):
        """
        Adauga client
        :param client: obiect
        :return: -
        """
        id_client = client.get_id_client()
        if id_client in self.__client_stocare:
            raise Exception("id client existent")
        self.__client_stocare[id_client] = client

    def delete_client(self, id_client_delete):
        """
        Sterge client
        :param id_client_delete: int
        :return: -
        """
        if id_client_delete not in self.__client_stocare:
            raise Exception("id client inexistent")
        self.__client_stocare.pop(id_client_delete)

    def get_all(self):
        """
        Returneaza toti clientii
        :return: dictionar
        """
        return [self.__client_stocare[i] for i in self.__client_stocare.keys()]