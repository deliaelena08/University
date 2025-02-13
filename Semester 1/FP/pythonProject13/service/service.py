from pprint import pprint

from repo.repo import *
from datetime import datetime
import datetime
class ServiceTractor():
    def __init__(self,repo:FileRepoTractor):
        self.__repo=repo
        self.__undo=[]
        self.__nume=""
        self.__pret=-1

    def add_tractor(self,id,name,price,model,data):
        date = datetime.strptime(data, "%d:%m:%Y")
        tractor=Tractor(id,name,price,model,date)
        self.__repo.add(tractor)
        self.__undo.append({"type": "delete", "tractor": tractor})

    def set_filtre(self,name,price):
        self.__nume=name
        price=int(price)
        self.__pret=price

    def get_tractor(self,id):
        id=int(id)
        return self.__repo.get_tractor(id)

    def delete_tractor(self,id):
        id=int(id)
        tractor=self.get_tractor(id)
        self.__repo.delete(id)
        self.__undo.append({"type": "add", "tractor": tractor})

    def get_all_tractoare(self):
        tractoare=self.__repo.get_all()
        if self.__pret>-1:
            tractoare=[x for x in tractoare if x.get_price()>=self.__pret]
        if self.__nume!="":
            tractoare=[x for x in tractoare if self.__nume in x.get_name()]
        return tractoare

    def undo(self):
        el=self.__undo.pop()
        if el["type"]=="add":
            print(el['tractor'])
            self.__repo.add(el['tractor'])
        else:
            self.__repo.delete(el["tractor"].get_id())

    def delete_tractor_by_sum(self,c):
        tractoare=self.__repo.get_all()
        nr=0
        c=int(c)
        for t in tractoare:
            ok=0
            sum=t.get_price()
            sum=int(sum)
            while sum>0:
                if sum%10==c:
                    ok=1
                sum=sum//10
            if ok==1:
                self.delete_tractor(t.get_id())
                nr+=1
        return nr

    def revision(self):
        today=datetime.today()
        tractoare=self.__repo.get_all()
        for t in tractoare:
            if t.get_date()<today:
                t.set_name("*"+t.get_name())
        return tractoare