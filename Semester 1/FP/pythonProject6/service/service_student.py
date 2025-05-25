from repository.Repo_Student import *
from validare.Valid_student import *

class ServiceStudent:
    def __init__(self,repo:FileRepoStudent,validator:ValidStudent):
        self.__repo=repo
        self.__validator=validator

    def add_student(self,id,name):
        self.__validator.valid_students(id,name)
        student=Student(id,name)
        self.__repo.add(student)

    def get_student(self,id):
        id=int(id)
        return self.__repo.get_student(id)

    def delete_student(self,id):
        id=int(id)
        return self.__repo.delete_student(id)

    def get_all_students(self):
        return self.__repo.get_all()

    def update_student(self,id,name):
        student=Student(id,name)
        self.__repo.modify_student(student)
        return student
