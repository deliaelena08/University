from service.Service import *

class Console():
    def __init__(self,service:Service):
        self.__service=service

    def print_menu(self):
        print("0.Exit")
        print("1.Adaugare")
        print("2.Creare melodii")
        print("3.Exporta melodii")
        print("4.Afisare melodii")
        print("5.Modify melody")

    def run(self):
        while True:
            self.print_menu()
            inp=input("Introduceti o optiune:")
            choice=int(inp)
            match choice:
                case 0:
                    exit()
                case 1:
                    self.add_ui()
                case 2:
                    self.add_melodies()
                case 3:
                    self.export_melodies()
                case 4:
                    self.print_all()
                case 5:
                    self.modfify()

    def add_ui(self):
        inp=input("Introduceti urmatoarele date separate printr-o virgula:titlu,artist,gen,data: \n")
        title,artist,type,date=inp.split(',')
        self.__service.add(title,artist,type,date)
        print( "Adaugare cu succes")

    def add_melodies(self):
        inp=input("Introduceti un numar de melodii de generat: ")
        titles=input("Introduceti tiluri: \n")
        artists=input("Introducei artisti: \n")
        print(self.__service.generate_random(inp,artists,titles))

    def export_melodies(self):
        filename=input("Introducet un nume de fisier: ")
        print(self.__service.export_sorted_list(filename))

    def print_all(self):
        list=self.__service.get_all()
        list=[str(l.get_title())+','+str(l.get_artist())+','+str(l.get_type())+','+l.get_data().strftime("%d.%m.%Y") for l in list]
        print(list)

    def modfify(self):
        inp=input("Introduceti datele pentru melodia care se modifica: ")
        title,artist,type,date=inp.split(',')
        print(self.__service.modify_melody(title,artist,type,date))
