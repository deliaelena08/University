import shutil
from unittest import TestCase

from domain.domain import Client
from repository.repo_client import *


class TestInMemoryRepoClient(TestCase):
    def test_add(self):
        repo_client = InMemoryRepoClient()
        client = Client(1, "Ovidiu", "112233")
        repo_client.add(client)

        assert len(repo_client.get_all()) == 1
        assert repo_client.get(1).get_name() == "Ovidiu"

        client = Client(1, "Delia", "114455")
        try:
            repo_client.add(client)
            assert False
        except ValueError:
            assert True

    def test_update(self):
        self.fail()

    def test_delete(self):
        self.fail()


class TestFileRepoClient(TestCase):
    def clear_test_file(self):
        with open("test_client.txt", "w") as f:
            pass

    def add_initial_data_to_test_file(self):
        shutil.copyfile("inital_client.txt", "test_client.txt")

    def test_read_from_file(self):
        self.clear_test_file()
        self.add_initial_data_to_test_file()

        repo_client = FileRepoClient("test_client.txt")
        assert len(repo_client.get_all()) == 4

    def test_add(self):
        self.clear_test_file()
        repo_client = FileRepoClient("test_client.txt")
        client = Client(1, "Ovidiu", "112233")
        repo_client.add(client)

        assert len(repo_client.get_all()) == 1
        assert repo_client.get(1).get_name() == "Ovidiu"

        client = Client(1, "Delia", "114455")
        try:
            repo_client.add(client)
            assert False
        except ValueError:
            assert True

    def test_update(self):
        self.fail()

    def test_delete(self):
        self.fail()
