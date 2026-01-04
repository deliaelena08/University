from ui.console import *

repo_player=FileRepo("players.txt")
repo_mvp=InMemoMvp()

service_player=ServicePlayer(repo_player)
service_mvp=ServiceMvp(repo_player,repo_mvp)

console=Console(service_player,service_mvp)
console.run()