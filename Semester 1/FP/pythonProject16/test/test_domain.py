from unittest import TestCase
from domain.domain import *
from datetime import  datetime
class TestMelody(TestCase):
    def test_get_title(self):
        data=datetime.strptime("11.02.2020","%d.%m.%Y")
        melody=Melody("Hills","The Weeknd","Pop",data)
        assert melody.get_title()=="Hills"

    def test_set_title(self):
        data = datetime.strptime("11.02.2020", "%d.%m.%Y")
        melody = Melody("Hills", "The Weeknd", "Pop", data)
        melody.set_title("Mouth to flame")
        assert melody.get_title() == "Mouth to flame"

    def test_get_artist(self):
        data = datetime.strptime("11.02.2020", "%d.%m.%Y")
        melody = Melody("Hills", "The Weeknd", "Pop", data)
        assert melody.get_artist() == "The Weeknd"

    def test_set_artist(self):
        data = datetime.strptime("11.02.2020", "%d.%m.%Y")
        melody = Melody("Hills", "The Weeknd", "Pop", data)
        melody.set_artist("Lana del Ray")
        assert melody.get_artist() == "Lana del Ray"

    def test_get_type(self):
        data = datetime.strptime("11.02.2020", "%d.%m.%Y")
        melody = Melody("Hills", "The Weeknd", "Pop", data)
        assert melody.get_type() == "Pop"

    def test_set_type(self):
        data = datetime.strptime("11.02.2020", "%d.%m.%Y")
        melody = Melody("Hills", "The Weeknd", "Pop", data)
        melody.set_type("Rock")
        assert melody.get_type() == "Rock"

    def test_get_data(self):
        data = datetime.strptime("11.02.2020", "%d.%m.%Y")
        melody = Melody("Hills", "The Weeknd", "Pop", data)
        assert melody.get_data() == data

    def test_set_data(self):
        data = datetime.strptime("11.02.2020", "%d.%m.%Y")
        melody = Melody("Hills", "The Weeknd", "Pop", data)
        data1=datetime.strptime("10.02.2020", "%d.%m.%Y")
        melody.set_data(data1)
        assert melody.get_data() == data1
