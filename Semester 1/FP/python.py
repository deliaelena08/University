#from consola import *
#from cheltuiala import *

def print_menu_principal():
    print("1.Adauga cheltuiala")
    print("2.Stergere")
    print("3.Cautari")
    print("4.Raportare")
    print("5.Filtrare")
    print("6.Undo")

def menu_1():
    print("1.1 Adaugă o nouă cheltuială (se specifică ziua, suma, tipul) \n1.2 Actualizează cheltuială (se specifică ziua, suma, tipul) \n1.3 Afisare dictionar ")

def menu_2():
    print("2.1 Șterge toate cheltuielile pentru ziua data")
    print("2.2 Șterge cheltuielile pentru un interval de timp (se dă ziua de început și sfârșit, se șterg toate cheltuielile pentru perioada dată) ")
    print("2.3 Șterge toate cheltuielile de un anumit tip ")

def menu_3():
    print("3.1 Tipărește toate cheltuielile mai mari decât o sumă data")
    print("3.2 Tipărește toate cheltuielile efectuate înainte de o zi dată și mai mici decât o sumă ")
    print("3.3 Tipareste toate cheltuielile de un anumit tip")
def menu_4():
    print("4.1 Tipărește suma totală pentru un anumit tip de cheltuială")
    print("4.2 Găsește ziua în care suma cheltuită e maximă")
    print("4.3 Tipărește toate cheltuielile ce au o anumită sumă")
    print("4.4 Tipărește cheltuielile sortate după tip")

def menu_5():
    print("5.1 Elimină toate cheltuielile de un anumit tip")
    print("5.2 Elimină toate cheltuielile mai mici decât o sumă dată")

def menu_6():
    print("6 Reface ultima operație ")


def adaugare_cheltuiala(cheltuiala_list: list, cheltuiala: dict) -> None:
    """
    Adauga o cheltuiala in lista de cheltuieli
    :param cheltuiala_list: lista de cheltuieli
    :type cheltuiala_list: list
    :param cheltuiala: o cheltuiala de adaugat
    :type cheltuiala: dict
    :return: -; modifica lista prin adaugarea la sfarsit a cheltuielei
    :rtype:none
    """
    cheltuiala_list.append(cheltuiala)

def creare_cheltuiala(cheltuiala_str: str) -> dict:
    """
    :param cheltuiala_str: string care reprezinta cheltuiala facuta
    :type cheltuiala_str: str
    :rtype: dictionar
    :return: cheltuiala data
    """
    ziua,suma,tip = cheltuiala_str.split(',')
    ziua = ziua.strip()
    suma = suma.strip()
    suma= int (suma)
    tip = tip.strip()
    cheltuiala_dict = {'ziua': ziua, 'suma': suma, 'tip': tip}
    return cheltuiala_dict

def adaugare_cheltuieli(cheltuiala_str, cheltuiala_list, undo_list):
    # ui = user interface/interfata cu utilizatorul
    cheltuiala = creare_cheltuiala(cheltuiala_str)
    adaugare_cheltuiala(cheltuiala_list, cheltuiala)
    undo_list.append({'tip':'adaugare','element':cheltuiala})
    return cheltuiala

def undo(operatii,cheltuieli):
    """
    Functia de do/undo
    :param operatie: lista de operatii
    :param cheltuieli: lista de cheltuieli
    :return: sterge operatia /reface operatia
    """
    if len(operatii) == 0:
        return
    operatie = operatii.pop()
    if operatie['tip']=='adaugare':
        cheltuieli.remove(operatie['element'])
    if operatie['tip']=='stergere':
        cheltuieli.extend(operatie['elemente'])
        
def gasire_tip_cheltuiala(type,cheltuiala_list):
    """
    Cauta toate sumele cheltuite de un anumit tip
    :param type: string
    :return: list
    """
    lista_noua=[]
    for c in cheltuiala_list:
        if c['tip']==type:
            lista_noua.append(c)
    return lista_noua

def stergere_cheltuiala_tip(type,cheltuiala_list,undo_list):
    i = 0
    cheltuieli_sterse = []
    while i < len(cheltuiala_list):
        if cheltuiala_list[i]['tip'] == type:
            cheltuiala = cheltuiala_list.pop(i)
            cheltuieli_sterse.append(cheltuiala)
            i = i - 1;
        i = i + 1
    undo_list.append({'tip': 'stergere', 'elemente': cheltuieli_sterse})
    return cheltuiala_list

def stergere_cheltuiala_ziua(ziua,cheltuiala_list, undo_list):
    i=0
    cheltuieli_sterse = []
    while i<len(cheltuiala_list):
        if cheltuiala_list[i]['ziua'] == ziua:
            cheltuiala = cheltuiala_list.pop(i)
            cheltuieli_sterse.append(cheltuiala)
            i=i-1;
        i=i+1
    undo_list.append({'tip': 'stergere', 'elemente': cheltuieli_sterse})
    return cheltuiala_list

def stergere_cheltuiala_interval(zi_inceput, zi_sfarsit,cheltuiala_list, undo_list):
    cheltuieli_sterse = []
    for zi in range(zi_inceput, zi_sfarsit+1):
        ziua = str(zi)
        i=0
        while i<len(cheltuiala_list):
            if cheltuiala_list[i]['ziua'] == ziua:
                cheltuiala = cheltuiala_list.pop(i)
                cheltuieli_sterse.append(cheltuiala)
                i=i-1;
            i=i+1
    undo_list.append({'tip': 'stergere', 'elemente': cheltuieli_sterse})
    return cheltuiala_list

def cheltuieli_mai_mari(suma,cheltuiala_list):
    lista_noua=[]
    for i in cheltuiala_list:
        if i['suma']>int(suma):
            lista_noua.append(i)
    return lista_noua

def sume_mai_mici(suma,lista):
    lista_noua = []
    for i in lista:
        if i['suma'] <int(suma):
            lista_noua.append(i)
    return lista_noua

def ziua_sumei_maxime(cheltuiala_list):
    max=0
    ziua=0
    for i in cheltuiala_list:
        if max<i['suma']:
            max=i['suma']
            ziua=i['ziua']
    return ziua

def cheltuieli_din_urma(data,cheltuiala_list):
    lista_noua = []
    for i in cheltuiala_list:
        if int(i['ziua']) < int(data):
            lista_noua.append(i)
    return lista_noua

def stergere_cheltuieli_suma(s,cheltuiala_list, undo_list):
    i = 0
    cheltuieli_sterse = []
    while i < len(cheltuiala_list):
        if cheltuiala_list[i]['suma'] <int(s):
            cheltuiala = cheltuiala_list.pop(i)
            cheltuieli_sterse.append(cheltuiala)
            i = i - 1;
        i = i + 1
    undo_list.append({'tip':'stergere', 'elemente': cheltuieli_sterse})
    return cheltuiala_list

def test1():
    undo_list = []
    cheltuiala_list = []
    adaugare_cheltuieli("20,29,mancare", cheltuiala_list, undo_list)
    adaugare_cheltuieli("10,5,telefon", cheltuiala_list, undo_list)
    adaugare_cheltuieli("40,15,altele", cheltuiala_list, undo_list)
    assert(len(cheltuiala_list) == 3)
    undo(undo_list, cheltuiala_list)
    assert(len(cheltuiala_list) == 2)
    assert(ziua_sumei_maxime(cheltuiala_list) == '20')
    assert(gasire_tip_cheltuiala('telefon', cheltuiala_list) == [{'ziua': "10", 'suma': 5, 'tip': 'telefon'}])
    adaugare_cheltuieli("40,15,altele", cheltuiala_list, undo_list)
    stergere_cheltuiala_interval(10,20, cheltuiala_list, undo_list)
    assert(len(cheltuiala_list) == 1)
    undo(undo_list, cheltuiala_list)
    assert(len(cheltuiala_list) == 3)
    '''
    type=cheltuiala_list[0]['tip']
    ziua=cheltuiala_list[0]['ziua']
    assert(suma_maxima(cheltuiala_list)==cheltuiala_list[0]['ziua'])
    assert(gasire_tip_cheltuiala(type,cheltuiala_list)==cheltuiala_list)
    assert(stergere_cheltuiala_tip(type,cheltuiala_list)==[])
    assert(stergere_cheltuiala_ziua('20',cheltuiala_list)==[])
    '''


cheltuiala_list=[]
cheltuiala=[]
test1()
suma_actualizata=0
data_actualizarii=0
tipul_sumei=0
undo_list=[]
while True:
    print_menu_principal()
    option = input("Introduceti optiune:")
    option = option.strip()
    option = int(option)
    match option:
        case 0:
            exit()
        case 1:
            menu_1()
            functionalitate=input("Introduceti optiune:")
            functionalitate=functionalitate.strip()
            functionalitate=float(functionalitate)
            match functionalitate:
                case 1.1:
                    cheltuiala_str = input('Introduceti detaliile cheltuielii separate print-o virgula:')
                    cheltuiala=adaugare_cheltuieli(cheltuiala_str, cheltuiala_list, undo_list)
                    data_actualizarii = cheltuiala['ziua']
                    tipul_sumei = cheltuiala['tip']
                    print("Cheltuiala a fost adaugata cu succes!")
                case 1.2:
                    sume_tip=gasire_tip_cheltuiala(tipul_sumei,cheltuiala_list)
                    suma_actualizata=0
                    for i in sume_tip:
                        suma_actualizata+=i['suma']
                    print("Suma de",suma_actualizata,"pentru",tipul_sumei,"a fost actualizata ultima data pentru data de",data_actualizarii)
                case 1.3:
                    print(cheltuiala_list)
        case 2:
            menu_2()
            functionalitate = input("Introduceti optiune:")
            functionalitate = functionalitate.strip()
            functionalitate = float(functionalitate)
            match functionalitate:
                case 2.1:
                    ziua=input("Introduceti ziua:")
                    stergere_cheltuiala_ziua(ziua, cheltuiala_list, undo_list)
                    print(cheltuiala_list)
                case 2.2:
                    inceput=input("Introduceti ziua de inceput:")
                    inceput=int(inceput)
                    final=input("Introduceti ziua de final:")
                    final=int(final)
                    stergere_cheltuiala_interval(inceput, final, cheltuiala_list, undo_list)
                    print(cheltuiala_list)
                case 2.3:
                    type = input("Introduceti tipul:")
                    stergere_cheltuiala_tip(type, cheltuiala_list, undo_list)
                    print(cheltuiala_list)

        case 3:
            menu_3()
            functionalitate = input("Introduceti optiune:")
            functionalitate = functionalitate.strip()
            functionalitate = float(functionalitate)
            match functionalitate:
                case 3.1:
                    suma=input("Introduceti suma data:")
                    if len(cheltuieli_mai_mari(suma,cheltuiala_list))>0:
                        print (cheltuieli_mai_mari(suma,cheltuiala_list))
                    else:
                        print("Nu exista cheltuieri mai mari decat suma data")
                case 3.2:
                    data=input("Introduceti o zi din luna:")
                    suma=input("Introduceti o suma:")
                    cheltuieli=sume_mai_mici(suma,cheltuieli_din_urma(data,cheltuiala_list))
                    if len(cheltuieli)>0:
                        print (cheltuieli)
                    else:
                        print("Nu exista astfel de cheltuieli")

                case 3.3:
                    type=input("Introduceti tipul de cheltuaiala pe care sa o afisam: ")
                    print(gasire_tip_cheltuiala(type,cheltuiala_list))


        case 4:
            menu_4()
            functionalitate = input("Introduceti optiune:")
            functionalitate = functionalitate.strip()
            functionalitate = float(functionalitate)
            match functionalitate:
                case 4.1:
                    type=input("Introduceti tipul cheltuielii:")
                    suma_tip=gasire_tip_cheltuiala(type,cheltuiala_list)
                    sum=0
                    for i in suma_tip:
                        sum=i['suma']+sum
                    print("Suma cheltuita pentru",type,"este",sum)
                case 4.2:
                    print("Suma maxima a fost cheltuita in ziua",ziua_sumei_maxime(cheltuiala_list))
                case 4.3:
                    suma_cautata=int(input("Introduceti suma dorita pentru cautare:"))
                    ok=0
                    for i in cheltuiala_list:
                        if i['suma']==suma_cautata:
                            print(i)
                            ok=1
                       # print(i)
                    if ok==0:
                        print("Nu exista o astfel de suma")
                case 4.4:
                    if(len(gasire_tip_cheltuiala('mancare',cheltuiala_list))>0):
                        print("Cheltuielile pentru mancare sunt:")
                        print(gasire_tip_cheltuiala('mancare',cheltuiala_list))
                    if (len(gasire_tip_cheltuiala('altele', cheltuiala_list)) > 0):
                        print("Cheltuielile pentru altele sunt:")
                        print(gasire_tip_cheltuiala('altele', cheltuiala_list))
                    if (len(gasire_tip_cheltuiala('imbracaminte', cheltuiala_list)) > 0):
                        print("Cheltuielile pentru imbracaminte sunt:")
                        print(gasire_tip_cheltuiala('imbracaminte', cheltuiala_list))
                    if (len(gasire_tip_cheltuiala('intretinere', cheltuiala_list)) > 0):
                        print("Cheltuielile pentru intretinere sunt:")
                        print(gasire_tip_cheltuiala('intretinere', cheltuiala_list))
                    if (len(gasire_tip_cheltuiala('telefon', cheltuiala_list)) > 0):
                        print("Cheltuielile pentru telefon sunt:")
                        print(gasire_tip_cheltuiala('telefon', cheltuiala_list))



        case 5:
            menu_5()
            functionalitate = input("Introduceti optiune:")
            functionalitate = functionalitate.strip()
            functionalitate = float(functionalitate)
            match functionalitate:
                case 5.1:
                    type=input("Introduceti tipul:")
                    stergere_cheltuiala_tip(type,cheltuiala_list, undo_list)
                    print(cheltuiala_list)
                case 5.2:
                    s=input("Introduceti suma:")
                    stergere_cheltuieli_suma(s,cheltuiala_list, undo_list)
                    print(cheltuiala_list)
        case 6:
            undo(undo_list,cheltuiala_list)
            #print(cheltuiala_list)