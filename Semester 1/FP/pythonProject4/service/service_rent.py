from repository.repo_rent import *
from repository.repo_client import *
from repository.repo_film import *


class ServiceRent:
    def __init__(self, repo_film: InMemoryRepoFilm, repo_client: InMemoryRepoClient, repo_rent: InMemoryRepoRent):
        self.__repo_client = repo_client
        self.__repo_film = repo_film
        self.__repo_rent = repo_rent

    def rent_a_film(self, client, film):
        '''
        Realizeaza inchirierea unui film
        :param client: id-ul clientului
        :param film: id-ul unui film
        :return: lista de inchirieri
        '''
        client = self.__repo_client.get(client)
        film = self.__repo_film.get(film)
        rent = Rent(client.get_id(), film.get_id())
        self.__repo_rent.add(rent)
        print([str(x) for x in self.__repo_rent.get_all()])

    def last_three(self):
        '''
        Calculeaza primele trei cele mai putin inchiriate filme
        :return: lista de filme inchiriate ordonate crescator
        '''
        list_film_id = []
        list_rented_count = []
        for rent in self.__repo_rent.get_all():
            if rent.get_film_id() not in list_film_id:
                list_film_id.append(rent.get_film_id())
                list_rented_count.append(0)
            else:
                index = list_film_id.index(rent.get_film_id())
                list_rented_count[index] += 1

        for i in range(0, len(list_film_id) - 1):
            for j in range(i + 1, len(list_film_id)):
                if list_rented_count[i] > list_rented_count[j]:
                    client = list_film_id[i]
                    list_film_id[i] = list_film_id[j]
                    list_film_id[j] = client
                    count = list_rented_count[i]
                    list_rented_count[i] = list_rented_count[j]
                    list_rented_count[j] = count

        return [self.__repo_film.get(film_id) for film_id in list_film_id]

    def return_a_film(self, client, film):
        '''
        Returneaza un film
        :param client: id client
        :param film: id film
        :return: lista de inchirieri dupa stergere
        '''
        client = self.__repo_client.get(client)
        film = self.__repo_film.get(film)
        self.__repo_rent.delete(client.get_id(), film.get_id())
        print([str(x) for x in self.__repo_rent.get_all()])

    def delete_all_rents_by_client(self, client):
        '''
        Stergere inchirierilor in functie de client
        :param client: id client
        :return: nu returneaza nimic
        '''
        for rent in self.__repo_rent.get_all():
            if rent.get_client_id() == client:
                self.__repo_rent.delete(rent.get_client_id(), rent.get_film_id())

    def sort(self, list, p1, p2):
        '''
        Sortarea pentru metoda de quicksort
        :param list:lista care sa fie sortata
        :param p1: o pozitie mai mica decat lungimea listei
        :param p2: o pozitie mai mare sau egala cu p1
        :return: pozitia maxima
        '''
        p1=int(p1)
        p2=int(p2)
        pivot = list[p2]
        i = p1 - 1
        for j in range(p1, p2):
            if list[j] <= pivot:
                i = i + 1
                (list[i], list[j]) = (list[j], list[i])
        (list[i + 1], list[p2]) = (list[p2], list[i + 1])
        return i + 1

    # Function to perform quicksort
    def quicksort(self, list, p1, p2):
        '''
        Algoritmul de quicksort
        :param list: lista pentru sortare
        :param p1: pozitia 1
        :param p2: pozitia 2
        :return: lista sortata crescator
        '''
        p1=int(p1)
        p2=int(p2)
        if p1 < p2:
            pi = self.sort(list, p1, p2)
            self.quicksort(list, p1, pi - 1)
            self.quicksort(list, pi + 1, p2)

    def delete_all_rents_by_film(self, film, count):
        '''
        Stergerea inchirierilor in functie de film
        :param film: id film
        :return: nimic
        '''
        rents = self.__repo_rent.get_all()
        count = int(count)
        film = int(film)
        if count < len(rents):
            if rents[count].get_film_id() == film:
                self.__repo_rent.delete(rents[count].get_client_id(), rents[count].get_film_id())
            self.delete_all_rents_by_film(film, count + 1)

    def order_clients_by_name(self):
        '''
        Ordoneaza clientii in functie de numele clientilor
        :return: lista ordonata
        '''
        dict_clients = {}
        self.get_all_renters_recursive(0, dict_clients)

        list_name_clients = []
        list_client = list(dict_clients.values())
        for i in range(0, len(list_client)):
            list_name_clients.append(list_client[i].get_name())
        self.quicksort(list_name_clients, 0, len(list_name_clients)-1)
        return list_name_clients

    def sort_by_two_features(self):
        '''
        Sortarea dupa doua criterii
        :return: Lista sortata
        '''
        list_for_sort=[]
        for rent in self.__repo_rent.get_all():
            list_names = []
            client_id=rent.get_client_id()
            client=self.__repo_client.get(int(client_id))
            list_names.append(client.get_name())
            movie_id=rent.get_film_id()
            film=self.__repo_film.get(int(movie_id))
            list_names.append(film.get_title())
            list_for_sort.append(list_names)

        list_for_sort = sorted(list_for_sort, key=lambda x: (x[0], x[1]))
        return list_for_sort


    def get_all_renters_recursive(self, count, dict_clients):
        '''
        Obtinerea tuturor clientilor care au inchiriat un film
        :param count: Index pentru metoda recursiva
        :param dict_clients: Dictionarul cu clienti
        :return: Dictionarul
        pasi:n-1+1
        nr de executie pentru un pas:1
        Theta(n)=1+1+....+1 de n ori => Complexitatea este Theta(n)=O(n)
        '''
        count = int(count)
        if count < len(self.__repo_rent.get_all()):
            rents = self.__repo_rent.get_all()
            rent = rents[count]
            if rent.get_client_id() not in dict_clients:
                dict_clients[rent.get_client_id()] = self.__repo_client.get(rent.get_client_id())
            self.get_all_renters_recursive(count + 1, dict_clients)

    def order_clients_by_rented_films(self):
        '''
        Ordoneaza lista de Clienti in functie de cei cu cele mai inchiriate filme
        :return: lista ordonata
        '''
        list_client_id = []
        list_rented_count = []
        for rent in self.__repo_rent.get_all():
            if rent.get_client_id() not in list_client_id:
                list_client_id.append(rent.get_client_id())
                list_rented_count.append(1)
            else:
                index = list_client_id.index(rent.get_client_id())
                list_rented_count[index] += 1

        for i in range(0, len(list_client_id) - 1):
            for j in range(i + 1, len(list_client_id)):
                if list_rented_count[i] > list_rented_count[j]:
                    client = list_client_id[i]
                    list_client_id[i] = list_client_id[j]
                    list_client_id[j] = client
                    count = list_rented_count[i]
                    list_rented_count[i] = list_rented_count[j]
                    list_rented_count[j] = count

        return [self.__repo_client.get(client_id) for client_id in list_client_id]

    def order_films_by_most_rented(self):
        '''
        Ordoneaza lista de filme in functie de cele mai inchiriate
        :return: lista ordonata
        '''
        list_film_id = []
        list_rented_count = []
        for rent in self.__repo_rent.get_all():
            if rent.get_film_id() not in list_film_id:
                list_film_id.append(rent.get_film_id())
                list_rented_count.append(0)
            else:
                index = list_film_id.index(rent.get_film_id())
                list_rented_count[index] += 1

        for i in range(0, len(list_film_id) - 1):
            for j in range(i + 1, len(list_film_id)):
                if list_rented_count[i] < list_rented_count[j]:
                    client = list_film_id[i]
                    list_film_id[i] = list_film_id[j]
                    list_film_id[j] = client
                    count = list_rented_count[i]
                    list_rented_count[i] = list_rented_count[j]
                    list_rented_count[j] = count

        return [self.__repo_film.get(film_id) for film_id in list_film_id]

    def list_name_client_and_count_for_most_rented(self):
        '''
        Primii 30%clienti in functie de cei cu cele mai multe filme inchiriate
        :return: lista ordonata
        '''
        list_client_id = []
        list_rented_count = []
        for rent in self.__repo_rent.get_all():
            if rent.get_client_id() not in list_client_id:
                list_client_id.append(rent.get_client_id())
                list_rented_count.append(1)
            else:
                index = list_client_id.index(rent.get_client_id())
                list_rented_count[index] += 1

        for i in range(0, len(list_client_id) - 1):
            for j in range(i + 1, len(list_client_id)):
                if list_rented_count[i] < list_rented_count[j]:
                    client = list_client_id[i]
                    list_client_id[i] = list_client_id[j]
                    list_client_id[j] = client
                    count = list_rented_count[i]
                    list_rented_count[i] = list_rented_count[j]
                    list_rented_count[j] = count

        return [[self.__repo_client.get(client_id).get_name(), count] for client_id, count in
                zip(list_client_id, list_rented_count)]
