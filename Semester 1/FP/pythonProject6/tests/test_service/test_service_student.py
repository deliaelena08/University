import shutil
from unittest import TestCase
from service.service_student import *

class TestServiceClient(TestCase):
    def clear_test_file(self):
        with open("test_students.txt", "w") as f:
            pass

    def copy_test_file_content(self):
        shutil.copyfile("test_initial_students.txt","test_students.txt")

    def test_add_student(self):
        self.copy_test_file_content()
        test_valid=ValidStudent()
        test_repo=FileRepoStudent("test_students.txt")
        test_service=ServiceStudent(test_repo,test_valid)

        test_service.add_student("2","Cristian Soviet")
        assert len(test_service.get_all_students())==2
        assert test_service.get_student("1").get_nume()=="Cristiana Mihut"
        try:
            test_service.add_student("1","Marius")
            assert False
        except:
            assert True

    def test_delete_student(self):
        test_valid = ValidStudent()
        test_repo = FileRepoStudent("test_students.txt")
        test_service = ServiceStudent(test_repo, test_valid)

        test_service.add_student("3","Floricica Dansatoare")
        assert len(test_service.get_all_students())==3
        test_service.delete_student("3")
        assert len(test_service.get_all_students())==2
        try:
            test_service.delete_student("3")
            assert False
        except:
            assert True


    def test_update_student(self):
        test_valid = ValidStudent()
        test_repo = FileRepoStudent("test_students.txt")
        test_service = ServiceStudent(test_repo, test_valid)

        test_service.update_student("2","Marina Marin")
        assert test_service.get_student("2").get_nume()=="Marina Marin"
