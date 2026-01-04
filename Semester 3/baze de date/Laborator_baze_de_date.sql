CREATE DATABASE Lant_de_cofetarii;
USE  Lant_de_cofetarii;

CREATE TABLE Cofetarie(
	cod_cofetarie INT IDENTITY(1,1) PRIMARY KEY,
	nume_administrator	VARCHAR(200),
	prenume_administrator VARCHAR(200),
	numar_de_telefon INT
);

CREATE TABLE Adresa(
	cod_adresa INT IDENTITY(1,1) PRIMARY KEY,
	oras VARCHAR(200),
	strada VARCHAR(200),
	numar_strada INT,
	cod_postal INT,
	cod_cofetarie INT UNIQUE,
	FOREIGN KEY (cod_cofetarie) REFERENCES Cofetarie(cod_cofetarie)
);

--use Lant_de_cofetarii;
--drop table Adresa;

CREATE TABLE Patiser(
	cod_patiser INT IDENTITY(1,1) PRIMARY KEY,
	nume_patiser VARCHAR(200),
	prenume_patiser VARCHAR(200),
	varsta INT,
	cod_cofetarie INT,
	FOREIGN KEY (cod_cofetarie) REFERENCES Cofetarie(cod_cofetarie)
);

CREATE TABLE Prajitura(
	cod_prajitura INT IDENTITY(1,1) PRIMARY KEY,
	denumire VARCHAR(200),
	gramaj INT,
	pret INT
);

CREATE TABLE Cantitate(
	cod_patiser INT,
	cod_prajitura INT,
	porti INT,
	PRIMARY KEY(cod_patiser,cod_prajitura),
	FOREIGN KEY (cod_patiser) REFERENCES Patiser(cod_patiser),
	FOREIGN KEY (cod_prajitura) REFERENCES Prajitura(cod_prajitura)
);

CREATE TABLE Furnizor(
	cod_furnizor INT IDENTITY(1,1) PRIMARY KEY,
	nume_furnizor VARCHAR(200),
	prenume_furnizor VARCHAR(200),
	data_sosirii DATE
);

CREATE TABLE Ingredient(
	cod_ingredient INT IDENTITY(1,1) PRIMARY KEY,
	gramaj INT,
	cod_furnizor INT,
	FOREIGN KEY (cod_furnizor) REFERENCES Furnizor(cod_furnizor)
);


CREATE TABLE Ingredient_Prajitura(
	cod_prajitura INT,
	cod_ingredient INT,
	cantitate INT,
	PRIMARY KEY(cod_prajitura,cod_ingredient),
	FOREIGN KEY (cod_prajitura) REFERENCES Prajitura(cod_prajitura),
	FOREIGN KEY (cod_ingredient) REFERENCES Ingredient(cod_ingredient)
);

 CREATE TABLE Client(
	cod_client INT IDENTITY(1,1) PRIMARY KEY,
	nume_client VARCHAR(200),
	prenume_client VARCHAR(200)
 );

 CREATE TABLE Comanda(
	numar_comanda INT IDENTITY(100,1) PRIMARY KEY, 
	cod_cofetarie INT,
	cod_client INT,
	FOREIGN KEY (cod_client) REFERENCES Client(cod_client),
	FOREIGN KEY (cod_cofetarie) REFERENCES Cofetarie(cod_cofetarie)
);

CREATE TABLE Comenzi_Prajituri(
	cod_prajitura INT,
	numar_comanda INT,
	cantitate INT,
	FOREIGN KEY (cod_prajitura) REFERENCES Prajitura(cod_prajitura),
	FOREIGN KEY (numar_comanda) REFERENCES Comanda(numar_comanda)
);

