from service.service import *

class Console():
    def __init__(self,service:Service):
        self.__service=service

    def print_menu(self):
        print("0.Exit")
        print("1.Adaugare examen")
        print("2.Afisare examene dupa data")
        print("3.Export fisiere")
        print("4.Afisarea lista")

    def run(self):
        while True:
            self.print_menu()
            inp=input("Introduceti o optiune")
            inp=int(inp)
            match inp:
                case 0:
                    exit()
                case 1:
                    self.add_ui()
                case 2:
                    self.print_by_date()
                case 3:
                    self.export_list()
                case 4:
                    self.list(self.__service.get_all())

    def list(self,list):
        list = [l.get_date().strftime("%d:%m") + ',' + l.get_hour().strftime("%H:%M") + ',' + str(l.get_subject()) + ',' + str(l.get_type()) for l in list]
        print(list)

    def add_ui(self):
        inp=input("Introduceti urmatoarele date separate printr-o virgula: data ora subiect tip")
        print('\n')
        date,hour,subject,type=inp.split(',')
        self.list(self.__service.add(date,hour,subject,type))

    def print_by_date(self):
        inp=input("Introduceti o data valida: ")
        inp = datetime.strptime(inp, "%d:%m")
        self.list(self.__service.date_seted_list(inp))

    def export_list(self):
        filename=input("Introduceti un nume de fisier: ")
        string=input("Introduceti un string: ")
        self.__service.export_list(filename,string)
        print("Adaugare cu succes!")

