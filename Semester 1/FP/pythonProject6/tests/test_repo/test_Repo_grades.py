from unittest import TestCase
from repository.Repo_grades import *

class TestInMemoryCatalogue(TestCase):
    def test_add_catalogue(self):
        repo_catalogue=InMemoryCatalogue()
        grade=Catalogue("1","1","10")
        repo_catalogue.add_catalogue(grade)

        assert len(repo_catalogue.get_all())==1

    def test_delete_catalogue(self):
        repo_catalogue = InMemoryCatalogue()
        grade1 = Catalogue("1", "1", "10")
        repo_catalogue.add_catalogue(grade1)
        grade2 = Catalogue("2","1","9")
        repo_catalogue.add_catalogue(grade2)

        assert len(repo_catalogue.get_all())==2
        repo_catalogue.delete_catalogue(grade2)
        assert len(repo_catalogue.get_all())==1
