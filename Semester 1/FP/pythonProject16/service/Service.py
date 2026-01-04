import random

from validator.validator import *
from repository.Repository import *
from datetime import datetime

class Service():
    def __init__(self,repo:InFile,validator:Validate):
        self.__repo=repo
        self.__validator=validator

    def get_all(self):
        '''
        Obtinerea listelor de obiecte
        :return: toata lista
        '''
        return self.__repo.get_all()

    def add(self,title,artist,type,data):
        '''
        adaugarea unei noi melodii in fisier
        :param title: titlu introdus de utilizator
        :param artist: artistul introdus de utilizator
        :param type: tipul introdus de utilizator
        :param data: data oferita de utilizator
        :return: lista modificata
        '''
        self.__validator.valid_melody(title,artist,type,data)
        list=self.__repo.get_all()
        for l in list:
            if l.get_artist()==artist:
                if l.get_title()==title:
                    raise ValueError("Nu se pot adauga un artist cu aceleasi melodii de mia multe ori")
        data=datetime.strptime(data,"%d.%m.%Y")
        melody=Melody(title,artist,type,data)
        self.__repo.add(melody)
        return melody

    def modify_melody(self,title,artist,type,date):
        '''
        Modificarea unei melodii
        :param title: titlul introdus de utilizator
        :param artist: artistul introdus de utilizator
        :param type: tipul introdus de utilizator
        :param date: data introdusa de utilizator
        :return: obiectul modificat
        '''
        list=self.__repo.get_all()
        for l in list:
            if l.get_title()==title:
                if l.get_artist()==artist:
                    self.__repo.modify(l,type,date)
                    return str(l.get_title())+','+str(l.get_artist())+','+str(l.get_type())+','+l.get_data().strftime("%d.%m.%Y")
        return "Nu exista melodia!"

    def generate_random(self,n,artists,titles):
        '''
        Generare aleatoriu a n  melodii
        :param n: numarul de melodii de generat
        :param artists: lista de artisti dati
        :param titles: lista de titluri date
        :return: lista modificata
        '''
        n = int(n)
        maxim_generate=len(artists)*len(titles)
        if n>=maxim_generate:
            print("Se pot genera doar maxim "+str(maxim_generate)+" melodii")
            n=maxim_generate
        for i in range(0, n):
            name = random.choice(titles)
            artist = random.choice(artists)
            types = ["Pop", "Rock", "Jazz"]
            type = random.choice(types)
            day = random.choice([random.randint(00, 30) for _ in range(1)])
            month=random.choice([random.randint(00, 12) for _ in range(2)])
            year=random.choice([random.randint(0000, 9999) for _ in range(4)])
            date=str(day)+'.'+str(month)+'.'+str(year)
            self.__validator.valid_melody(name, artist, type, date)
            date=datetime.strptime(date,"%d.%m.%Y")
            self.__repo.add(Melody(name, artist, type,date))
        return n
    def sort_two_list(self,list1,list2):
        '''
        Sortarea a doua liste dupa timp
        :param list1: lista primei jumatati
        :param list2: lista a ultimei jumatati
        :return: lista ordonata crescator dupa timp
        '''
        i=0
        j=0
        new_list=[]
        while i<len(list1) and j<len(list2):
            if list1[i].get_data()<=list2[j].get_data():
                new_list.append(list1[i])
                i+=1
            else:
                new_list.append(list2[j])
                j+=1
        while i<len(list1):
            new_list.append(list1[i])
            i += 1

        while j<len(list2):
            new_list.append(list2[j])
            j += 1

        return new_list

    def merge_sort(self,list):
        '''
        ALgoritmul de merge sort
        :param list: lista care va fi sortata
        :return: lista sortata
        '''
        if len(list)<=1:
            return list
        middle=len(list)//2
        list_1=self.merge_sort(list[:middle])
        list_2=self.merge_sort(list[middle:])
        sorted_list=self.sort_two_list(list_1,list_2)
        return sorted_list

    def export_sorted_list(self,filename):
        '''
        Exportam intr-un fisier dat lista sortata
        :param filename: numele fisierulu in care exportam
        :return: un mesaj ca a fost adaugat
        '''
        list=self.__repo.get_all()
        list_sorted=self.merge_sort(list)
        list = [str(l.get_artist()) + ',' + str(l.get_title()) + ',' + l.get_data().strftime("%d.%m.%Y")+','+ str(l.get_type())  for l in list_sorted]
        with open(filename, mode="w", encoding="utf-8") as melody_file:
            text_to_wite = '\n'.join(list)
            melody_file.write(text_to_wite)
        return "Adaugare cu succes!"