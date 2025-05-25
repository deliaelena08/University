from domain.domain import *
from datetime import datetime
class InMemo():
    def __init__(self):
        self.__list=[]

    def add_memo(self,object):
        '''
        Adaugam un obiect in lista
        :param object: o melodia de clasa obiect
        :return: obiectul adaugat
        '''
        self.__list.append(object)
        return object

    def get_all(self):
        '''
        Returneaza toate  listele
        :return: lista de obiecte
        '''
        return self.__list

    def modify(self,m,date,type):
        m.set_type(type)
        date=datetime.strptime(date,"%d.%m.%Y")
        m.set_data(date)
        return m

class InFile(InMemo):
    def __init__(self,filename):
        self.__filename=filename
        InMemo.__init__(self)
        self.load_in_memo()

    def load_in_memo(self):
        with open(self.__filename,mode="r",encoding="utf-8")as melody_file:
            lines=melody_file.readlines()
            lines=[line.strip() for line in lines if line.strip()!=""]
            for line in lines:
                title,artist,type,data=line.split(",")
                data=datetime.strptime(data,"%d.%m.%Y")
                melody=Melody(title,artist,type,data)
                InMemo.add_memo(self,melody)

    def write_to_file(self):
        list=InMemo.get_all(self)
        list=[str(l.get_title())+','+str(l.get_artist())+','+str(l.get_type())+','+l.get_data().strftime("%d.%m.%Y") for l in list]
        with open(self.__filename,mode="w",encoding="utf-8")as melody_file:
            text_to_wite='\n'.join(list)
            melody_file.write(text_to_wite)

    def add(self,object):
        InMemo.add_memo(self,object)
        self.write_to_file()
        return object

    def modify(self,m,type,date):
        InMemo.modify(self,m,date,type)
        self.write_to_file()
        return m