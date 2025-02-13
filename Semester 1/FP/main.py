def bfs(mat, n, m, x, y):
    if x <0 or y < 0 or x >= n or y >= m:
        return False
    if mat[x][y] == "F":
        return True
    if mat[x][y] == "1" or mat[x][y] == "*" or mat[x][y] == "-":
        return False
    mat[x][y]="*"
    ok = bfs(mat, n, m, x+1, y) or bfs(mat,n,m,x,y+1) or bfs(mat,n,m,x-1,y) or bfs(mat,n,m,x,y-1)
    if ok == False:
        mat[x][y] = "-"
    return ok

file = open("test.txt", "r")
data = file.read()
rows = data.split('\n')
mat = [list(x) for x in rows]

n = len(mat)
m = len(mat[0])

start_x = -1
start_y = -1

for i in range(0, len(mat)):
    for j in range(0, len(mat[i])):
        if mat[i][j] == "S":
            start_x = i
            start_y = j
            break
    if start_x != -1:
        break

bfs(mat,n,m,start_x, start_y)

for i in range (0, len(mat)):
    for j in range(0, len(mat[i])):
        if mat[i][j] == "-":
            mat[i][j] = " "

row = [''.join(x) for x in mat]

print('\n'.join(row))