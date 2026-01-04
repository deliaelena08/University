from domain.domain import Subject


class InMemorySubject:
    def __init__(self):
        self.__list=[]

    def get_subject(self,id):
        for s in self.__list:
            if int(s.get_id())==int(id):
                return s
        return None

    def modify_subject(self,subject):
        for s in self.__list:
            if int(s.get_id())==int(subject.get_id()):
                s.set_name_subject(subject.get_name_subject())
                s.set_name_teacher(subject.get_name_teacher())
                return s
        return "Subiectul nu exista"

    def add_subject(self,subject):
        if self.get_subject(subject.get_id()) is not None:
            raise ValueError("Id-ul exista deja")
        self.__list.append(subject)
        return subject

    def delete_subject(self,id):
        for i in range(0,len(self.__list)):
            if int(id)==int(self.__list[i].get_id()):
                self.__list.pop(i)
                return self.__list
        return "Subiectul nu exista"

    def get_all(self):
        return self.__list

class FileRepoSubject(InMemorySubject):
    def __init__(self,filename):
        InMemorySubject.__init__(self)
        self.__filename=filename
        self.load_in_memory()

    def load_in_memory(self):
        with open(self.__filename, mode='r', encoding='utf-8') as subjects_file:
            lines=subjects_file.readlines()
            lines=[line.strip() for line in lines if line.strip() != '']
            for line in lines:
                id,name_subject,name_teacher=line.split(',')
                InMemorySubject.add_subject(self,Subject(int(id),name_subject,name_teacher))

    def write_to_file(self):
        subjects=InMemorySubject.get_all(self)
        subjects=[str(subject.get_id()) + ',' + str(subject.get_name_subject()) + ',' + str(subject.get_name_teacher()) for subject in subjects]
        with open(self.__filename, mode='w', encoding='utf-8') as subjects_file:
            write_in_file='\n'.join(subjects)
            subjects_file.write(write_in_file)

    def add(self,subject):
        InMemorySubject.add_subject(self,subject)
        self.write_to_file()
        return subject

    def update_subject(self,subject):
        InMemorySubject.modify_subject(subject)
        self.write_to_file()
        return subject

    def delete_subject(self,id):
        InMemorySubject.delete_subject(self,id)
        self.write_to_file()
        return InMemorySubject.get_all(self)