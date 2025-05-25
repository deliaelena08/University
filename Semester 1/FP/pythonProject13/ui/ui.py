'''facem get all la toate functiile'''
from service.service import *

class Console():
    def __init__(self,service:ServiceTractor):
        self.__service=service

    def print_menu(self):
        print("0.Exit")
        print("1.Adaugare tractor")
        print("2.Stergere tractor")
        print("3.Modificare filtrare")
        print("4.Undo")
        print("5.Afisare lista")
        print("6.Lista de tractoare dupa verificarea reviziei")

    def run(self):
        while True:
            self.print_menu()
            print("\n")
            option = input("Alegeti optiunea")
            option = option.strip()
            option = int(option)
            match option:
                case 0:
                    exit()
                case 1:
                    self.add_ui()
                case 2:
                    self.delete_ui()
                case 3:
                    self.modify_filtre()
                case 4:
                    self.undo_ui()
                case 5:
                    self.print_list()
                case 6:
                    self.verify_revision()

    def add_ui(self):
        inp=input("Introduceti urmatoarele date referitoare la tractor separate printr-o virgul:(id,nume,pret,model,data: ")
        id,name,price,model,data=inp.split(",")
        self.__service.add_tractor(id,name,price,model,data)
        self.print_list()
        print("Adaugare realizata cu succes!")


    def delete_ui(self):
        inp=input("Introduceti o cifra:")
        number=self.__service.delete_tractor_by_sum(inp)
        print("Numarul de tractoare sterse este "+ str(number) )
        self.print_list()


    def modify_filtre(self):
        inp=input("Introduceti un sir de caractere si un pret separate prin virgula: ")
        list,price=inp.split(",")
        self.__service.set_filtre(list,price)
        self.print_list()

    def undo_ui(self):
        self.__service.undo()
        print("Undo realizat cu succes!")

    def print_list(self):
        tractoare=self.__service.get_all_tractoare()
        tractoare=[str(tractor.get_id())+','+str(tractor.get_name())+','+str(tractor.get_price())+','+str(tractor.get_model())+','+tractor.get_date().strftime("%d:%m:%Y") for tractor in tractoare]
        print(tractoare)

    def verify_revision(self):
        tractoare=self.__service.revision()
        tractoare = [str(tractor.get_id()) + ',' + str(tractor.get_name()) + ',' + str(tractor.get_price()) + ',' + str( tractor.get_model()) + ',' + tractor.get_date().strftime("%d:%m:%Y") for tractor in tractoare]
        print(tractoare)








