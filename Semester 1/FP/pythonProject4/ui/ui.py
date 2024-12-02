from service.service_client import *
from service.service_film import *
from service.service_rent import *


class Console:
    def __init__(self, service_client:ServiceClient,service_film:ServiceFilm, service_rent: ServiceRent):
        self.__service_client = service_client
        self.__service_film = service_film
        self.__service_rent = service_rent

    def three_least_rented_movies_ui(self):
        list=self.__service_rent.last_three()
        for j in range(0,3):
            print(str(list[j]))

    def random_generate_ui(self):
        n = input("Introduceti numarul de clienti generati random")
        n = int(n)
        self.__service_client.random_client(n)
        self.print_lists_ui()

    def print_menu_princpal(self):
        print("1.Adaugare client")
        print("2.Adaugare film")
        print("3.Inchiriere film")
        print("4.Returnare film")
        print("5.Stergere film")
        print("6.Stergere client")
        print("7.Cautare film")
        print("8.Cautare client")
        print("9.Clienții cu filme închiriate ordonat dupa nume")
        print("10.Clienții cu filme închiriate ordonat dupa numărul de filme închiriate")
        print("11.Cele mai inchiriate filme")
        print("12.Primi 30% clienti cu cele mai multe filme (nume client și numărul de filme închiriate)")
        print("13.Modificarea datelor unui client")
        print("14.Modificarea datelor unui film")
        print("15.Afisarea listelor cu clienti si filme")
        print("16.Generare aleatoriu")
        print("17.Primele 3 cele mai putin inchiriate filme")
        print("18.Sortarea filmelor dupa id")
        print("19.Sortarea clientilor dupa id")
        print("20.Sortarea inchirerilor dupa numele clientului si a filmuluo")

    def print_list(self,list):
        print([str(element) for element in list])

    def sorted_clients_by_id_ui(self):
        self.print_list(self.__service_client.order_clients_by_id())

    def add_client_ui(self):
        client = input("Introduceti datele clientului separate printr-o virgula(id,nume,cnp):\n")
        id, name, cnp = client.split(',')
        try:
            self.__service_client.add_client(id,name,cnp)
            self.print_list(self.__service_client.get_all_clients())
        except ValueError as e:
            print(e)

    def add_film_ui(self):
        film = input("Introduceti datele filmului separate printr-o virgula(id,nume,descriere,gen):\n")
        id, title, description, type=film.split(',')
        try:
            self.__service_film.add_film(id, title,description,type)
            self.print_list(self.__service_film.get_all_films())
        except ValueError as e:
            print(e)

    def rent_ui(self):
        inp = input("Introduceti id clientului si filmului separate printr-o virgula:\n")
        client, film = inp.split(',')
        self.__service_rent.rent_a_film(client,film)
        print("Inchirierea a fost realizata cu succes")

    def return_film_ui(self):
        inp = input("Introduceti id clientului si filmului separate printr-o virgula:\n")
        client, film = inp.split(',')
        self.__service_rent.return_a_film(int(client), int(film))
        print("Inchirierea a fost realizata cu succes")

    def delete_film_ui(self):
        id = input("Introduceti id-ul filmului care doriti sa fie sters:\n")
        film = self.__service_film.delete_film(id)
        self.__service_rent.delete_all_rents_by_film(film.get_id(),0)
        self.print_list(self.__service_film.get_all_films())

    def delete_client_ui(self):
        id = input("Introduceti id-ul clientului care doriti sa fie sters:\n")
        client = self.__service_client.delete_client(id)
        self.__service_rent.delete_all_rents_by_client(client.get_id())
        self.print_list(self.__service_client.get_all_clients())
        
    def search_film_ui(self):
        id = input("Introduceti id-ul filmului pe care doriti sa-l cautati:\n")
        print(self.__service_film.get_film(id))

    def search_client_ui(self):
        id = input("Introduceti id-ul clientului pe care doriti sa-l cautati:\n")
        print(self.__service_client.get_client(id))

    def order_by_rented_ui(self):
        self.print_list(self.__service_rent.order_clients_by_rented_films())

    def most_rented_ui(self):
        self.print_list(self.__service_rent.order_films_by_most_rented())

    def order_by_name_ui(self):
        self.print_list(self.__service_rent.order_clients_by_name())

    def print_lists_ui(self):
        self.print_list(self.__service_film.get_all_films())
        self.print_list(self.__service_client.get_all_clients())

    def films_sorted_by_id_ui(self):
        self.print_list(self.__service_film.order_films_by_id())

    def print_list_1(self,list):
        for j in range(0, (len(list) + 2) // 3):
            print(str(list[j]))
    
    def clients_rented_films_ui(self):
        list=self.__service_rent.list_name_client_and_count_for_most_rented()
        if list != 0:
            self.print_list_1(list)

    def modify_client_ui(self):
        id = input("Introduceti id-ul clientului pe care vreti sa-l modificati")
        inp = input("Introduceti numele si cnp-ul pentru schimbare")
        name, cnp = inp.split(',')
        print(self.__service_client.update_client(id, name, cnp))

    def modify_film_ui(self):
        id = input("Introduceti id-ul filmului pe care vreti sa-l modificati")
        inp = input("Introduceti titlu,descrierea si tipul pentru schimbare separate printr-o virgula")
        title, description, type = inp.split(',')
        print(self.__service_film.update_film(id, title, description, type))

    def sort_by_two_features_ui(self):
        list=self.__service_rent.sort_by_two_features()
        self.print_list(list)