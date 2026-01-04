from repository.Repo_grades import *
from validare.Valid_grade import *
from repository.Repo_Student import *
from repository.Repo_subject import *

class ServiceCatalogue:
    def __init__(self, repo_catalogue:FileRepoCatalogue,repo_student:FileRepoStudent,repo_subject:FileRepoSubject, validator:ValidGrade):
        self.__repo=repo_catalogue
        self.__valid=validator
        self.__repo_student=repo_student
        self.__repo_subject=repo_subject

    def add_grade(self,id_student,id_subject,grade):

        self.__valid.valid_grade(id_student,id_subject,grade)
        grade=Catalogue(id_student,id_subject,grade)
        self.__repo.add_grade(grade)

    def get_grades_by_id_student(self,id_student):

        grades=self.__repo.get_all()
        subjects_for_student=[]
        id=int(id_student)

        for g in grades:
            if int(g.get_id_student())==id:
                subjects_for_student.append(g)

        if len(subjects_for_student)>0:
            return subjects_for_student
        return None

    def get_grade_by_id_subject(self,id_subject):

        id=int(id_subject)
        grades=self.__repo.get_all()
        students_for_subject=[]

        for g in grades:
            if int(g.get_id_subject())==id:
                students_for_subject.append(g)

        if len(students_for_subject)>0:
            return students_for_subject
        return None

    def delete_grade(self,id_student,id_subject,grade):
        grade=Catalogue(id_student,id_subject,grade)
        self.__repo.delete_grade(grade)
        return self.__repo.get_all()

    def students_order_by_name(self,id_subject):

        list=self.get_grade_by_id_subject(id_subject)
        student_names_list=[]
        if list is None:
            return "Nu exista aceasta materie"

        for g in list:
            id_name=g.get_id_student()
            student=self.__repo_student.get_student(id_name)
            student_names_list.append(student.get_nume())

        for i in range(0,len(student_names_list)-1):
            for j in range(i,len(student_names_list)):
                if student_names_list[i]>student_names_list[j]:
                    name=student_names_list[j]
                    student_names_list[j]=student_names_list[i]
                    student_names_list[i]=name

        if len(student_names_list)>0:
            return student_names_list
        return None

    def student_order_by_grade(self,id_subject):

        list=self.get_grade_by_id_subject(id_subject)
        if list is None:
            return "Nu exista aceasta materie"

        for i in range (0,len(list)-1):
            for j in range (i,len(list)):
                if int(list[i].get_grade())>int(list[j].get_grade()):
                    grade=list[j]
                    list[j]=list[i]
                    list[i]=grade
        print(list)
        students_list=[]
        for l in list:
            students_list.append(self.__repo_student.get_student(l.get_id_student()))

        if len(students_list)>0:
            return students_list
        return None

    def average_student(self,id_student):

        grades=self.__repo.get_all()
        id_student=int(id_student)
        s=0
        nr=0

        for g in grades:
            if int(g.get_id_student())==id_student:
                s=s+int(g.get_grade())
                nr=nr+1
        if nr!=0:
            return s/nr
        return None

    def order_by_average(self):

        students=self.__repo_student.get_all()
        average_list=[]
        for s in students:
            average=self.average_student(s.get_id())
            if average is not None:
                name=s.get_nume()
                average_list.append({'Nume student' : name,'Media' : average})
        print(average_list)
        for i in range(0,len(average_list)-1):
            for j in range(i,len(average_list)):
                if average_list[i]['Media'] < average_list[j]['Media']:
                    first=average_list[i]
                    average_list[i]=average_list[j]
                    average_list[j]=first

        return average_list

    def get_all_catalogue(self):
        return self.__repo.get_all()