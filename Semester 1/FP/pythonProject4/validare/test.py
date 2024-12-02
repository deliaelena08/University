from ui.ui import *

class tests:

    def __init__(self, repo_film: InMemoryRepoFilm, service_console: Console, repo_client: InMemoryRepoClient,
                 service_film: ServiceFilm, service_client: ServiceClient, repo_rent: InMemoryRepoRent,
                 service_rent: ServiceRent):
        self.__repo_film = repo_film
        self.__repo_client = repo_client
        self.__service_film = service_film
        self.__service_client = service_client
        self.__repo_rent = repo_rent
        self.__service_rent = service_rent
        self.__service_console = service_console

    def __test_create_film(self):
        id = "5"
        title = "spiderman"
        description = "ok"
        type = "actiune"
        ValidFilms.valid_film(self,id, title, description, type)
        film = Film(int(id), title, description, type)
        assert int(film.get_id()) == int(id)
        assert film.get_title() == title
        assert film.get_description() == description
        assert film.get_type() == type

    def __test_add_film(self):
        id1 = "6"
        id2 = "7"
        name1 = "The nun"
        name2 = "Divergent"
        description1 = "good"
        description2 = "great"
        type1 = "horror"
        type2 = "action"

        Film1 = Film(int(id1), name1, description1, type1)
        Film2 = Film(int(id2), name2, description2, type2)
        self.__repo_film.add(Film1)
        self.__repo_film.add(Film2)
        assert len(self.__repo_film.get_all()) == 2
        self.__repo_film.delete(int(id1))
        self.__repo_film.delete(int(id2))

    def __test_delete_film(self):
        id1 = "6"
        id2 = "7"
        name1 = "The nun"
        name2 = "Divergent"
        description1 = "good"
        description2 = "great"
        type1 = "horror"
        type2 = "action"

        Film1 = Film(int(id1), name1, description1, type1)
        Film2 = Film(int(id2), name2, description2, type2)
        self.__repo_film.add(Film1)
        self.__repo_film.add(Film2)
        self.__repo_film.delete(int(id1))
        assert len(self.__repo_film.get_all()) == 1

    def __test_crete_client(self):
        id = "8"
        name = "Mariana"
        cnp = "60973625279"
        ValidClients.valid_client(self,id,name,cnp)
        client = Client(int(id), name, cnp)
        assert int(client.get_id()) == int(id)
        assert client.get_name() == name
        assert client.get_cnp() == cnp

    def __test_add_client(self):
        id1 = "8"
        id2 = "5"
        name1 = "Ana"
        name2 = "Alex"
        cnp1 = "609387323"
        cnp2 = "509735722"

        Client1 = Client(int(id1), name1, cnp1)
        Client2 = Client(int(id2), name2, cnp2)

        self.__repo_client.add(Client1)
        self.__repo_client.add(Client2)
        assert len(self.__repo_client.get_all()) == 2
        self.__repo_client.delete(int(id1))
        self.__repo_client.delete(int(id2))

    def __test_delete_client(self):
        id1 = "8"
        id2 = "5"
        name1 = "Ana"
        name2 = "Alex"
        cnp1 = "609387323"
        cnp2 = "509735722"

        Client1 = Client(int(id1), name1, cnp1)
        Client2 = Client(int(id2), name2, cnp2)

        self.__repo_client.add(Client1)
        self.__repo_client.add(Client2)
        self.__repo_client.delete(int(id2))
        assert len(self.__repo_client.get_all()) == 1

        self.__repo_client.delete(int(id1))
        assert len(self.__repo_client.get_all()) == 0

    def __test_least_rented(self):
        id1 = "8"
        id2 = "5"
        name1 = "Ana"
        name2 = "Alex"
        cnp1 = "609387323"
        cnp2 = "509735722"

        Client1 = Client(int(id1), name1, cnp1)
        Client2 = Client(int(id2), name2, cnp2)

        self.__repo_client.add(Client1)
        self.__repo_client.add(Client2)
        self.__repo_client.delete(int(id2))

        id1 = "1"
        id2 = "2"
        id3 = "3"
        id4 = "4"
        name1 = "Superman"
        name2 = "Wonderwoman"
        name3 = "Pinocchio"
        name4 = "Convergent"
        description1 = "good"
        description2 = "good"
        description3 = "good"
        description4 = "good"
        type1 = "SF"
        type2 = "SF"
        type3 = "Children"
        type4 = "Comedy"

        Film1 = Film(int(id1), name1, description1, type1)
        Film2 = Film(int(id2), name2, description2, type2)
        Film3 = Film(int(id3), name3, description3, type3)
        Film4 = Film(int(id4), name4, description4, type4)

        self.__repo_film.add(Film1)
        self.__repo_film.add(Film2)
        self.__repo_film.add(Film3)
        self.__repo_film.add(Film4)

        rent1 = Rent(int(id1), int(id1))
        rent2 = Rent(int(id2), int(id1))
        rent3 = Rent(int(id2), int(id2))
        rent4 = Rent(int(id1), int(id2))
        rent5 = Rent(int(id1), int(id4))

        self.__repo_rent.add(rent1)
        self.__repo_rent.add(rent2)
        self.__repo_rent.add(rent3)
        self.__repo_rent.add(rent4)
        self.__repo_rent.add(rent5)
        #assert len(self.__service_rent.last_three()) == 3

    def run_all_tests(self):
        self.__test_crete_client()
        self.__test_create_film()
        self.__test_add_film()
        self.__test_add_client()
        self.__test_delete_film()
        self.__test_delete_client()
        self.__test_least_rented()

