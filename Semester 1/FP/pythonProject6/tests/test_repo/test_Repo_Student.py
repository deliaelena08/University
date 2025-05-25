from unittest import TestCase
from repository.Repo_Student import *

class TestInMemoryStudent(TestCase):
    def test_modify_student(self):
        repo_student=InMemoryStudent()
        student=Student("1","Adrian Teodor")
        repo_student.add_student(student)

        student_change=Student("1","Maria Castan")
        repo_student.modify_student(student_change)
        assert repo_student.get_student("1").get_nume()=="Maria Castan"

    def test_add_student(self):
        repo_student = InMemoryStudent()
        student = Student("1", "Adrian Teodor")
        repo_student.add_student(student)
        assert len(repo_student.get_all()) == 1
        assert repo_student.get_student("1").get_nume()=="Adrian Teodor"

    def test_delete_student(self):
        repo_student = InMemoryStudent()
        student1 = Student("1", "Adrian Teodor")
        repo_student.add_student(student1)

        student2=Student("2","Cristan Grecu")
        repo_student.add_student(student2)

        assert len(repo_student.get_all())==2
        repo_student.delete_student("2")
        assert len (repo_student.get_all())==1
