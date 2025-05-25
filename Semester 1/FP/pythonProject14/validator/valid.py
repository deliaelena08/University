from Domain.domain import *


class Valid():
    def valid_melody(self,title,artist,type,time):
        errors=[]

        if title=="":
            errors.append("Melodia trebuie sa aiba titlu")

        if artist=="":
            errors.append("Melodia trebuie sa aiba un artist")

        time=int(time)
        if time<0:
            errors.append("Durata trebuie sa fie un intreg pozitiv")

        list_type=["Pop","Rock","Jazz","altele"]
        if type not in list_type:
            errors.append("Tipul trebuie sa fie doar una din urmatoarele:Pop,Jazz,Rock,altele")

        if len(errors)>0:
            errors_string='\n'.join(errors)
            raise ValueError(errors_string)