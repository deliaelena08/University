class Tractor:
    def __init__(self,id,name,price,model,date):
        self.__id=id
        self.__name=name
        self.__price=price
        self.__model=model
        self.__date=date

    def get_id(self):
        return self.__id

    def set_id(self,new_id):
        self.__id=new_id

    def get_name(self):
        return self.__name

    def set_name(self,new_name):
        self.__name=new_name

    def get_price(self):
        return self.__price

    def set_price(self,new_price):
        self.__price=new_price

    def get_model(self):
        return self.__model

    def set_model(self,new_model):
        self.__model=new_model

    def get_date(self):
        return self.__date

    def set_date(self,new_date):
        self.__date= new_date