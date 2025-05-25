from unittest import TestCase
from repository.repository import *


class TestInMemory(TestCase):
    def test_add_inmemo(self):
        melody = Melody("Mouth to FLame", "The Weeknd", "Pop", 3)
        repo = InMemory()
        repo.add_inmemo(melody)
        assert len(repo.get_all()) == 1

    def test_get_one_melody(self):
        melody = Melody("Mouth to FLame", "The Weeknd", "Pop", 3)
        repo = InMemory()
        repo.add_inmemo(melody)

        melody2 = Melody("Hills", "The Weeknd", "Pop", 4)
        repo.add_inmemo(melody2)
        assert repo.get_one_melody("Hills") == melody2

    def test_delete_melody(self):
        melody = Melody("Mouth to FLame", "The Weeknd", "Pop", 3)
        repo = InMemory()
        repo.add_inmemo(melody)

        melody2 = Melody("Hills", "The Weeknd", "Pop", 4)
        repo.add_inmemo(melody2)
        repo.delete_melody("Mouth to FLame")
        assert len(repo.get_all()) == 1

