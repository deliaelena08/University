'''Se dă o listă de numere întregi a1,...an generați toate sub-secvențele
cu proprietatea că suma numerelor este divizibul cu N dat '''
'''
O(n)-maximul de pasi(cum merge in cel mai prost caz)
Omega (n)-best case(cum merge in cel mai  bun caz)
Theta (n)-average case(cum merge tot timpul)
'''
def afis(sol, list):
    '''
    Afisarea solutiilor
    :param sol: vectorul solutie
    :param list: lista introdusa de la tastatura
    :return: Afisarea solutiei
    '''
    af = " ".join([str(list[i]) for i in sol])
    print(af)

def valid(sol, k):
    '''
    Verificam daca ese o subsecventa valida
    :param sol: vectorul solutie
    :param k: pozitia pe care ne aflam
    :return: True daca respecta,False daca nu e valid
    '''
    if k == 0:
        return True
    if sol[k] > sol[k-1]:
        return True
    return False

def is_solution(sol, list, n):
    '''
    Verificam daca vectorul solutie respecta conditia cerintei(divizibil cu n)
    :param sol: vectorul solutie
    :param list: lista cu valorile introduse de la tastatura
    :param n: lungimea listei introduse si numarul cu care solutia trebuie sa fie divizibila
    :return: True daca e valid, False daca nu este solutie
    '''
    sum = 0
    for i in sol:
        sum += list[i]
    if sum % n == 0:
        return True
    return False

def backt_rec(n, list, sol, k):
    '''
    Backtracking recursiv
    :param n: lungiea listei
    :param list: lista cu valorile introduse
    :param sol: vectorul solutuie
    :param k: pozitia pe care ne aflam
    :return: afisarea solutiilor valide
    '''
    for i in range(0, n):
        if valid(sol+[i], k):
            if is_solution(sol+[i], list, n):
                afis(sol+[i], list)
            backt_rec(n, list, sol+[i], k+1)

def backt(n, list):
    '''
    Backtracking iterativ
    :param n: lungimea listei
    :param list: lista cu valorile introduse
    :return: afiseaza solutiile valide
    '''
    k=0
    sol = [-1]
    while k >= 0:
        sol[k] += 1
        while sol[k] < n:
            #print("sol["+str(k)+"]="+str(sol[k])+", n="+str(n))
            if valid(sol, k):
                if is_solution(sol, list, n):
                    afis(sol, list)
                k = k + 1
                sol.append(0)
            else:
                sol[k] = sol[k]+1
        sol.pop()
        k = k - 1
def citirea_datelor():
    '''
    Citirea unei liste si lungimii sale
    :return: lungimea listei,lista
    '''
    n=input("Introduceti numarul de elemente din sir: ")
    inp=input("Introduceti un sir de numere separate prin virgula")
    list=inp.split(",")
    list=[int(el) for el in list]
    return (n, list)

n,list=citirea_datelor()
n=int(n)
backt_rec(n, list, [], 0)
print('\n')
backt(n,list)
print('\n')


'''
def backt_rec2(x,n,list):
    x.append(0)
    for i in range (0,n):
        x[-1]=i
        if valid(x,i):
            if is_solution(x,list,n):
                afis(x,list)
            backt_rec2(x[:],n,list)
    x.pop()

'''
'''backt_rec2([],n,list)'''

def number_positive(list):
    if len(list)==0:
        return 0
    if len(list)==1:
        if list[0]>=0:
            return 1
    middle=len(list)//2
    sum1=number_positive(list[:middle])
    sum2=number_positive(list[middle:])
    sum1=int(sum1)
    sum2=int(sum2)
    return sum1+sum2

list=[0,2,3,-7,8,-8,6,-29,-6,0]
print(number_positive(list))