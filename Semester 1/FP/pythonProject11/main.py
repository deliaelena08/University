'''
a=[1,'a',5,60,-1]
print(a[:2])
b=a[:]
print(b)
print(a[4:])
print(a.pop())
print(a)
l1=range(10)
print(l1)
l3=range(0,12,3)
print(list(l3))
print(list(l1))
a=[1,[9,0,7],'k']
print(a)
t=12,21,'qns'
print(t[0])
print(t[2])
u=t, (23,23)
print(u)
x,y,z=t
print(x)
print(y)
print(z)
singleton=(12,)
print(singleton)
print(len(singleton))

def inverse_list(list):
    if len(list)==0:
        return []
    if len(list)==1:
        return list

    middle=len(list)//2
    list_1=inverse_list(list[:middle])
    list_2=inverse_list(list[middle:])
    return list_2+list_1

def sort_list(list1,list2):
    new_list=[]
    l1=len(list1)
    l2=len(list2)
    i=0
    j=0
    while i<l1 and j<l2:
        if list2[j]<=list1[i]:
            new_list.append(list1[i])
            i+=1
        else:
            new_list.append(list2[j])
            j+=1

    while i < l1:
        new_list.append(list1[i])
        i += 1

    while j < l2:
        new_list.append(list2[j])
        j += 1

    return new_list


def decreased_list(list):
    if len(list)==0:
        return []
    if len(list)==1:
        return list
    middle=len(list)//2
    list_1=decreased_list(list[:middle])
    list_2=decreased_list(list[middle:])
    return sort_list(list_1,list_2)

list=[3,2,5,-3,9,0,2]
print(inverse_list(list))
print(decreased_list(list))
n=6
l=[x for x in range(n-1,-1,-1)]
for i in range(n-1):
 l[i+1] += l[i]
print(l)
print(l[-1])


def number_positive(list):
    if len(list)==0:
        return 0
    if len(list)==1:
        if list[0]>=0:
            return 1
        return 0
    middle=len(list)//2
    sum1=number_positive(list[:middle])
    sum2=number_positive(list[middle:])
    sum1=int(sum1)
    sum2=int(sum2)
    return sum1+sum2

list=[0,2,3,-7,8,-8,6,-29,-6,0]
print(number_positive(list))

def afis(sol,list):
    print([list[poz] for poz in sol])
def valid(list,k):
    if k==0:
        return True
    if list[k-1]<list[k]:
        return True
    return False

def is_solution(sol,list,n):
    first_element=list[sol[0]]
    l=len(sol)
    for i in range(1,l):
        if list[sol[i]]>=0 and first_element<0:
            return False
        if list[sol[i]]<0 and first_element>=0:
            return False
    return True

def backt_rec(list,n,sol,k):
    for i in range (0,n):
        if valid(sol+[i],k):
            if is_solution(sol+[i],list,n):
                afis(sol+[i],list)
            backt_rec(list,n,sol+[i],k+1)

list=[0,2,3,-8,77]
backt_rec(list,len(list),[],0)

def find_number(list):
    if len(list)==0:
        return 0
    if len(list)==1:
        if list[0]%2==0:
            return True
        return False
    middle=len(list)//2
    if(find_number(list[:middle])==True):
        return True
    if(find_number(list[middle:])==True):
        return True
    return False

list=[3,5,7,0,9,1,1]
print(find_number(list))


def prime_numbers(list):
    prime_list=[]
    for i in range(0,len(list)):
        ok=0
        for d in range (2,list[i]//2+1):
            if list[i]%d==0:
                ok=1
        if ok==0:
            prime_list.append(list[i])
    l=[0]*len(prime_list)
    l[-1]=1
    p=[0]*len(prime_list)
    p[-1]=-1
    for poz in range (len(prime_list)-2,-1,-1):
        l[poz]=1
        p[poz]=-1
        for i in range(poz+1,len(prime_list)):
            if prime_list[poz]<prime_list[i] and l[i]+1>l[poz]:
                l[poz]=l[i]+1
                p[poz]=i
    maxi=0
    number=0
    for i in range(0,len(l)):
        if l[i]>maxi:
            maxi=l[i]
            number=i
    list=[]
    while number!=-1:
        list.append(prime_list[number])
        number=p[number]
    print(list)

list=[21,2,11,3,4,7,13]
prime_numbers(list)


def inverse_list(list):
    if len(list)==0:
        return []
    if len(list)==1:
        return list
    middle=len(list)//2
    list_1=inverse_list(list[:middle])
    list_2=inverse_list(list[middle:])
    return list_2+list_1

list=[1,23,4,56,7,4,8,0]
print(inverse_list(list))

def subarray_parity(list):
    l=[0]*len(list)
    l[-1]=1
    p=[0]*len(list)
    p[-1]=-1
    for poz in range (len(list)-2,-1,-1):
        l[poz]=1
        p[poz]=-1
        if list[poz]%2==0:
            for i in range(poz+1,len(list)):
                if list[i]%2==0:
                    if list[poz]>list[i] and l[poz]<l[i]+1:
                        l[poz]=l[i]+1
                        p[poz]=i
    maxi=0
    for i in range(0,len(list)):
        if maxi<l[i]:
            number=i
            maxi=l[i]

    new_list=[]
    while number!=-1:
        new_list.append(list[number])
        number=p[number]

    return new_list

list=[2,12,3,6,14,3,4,7,2]
print(subarray_parity(list))

def multiply(list,start):
    if len(list)==0:
        return 0
    if len(list)==1:
        if start%2==0:
            return list[0]
        return 1
    middle=len(list)//2
    el1=multiply(list[:middle],start)
    el2=multiply(list[middle:],start+middle)
    el1=int(el1)
    el2=int(el2)
    return el1*el2

list=[1,2,3,4,5,6]
print(multiply(list,0))

def negative_numbers(list):
    if len(list)==0:
        return 0
    if len(list)==1:
        if list[0]<0:
            return 1
        return 0
    middle=len(list)//2
    return negative_numbers(list[:middle])+negative_numbers(list[middle:])

list=[1,0,-8,7,9,-2,-3,5,4,-1]
print(negative_numbers(list))
def double_the_list(list):
    if len(list)==0:
        return 0
    if len(list)==1:
        list[0]=2*list[0]
        return list
    middle=len(list)//2
    left=double_the_list(list[:middle])
    right=double_the_list(list[middle:])
    return left+right

list=[0,1,2,3,4]
print(double_the_list(list))

def longest_subarray_parrity(list):
    l=[0]*len(list)
    l[-1]=1
    p=[0]*len(list)
    p[-1]=-1
    for poz in range (len(list)-2,-1,-1):
        l[poz] = 1
        p[poz] = -1
        if list[poz] % 2 == 0:
            for i in range(poz + 1, len(list)):
                if list[i] % 2 == 0:
                    if list[poz]<list[i] and l[poz]<l[i]+1:
                        l[poz]=l[i]+1
                        p[poz]=i
    maxi=0
    for i in range (0,len(l)):
        if l[i]>maxi:
            maxi=l[i]
            ind=i
    new_list=[]
    while ind!=-1:
        new_list.append(list[ind])
        ind=p[ind]
    return new_list

list=[22,2,11,10,4,7,8]
print(longest_subarray_parrity(list))

def non_parity_list(list):
    if len(list)==0:
        return 0
    if len(list)==1:
        if list[0]%2==1:
            return list
        return []
    middle=len(list)//2
    return non_parity_list(list[:middle])+non_parity_list(list[middle:])

list=[1,2,3,4,5,0]
print(non_parity_list(list))

def subarray(list):
    l=[0]*len(list)
    l[-1]=1
    p=[0]*len(list)
    p[-1]=-1
    for i in range (len(list)-2,-1,-1):
        p[i]=-1
        l[i]=1
        if list[i]//10!=0 and list[i]//100==0:
            for j in range (i+1,len(list)):
                if list[j]//10!=0 and list[j]//100==0:
                    if list[i]<list[j] and l[i]<l[j]+1:
                        l[i]=l[j]+1
                        p[i]=j
    maxi=0
    new_list=[]
    for i in range(0,len(list)):
        if l[i]>maxi:
            maxi=l[i]
            ind=i
    while ind!=-1:
        new_list.append(list[ind])
        ind=p[ind]
    return new_list

list=[22,10,16,11,4,13,14]
print(subarray(list))

def bynary_s(list,el,left,right):
    if right-1<=left:
        return right
    m=(left+right)//2
    if el<=list[m]:
        return bynary_s(list,el,left,m)
    return bynary_s(list,el,m,right)


def bynary_search(list,el):
    if el <list[0]:
        return 0
    if len(list)==0:
        return 0
    if el>list[len(list)-1]:
        return len(list)
    return bynary_s(list,el,0,len(list))

def bynart_search2(list,el):
    if len (list)==0:
        return 0
    if el<list[0]:
        return 0
    if el>list[len(list)-1]:
        return len(list)
    left=0
    right=len(list)
    while left<(right-1):
        m=(left+right)//2
        if el<=list[m]:
            right=m
        else:
            left=m
    return right
el=2
list1=[1,1,2,3,4,5]
print(bynary_search(list1,el))
list2=[1,1,3,4,5]
print(bynary_search(list2,el))
print(bynart_search2(list1,el))
print(bynart_search2(list2,el))

def selection_sort(list):
    for i in range(0,len(list)-1):
        ind=i
        for j in range(i+1,len(list)):
            while list[j]<list[ind]:
                ind=j
        if ind>i:
            list[i],list[ind]=list[ind],list[i]
    return list


def insertion_sort(list):
    for i in range (0,len(list)-1):
        ind=i-1
        a=list[i]
        while ind>=0 and a<list[ind]:
            list[ind+1]=list[ind]
            ind=ind-1
        list[ind+1]=a
    return list


def bubble_sort(list):
    for i in range (0,len(list)-1):
        for j in range (i+1,len(list)):
            if list[i]>list[j]:
                list[i],list[j]=list[j],list[i]
    return list

def quicksort(list):
    if len(list)<=1:
        return list
    pivot=list.pop()
    lesser=[]
    greater=[]
    for i in range(0,len(list)):
        if list[i]<=pivot:
            lesser.append(list[i])
        else:
            greater.append(list[i])
    less=quicksort(lesser)
    great=quicksort(greater)
    return less+[pivot]+great

def merge_sort(list):
    if len(list)<=1:
        return list
    m=(1+len(list))//2
    l1=list[:m]
    l2=list[m:]
    list1=merge_sort(l1)
    list2=merge_sort(l2)
    return sort_two_list(list1,list2)

def sort_two_list(l1,l2):
    i=0
    j=0
    new=[]
    while i<len(l1) and j<len(l2):
        if l1[i]<=l2[j]:
            new.append(l1[i])
            i=i+1
        else:
            new.append(l2[j])
            j=j+1

    while i<len(l1):
        new.append(l1[i])
        i=i+1

    while j<len(l2):
        new.append(l2[j])
        j=j+1

    return new

list=[3,7,0,5,9,-3,14,7,21,2]
print(selection_sort(list))
print(insertion_sort(list))
print(bubble_sort(list))
print(quicksort(list))
print(merge_sort(list))
#poza_5
def merge_sort(list):
    if len(list) <= 1:
        return list
    m=(1+len(list))//2
    list1=list[:m]
    list2=list[m:]
    l1=merge_sort(list1)
    l2=merge_sort(list2)
    return sort_two(l1,l2)

def sort_two(l1,l2):
    i=0
    j=0
    new_list=[]
    while i<len(l1) and j<len(l2):
        if l1[i]<=l2[j]:
            new_list.append(l1[i])
            i+=1
        else:
            new_list.append(l2[j])
            j+=1

    while i < len(l1):
        new_list.append(l1[i])
        i += 1

    while j<len(l2):
        new_list.append(l2[j])
        j+=1
    return new_list

list=[2,7,5,0,9,1]
print (merge_sort(list))

def f2(n):
    if n<=0: raise ValueError()
    l = [x for x in range(n-1,-1,-1)]
    for i in range(n-1):
        l[i+1] += l[i]
    return l[-1]

def test():
    #blackbox
    n1=3
    assert f2(n1)==n1
    assert f2(3)==3
    n2=-2
    try:
        f2(n2)
        assert False
    except ValueError:
        assert True
    n3=1
    assert f2(n3)==0
    #white box
    n4=0
    try:
        f2(n4)
        assert False
    except ValueError:
        assert True
    n5=-100
    try:
        f2(n5)
        assert False
    except ValueError:
        assert True
    assert f2(5)==10
    n=4
    assert f2(n)==6

test()

def divide_impera(list):
    if len(list)<1:
        return 0
    if len(list)==1:
        if list[0]<0:
            return 1
        return 0
    m=len(list)//2
    return divide_impera(list[m:])+divide_impera(list[:m])

list=[0,1,3,5,7,-9,8,-3,2]
print(divide_impera(list))

Candidat: x=[x0,x1,...xi] oricare ar fi x=1,n
        unde xi reprezinta pozitia elementului din lista
Consistenta:
            xi!=xj oricare ar fi i si j diferiti
Solutie:
        daca e consistenta si toate elementele de pe pozitiile din lista candidat sunt doar pare sau doar impare        



#poza8
def bynary_search(list,el,left,right):
    if right-1<=left:
        return right
    m=(left+right)//2
    if el<=list[m]:
        return bynary_search(list,el,left,m)
    return bynary_search(list,el,m,right)


def bynary_conditions(list,el):
    if len(list)==0 or el<list[0]:
        return 0
    if el>list[len(list)-1]:
        return len(list)
    return bynary_search(list,el,0,len(list))

def bynary_search2(list,el):
    if len(list)==0 or el<list[0]:
        return 0
    if el>list[len(list)-1]:
        return len(list)
    left=0
    right=len(list)
    while right-left>1:
        m=(left+right)//2
        if el <=list [m]:
            right=m
        else:
            left=m
    return right

list=[1,2,3,4,5,6,7,8]
print(bynary_conditions(list,6))
print(bynary_search2(list,6))

def f(n):
    if n<=0: raise ValueError()
    while n>0:
        c = n % 10
        n = n//10
    if c%2==0: return True
    return False
def test():
    #Blackbox
    n=9286
    assert f(n)==False
    assert f(12)==False
    n1=4670
    assert f(n1)==True
    assert f(2)==True
    try:
        f(0)
        assert False
    except ValueError:
        assert True
    #WhiteBox
    try:
        f(0)
        assert False
    except ValueError:
        assert True
    n2=-2754
    try:
        f(n2)
        assert False
    except ValueError:
        assert True
    n3=1
    assert f(n3)==False
    assert f(30000)==False
    n4=293782
    assert f(n4)==True
    assert f(8)==True

test()

def div_imp(list):
    if len(list)==0:
        return 0
    if len(list)==1:
        if list[0]%2==0:
            return 1
        return 0
    m=len(list)//2
    return (div_imp(list[:m])+div_imp(list[m:]))>0

list1=[1,3,5,7,9]
print(div_imp(list1))
list2=[1,3,3,0,6,7]
print(div_imp(list2))

def prime_list(list):
    new_list=[]
    for i in range(0,len(list)):
        ok=0
        if list[i]>1 and list[i]%2==1:
            for d in range(3,(list[i]+1)//2,2):
                if list[i]%d==0:
                    ok=1
        else:
            ok=1
        if ok==0:
            new_list.append(list[i])
    return new_list

def dynamic_programming(list):
    my_list=prime_list(list)
    l=[0]*len(my_list)
    l[-1]=1
    p=[0]*len(my_list)
    p[-1]=-1
    for j in range(len(my_list)-2,-1,-1):
        p[j]=-1
        l[j]=1
        for i in  range(j+1,len(my_list)):
            if my_list[j]<my_list[i] and l[j]<l[i]+1:
                l[j]=l[i]+1
                p[j]=i
    maxi=-1
    new_list=[]
    poz=0
    for i in range(0,len(my_list)):
        if l[i]>maxi:
            maxi=l[i]
            poz=i
    while poz!=-1:
        new_list.append(my_list[poz])
        poz=p[poz]
    return new_list

list=[1,3,0,2,5,7,9,3,4,11,21,31,29]
print(dynamic_programming(list))

#poza9
def quick_sort(list):
    if len(list)<=1:
        return list
    lesser=[]
    greater=[]
    pivot=list.pop()
    for i in range(0,len(list)):
        if list[i]<=pivot:
            lesser.append(list[i])
        else:
            greater.append(list[i])
    less=quick_sort(lesser)
    great=quick_sort(greater)
    return less+[pivot]+great

list=[3,6,7,-1,2,0,6,9,-4,22,4,8]
print(quick_sort(list))

def f(n):
    if n<=0: raise ValueError()
    l = []
    while n>0:
        c = n % 10
        n = n//10
        l.append(c)
    for i in range(len(l)-1): l[i + 1] += l[i]
    return l[-1]
def test():
    #BlackBox
    assert f(123)==6
    n0=4560
    assert f(n0)==15
    assert f(3)==3
    n=0
    try:
        f(n)
        assert False
    except ValueError:
        assert True
    #WhiteBox
    try:
        f(-5)
        assert False
    except ValueError:
        assert True
    try:
        f(0)
        assert False
    except ValueError:
        assert True
    assert f(8)==8
    n1=12349
    n2=11111
    assert f(n1)==19
    assert f(n2)==5

test()

'''

def divide(list):
    if len(list)==0:
        return 0
    if len(list)==1:
        if list[0]<0:
            return 1
        return 0
    m=len(list)//2
    return divide(list[:m])+divide(list[m:])

list=[20,3,56,2,-8,4,9,0,-1,2,-3]
print(divide(list))

def divide2(l):
    if len(l)==0:
        return l
    if len(l)==1:
        l[0]=l[0]*2
        return l
    m=len(l)//2
    return divide2(l[m:])+divide2(l[:m])

print(divide2(list))
def parity(list):
    new=[]
    for i in range(0,len(list)):
        if list[i]%2==0:
            new.append(list[i])
    return new

def dynaming_programing(list):
    my_list=parity(list)
    if len(my_list)<=1:
        return my_list

    lenghts=[0]*len(my_list)
    lenghts[-1]=1
    positions=[0]*len(my_list)
    positions[-1]=-1

    for i in range (len(my_list)-2,-1,-1):
        positions[i]=-1
        lenghts[i]=1
        for j in range(i+1,len(my_list)):
            if my_list[i]<my_list[j] and lenghts[i]<lenghts[j]+1:
                positions[i]=j
                lenghts[i]=lenghts[j]+1
    new=[]
    maxi=0
    for i in range (0,len(my_list)):
        if lenghts[i]>maxi:
            maxi=lenghts[i]
            ind=i
    while ind!=-1:
        new.append(my_list[ind])
        ind=positions[ind]
    return new

print(dynaming_programing(list))