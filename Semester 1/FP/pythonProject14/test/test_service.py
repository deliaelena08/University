from unittest import TestCase
from service.service import *


class TestService(TestCase):
    def test_add_melody(self):
        open('test.txt', 'w').close()
        repo=InFile("test.txt")
        validator=Valid()
        service=Service(repo,validator)
        service.add_melody("Mouth to FLame","The Weeknd","Pop",3)
        assert len(service.get_all())==1

    def test_delete_melody(self):
        open('test.txt', 'w').close()
        repo = InFile("test.txt")
        validator = Valid()
        service = Service(repo, validator)
        service.add_melody("Mouth to FLame", "The Weeknd", "Pop", 3)
        service.delete_melody(3)
        assert len(service.get_all())==0


    def test_undo(self):
        open('test.txt', 'w').close()
        repo = InFile("test.txt")
        validator = Valid()
        service = Service(repo, validator)
        service.add_melody("Mouth to FLame", "The Weeknd", "Pop", 3)
        service.delete_melody(3)
        service.undo()
        assert len(service.get_all())==1

    def test_random_generate(self):
        open('test.txt', 'w').close()
        repo = InFile("test.txt")
        validator = Valid()
        service = Service(repo, validator)
        names=["Hannah Montana","Mickel Jackson"]
        titles=["When I see you again","All of me"]
        service.random_generate(2,names,titles)
        assert len(service.get_all())==2
