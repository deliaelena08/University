from unittest import TestCase
from Domain.domain import *

class TestMelody(TestCase):
    def test_get_title(self):
        melody=Melody("Mouth to FLame","The Weeknd","Pop",3)
        assert melody.get_title()=="Mouth to FLame"

    def test_set_title(self):
        melody=Melody("Mouth to FLame","The Weeknd","Pop",3)
        melody.set_title("Hills")
        assert melody.get_title()=="Hills"

    def test_get_artist(self):
        melody = Melody("Mouth to FLame", "The Weeknd", "Pop", 3)
        assert melody.get_artist()=="The Weeknd"

    def test_set_artist(self):
        melody = Melody("Mouth to FLame", "The Weeknd", "Pop", 3)
        melody.set_artist("Abel")
        assert melody.get_artist()=="Abel"

    def test_get_type(self):
        melody = Melody("Mouth to FLame", "The Weeknd", "Pop", 3)
        assert melody.get_type()=="Pop"

    def test_set_type(self):
        melody=Melody("Mouth to FLame","The Weeknd","Pop",3)
        melody.set_type("Rock")
        assert melody.get_type()=="Rock"

    def test_get_time(self):
        melody = Melody("Mouth to FLame", "The Weeknd", "Pop", 3)
        assert int(melody.get_time())==3

    def test_set_time(self):
        melody = Melody("Mouth to FLame", "The Weeknd", "Pop", 3)
        melody.set_time("2")
        assert int(melody.get_time())==2
