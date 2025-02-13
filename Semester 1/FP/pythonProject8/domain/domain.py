

class Player:
    def __init__(self,id_player,name,position,points):
        self.__id_player=id_player
        self.__name=name
        self.__position=position
        self.__points=points

    def get_id_player(self):
        return self.__id_player

    def get_name(self):
        return self.__name

    def get_position(self):
        return self.__position

    def get_points(self):
        return self.__points

    def set_id_player(self,id):
        self.__id_player=id

    def set_name(self,name):
        self.__name=name

    def set_position(self,position):
        self.__position=position

    def set_points(self,points):
        self.__points=points

    def __str__(self):
        return str(self.__id_player)+','+str(self.__name)+','+str(self.__position)+','+str(self.__points)

class MVPPlayer:
    def __init__(self,player:Player,game_number,average):
        self.__player=player
        self.__game_number=game_number
        self.__average_points=average

    def get_player(self):
        return self.__player

    def get_number_games(self):
        return self.__game_number

    def get_average_points(self):
        return self.__average_points

    def set_average_points(self,avg):
        self.__average_points=avg

    def set_player(self,player):
        self.__player=player

    def set_number_games(self,number):
        self.__game_number=number

    def __str__(self):
        return str(self.__player)+','+str(self.__game_number)+','+str(self.__average_points)
