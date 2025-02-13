from repo.repo_player import *


class ServicePlayer:
    def __init__(self,repo:FileRepo):
        self.__repo=repo

    def add_player(self,id,name,position,points):
        '''
        Adaugarea unui nou jucator in clasa
        :param id: id-ul jucatorului
        :param name: numele jucatorului
        :param position: pozitia lui
        :param points: punctele lui
        :return: jucatorul creat-obiect clasa
        '''
        player=Player(int(id),name,position,points)
        self.__repo.add(player)

    def find_string(self,str):
        '''
        Toti jucatrorii pentru care string-ul pozitiei contine un sir introdus
        :param str: String-ul introdus de utilizator
        :return: retruneaza un mesaj daca nu exista sau lista de jucatori valabila
        '''
        list=self.__repo.get_all()
        list_players=[]
        for p in list:
            position=p.get_position()
            if str in position:
                list_players.append(p)
        if len(list_players)<1:
            return "Nu exista pozitii cu acest string"
        else:
            return list_players

    def get_all_players(self):
        return self.__repo.get_all()
