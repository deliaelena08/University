from unittest import TestCase

from domain.domain import *


class TestFilm(TestCase):
    def test_create(self):
        film = Film(5, "spiderman", "ok", "actiune")
        assert film.get_id() == 5
        assert film.get_title() == "spiderman"
        assert film.get_description() == "ok"
        assert film.get_type() == "actiune"


class TestClient(TestCase):
    def test_create(self):
        client = Client(8, "Mariana", "60973625279")
        assert client.get_id() == 8
        assert client.get_name() == "Mariana"
        assert client.get_cnp() == "60973625279"


class TestRent(TestCase):
    def test_create(self):
        rent = Rent(1,2)
        assert rent.get_client_id() == 1
        assert rent.get_film_id() == 2
