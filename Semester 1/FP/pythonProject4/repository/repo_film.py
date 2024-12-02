from domain.domain import Film


class InMemoryRepoFilm:
    def __init__(self):
        self.__list = []

    def get(self, id):
        '''
        Gasirea unui film
        :param id: id ul unui film existent
        :return: Filmul daca exista
        '''
        for f in self.__list:
            if int(f.get_id()) == int(id):
                return f
        return None
    def add(self, film):
        '''
        Adaugarea unui film in lista
        :param film: un obiect film
        :return:filmul adaugat
        '''
        if self.get(film.get_id()) is not None:
            raise ValueError("Filmul exista deja")
        self.__list.append(film)
        return film


    def update(self, film):
        '''
        Moddificarea unui film
        :param film: Obiectul film care sa fie modificat
        :return: filmul modificat
        '''
        for f in self.__list:
            if f.get_id() == film.get_id():
                f.set_description(film.get_description())
                f.set_title(film.get_title())
                f.set_type(film.get_type())
                return f
        raise ValueError("Filmul nu exista")

    def delete(self, id):
        '''
        Stergerea unui film dupa id
        :param id: un numar intreg,un id existent
        :return: Filmul sters
        '''
        for i in range(0, len(self.__list)):
            if self.__list[i].get_id() == id:
                return self.__list.pop(i)
        raise ValueError("Filmul nu exista")

    def get_all(self):
        '''
        Obtinerea listei cu filme
        :return: lista
        '''
        return self.__list


class FileRepoFilm(InMemoryRepoFilm):

    def __init__(self, filename):
        InMemoryRepoFilm.__init__(self)
        self.__filename = filename
        self.load_in_memory()

    def load_in_memory(self):
        '''
        Incarcarea in memorie
        :return: -
        '''
        with open(self.__filename, mode='r', encoding='utf-8') as films_file:
            lines = films_file.readlines()
            lines = [line.strip() for line in lines if line.strip() != '']
            for line in lines:
                id, title, description, type = line.split(',')
                InMemoryRepoFilm.add(self, Film(int(id),title,description,type))

    def write_to_file(self):
        '''
        Scrierea in fisier
        :return: -
        '''
        films = InMemoryRepoFilm.get_all(self)
        films = [str(film.get_id()) + ',' + film.get_title() + ',' + film.get_description() + ',' + film.get_type() for film in films]
        with open(self.__filename, mode='w', encoding='utf-8') as films_file:
            text_to_write = '\n'.join(films)
            films_file.write(text_to_write)

    def add(self, film):
        '''
        Adaugarea unui film in fisier
        :param film: Obiectul film care sa fie adaugat
        :return: Filmul adaugat
        '''
        film = InMemoryRepoFilm.add(self, film)
        self.write_to_file()
        return film

    def update(self, film):
        '''
        Modificarea unui film
        :param film: Obiectul unui film care sa fie modificat
        :return: Filmul modificat
        '''
        film = InMemoryRepoFilm.update(self, film)
        self.write_to_file()
        return film

    def delete(self, id):
        '''
        Stergerea unui film din fisier
        :param id: un numar intreg
        :return: Filmul sters
        '''
        film = InMemoryRepoFilm.delete(self, id)
        self.write_to_file()
        return film