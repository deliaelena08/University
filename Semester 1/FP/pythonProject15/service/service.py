from repository.repository import *
from validator.valid import *
from datetime import datetime,timedelta

class Service():
    def __init__(self,repo:RepoFile,validator:Valid):
        self.__repo=repo
        self.__valid=validator
        self.__date=0

    def add(self,date,hour,subject,type):
        self.__valid.validare(date,hour,subject,type)
        date=datetime.strptime(date,"%d:%m")
        hour=datetime.strptime(hour,"%H:%M")
        object=Exam(date,hour,subject,type)
        self.__repo.add(object)
        if self.__date!=0:
            return self.date_seted_list()
        return self.get_by_today()

    def get_all(self):
        return self.__repo.get_all()

    def get_by_today(self):
        date=datetime.today().strftime("%d:%m")
        exams=self.get_all()
        new_list=[]
        for e in exams:
            date2=e.get_date().strftime("%d:%m")
            if date2==date:
                new_list.append(e)

        for i in range (0,len(new_list)-1):
            ind=i
            for j in range(i+1,len(new_list)):
                if new_list[ind].get_hour()>new_list[j].get_hour():
                    ind=j
            if ind>i:
                new_list[i],new_list[ind]=new_list[ind],new_list[i]
        return new_list

    def order_by_day(self):
        list=self.__repo.get_all()
        for i in range(0,len(list)-1):
            ind=i-1
            a=list[i]
            while ind>=0 and a.get_date()<list[ind].get_date():
                list[ind+1]=list[ind]
                ind=ind-1
            list[ind+1]=a
        return list

    def date_seted_list(self,date):
        date_begin=date
        self.__date=date
        list=self.order_by_day()
        date_end=date+timedelta(days=3)
        new_list=[]
        for e in list:
            if e.get_date()>=date_begin and e.get_date()<=date_end:
                new_list.append(e)
        return new_list

    def export_list(self,filename,string):
        exams=self.get_all()
        new_list=[]
        for e in exams:
            if string in e.get_subject():
                new_list.append(e)
        with open(filename,mode="w",encoding='utf-8')as file_to_write:
            new_list = [l.get_date().strftime("%d:%m") + ',' + l.get_hour().strftime("%H:%M") + ',' + str( l.get_subject()) + ',' + str(l.get_type()) for l in new_list]
            text_to_write='\n'.join(new_list)
            file_to_write.write(text_to_write)




