from service.service import *

class Console():
    def __init__(self,service:Service):
        self.__service=service

    def print_menu(self):
        print("0.Exit")
        print("1.Adaugare melodie")
        print("2.Steregere melodie dupa durata")
        print("3.Generare aleatoriu")
        print("4.Undo")
        print("5.Afisare lista")

    def run(self):
        while True:
            self.print_menu()
            inp=input("Alegeti o optiune: ")
            inp=int(inp)
            match inp:
                case 0:
                    exit()
                case 1:
                    self.add_ui()
                case 2:
                    self.delete_ui()
                case 3:
                    self.generate()
                case 4:
                    self.undo_ui()
                case 5:
                    self.print_ui()


    def print_ui(self):
        self.print_all(self.__service.get_all())

    def add_ui(self):
        inp=input("Introduceti un nume de melodie, artist,tip si durata acesteia separate printr-o vrigula: ")
        print('\n')
        title,artist,type,time=inp.split(",")
        self.print_all(self.__service.add_melody(title,artist,type,time))
        return "Adaugare realizata cu succes!"

    def print_all(self,list):
        text_to_write=[str(l.get_title())+','+str(l.get_artist())+','+str(l.get_type())+','+str(l.get_time()) for l in list]
        text_to_write='\n'.join(text_to_write)
        print(text_to_write)

    def delete_ui(self):
        inp=input("Introducet o durata dupa care doriti sa fie stere melodiile: ")
        self.print_all(self.__service.delete_melody(inp))

    def generate(self):
        n=input("Introduceti un numar de melodii de genarat: ")
        titles=input("Introduceti o lista cu titluri de melodii: ")
        artists=input("Introduceti o lista de artisti: ")
        self.__service.random_generate(n,artists,titles)

    def undo_ui(self):
        print(self.__service.undo())
        self.print_all(self.__service.get_all())
