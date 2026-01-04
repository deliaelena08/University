from repository.Repo_Student import *
from repository.Repo_subject import *


class ValidGrade:
    def valid_grade(self,id_student,id_subject,grade):
        errors=[]
        if int(id_student)<1:
            errors.append("Id-ul pentru student nu este valid")

        if int(id_subject)<1:
            errors.append("Id-ul pentru subiect nu este valid")

        if int(grade)<1 or int(grade)>10:
            errors.append("Nota nu este valida")

        if len(errors)>0:
            raise ValueError(errors)