
class Student:
    def __init__(self,id,name):
        self.__name=name
        self.__id=id

    def get_id(self):
        return self.__id

    def get_nume(self):
        return self.__name

    def set_nume(self,name):
        self.__name=name

    def __str__(self):
        return "Id: "+ str(self.__id)+ " | Name:"+str(self.__name)

class Subject:
    def __init__(self,id,name_subject,name_teacher):
        self.__id=id
        self.__name_subject=name_subject
        self.__name_teacher=name_teacher

    def get_id(self):
        return self.__id

    def get_name_subject(self):
        return self.__name_subject

    def get_name_teacher(self):
        return self.__name_teacher

    def set_name_subject(self,name_subject):
        self.__name_subject=name_subject

    def set_name_teacher(self,name_teacher):
        self.__name_teacher=name_teacher

    def __str__(self):
        return "Id: " + str(self.__id)+ " | Name subject: "+ str(self.__name_subject)+" | Name teacher: "+self.__name_teacher

class Catalogue:
    def __init__(self,id_student,id_subject,grade):
        self.__id_student=id_student
        self.__id_subject=id_subject
        self.__grades=grade

    def get_id_student(self):
        return self.__id_student

    def get_id_subject(self):
        return self.__id_subject

    def get_grade(self):
        return self.__grades

    def __str__(self):
        return "Id_student: "+ str(self.__id_student)+ " | Id_subject: "+ str(self.__id_subject)+ " | Grades: "+str(self.__grades)


