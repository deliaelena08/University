import shutil
from unittest import TestCase
from service.service_subject import *


class TestServiceSubject(TestCase):
    def clear_test_file(self):
        with open ("test_subjects.txt","w") as f:
            pass
    def copy_initial_file_test(self):
        shutil.copyfile("intitial_subjects_test.txt","test_subjects.txt")

    def test_add_subject(self):
        self.copy_initial_file_test()
        valid_subject=ValidSubject()
        test_repo=FileRepoSubject("test_subjects.txt")
        servce_test=ServiceSubject(test_repo,valid_subject)

        servce_test.add_subject("5","Limba si literatura romana","Zaharescu Camelia")
        assert servce_test.get_subject("5").get_name_subject()=="Limba si literatura romana"
        assert servce_test.get_subject("5").get_name_teacher()=="Zaharescu Camelia"
        try:
            servce_test.add_subject("2","Sport","Mihaela Paraschiv")
            assert False
        except:
            assert True


    def test_delete_subject(self):
        valid_subject = ValidSubject()
        test_repo = FileRepoSubject("test_subjects.txt")
        servce_test = ServiceSubject(test_repo, valid_subject)
        self.copy_initial_file_test()

        assert len(servce_test.get_all_subjects())==5
        servce_test.delete_subject("5")
        assert len(servce_test.get_all_subjects())==4
        try:
            servce_test.delete_subject("5")
            assert False
        except:
            assert True

    def test_update_subject(self):
        valid_subject = ValidSubject()
        test_repo = FileRepoSubject("test_subjects.txt")
        servce_test = ServiceSubject(test_repo, valid_subject)
        self.copy_initial_file_test()

        servce_test.update_subject("2","Informatica","Nour Georgeta")
        assert servce_test.get_subject("2").get_name_teacher()=="Nour Georgeta"
        assert servce_test.get_subject("2").get_name_subject()=="Informatica"
