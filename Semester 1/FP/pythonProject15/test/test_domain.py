from unittest import TestCase
from domain.domain import *

class Testexam(TestCase):
    def test_get_date(self):
        object=Exam("20:02","14:00","Analiza","normala")
        assert object.get_date()=="20:02"

    def test_set_date(self):
        object = Exam("20:02", "14:00", "Analiza", "normala")
        object.set_date("20:01")
        assert object.get_date() == "20:01"

    def test_get_hour(self):
        object = Exam("20:02", "14:00", "Analiza", "normala")
        assert object.get_hour() == "14:00"

    def test_set_hour(self):
        object = Exam("20:02", "14:00", "Analiza", "normala")
        object.set_hour("10:00")
        assert object.get_hour() == "10:00"


    def test_get_subject(self):
        object = Exam("20:02", "14:00", "Analiza", "normala")
        assert object.get_subject() == "Analiza"

    def test_set_subject(self):
        object = Exam("20:02", "14:00", "Analiza", "normala")
        object.set_subject("Algebra")
        assert object.get_subject() == "Algebra"

    def test_get_type(self):
        object = Exam("20:02", "14:00", "Analiza", "normala")
        assert object.get_type() == "normala"

    def test_set_type(self):
        object = Exam("20:02", "14:00", "Analiza", "normala")
        object.set_type("restanta")
        assert object.get_type() == "restanta"
