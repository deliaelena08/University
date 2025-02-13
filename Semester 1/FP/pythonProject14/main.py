from console.console import *
repo=InMemory()
repofile=InFile("melodii.txt")
valid=Valid()
service=Service(repofile,valid)
console=Console(service)
console.run()