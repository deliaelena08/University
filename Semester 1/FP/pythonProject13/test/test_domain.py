from unittest import TestCase
from datetime import datetime

from domain.domain import Tractor


class TestTractor(TestCase):
    def test_get_id(self):
        date=datetime.strptime("11:12:2026","%d:%m:%Y")
        tractor=Tractor(1,"Marcel",12000,"Mercedes",date)
        assert tractor.get_id()==1

    def test_set_id(self):
        date = datetime.strptime("11:12:2026", "%d:%m:%Y")
        tractor = Tractor(1, "Marcel", 12000, "Mercedes", date)
        tractor.set_id(2)
        assert tractor.get_id() == 2

    def test_get_name(self):
        date = datetime.strptime("11:12:2026", "%d:%m:%Y")
        tractor = Tractor(1, "Marcel", 12000, "Mercedes", date)
        assert tractor.get_name()=="Marcel"

    def test_set_name(self):
        date = datetime.strptime("11:12:2026", "%d:%m:%Y")
        tractor = Tractor(1, "Marcel", 12000, "Mercedes", date)
        tractor.set_name("Mirel")
        assert tractor.get_name() == "Mirel"

    def test_get_price(self):
        date = datetime.strptime("11:12:2026", "%d:%m:%Y")
        tractor = Tractor(1, "Marcel", 12000, "Mercedes", date)
        assert tractor.get_price()==12000

    def test_set_price(self):
        date = datetime.strptime("11:12:2026", "%d:%m:%Y")
        tractor = Tractor(1, "Marcel", 12000, "Mercedes", date)
        tractor.set_price(1200)
        assert tractor.get_price() == 1200

    def test_get_model(self):
        date = datetime.strptime("11:12:2026", "%d:%m:%Y")
        tractor = Tractor(1, "Marcel", 12000, "Mercedes", date)
        assert tractor.get_model()=="Mercedes"

    def test_set_model(self):
        date = datetime.strptime("11:12:2026", "%d:%m:%Y")
        tractor = Tractor(1, "Marcel", 12000, "Mercedes", date)
        tractor.set_model("Nissan")
        assert tractor.get_model() == "Nissan"

    def test_get_date(self):
        date = datetime.strptime("11:12:2026", "%d:%m:%Y")
        tractor = Tractor(1, "Marcel", 12000, "Mercedes", date)
        assert tractor.get_date() == date

    def test_set_date(self):
        date1 = datetime.strptime("11:12:2026", "%d:%m:%Y")
        tractor = Tractor(1, "Marcel", 12000, "Mercedes", date1)
        date2=datetime.strptime("10:10:2010", "%d:%m:%Y")
        tractor.set_date(date2)
        assert tractor.get_date() == date2
