from ui.ui import *

inp=input("Introduceti un nume de fisier")
repo=FileRepoTractor(inp)
service=ServiceTractor(repo)
console=Console(service)
console.run()