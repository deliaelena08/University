from unittest import TestCase
from service.Service import *
from datetime import *

class TestService(TestCase):
    def test_add(self):
        open('melodii_test.txt', 'w').close()
        repo=InFile("melodii_test.txt")
        valid=Validate()
        service=Service(repo,valid)
        service.add("Hills","The Weeknd","Pop","20.01.2009")
        assert len(service.get_all())==1
        service.add("Worry", "Billie Eilish", "Pop", "20.01.2019")
        service.add("Call me", "Lost Frequencies", "Jazz", "20.01.2019")
        assert len(service.get_all())==3


    def test_generate_random(self):
        open('melodii_test.txt', 'w').close()
        repo = InFile("melodii_test.txt")
        valid = Validate()
        service = Service(repo, valid)
        artists=["Ariana Grande","Nicky Minaj","Ed Sheeran"]
        titles=["Bills","Love me","Anaconda","Photograph"]
        service.generate_random(3,artists,titles)
        assert len(service.get_all())==3

    def test_export_sorted_list(self):
        open('generare_test.txt', 'w').close()
        repo = InFile("melodii_test.txt")
        valid = Validate()
        service = Service(repo, valid)
        service.add("Hills","The Weeknd","Pop","20.01.2009")
        service.add("Worry", "Billie Eilish", "Pop", "20.01.2019")
        service.add("Call me", "Lost Frequencies", "Jazz", "20.01.2019")
        self.test_generate_random()
        service.export_sorted_list('generare_test.txt')
        assert True

    def test_modify_melody(self):
        open('melodii_test.txt', 'w').close()
        repo = InFile("melodii_test.txt")
        valid = Validate()
        service = Service(repo, valid)
        service.add("Hills", "The Weeknd", "Pop", "20.01.2009")
        service.add("Worry", "Billie Eilish", "Pop", "20.01.2019")
        service.modify_melody("Hills","The Weeknd","Rock","20.02.2009")
        assert True