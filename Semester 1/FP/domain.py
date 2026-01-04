class Rent:
    
    def __init__(self,client,film):
        self.__client=client
        self.__film=film
        self.__status=0

    def set_rent(self):
        self.__status=1

    def set_return(self):
        self.__status=0

    def get_date(self):
        return self.__date

    def get_status(self):
        return self.__status

    def get_film_id(self):
        return self.__film

    def get_client_id(self):
        return self.__client


    def set_date(self,date):
        self.__list[3]=date

    def get_all(self):
        return self.__list








class Film:

    def __init__(self, id_film, titlu, descriere, gen):
        self.__atribute = [id_film, titlu, descriere, gen]

    def get_id_film(self):
        return self.__atribute[0]

    def get_titlu(self):
        return self.__atribute[1]

    def get_descriere(self):
        return self.__atribute[2]

    def get_gen(self):
        return self.__atribute[3]

    def set_id_film(self, id_nou):
        set.__atribute[0] = id_nou

    def set_titlu(self, titlu_nou):
        set.__atribute[1] = titlu_nou

    def set_descriere(self, descriere_nou):
        set.__atribute[2] = descriere_nou

    def set_gen(self, gen_nou):
        set.__atribute[3] = gen_nou

    def __eq__(self, film_verif):
        return self.__atribute[0] == film_verif.__atribute[0]

    def  __str__(self):
        return "ID: " + str(self.get_id_film()) + " | " + "Titlu: " + self.get_titlu() + " | " + "Descriere: " + self.get_descriere() + " | " + "Gen: " + self.get_gen()

class Client:

    def __init__(self, id_client, nume, cnp):
        self.__id_client = id_client
        self.__nume = nume
        self.__cnp = cnp

    def get_id_client(self):
        return self.__id_client

    def get_nume(self):
        return self.__nume

    def get_cnp(self):
        return self.__cnp

    def set_id_client(self, id_nou):
        set.__id_client = id_nou

    def set_nume(self, nume_nou):
        set.__nume = nume_nou

    def set_cnp(self, cnp_nou):
        set.__cnp = cnp_nou

    def __eq__(self, client_verif):
        return self.__id_client == client_verif.__id_client

    def  __str__(self):
        return "ID: " + str(self.get_id_client()) + " | " + "Nume: " + self.get_nume() + " | " + "CNP: " + self.get_cnp()