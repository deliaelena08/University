from console.console import *

repo=InFile("melodii.txt")
validator=Validate()
service=Service(repo,validator)
console=Console(service)
console.run()