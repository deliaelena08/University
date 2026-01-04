


class ValidSubject():
    def validare_subject(self,id,name_subject,name_teacher):
        errors=[]

        if int(id)<1:
            errors.append("Id-ul nu este valid")

        if len(name_subject)<3:
            errors.append("Materia nu este valida")

        if len(name_teacher)<3:
            errors.append("Numele profesorului nu este valid")

        if len(errors)>0:
            errors_string='\n'.join(errors)
            raise ValueError(errors_string)
