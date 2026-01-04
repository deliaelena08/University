from domain.domain import Catalogue

class InMemoryCatalogue:
    def __init__(self):
        self.__list=[]

    def add_catalogue(self,input):
        self.__list.append(input)
        return input

    def delete_catalogue(self,inp):
        for i in range (0,len(self.__list)):
            if int(self.__list[i].get_id_subject())==int(inp.get_id_subject()) and int(self.__list[i].get_id_student())==int(inp.get_id_student()) and int(self.__list[i].get_grade())==int(inp.get_grade()):
                self.__list.pop(i)
                return self.__list
        return "Nu exista aceasta nota"

    def get_all(self):
        return self.__list


class FileRepoCatalogue(InMemoryCatalogue):
    def __init__(self,filename):
        InMemoryCatalogue.__init__(self)
        self.__filename=filename
        self.load_in_memory()

    def load_in_memory(self):
        with open(self.__filename, mode='r', encoding='utf-8') as catalogue_file:
            lines=catalogue_file.readlines()
            lines=[line.strip() for line in lines if line.strip() != '']
            for line in lines:
                id_student,id_subject,grade=line.split(',')
                InMemoryCatalogue.add_catalogue(self,Catalogue(int(id_student),int(id_subject),grade))

    def write_to_file(self):
        catalogue=InMemoryCatalogue.get_all(self)
        catalogue=[str(grade.get_id_student())+','+str(grade.get_id_subject())+','+str(grade.get_grade()) for grade in catalogue]
        with open(self.__filename, mode='w', encoding='utf-8') as catalogue_file:
            text_to_write='\n'.join(catalogue)
            catalogue_file.write(text_to_write)

    def add_grade(self,inp):
        InMemoryCatalogue.add_catalogue(self,inp)
        self.write_to_file()
        return inp

    def delete_grade(self,inp):
        InMemoryCatalogue.delete_catalogue(self,inp)
        self.write_to_file()
        return InMemoryCatalogue.get_all(self)