from unittest import TestCase
from repo.repo import *
from datetime import datetime


class TestInMemoryTractor(TestCase):
    def test_add_tractor(self):
        date = datetime.strptime("11:12:2026", "%d:%m:%Y")
        tractor = Tractor(1, "Marcel", 12000, "Mercedes", date)
        repo_tractor=InMemoryTractor()
        repo_tractor.add_tractor(tractor)
        assert len(repo_tractor.get_all())==1
        assert repo_tractor.get_tractor("1").get_name()=="Marcel"

    def test_get_tractor(self):
        date = datetime.strptime("11:12:2026", "%d:%m:%Y")
        tractor = Tractor(1, "Marcel", 12000, "Mercedes", date)
        repo_tractor = InMemoryTractor()
        repo_tractor.add_tractor(tractor)
        tractor2=repo_tractor.get_tractor(1)
        assert tractor==tractor2

    def test_delete_tractor(self):
        date = datetime.strptime("11:12:2026", "%d:%m:%Y")
        tractor = Tractor(1, "Marcel", 12000, "Mercedes", date)
        repo_tractor = InMemoryTractor()
        repo_tractor.add_tractor(tractor)
        assert len(repo_tractor.get_all()) == 1
        repo_tractor.delete_tractor("1")
        assert len(repo_tractor.get_all())==0
