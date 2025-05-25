USE  Lant_de_cofetarii;

INSERT INTO Cofetarie(nume_administrator,prenume_administrator,numar_de_telefon)
VALUES ('Andrei','Pintilescu',0765432182),
	   ('Maria', 'Popescu', 0765123456),
       ('Ion', 'Georgescu', 0722334455),
       ('Elena', 'Ionescu', 0744332211);

INSERT INTO Adresa (oras, strada, numar_strada, cod_postal, cod_cofetarie)
VALUES ('Bucuresti', 'Calea Victoriei', 45, 010062, 1),
       ('Cluj-Napoca', 'Strada Memorandumului', 12, 400114, 2),
       ('Iasi', 'Bulevardul Stefan cel Mare', 23, 700124, 3),
       ('Constanta', 'Strada Mircea cel Batran', 89, 900178, 4);

INSERT INTO Patiser (nume_patiser, prenume_patiser, varsta, cod_cofetarie)
VALUES ('Popescu', 'Ion', 45, 1),
       ('Ionescu', 'Maria', 32, 2),
       ('Georgescu', 'Andrei', 38, 3),
       ('Dumitrescu', 'Elena', 29, 4),
       ('Marinescu', 'Florin', 1, 1),
       ('Vasilescu', 'Ana', 41, 2),
       ('Ciobanu', 'Alexandru', 35, 4);

INSERT INTO Prajitura (denumire, gramaj, pret)
VALUES ('Tort de ciocolată', 250, 30),
       ('Ecler cu vanilie', 120, 15),
       ('Pavlova cu fructe', 200, 25),
       ('Tiramisu', 150, 20),
       ('Cheesecake cu fructe de pădure', 180, 35);

INSERT INTO Prajitura (denumire, gramaj, pret)
VALUES ('Tort cu fructe de padure', 1000, 60),
       ('Ecler cu fistic', 120, 16),
       ('Brownie', 200, 20),
       ('Cookie cu fulgi de ciocolata', 80, 8),
       ('Tort trio chocolate', 1800, 65);

INSERT INTO Cantitate (cod_patiser, cod_prajitura, porti)
VALUES (1, 1, 10),   
       (2, 2, 15),   
       (3, 3, 8),    
       (4, 4, 12),   
       (5, 5, 20),   
       (6, 1, 7),    
       (7, 3, 5);

INSERT INTO Furnizor (nume_furnizor, prenume_furnizor, data_sosirii)
VALUES ('Mihaila', 'Vasile', '2023-05-12'),
       ('Popescu', 'Elena', '2023-06-20'),
       ('Ion', 'Daciana', '2023-07-15');


INSERT INTO Ingredient (gramaj, cod_furnizor, denumire)
VALUES (100, 1, 'Zahar'),  
	   (500, 1, 'Ciocolata neagra'), 
       (2000, 2, 'Faina'),  
       (150, 3, 'Ciocolata ruby');

INSERT INTO Ingredient_Prajitura (cod_prajitura, cod_ingredient, cantitate)
VALUES (1, 1, 50), 
       (2, 2, 100),
       (3, 3, 75); 

INSERT INTO Ingredient_Prajitura (cod_prajitura, cod_ingredient, cantitate)
VALUES (2, 3, 500), 
       (15, 1, 100),
	   (15, 2, 50),
	   (15, 4, 50);
	   
INSERT INTO Client (nume_client, prenume_client)
VALUES ('Radu', 'Gabriel'),
       ('Ionescu', 'Irina'),
       ('Popa', 'Andrei');

INSERT INTO Client (nume_client, prenume_client)
VALUES ('Paramon', 'Marina'),
       ('Tapuc', 'Daniela'),
	   ('Savu', 'Tudor'),
	   ('Gorcea', 'Cosmin'),
       ('Olariu', 'Denis');

INSERT INTO Comanda (cod_cofetarie, cod_client)
VALUES (1, 1),  
       (1, 2),  
       (2, 3); 

INSERT INTO Comanda (cod_cofetarie, cod_client)
VALUES (1, 7),  
       (1, 8),  
       (2, 11),
	   (3,10),
	   (2,8),
	   (3,9); 


INSERT INTO Comenzi_Prajituri (cod_prajitura, numar_comanda, cantitate)
VALUES (1, 100, 3), 
       (2, 101, 5), 
       (3, 102, 2);

INSERT INTO Comenzi_Prajituri (cod_prajitura, numar_comanda, cantitate)
VALUES (13, 111, 2), 
       (15, 111, 2), 
	   (14, 111, 2), 
	   (12, 111, 2), 
       (2, 111, 2);

INSERT INTO Comenzi_Prajituri (cod_prajitura, numar_comanda, cantitate)
VALUES (13, 114, 20);

use Lant_de_cofetarii;
Delete from Prajitura where cod_prajitura>5;