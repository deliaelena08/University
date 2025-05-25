from repo.repo_player import *
from repo.repo_mvp import *

class ServiceMvp:
    def __init__(self,repo_player:FileRepo,repo_mvp:InMemoMvp):
        self.__repo_player=repo_player
        self.__repo_mvp=repo_mvp

    def add_in_mvp(self,id_player,game_number):
        '''

        :param id_player: id-ul jucatorului
        :param game_number: numarul de jocuri
        :return:
        '''
        list_players=self.__repo_player.get_all()
        for p in list_players:
            if int(p.get_id_player())==int(id_player):
                points=p.get_points()
        if p==0:
            raise ValueError("Id-ul jucatorului nu exista")
        else:
            points=int(points)
            game_number=int(game_number)
            average=points/game_number
        player=self.__repo_player.get_player(int(id_player))
        inp=MVPPlayer(player,game_number,average)
        self.__repo_mvp.add_in_mvp(inp)
        return inp

    def evaluation(self,id):
        id_player=int(id)
        player=self.__repo_player.get_player(id_player)
        name_player=player.get_name()
        mvp_list=self.__repo_mvp.get_all()
        for m in mvp_list:
            if int(m.get_player().get_id_player())==id:
                average=m.get_average_points()
                return str(id_player)+','+str(name_player)+','+str(average)

    def get_all(self):
        return self.__repo_mvp.get_all()





