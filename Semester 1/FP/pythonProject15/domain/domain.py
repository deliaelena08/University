class Exam():
    def __init__(self,date,hour,subject,type):
        self.__date=date
        self.__hour=hour
        self.__subject=subject
        self.__type=type

    def get_date(self):
        return self.__date

    def set_date(self,date):
        self.__date=date

    def get_hour(self):
        return self.__hour

    def set_hour(self,hour):
        self.__hour=hour

    def get_subject(self):
        return self.__subject

    def set_subject(self,subject):
        self.__subject=subject

    def get_type(self):
        return self.__type

    def set_type(self,type):
        self.__type=type