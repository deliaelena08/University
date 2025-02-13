from domain.domain import Student


class InMemoryStudent:
    def __init__(self):
        self.__list=[]

    def get_student(self,id):
        id=int(id)
        for s in self.__list:
            if int(s.get_id())==id:
                return s
        return None

    def modify_student(self,student):
        for s in self.__list:
            if int(s.get_id())==int(student.get_id()):
                s.set_nume(student.get_nume())
                return s
        return "Studentul nu exista"

    def add_student(self,student):
        if self.get_student(student.get_id()) is not None:
            raise ValueError("Id-ul exista deja")
        self.__list.append(student)
        return student

    def delete_student(self,id):
        for i in range(0,len(self.__list)):
            if int(id)==int(self.__list[i].get_id()):
                self.__list.pop(i)
                return self.__list
        return "Studentul nu exista"

    def get_all(self):
        return self.__list

class FileRepoStudent(InMemoryStudent):
    def __init__(self,filename):
        InMemoryStudent.__init__(self)
        self.__file_name=filename
        self.load_in_memory()

    def load_in_memory(self):
        with open(self.__file_name, mode='r', encoding='utf-8') as students_file:
            lines = students_file.readlines()
            lines = [line.strip() for line in lines if line.strip() != '']
            for line in lines:
                id ,name=line.split(',')
                InMemoryStudent.add_student(self,Student(int(id),name))

    def write_to_file(self):
        students=InMemoryStudent.get_all(self)
        students=[str(student.get_id()) +','+ str(student.get_nume()) for student in students]
        with open(self.__file_name, mode='w', encoding='utf-8') as students_file:
            text_to_write = '\n'.join(students)
            students_file.write(text_to_write)

    def add(self,student):
        InMemoryStudent.add_student(self,student)
        self.write_to_file()
        return student

    def update(self,student):
        InMemoryStudent.modify_student(self,student)
        self.write_to_file()
        return student

    def delete(self,student_id):
        InMemoryStudent.delete_student(self,student_id)
        self.write_to_file()
        return InMemoryStudent.get_all(self)
