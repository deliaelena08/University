from domain.domain import Client


class InMemoryRepoClient:
    def __init__(self):
        self.__list = []

    def add(self, client):
        '''
        Adaugarea unui client in lista de clienti
        :param client: un client
        :return: clientul daca a fost adaugat
        '''
        if self.get(client.get_id()) is not None:
            raise ValueError("Clientul exista deja")
        self.__list.append(client)
        return client

    def get(self,id):
        '''
        Gasirea unui client dupa id
        :param id: id-ul clientului pe care il cauta
        :return: clientul daca exista
        '''
        for c in self.__list:
            if int(c.get_id())==int(id):
                return c
        return None

    def update(self, client):
        '''
        Setarea unor date noi la un client existential
        :param client: datele clientului pentru modificare
        :return: Clientul modificat
        '''
        for c in self.__list:
            if c.get_id()==client.get_id():
                c.set_name(client.get_name())
                c.set_cnp(client.get_cnp())
                return c
        raise ValueError("Clientul nu exista")

    def delete(self,id):
        '''
        Stergerea unui client dupa id
        :param id: Id-ul clientului care sa fie sters
        :return: Clientul sters daca exista
        '''
        for i in range(0,len(self.__list)):
            if self.__list[i].get_id()==id:
                return self.__list.pop(i)
        raise ValueError("Clientul nu exista")

    def get_all(self):
        '''
        Returnarea listei de clienti
        :return: lista
        '''
        return self.__list


class FileRepoClient(InMemoryRepoClient):
    def __init__(self, filename):
        InMemoryRepoClient.__init__(self)
        self.__filename = filename
        self.load_in_memory()
    def load_in_memory(self):
        '''
        Adauga in memorie un client
        :return: -
        '''
        with open(self.__filename, mode='r', encoding='utf-8') as clients_file:
            lines = clients_file.readlines()
            lines = [line.strip() for line in lines if line.strip() != '']
            for line in lines:
                id, nume, cnp = line.split(',')
                InMemoryRepoClient.add(self, Client(int(id), nume, cnp))

    def write_to_file(self):
        '''
        Scrie in fisier
        :return: -
        '''
        clients = InMemoryRepoClient.get_all(self)
        clients = [str(client.get_id()) + ',' + client.get_name() + ',' + client.get_cnp() for client in clients]
        with open(self.__filename, mode='w', encoding='utf-8') as clients_file:
            text_to_write = '\n'.join(clients)
            clients_file.write(text_to_write)

    def add(self, client):
        '''
        Adauga un client in fisier
        :param client: Clientul de adaugat
        :return: Obiectul client
        '''
        client = InMemoryRepoClient.add(self, client)
        self.write_to_file()
        return client

    def update(self, client):
        '''
        Modificarea datelor unui client
        :param client: Clientul modificat
        :return: Clientul dupa modificare
        '''
        client = InMemoryRepoClient.update(self, client)
        self.write_to_file()
        return client

    def delete(self, id):
        '''
        Stergerea unui client din fisier
        :param id: id-ul clientului pe care sa l stearga
        :return: Clientul sters
        '''
        client = InMemoryRepoClient.delete(self, id)
        self.write_to_file()
        return client
