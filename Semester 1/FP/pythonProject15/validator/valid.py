from repository.repository import *
from datetime import datetime
class Valid():
    def validare(self,date,hour,subject,type):
        errors=[]
        repo=RepoinMemo()
        try:
            date=datetime.strptime(date,"%d:%m")
            correctDate=True
        except ValueError:
            correctDate=False
        if correctDate==False:
            errors.append("Data introdusa nu este valida")

        try:
            hour=datetime.strptime(hour,"%H:%M")
            correcthour=True
        except  ValueError:
            correcthour=False
        if correcthour==False:
            errors.append("Ora introdusa nu este corecta")

        exams=repo.get_all()
        for e in exams:
            if e.get_subject()==subject:
                if e.get_type()==type:
                    errors.append("Nu pot exista 2 examene de acelasi tip la fel")

        if len(errors)>0:
            raise ValueError(errors)