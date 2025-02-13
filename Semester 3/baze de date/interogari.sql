USE  Lant_de_cofetarii;
SELECT * FROM Client;
SELECT * FROM Cofetarie;
SELECT * FROM Comanda;
SELECT * FROM Comenzi_Prajituri;
SELECT * FROM Adresa;
SELECT * FROM Furnizor;
SELECT * FROM Cantitate;
SELECT * FROM Ingredient;
SELECT * FROM Patiser;
SELECT * FROM Prajitura;
SELECT * FROM Ingredient_Prajitura;


-- INTEROGARI CU GROUP BY SI HAVING

--selecteaza cate parajituri din cate cofetarii a facut fiecare patiser
SELECT Cofetarie.nume_administrator, Cofetarie.prenume_administrator, Patiser.nume_patiser, Patiser.prenume_patiser, COUNT(Cantitate.porti) AS numar_prajituri
FROM Patiser
JOIN Cantitate ON Patiser.cod_patiser = Cantitate.cod_patiser
JOIN Cofetarie ON Patiser.cod_cofetarie = Cofetarie.cod_cofetarie
GROUP BY Cofetarie.nume_administrator, Cofetarie.prenume_administrator, Patiser.nume_patiser, Patiser.prenume_patiser;

--selecteaza distinct clienții care au comandat mai mult de 5 prăjituri în total
SELECT DISTINCT Client.nume_client, Client.prenume_client, SUM(Comenzi_Prajituri.cantitate) AS total_prajituri
FROM Client
INNER JOIN Comanda ON Client.cod_client = Comanda.cod_client
INNER JOIN Comenzi_Prajituri ON Comanda.numar_comanda = Comenzi_Prajituri.numar_comanda
GROUP BY Client.nume_client, Client.prenume_client
HAVING SUM(Comenzi_Prajituri.cantitate) > 5;

--Vrem să vedem cofetăriile care au primit mai mult de 2 comenzi de prăjituri.
SELECT Cofetarie.nume_administrator, Cofetarie.prenume_administrator, COUNT(Comanda.numar_comanda) AS numar_comenzi
FROM Cofetarie
INNER JOIN Comanda ON Cofetarie.cod_cofetarie = Comanda.cod_cofetarie
GROUP BY Cofetarie.nume_administrator, Cofetarie.prenume_administrator
HAVING COUNT(Comanda.numar_comanda) > 2;


-- 7 interogari ce extrag informatii din MAI MULT DE 2 TABELE

--extrag informațiiLE despre clienți, comenzile lor și prăjiturile pe care le-au comandat
SELECT DISTINCT Client.nume_client, Client.prenume_client, Prajitura.denumire, Comenzi_Prajituri.cantitate
FROM Client
JOIN Comanda ON Client.cod_client = Comanda.cod_client
JOIN Comenzi_Prajituri ON Comanda.numar_comanda = Comenzi_Prajituri.numar_comanda
JOIN Prajitura ON Comenzi_Prajituri.cod_prajitura = Prajitura.cod_prajitura;

--aflam cantitatea pe care furnizorul le aduce,cat aduce daca e intre 99 and 999,cat se foloseste si la ce prajitura
SELECT Furnizor.nume_furnizor,Ingredient.denumire,Ingredient.gramaj,Ingredient_Prajitura.cantitate,Prajitura.denumire
FROM Ingredient
JOIN Furnizor ON Ingredient.cod_furnizor=Furnizor.cod_furnizor
JOIN Ingredient_Prajitura ON Ingredient_Prajitura.cod_ingredient=Ingredient.cod_ingredient
JOIN Prajitura ON Prajitura.cod_prajitura=Ingredient_Prajitura.cod_prajitura
WHERE Ingredient.gramaj BETWEEN 99 AND 999
ORDER BY Ingredient.gramaj ;

--Pentru PRIMII 7 patiseri ce ingredient foloseste si cantitatea necesara
SELECT TOP 7 Patiser.nume_patiser, Prajitura.denumire AS prajitura, Ingredient.denumire AS ingredient
FROM Patiser
JOIN Cantitate ON Patiser.cod_patiser = Cantitate.cod_patiser
JOIN Prajitura ON Cantitate.cod_prajitura = Prajitura.cod_prajitura
JOIN Ingredient_Prajitura ON Prajitura.cod_prajitura = Ingredient_Prajitura.cod_prajitura
JOIN Ingredient ON Ingredient_Prajitura.cod_ingredient = Ingredient.cod_ingredient;

--Afișarea administratorilor de cofetărie cu prăjiturilor comandate care incep cu T sau C  și cantitățile comandate
SELECT Cofetarie.nume_administrator, Prajitura.denumire, Comenzi_Prajituri.cantitate
FROM Cofetarie
JOIN Comanda ON Cofetarie.cod_cofetarie = Comanda.cod_cofetarie
JOIN Comenzi_Prajituri ON Comanda.numar_comanda = Comenzi_Prajituri.numar_comanda
JOIN Prajitura ON Comenzi_Prajituri.cod_prajitura = Prajitura.cod_prajitura
WHERE Prajitura.denumire LIKE '[CT]%';

--Afișarea prăjiturilor comandate în cofetăriile dintr-un anumit oraș (de exemplu, "Cluj-Napoca") și cantitățile comandate
SELECT Prajitura.denumire, Comenzi_Prajituri.cantitate, Adresa.oras
FROM Prajitura
JOIN Comenzi_Prajituri ON Prajitura.cod_prajitura = Comenzi_Prajituri.cod_prajitura
JOIN Comanda ON Comenzi_Prajituri.numar_comanda = Comanda.numar_comanda
JOIN Cofetarie ON Comanda.cod_cofetarie = Cofetarie.cod_cofetarie
JOIN Adresa ON Cofetarie.cod_cofetarie = Adresa.cod_cofetarie
WHERE Adresa.oras = 'Cluj-Napoca';


--2 interogări pe tabele alfate în RELATIE DE M-N.

--Afișarea prăjiturilor care conțin ciocolata neagra si cat folosesc
SELECT Prajitura.denumire,Ingredient_Prajitura.cantitate
FROM Prajitura
JOIN Ingredient_Prajitura ON Prajitura.cod_prajitura = Ingredient_Prajitura.cod_prajitura
JOIN Ingredient ON Ingredient_Prajitura.cod_ingredient = Ingredient.cod_ingredient
WHERE Ingredient.denumire = 'Ciocolata neagra';

--Afișarea numelui și prenumelui patiserilor care au produs mai mult de 10 porții de prăjituri și denumirile prăjiturilor produse
SELECT Patiser.nume_patiser, Patiser.prenume_patiser, Prajitura.denumire, Cantitate.porti
FROM Patiser
INNER JOIN Cantitate ON Patiser.cod_patiser = Cantitate.cod_patiser
INNER JOIN Prajitura ON Cantitate.cod_prajitura = Prajitura.cod_prajitura
WHERE Cantitate.porti > 10;
