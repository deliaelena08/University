from datetime import datetime

from domain.domain import *

class RepoinMemo():
    def __init__(self):
        self.__list=[]
    def get_all(self):
        return self.__list

    def add_subject(self,subject):
        self.__list.append(subject)
        return self.get_all()

class RepoFile(RepoinMemo):
    def __init__(self,filename):
        self.__filename=filename
        RepoinMemo.__init__(self)
        self.load_in_memo()

    def load_in_memo(self):
        with open(self.__filename,mode="r",encoding='utf-8')as exam_file:
            lines=exam_file.readlines()
            lines=[line.strip() for line in lines if line.strip()!=""]
            for line in lines:
                date,hour,subject,type=line.split(',')
                date=datetime.strptime(date,"%d:%m")
                hour=datetime.strptime(hour,"%H:%M")
                RepoinMemo.add_subject(self,Exam(date,hour,subject,type))

    def write_to_file(self):
        list=RepoinMemo.get_all(self)
        list=[l.get_date().strftime("%d:%m")+','+ l.get_hour().strftime("%H:%M")+','+str(l.get_subject())+','+str(l.get_type()) for l in list]
        with open(self.__filename,mode="w",encoding='utf-8')as exam_file:
            text_to_write='\n'.join(list)
            exam_file.write(text_to_write)

    def add(self,subject):
        RepoinMemo.add_subject(self,subject)
        self.write_to_file()
        return subject

