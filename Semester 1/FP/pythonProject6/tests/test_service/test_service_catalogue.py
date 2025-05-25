import shutil
from unittest import TestCase
from service.service_catalogue import *

class TestServiceCatalogue(TestCase):
    def clear_file_test(self):
        with open("test_catalogue.txt","w") as f:
            pass

    def copy_initial_test(self):
        shutil.copyfile("initial_catalogue_test.txt","test_catalogue.txt")

    def test_add(self):
        self.copy_initial_test()
        valid=ValidGrade()
        repo_catalogue=FileRepoCatalogue("test_catalogue.txt")
        repo_student=FileRepoStudent("test_students.txt")
        repo_subject=FileRepoSubject("test_subjects.txt")
        service_catalogue=ServiceCatalogue(repo_catalogue,repo_student,repo_subject,valid)

        service_catalogue.add_grade("1","1","5")
        assert len(service_catalogue.get_all_catalogue())==8
        try:
            service_catalogue.add_grade("1","1","10")
            assert False
        except:
            assert True

    def test_delete(self):
        self.copy_initial_test()
        valid = ValidGrade()
        repo_catalogue = FileRepoCatalogue("test_catalogue.txt")
        repo_student = FileRepoStudent("test_students.txt")
        repo_subject = FileRepoSubject("test_subjects.txt")
        service_catalogue = ServiceCatalogue(repo_catalogue, repo_student, repo_subject, valid)

        service_catalogue.delete_grade("1","1","5")
        assert len(service_catalogue.get_all_catalogue())==7
        try:
            service_catalogue.delete_grade("1","1","5")
            assert False
        except:
            assert True

    def test_get_grades_by_student(self):
        self.copy_initial_test()
        valid = ValidGrade()
        repo_catalogue = FileRepoCatalogue("test_catalogue.txt")
        repo_student = FileRepoStudent("test_students.txt")
        repo_subject = FileRepoSubject("test_subjects.txt")
        service_catalogue = ServiceCatalogue(repo_catalogue, repo_student, repo_subject, valid)

        assert len(service_catalogue.get_grades_by_id_student(1))==3
        service_catalogue.add_grade("1","3","7")
        assert len(service_catalogue.get_grades_by_id_student(1))==4

    def test_get_grade_by_id_subject(self):
        self.copy_initial_test()
        valid = ValidGrade()
        repo_catalogue = FileRepoCatalogue("test_catalogue.txt")
        repo_student = FileRepoStudent("test_students.txt")
        repo_subject = FileRepoSubject("test_subjects.txt")
        service_catalogue = ServiceCatalogue(repo_catalogue, repo_student, repo_subject, valid)

        assert len(service_catalogue.get_grade_by_id_subject("2"))==2
        service_catalogue.add_grade("3","2","9")
        assert len(service_catalogue.get_grade_by_id_subject("2"))==3

    def test_order_by_name(self):
        self.copy_initial_test()
        valid = ValidGrade()
        repo_catalogue = FileRepoCatalogue("test_catalogue.txt")
        repo_student = FileRepoStudent("test_students.txt")
        repo_subject = FileRepoSubject("test_subjects.txt")
        service_catalogue = ServiceCatalogue(repo_catalogue, repo_student, repo_subject, valid)

        list=service_catalogue.students_order_by_name(1)
        assert list[1]=='Cristian Mihut'