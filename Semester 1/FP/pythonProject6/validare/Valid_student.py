

class ValidStudent:
    def valid_students(self,id,name):
        errors=[]

        if int(id)<1:
            errors.append("Id-ul nu este valid")
        if len(name)<3:
            errors.append("Numele nu este valid")

        if len(errors)>0:
            errors_string='\n'.join(errors)
            raise ValueError(errors_string)