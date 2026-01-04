
class Melody():
    def __init__(self,title,artist,type,data):
        self.__title=title
        self.__artist=artist
        self.__type=type
        self.__data=data

    def get_title(self):
        return self.__title

    def set_title(self,title):
        self.__title=title

    def get_artist(self):
        return self.__artist

    def set_artist(self,artist):
        self.__artist=artist

    def get_type(self):
        return self.__type

    def set_type(self,type):
        self.__type=type

    def get_data(self):
        return self.__data

    def set_data(self,data):
        self.__data=data