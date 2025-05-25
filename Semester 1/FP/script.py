
class Baller:
    def __init__(self,id_baller,name,value):
        self.__id_baller = id_baller
        self.__name = name
        self.__value = value

    def get_id_baller(self):
        return self.__id_baller

    def get_name(self):
        return self.__name

    def get_value(self):
        return self.__value

    def set_name(self,new_name):
        self.__name=new_name

    def set_value(self,new_value):
        self.__value=new_value

    def __eq__(self,gigi):
        return self.__id_baller == gigi.__id_baller

    def __str__(self):
        return f"[{self.__id_baller}]{self.__name}:{self.__value}"
class ValidatorBaller:

    def valideaza_baller(self,baller):
        erori = ""
        if baller.get_id_baller()<0:
            erori += "id invalid!\n"
        if baller.get_name()=="":
            erori += "nume invalid!\n"
        if baller.get_value()<0.0:
            erori += "valoare invalida!\n"
        if len(erori)>0:
            raise Exception(erori)

class BallerRepository:

    def __init__(self):
        self.__ballerz = {}

    def add_baller(self,baller):
        id_baller = baller.get_id_baller()
        if id_baller in self.__ballerz:
            raise Exception("id existent!\n")
        self.__ballerz[id_baller] = baller
    def get_all(self):
        return [self.__ballerz[x] for x in self.__ballerz.keys()]
class BallerService:

    def __init__(self,repo_ballerz,validator_baller):
        self.__repo_ballerz = repo_ballerz
        self.__validator_baller = validator_baller

    def add_baller(self,id_baller,name,value):
        baller = Baller(id_baller,name,value)
        self.__validator_baller.valideaza_baller(baller)
        self.__repo_ballerz.add_baller(baller)

    def get_all_ballers(self):
        return self.__repo_ballerz.get_all()
class UI:

    def __init__(self,service_ballerz):
        self.__service_ballerz = service_ballerz
        self.__commandz = {
            "adauga_baller":self.__ui_add_baller,
            "print_ballers":self.__ui_print_ballers,
        }
    def __ui_print_ballers(self,params):
        if len(params)!=0:
            print("nr parametri invalid!")
            return
        ballerz = self.__service_ballerz.get_all_ballers()
        for baller in ballerz:
            print(baller)
    def __ui_add_baller(self,params):
        if len(params)!=3:
            print("nr parametri invalid!")
            return
        try:
            id_baller = int(params[0])
            name = params[1]
            valoare = float(params[2])
        except ValueError:
            raise Exception("valoare numerica invalida!")
        self.__service_ballerz.add_baller(id_baller,name,valoare)
        print("baller adaugat cu succes!")

    def run(self):
        while True:
            cmd = input(">>>")
            cmd = cmd.strip()
            if cmd == "exit":
                return
            parts = cmd.split(" ")
            cmd_name = parts[0]
            params = parts[1:]
            if cmd_name in self.__commandz:
                try:
                    self.__commandz[cmd_name](params)
                except Exception as ex:
                    print(ex)
            else:
                print("comanda invalida!")

class Teste:
    def __test_create_baller(self):
        id_baller = 23
        name = "Jordan"
        value =9000.1
        epsilon = 0.0001
        baller = Baller(id_baller,name,value)
        other_id_baller = 23
        other_name ="LeBum"
        other_value = 7300.34
        other_baller = Baller(other_id_baller,other_name,other_value)
        assert baller.get_id_baller() == id_baller
        assert baller.get_name() == name
        assert abs(baller.get_value()-value)<epsilon
        assert baller == other_baller

    def __test_validate_baller(self):
        id_baller = 23
        name = "Jordan"
        value = 9000.1
        epsilon = 0.0001
        baller = Baller(id_baller, name, value)
        bad_id_baller = -23
        bad_name = ""
        bad_value = -9000.1
        epsilon = 0.0001
        bad_baller = Baller(bad_id_baller, bad_name, bad_value)
        validator = ValidatorBaller()
        validator.valideaza_baller(baller)
        try:
            validator.valideaza_baller(bad_baller)
            assert False
        except Exception as ex:
            assert(str(ex)=="id invalid!\nnume invalid!\nvaloare invalida!\n")
    def run_all_tests(self):
        self.__test_create_baller()
        self.__test_validate_baller()


t = Teste()
t.run_all_tests()
repo = BallerRepository()
validator = ValidatorBaller()
service = BallerService(repo,validator)
console = UI(service)
console.run()