from repository.Repo_subject import *
from validare.Valid_subject import *

class ServiceSubject():
    def __init__(self,repo:FileRepoSubject,validate:ValidSubject):
        self.__repo=repo
        self.__validator=validate

    def add_subject(self,id,name_subject,name_teacher):
        self.__validator.validare_subject(id,name_subject,name_teacher)
        subject=Subject(id,name_subject,name_teacher)
        self.__repo.add(subject)

    def get_subject(self, id):
        id=int (id)
        return self.__repo.get_subject(id)

    def delete_subject(self,id):
        id=int(id)
        return self.__repo.delete_subject(id)

    def update_subject(self,id,name_subject,name_teacher):
        self.__validator.validare_subject(id,name_subject,name_teacher)
        subject=Subject(id,name_subject,name_teacher)
        self.__repo.modify_subject(subject)
        return subject

    def get_all_subjects(self):
        return self.__repo.get_all()