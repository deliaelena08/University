from unittest import TestCase
from repository.Repo_subject import *

class TestInMemorySubject(TestCase):
    def test_modify_subject(self):
        repo_subject=InMemorySubject()
        subject=Subject("2","Romana","Adriana")
        repo_subject.add_subject(subject)

        subject_change=Subject("2","Romana","Angelica")
        repo_subject.modify_subject(subject_change)

        assert repo_subject.get_subject("2").get_name_subject()=="Romana"
        assert repo_subject.get_subject("2").get_name_teacher()=="Angelica"

    def test_add_subject(self):
        repo_subject=InMemorySubject()
        subject=Subject("1","Matematica","Popovici Maria")
        repo_subject.add_subject(subject)

        assert len(repo_subject.get_all())==1
        assert repo_subject.get_subject("1").get_name_subject()=="Matematica"

    def test_delete_subject(self):
        repo_subject=InMemorySubject()
        subject1=Subject("3","fizica","Stroia Maria")
        repo_subject.add_subject(subject1)

        subject2=Subject("4","chimie","Doina Ema")
        repo_subject.add_subject(subject2)

        assert len(repo_subject.get_all())==2
        repo_subject.delete_subject("3")
        assert  len(repo_subject.get_all())==1