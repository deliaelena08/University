from domain.domain import *

class InMemoMvp:
    def __init__(self):
        self.__list=[]

    def add_in_mvp(self,inp):
        self.__list.append(inp)

    def get_all(self):
        return self.__list
