import shutil
from unittest import TestCase
from repository.repo_client import *
from validare.valid import ValidClients
from service.service_client import *

class TestServiceClient(TestCase):
    def clear_test_file(self):
        with open("test_client.txt", "w") as f:
            pass

    def add_initial_data_to_test_file(self):
        shutil.copyfile("inital_client.txt", "test_client.txt")

    def test_add_client(self):
        self.fail()

    def test_update_client(self):
        test_valid = ValidClients()
        test_repo = InMemoryRepoClient()
        test_service = ServiceClient(test_repo, test_valid)

        test_service.add_client(1, "Oidiu", "112233")
        assert test_service.get_client(1).get_name() == "Oidiu"

        test_service.update_client(1, "Delia", "332211")
        assert test_service.get_client(1).get_name() == "Delia"

        try:
            test_service.update_client(1, "A", "112233")
            assert False
        except ValueError:
            assert True

        try:
            test_service.update_client(2, "Mama", "333333")
            assert False
        except ValueError:
            assert True

    def test_delete_client(self):
        self.fail()

    def test_random_client(self):
        self.fail()

    def test_add_client_file(self):
        self.fail()

    def test_update_client_file(self):
        self.clear_test_file()
        test_valid = ValidClients()
        test_repo = FileRepoClient("test_client.txt")
        test_service = ServiceClient(test_repo, test_valid)

        test_service.add_client(1, "Oidiu", "112233")
        assert test_service.get_client(1).get_name() == "Oidiu"

        test_service.update_client(1, "Delia", "332211")
        assert test_service.get_client(1).get_name() == "Delia"

        try:
            test_service.update_client(1, "A", "112233")
            assert False
        except ValueError:
            assert True

        try:
            test_service.update_client(2, "Mama", "333333")
            assert False
        except ValueError:
            assert True

    def test_delete_client_file(self):
        self.fail()

    def test_random_client_file(self):
        self.fail()
