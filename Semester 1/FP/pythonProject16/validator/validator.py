from repository.Repository import *
from datetime import *

class Validate():
    def valid_melody(self,title,artist,type,data):
        errors=[]


        if title=="":
            errors.append("Melodia trebuie sa aiba titlu")

        if artist=="":
            errors.append("Melodia trebuie sa aiba un artist")

        for l in list():
            if l.get_artist()==artist:
                if l.get_title()==title:
                    errors.append("Nu se pot adauga un artist cu aceleasi melodii de mia multe ori")

        ok=True
        try:
            data=datetime.strptime(data,"%d.%m.%Y")
        except ValueError:
            ok=False

        if ok==False:
            errors.append("Data nu ete de formatul cerut")

        list_type=["Pop","Rock","Jazz"]
        if type not in list_type:
            errors.append("Tipul trebuie sa fie doar una din urmatoarele:Pop,Jazz,Rock,altele")

        if len(errors)>0:
            errors_string='\n'.join(errors)
            raise ValueError(errors_string)