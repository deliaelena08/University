from console.console import *
repo_file=RepoFile("exams.txt")
validator=Valid()
service=Service(repo_file,validator)
console=Console(service)
console.run()