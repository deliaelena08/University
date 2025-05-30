/*AFLU ORASUL DE PROVENIENTA AL TUTUROR PARTICIPANTILOR LA CONCURSURI: 
where-1*/
SELECT P.Nume,A.Oras FROM Participanti P,Adrese A WHERE P.IdAdresa=A.IdAdresa 

/*AFLU TOATE ABONAMENTELE LA CE DEPARTAMENT SUNT REPARTIZATE: 
where-2, mn-1*/
SELECT A.IdAbonament,D.Nume FROM Abonamente A, Departamente D, AbonamenteDepartamente AD
WHERE A.IdAbonament=AD.IdAbonament AND D.IdDepartament=AD.IdDepartament

/*AFLU TOATE PREMIILE CU VALORI DISTINCTE >1000 LEI, ALATURI DE DEPARTAMENTELE LA CARE SUNT ASIGNATE
where-3, 3tabele-2, distinct-1*/
SELECT DISTINCT P.Valoare,D.Nume FROM Premii P,Departamente D,Concursuri C 
WHERE P.Valoare>=1000 AND P.IdConcurs=C.IdConcurs AND C.IdDepartament=D.IdDepartament

/*AFLU DATA INITIERII TUTUROR ABONAMENTELOR DE LA DEPARTAMENTUL DE ALERGARE
mn-2, where-4*/
SELECT A.DataInitierii FROM Abonamente A, Departamente D, AbonamenteDepartamente AD
WHERE A.IdAbonament=AD.IdAbonament AND D.IdDepartament=AD.IdDepartament AND D.Nume='Alergare'

/*gasesc numarul de participanti din fiecare oras
where-5, group by-1*/
SELECT A.Oras,COUNT(A.Oras) AS nr_participanti FROM Adrese A, Participanti P 
WHERE A.IdAdresa=P.IdAdresa GROUP BY A.Oras

/*GRUPEZ SI NUMAR ANTRENORII DUPA DEPARTAMENTE
----pot sa afisez doar criteriile dupa care grupez----
group by-2
*/
/***********care au asignate minim 2 concursuri*/
SELECT D.Nume, COUNT(D.Nume) AS nr_antrenori FROM Antrenori A, Departamente D, Concursuri C
WHERE A.IdDepartament=D.IdDepartament AND D.IdDepartament=C.IdDepartament GROUP BY D.Nume HAVING COUNT(C.IdConcurs)>2

SELECT D.Nume,COUNT(D.Nume) AS nr_antrenori FROM Antrenori A, Departamente D, Concursuri C
WHERE A.IdDepartament=D.IdDepartament  AND D.IdDepartament=C.IdDepartament GROUP BY D.Nume

/*AFLU CARE ABONATI AU FACUT PLATI MEDII PE ABONAMENTE DE >200 LEI
having-1, group by-3*/
SELECT A.Nume,AVG(Suma) AS plata_medie FROM Plati P, Abonati A WHERE A.IdAbonat=P.IdAbonat GROUP BY A.Nume HAVING AVG(Suma)>200

/*AFLU CONCURSURILE CARE AU PARTICIPANTI DIN MAI MULT DE 2 ADRESE
????cum as face sa fie orase diferite
having-2, 3tabele-3*/
SELECT C.IdConcurs,COUNT(C.IdConcurs) AS nr_adrese_diferite 
FROM Concursuri C, Participanti P, Adrese A 
WHERE C.IdConcurs=P.IdConcurs AND P.IdAdresa=A.IdAdresa GROUP BY C.IdConcurs HAVING COUNT(C.IdConcurs)>=2

/*AFLU ORASELE DISTINCTE AM PARTICIPANTI
distinct-2*/
SELECT DISTINCT A.Oras FROM  Adrese A, Participanti P WHERE A.IdAdresa=P.IdAdresa

/*AFLU ANTRENORII A CATOR EXPERIENTA E DE MAI MULT DE 10 ANI*/
SELECT Nume FROM Antrenori WHERE AniExperienta>=10

/**************************/
/*AFLU DATA INITIERII TUTUROR ABONAMENTELOR DE LA DEPARTAMENTUL DE ALERGARE
mn-2, where-4*/
SELECT A.DataExpirarii,A.DataInitierii,D.Nume FROM Abonamente A, Departamente D, AbonamenteDepartamente AD
WHERE A.IdAbonament=AD.IdAbonament AND D.IdDepartament=AD.IdDepartament AND D.Nume='Seniori'

select * from Participanti
select * from Plati
select * from Abonati
select * from Concursuri

/*concursuri cu premii de*/
SELECT C.IdConcurs FROM Concursuri C, Premii P, Departamente D
WHERE C.IdDepartament=D.IdDepartament AND P.IdConcurs=C.IdConcurs GROUP BY C.IdConcurs HAVING COUNT (C.IdConcurs)>3
