from service.Service_Mvp import *
from service.service_player import *

class Console:
    def __init__(self,service_player:ServicePlayer,service_mvp:ServiceMvp):
        self.__service_player=service_player
        self.__service_mvp=service_mvp

    def print_menu(self):
        print("1.Adaugare jucator")
        print("2.Adaugare Evaluare")
        print("3.Afisare lista jucatori")
        print("4.Afisari lista evaluari")
        print("5.Jucatorii pentru care pozitia contine stringul dat de utilizator")
        print("6.Tiparirea evaluarii")

    def run(self):
        while True:
            self.print_menu()
            print("\n")
            option = input("Alegeti optiunea")
            option = option.strip()
            option = int(option)
            match option:
                case 1:
                    self.add_player()
                case 2:
                    self.add_mvd()
                case 3:
                    self.print_list_players()
                case 4:
                    self.print_all_mvp()
                case 5:
                    self.search_string()
                case 6:
                    self.print_evaluation()



    def add_player(self):
        inp=input("Introduceti un id,nume,pozitie si puncte seprate printr-o virgula")
        id,name,position,points=inp.split(',')
        self.__service_player.add_player(int(id),name,position,points)
        print("\n Adaugarea cu succes!")

    def add_mvd(self):
        inp=input("Introduceti un id al unui jucator si un numar de meciuri separate printr-o virgula")
        id,number=inp.split(',')
        self.__service_mvp.add_in_mvp(id,number)
        print("\n Adaugare cu succes!")

    def print_list_players(self):
        players=self.__service_player.get_all_players()
        print(str(player.__str__() for player in players))

    def print_evaluation(self):
        id=input("Introduceti id")
        id=int(id)
        print(self.__service_mvp.evaluation(id))

    def print_all_mvp(self):
        list=self.__service_mvp.get_all()
        print(str(mvp.__str__() for mvp in list))

    def search_string(self):
        str=input("Introduceti un string")
        players=self.__service_player.find_string(str)
        print(str(player.__str__()) for player in players)