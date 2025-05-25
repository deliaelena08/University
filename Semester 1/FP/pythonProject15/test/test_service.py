from unittest import TestCase
from service.service import *


class TestService(TestCase):

    def test_add(self):
        open('test.txt', 'w').close()
        repo=RepoFile("test.txt")
        valid=Valid()
        service=Service(repo,valid)
        service.add("20:02","14:00","FP","normal")
        assert len(service.get_all())==1

    def test_get_by_today(self):
        open('test.txt', 'w').close()
        repo = RepoFile("test.txt")
        valid = Valid()
        service = Service(repo, valid)
        service.add("20:02", "14:00", "FP", "normal")
        service.add("04:02", "13:00", "FP", "restanta")
        service.add("04:02", "09:00", "Acasa", "normal")
        assert len(service.get_by_today())==2

    def test_date_seted_list(self):
        open('test.txt', 'w').close()
        repo = RepoFile("test.txt")
        valid = Valid()
        service = Service(repo, valid)
        service.add("20:02", "14:00", "FP", "normal")
        service.add("04:02", "13:00", "FP", "restanta")
        service.add("04:02", "09:00", "Acasa", "normal")
        date=datetime.strptime("02:02","%d:%m")
        assert len(service.date_seted_list(date))==2
