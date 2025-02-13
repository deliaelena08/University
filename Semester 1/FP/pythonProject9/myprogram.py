message=[]
array=[]
def load_in_memory():
    with open("hidden_message.txt", mode='r', encoding='utf-8') as message_file:
        lines = message_file.readlines()
        lines = [line.strip() for line in lines if line.strip() != '']
        for line in lines:
            number,code = line.split(' ')
            number=int(number)
            array.insert(number, code)

def read_input():
    with open("input.txt", mode='r', encoding='utf-8') as input_file:
        lines = input_file.readlines()
        lines = [line.strip() for line in lines if line.strip() != '']
        for line in lines:
            numbers = line.split(" ")
            number=numbers.pop()
            number = int(number)
            message.append(str(array[number]))

load_in_memory()
read_input()

print(' '.join(message))
