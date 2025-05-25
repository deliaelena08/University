import random

from repository.repository import *
from validator.valid import *
class Service():
    def __init__(self,repo:InFile,valid:Valid):
        self.__repo=repo
        self.__valid=valid
        self.__undo=[]

    def add_melody(self,title,artist,type,time):
        self.__valid.valid_melody(title,artist,type,time)
        melody=Melody(title,artist,type,int(time))
        self.__repo.add(melody)
        self.__undo.append({"type":"Steregere","elemente":melody})
        return self.get_all()

    def get_all(self):
        return self.__repo.get_all()

    def delete_melody(self,time):
        melodies=self.__repo.get_all()
        deleted_melodies=[]
        time=int(time)
        for m in melodies:
            if int(m.get_time())==time:
               melody= self.__repo.delete_melody(m.get_title())
               deleted_melodies.append(melody)
        if len(deleted_melodies)!=0:
            self.__undo.append({"type":"Adaugare","elemente":deleted_melodies})
        return self.get_all()

    def undo(self):
        last_one=self.__undo.pop()
        if last_one["type"]=="Stergere":
            self.__repo.delete_melody(last_one["elemente"].get_title())
        if last_one["type"]=="Adaugare":
            for m in last_one["elemente"]:
                self.__repo.add(m)
        return "Undo realizat cu succes!"

    def random_generate(self,n,artists,melodies):
        n=int(n)
        for i in range(0,n):
            name=random.choice(melodies)
            artist=random.choice(artists)
            types=["Pop","Rock","Jazz","altele"]
            type=random.choice(types)
            time=random.choice([random.randint(0, 9) for _ in range(1)])
            self.__valid.valid_melody(name,artist,type,time)
            self.__repo.add(Melody(name,artist,type,int(time)))



