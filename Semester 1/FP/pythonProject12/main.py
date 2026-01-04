

def divide_list(list):
    if len(list)==0:
        return 0
    if len(list)==1:
        return list[0]
    middle=len(list)//2
    return max(divide_list(list[:middle]),divide_list(list[middle:]))

list=[2,8,7,4,-1,4,3,0]
print(divide_list(list))