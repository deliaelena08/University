from Domain.domain import *

class InMemory():
    def __init__(self):
        self.__list=[]

    def add_inmemo(self,melody):
        self.__list.append(melody)
        return melody

    def get_all(self):
        return self.__list

    def get_one_melody(self,title):
        melodies=self.get_all()
        for m in melodies:
            if m.get_title()==title:
                return m
        return None

    def delete_melody(self,title):
        for i in range(0,len(self.__list)):
            if self.__list[i].get_title()==title:
                return self.__list.pop(i)
        return None

class InFile(InMemory):
    def __init__(self,filename):
        InMemory.__init__(self)
        self.__filename=filename
        self.load_in_memo()

    def load_in_memo(self):
        with open (self.__filename,mode="r",encoding='utf-8') as melody_file:
            lines=melody_file.readlines()
            lines=[line.strip() for line in lines if line.strip()!=""]
            for line in lines:
                title,artist,type,time=line.split(",")
                InMemory.add_inmemo(self,Melody(title,artist,type,int(time)))

    def write_to_file(self):
        list=InMemory.get_all(self)
        list=[str(melody.get_title())+','+str(melody.get_artist())+','+str(melody.get_type())+','+str(melody.get_time()) for melody in list]
        with open(self.__filename,mode="w",encoding="utf-8")as melody_file:
            text_to_wite='\n'.join(list)
            melody_file.write(text_to_wite)

    def add(self,melody):
        InMemory.add_inmemo(self,melody)
        self.write_to_file()
        return melody

    def delete(self,name):
        melody=InMemory.delete_melody(self,name)
        self.write_to_file()
        return melody
