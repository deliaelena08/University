
class Melody():
    def __init__(self,title,artist,type,time):
        self.__title=title
        self.__artist=artist
        self.__type=type
        self.__time=time

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

    def get_time(self):
        return self.__time

    def set_time(self,time):
        self.__time=time