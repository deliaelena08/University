from service.service_student import *
from service.service_catalogue import*
from service.service_subject import *


class Console:
    def __init__(self,service_subject:ServiceSubject,service_student:ServiceStudent,service_catalogue:ServiceCatalogue):
        self.__service_student=service_student
        self.__service_subject=service_subject
        self.__service_catalogue=service_catalogue

    def print_menu(self):
        print("0.Exit")
        print("1.Adaugare student")
        print("2.Adaugare materie")
        print("3.Modificare student")
        print("4.Modificare materie")
        print("5.Stergere student")
        print("6.Stergere materie")
        print("7.Adauagare nota")
        print("8.Stergere Nota")
        print("9.Afisare lista studenti")
        print("10.Afisare lista materii")
        print("11.Afisare note")
        print("12.Ordonarea listelor de studenti dupa nume la o materie data")
        print("13.Ordonarea listelor de studenti dupa notele al unei materii date")
        print("14.Primii 20% de studneti cu mediile cele mai mari")

    def run(self):
        while True:
            self.print_menu()
            print("\n")
            option=input("Alegeti optiunea")
            option=option.strip()
            option=int(option)
            match option:
                case 0:
                    exit()
                case 1:
                    self.add_student_ui()
                case 2:
                    self.add_subject_ui()
                case 3:
                    self.modify_student_ui()
                case 4:
                    self.modify_subject_ui()
                case 5:
                    self.delete_student_ui()
                case 6:
                    self.delete_subject_ui()
                case 7:
                    self.add_grade_ui()
                case 8:
                    self.delete_grade_ui()
                case 9:
                    self.print_students_list_ui()
                case 10:
                    self.print_subjects_list_ui()
                case 11:
                    self.print_grades_list_ui()
                case 12:
                    self.order_by_name_ui()
                case 13:
                    self.order_by_grade_ui()
                case 14:
                    self.order_by_average_ui()

    def print_list(self,list):
        print([str(element) for element in list])

    def add_student_ui(self):
        inp=input("Introduceti un id si nume de student seprate de o virgula")
        print("\n")
        id,name=inp.split(',')
        try :
            self.__service_student.add_student(int(id),name)
            self.print_list(self.__service_student.get_all_students())
        except ValueError as e:
            print(e)

    def add_subject_ui(self):
        inp=input("Introduceti un id,materia si profesorul separate printr-o virgula")
        print("\n")
        id,name_subject,name_teacher=inp.split(',')
        try:
            self.__service_subject.add_subject(int(id),name_subject,name_teacher)
            self.print_list(self.__service_subject.get_all_subjects())
        except ValueError as e:
            print(e)

    def modify_student_ui(self):
        inp=input("Introduceti id-ul studentului pe care vreti sa-l schimbati si apoi numele separate printr-o virgua")
        id,name=inp.split(',')
        print(self.__service_student.update_student(int(id),name))

    def modify_subject_ui(self):
        inp=input("Introduceti id-ul materiei pe care vreti sa o schimbati urmat de un numele acesteie si numele profesorului separate prin virgula")
        id,name_subject,name_teacher=inp.split(',')
        print(self.__service_subject.update_subject(int(id),name_subject,name_teacher))

    def delete_student_ui(self):
        inp=input("Introduceti id-ul studentului care doriti sa fie sters")
        id=int(inp)
        self.print_list(self.__service_student.delete_student(id))

    def delete_subject_ui(self):
        inp=input("Introduceti id-ul materiei care doriti sa fie stearsa")
        id=int(inp)
        self.print_list(self.__service_subject.delete_subject(id))

    def add_grade_ui(self):
        inp=input("Introduceti id-ul unui student,id-ul unei materii si nota obtinuta separata prin virgula")
        id_student,id_subject,grade=inp.split(',')
        try:
            self.__service_catalogue.add_grade(id_student, id_subject, grade)
            self.print_list(self.__service_catalogue.get_all_catalogue())
        except ValueError as e:
            print(e)

    def delete_grade_ui(self):
        inp=input("Introduceti detaliile notei care doriti sa fie stearsa sepaarate printr-o virgula")
        id_student,id_subject,grade=inp.split(',')
        self.print_list(self.__service_catalogue.delete_grade(id_student, id_subject, grade))

    def print_students_list_ui(self):
        self.print_list(self.__service_student.get_all_students())

    def print_subjects_list_ui(self):
        self.print_list(self.__service_subject.get_all_subjects())

    def print_grades_list_ui(self):
        self.print_list(self.__service_catalogue.get_all_catalogue())

    def order_by_name_ui(self):
        inp=input("Introduceti id-ul unei materii")
        id=int(inp)
        self.print_list(self.__service_catalogue.students_order_by_name(id))

    def order_by_grade_ui(self):
        inp=input("Introduceti id-ul unei materii")
        id=int(inp)
        self.print_list(self.__service_catalogue.student_order_by_grade(id))

    def order_by_average_ui(self):
        list=self.__service_catalogue.order_by_average()
        output=len(list)*2
        output=output//10
        if output>0:
            for i in range(0,output):
                print(str(list[i]))
        else:
            print(str(list[1]))

