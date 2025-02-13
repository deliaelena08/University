from ui.ui import *

repo_student=FileRepoStudent("students.txt")
repo_subject=FileRepoSubject("subjects.txt")
repo_catalogue=FileRepoCatalogue("catalogue.txt")

valid_student=ValidStudent()
valid_subject=ValidSubject()
valid_grade=ValidGrade()

service_student=ServiceStudent(repo_student,valid_student)
service_subject=ServiceSubject(repo_subject,valid_subject)
service_catalogue=ServiceCatalogue(repo_catalogue,repo_student,repo_subject,valid_grade)

console=Console(service_subject,service_student, service_catalogue)
console.run()