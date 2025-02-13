from domain.domain import *
class InMemoryPlayer:
    def __init__(self):
        self.__list=[]

    def add_player(self,player):
        '''
        Adaugare jucator
        :param player: obiectul jucator
        :return: jucatorul
        '''
        for p in self.__list:
            if int(p.get_id_player())==int(player.get_id_player()):
                raise ValueError("Jucatorul exista deja")
        self.__list.append(player)
        return player

    def get_player(self,id_player):
        '''
        Obtine jucator
        :param id_player: id ul unui jucator
        :return: Un obiect de tip jucator
        '''
        id_player=int(id_player)
        for p in self.__list:
            if int(p.get_id_player())==id_player:
                return p
        return None

    def get_all(self):
        return self.__list

class FileRepo(InMemoryPlayer):
    def __init__(self, filename):
        InMemoryPlayer.__init__(self)
        self.__filename = filename
        self.load_in_memory()

    def load_in_memory(self):
        with open(self.__filename, mode='r', encoding='utf-8') as players_file:
            lines = players_file.readlines()
            lines = [line.strip() for line in lines if line.strip() != '']
            for line in lines:
                id,name,position,points = line.split(',')
                InMemoryPlayer.add_player(self, Player(int(id),name,position,points))

    def write_to_file(self):
        players = InMemoryPlayer.get_all(self)
        players = [player.__str__()  for player in players]
        with open(self.__filename, mode='w', encoding='utf-8') as players_file:
            text_to_write = '\n'.join(players)
            players_file.write(text_to_write)

    def add(self, player):
        player = InMemoryPlayer.add_player(self, player)
        self.write_to_file()
        return player

    def get_all(self):
        return InMemoryPlayer.get_all(self)


