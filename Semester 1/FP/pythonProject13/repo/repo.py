from domain.domain import *
from datetime import datetime

class InMemoryTractor:
    def __init__(self):
        self.__list=[]

    def get_tractor(self,id):
        id=int(id)
        for tractor in self.__list:
            if int(tractor.get_id())==id:
                return tractor
        return None

    def add_tractor(self,tractor):
        id=tractor.get_id()def
        id=int(id)
        if self.get_tractor(id) is not None:
            raise ValueError("Tractorul exista deja!")
        self.__list.append(tractor)
        return tractor

    def delete_tractor(self,id_tractor):
        id_tractor=int(id_tractor)
        for i in range (0,len(self.__list)):
            if id_tractor== int(self.__list[i].get_id()):
                return self.__list.pop(i)
        return "Tractorul nu exista"

    def get_all(self):
        return self.__list

class FileRepoTractor(InMemoryTractor):
    def __init__(self,filename):
        self.__filename=filename
        InMemoryTractor.__init__(self)
        self.load_in_memory()

    def load_in_memory(self):
        with open(self.__filename,mode="r",encoding='utf-8')as tractor_file:
            lines=tractor_file.readlines()
            lines=[line.strip() for line in lines if line.strip()!='']
            for line in lines:
                id,name,price,model,date=line.split(',')
                date= datetime.strptime(date, "%d:%m:%Y")
                InMemoryTractor.add_tractor(self,Tractor(int(id),name,int(price),model,date))

    def write_to_file(self):
        tractoare=InMemoryTractor.get_all(self)
        tractoare=[str(tractor.get_id())+','+str(tractor.get_name())+','+str(tractor.get_price())+','+str(tractor.get_model())+','+tractor.get_date().strftime("%d:%m:%Y") for tractor in tractoare]
        with open (self.__filename,mode="w",encoding='utf-8')as tractor_file:
            text_to_write='\n'.join(tractoare)
            tractor_file.write(text_to_write)

    def add(self,tractor):
        InMemoryTractor.add_tractor(self,tractor)
        self.write_to_file()
        return tractor
    def delete(self,tractor_id):
        InMemoryTractor.delete_tractor(self,tractor_id)
        self.write_to_file()
        return InMemoryTractor.get_all(self)
