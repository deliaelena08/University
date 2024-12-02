class ValidFilms:
    def valid_film(self,id,title,description,type):
        errors=[]

        if len(title)<2:
            errors.append("Ttilul trebuie sa aiba o lungime mai mare decat 2")

        if len(description)<2:
            errors.append("Desrierea trebuie sa aiba o lungime mai mare decat 2")

        if len(type)<2:
            errors.append("Genul trebuie sa aiba o lungime mai mare decat 2")
    
        if len(errors) > 0:
            error_string = '\n'.join(errors)
            raise ValueError(error_string)

class ValidClients:
    def valid_client(self,id,name,cnp):
    
        errors = []

        if len(name) < 2:
            errors.append("Numele trebuie sa aiba o lungime mai mare decat 2")
        if len(cnp) < 2:
            errors.append("Cnp-ul trebuie sa aiba o lungime mai mare decat 2")
    
        if len(errors) > 0:
            error_string = '\n'.join(errors)
            raise ValueError(error_string)
