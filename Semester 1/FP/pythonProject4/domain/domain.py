
class Film:
    def __init__(self, id, title, description, type):
        self.__id=id
        self.__title=title
        self.__description=description
        self.__type=type

    def get_id(self):
        return self.__id

    def get_title(self):
        return self.__title

    def set_title(self, title):
        self.__title= title

    def get_description(self):
        return self.__description

    def set_description(self, description):
        self.__description = description

    def get_type(self):
        return self.__type

    def set_type(self, type):
        self.__type = type

    def __str__(self):
        return "{Id: " + str(self.__id) + ",Title:" + self.__title + ", Description: " + self.__description  +", Type: "+self.__type+ "}"


class Client:
    def __init__(self, id, name, cnp):
        self.__id=id
        self.__name=name
        self.__cnp=cnp

    def get_id(self):
        return self.__id

    def get_name(self):
        return self.__name

    def set_name(self, name):
        self.__name = name

    def get_cnp(self):
        return self.__cnp

    def set_cnp(self, cnp):
        self.__cnp= cnp

    def __str__(self):
        return "{Id: " + str(self.__id) + ", Name: " + self.__name + ", CNP: " + self.__cnp  + "}"

class Rent:
    def __init__(self, client, film):
        self.__client = client
        self.__film = film

    def get_film_id(self):
        return self.__film

    def get_client_id(self):
        return self.__client

    def __eq__(self, other):
        return self.__film == other.get_film_id() and self.__client == other.get_client_id()

    def __str__(self):
        return "{Client: " + str(self.__client) + ", Film: " + str(self.__film)+"}"


