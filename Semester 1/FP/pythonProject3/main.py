from teste import *

test1()
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
            functionalitate = input("Introduceti optiune:")
            functionalitate = functionalitate.strip()
            functionalitate = float(functionalitate)
            match functionalitate:
                case 1.1:
                    introducere_cheltuiala_ui(cheltuiala_list, undo_list)
                case 1.2:
                    actualizare_suma_ui(cheltuiala_list)
                case 1.3:
                    print(cheltuiala_list)
        case 2:
            menu_2()
            functionalitate = input("Introduceti optiune:")
            functionalitate = functionalitate.strip()
            functionalitate = float(functionalitate)
            match functionalitate:
                case 2.1:
                    stergere_ziua_data_ui(cheltuiala_list, undo_list)
                case 2.2:
                    stergere_cheltuiala_interval_ui(cheltuiala_list, undo_list)
                case 2.3:
                    stergere_cheltuiala_tip_ui(cheltuiala_list, undo_list)

        case 3:
            menu_3()
            functionalitate = input("Introduceti optiune:")
            functionalitate = functionalitate.strip()
            functionalitate = float(functionalitate)
            match functionalitate:
                case 3.1:
                    cheltuieli_mai_mari_ui(cheltuiala_list)
                case 3.2:
                    cheltuieli_zi_suma_ui(cheltuiala_list)
                case 3.3:
                    cheltuieli_dupa_tip(cheltuiala_list)

        case 4:
            menu_4()
            functionalitate = input("Introduceti optiune:")
            functionalitate = functionalitate.strip()
            functionalitate = float(functionalitate)
            match functionalitate:
                case 4.1:
                    suma_totala_tip_ui(cheltuiala_list)
                case 4.2:
                    ziua_suma_maxima_ui(cheltuiala_list)
                case 4.3:
                    cheltuiala_cautata_suma_ui(cheltuiala_list)
                case 4.4:
                    toate_cheltuielile_dupa_tip_ui(cheltuiala_list)
        case 5:
            menu_5()
            functionalitate = input("Introduceti optiune:")
            functionalitate = functionalitate.strip()
            functionalitate = float(functionalitate)
            match functionalitate:
                case 5.1:
                    stergere_cheltuiala_tip_ui(cheltuiala_list, undo_list)
                case 5.2:
                    stergere_cheltuieli_sume_mici_ui(cheltuiala_list, undo_list)
        case 6:
            undo(undo_list, cheltuiala_list)