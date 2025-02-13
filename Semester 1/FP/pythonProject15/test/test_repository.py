from unittest import TestCase
from repository.repository import *
from datetime import datetime


class TestRepoinMemo(TestCase):
    def test_add_subject(self):
        repo=RepoinMemo()
        date=datetime.strptime("20:02","%d:%m")
        subject=Exam(date,"14:00","FP","normal")
        repo.add_subject(subject)
        assert len(repo.get_all())==1