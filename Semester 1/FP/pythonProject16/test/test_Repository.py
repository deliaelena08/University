from unittest import TestCase
from repository.Repository import *
from datetime import datetime

class TestInMemo(TestCase):
    def test_add_memo(self):
        repo=InMemo()
        data = datetime.strptime("11.02.2020", "%d.%m.%Y")
        melody = Melody("Hills", "The Weeknd", "Pop", data)
        repo.add_memo(melody)
        assert len(repo.get_all())==1
        data1 = datetime.strptime("01.02.2020", "%d.%m.%Y")
        melody1 = Melody("Mamacita", "Louis Fonsi", "Pop", data1)
        repo.add_memo(melody1)
        data2 = datetime.strptime("11.09.2021", "%d.%m.%Y")
        melody2 = Melody("Hills", "The Weeknd", "Pop", data2)
        repo.add_memo(melody2)
        assert len(repo.get_all())==3

