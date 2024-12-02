from ui.ui import *
from validare.test import *

validclients = ValidClients()
validfilms=ValidFilms()
repo_client = FileRepoClient("client.txt")
repo_film=FileRepoFilm("films.txt")
repo_rent=FileRepoRent("rents.txt")
service_client = ServiceClient(repo_client, validclients)
service_film = ServiceFilm(repo_film, validfilms)
service_rent = ServiceRent(repo_film, repo_client, repo_rent)
console=Console(service_client, service_film, service_rent)
test=tests(InMemoryRepoFilm(),Console(service_client,service_film,service_rent),InMemoryRepoClient(),service_film,service_client,InMemoryRepoRent(),service_rent)
test.run_all_tests()


while True:
    console.print_menu_princpal()
    option = input("Introduceti optiunea:")
    option = option.strip()
    option = int(option)
    match option:
        case 0:
            exit()
        case 1:
            console.add_client_ui()
        case 2:
            console.add_film_ui()
        case 3:
            console.rent_ui()
        case 4:
            console.return_film_ui()
        case 5:
            console.delete_film_ui()
        case 6:
            console.delete_client_ui()
        case 7:
            console.search_film_ui()
        case 8:
            console.search_client_ui()
        case 9:
            console.order_by_name_ui()
        case 10:
            console.order_by_rented_ui()
        case 11:
            console.most_rented_ui()
        case 12:
            console.clients_rented_films_ui()
        case 13:
            console.modify_client_ui()
        case 14:
            console.modify_film_ui()
        case 15:
            console.print_lists_ui()
        case 16:
            console.random_generate_ui()
        case 17:
            console.three_least_rented_movies_ui()
        case 18:
            console.films_sorted_by_id_ui()
        case 19:
            console.sorted_clients_by_id_ui()
        case 20:
            console.sort_by_two_features_ui()