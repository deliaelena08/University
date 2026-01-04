from unittest import TestCase
from domain.domain import *


class TestStudent(TestCase):
    def test_create_student1(self):
        student = Student('1', "Maria")
        assert student.get_id() == '1'
        assert student.get_nume() == 'Maria'

    def test_create_student2(self):
        student = Student('2', "Ana")
        assert student.get_id() == '2'
        assert student.get_nume() == 'Ana'


class TestSubject(TestCase):
    def test_create_subject1(self):
        subject = Subject('1', 'Matematica', 'Berinde')
        assert subject.get_id() == "1"
        assert subject.get_name_subject() == "Matematica"
        assert subject.get_name_teacher() == "Berinde"

    def test_create_subject1(self):
        subject = Subject('2', 'Fizica', "Stroia Maria")
        assert subject.get_id() == "2"
        assert subject.get_name_subject() == "Fizica"
        assert subject.get_name_teacher() == "Stroia Maria"


class TestCatalogue(TestCase):
    def test_create_grade1(self):
        grade1=Catalogue("1","1","10")
        assert grade1.get_grade()=="10"
        assert grade1.get_id_student()=="1"
        assert grade1.get_id_subject()=="1"

    def test_create_grade2(self):
        grade2=Catalogue("2","1","7")
        assert grade2.get_grade()=="7"
        assert grade2.get_id_subject()=="1"
        assert grade2.get_id_student()=="2"
