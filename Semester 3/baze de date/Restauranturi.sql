CREATE DATABASE Restaurante;
Use Restaurante;
Go
CREATE TABLE Tip(
	id_tip INT PRIMARY KEY IDENTITY(1,1) NOT NULL,
	nume VARCHAR(100),
	descriere VARCHAR(300)
);
GO
CREATE TABLE Oras(
	id_oras INT PRIMARY KEY IDENTITY(1,1) NOT NULL,
	nume VARCHAR(100)
);
GO
CREATE TABLE Restaurant(
	id_restaurant INT PRIMARY KEY IDENTITY(1,1) NOT NULL,
	adresa VARCHAR(100),
	numar_de_telefon INT,
	id_oras INT FOREIGN KEY REFERENCES Oras(id_oras),
	id_tip INT FOREIGN KEY REFERENCES Tip(id_tip)
);
GO
CREATE TABLE Utilizator( 
	id_utilizator INT PRIMARY KEY NOT NULL IDENTITY(1,1),
	nume VARCHAR(100),
	email VARCHAR(100),
	parola VARCHAR(100)
);
--DROP TABLE Utlizator;
GO
CREATE TABLE Nota(
	nota INT NOT NULL,
	id_utilizator INT FOREIGN KEY REFERENCES Utilizator(id_utilizator),
	id_restaurant INT FOREIGN KEY REFERENCES Restaurant(id_restaurant),
	PRIMARY KEY (id_utilizator,id_restaurant)
);
GO
INSERT INTO Oras(nume) VALUES('Cluj-Napoca'),('Piatra-Neamt'),('Targul-Mures'),('Bacau'),('Timisoara'),('Braila');
SELECT * FROM Oras;
GO
INSERT INTO Tip(nume,descriere) VALUES 
('brunch','doar dimineata si dupa masa deschis'),
('fine-dinning','preparata sofisticate'),
('A la carte','evenimente speciale'),
('cantina','dedicata studentiilor');
SELECT * FROM Tip;
GO
INSERT INTO Restaurant(adresa,numar_de_telefon,id_oras,id_tip) VALUES
('Strada Ciocanitoarei',0766543122,1,1),
('Strada Galati',0788888888,1,2),
('Strada Mihai Viteazu',0755253199,2,3),
('Strada Pupazei',0716523489,4,1);
SELECT * FROM Restaurant;
GO
INSERT INTO Utilizator(nume,email,parola) VALUES 
('Alexandru Coman','alexandrucoman@yahoo.com','alexutzul1234'),
('Maria Ioana','marioara_ilincai@gmail.com','marioara19aprilie'),
('Ceistian Pantiru','pantiru_cristi@romanel.ro','pantirucristi');
SELECT * FROM Utilizator;
GO
INSERT INTO Nota(nota,id_restaurant,id_utilizator) VALUES
(5,4,2),
(10,2,1),
(7,1,3),
(7,1,1),
(9,3,3),
(10,2,2);
SELECT * FROM Nota;

GO
CREATE PROCEDURE Deservire_Nota
@id_restaurant INT,
@id_utilizator INT,
@nota INT
AS
BEGIN
	IF EXISTS(SELECT * FROM Nota WHERE id_restaurant=@id_restaurant AND id_utilizator=@id_utilizator)
	BEGIN
		UPDATE Nota
		SET nota=@nota WHERE id_restaurant=@id_restaurant AND id_utilizator=@id_utilizator;
		PRINT 'S-a modificat o inregistrare'
	END
	ELSE
	BEGIN
		INSERT INTO Nota(nota,id_restaurant,id_utilizator) VALUES
		(@nota,@id_restaurant,@id_utilizator);
		PRINT 'S-a inserat o valoare noua'
	END
END;
EXEC Deservire_Nota 2,1,7;

GO
CREATE FUNCTION Functie(@email VARCHAR(100))
RETURNS @Rezultat TABLE
(
	Tip_Restaurant VARCHAR(100),
    Nume_Restaurant VARCHAR(100),
    Numar_Telefon INT,
    Oras VARCHAR(100),
    Nota INT,
    Nume_Utilizator VARCHAR(100),
    Email_Utilizator VARCHAR(100)
)
AS
BEGIN
	INSERT INTO @Rezultat
    SELECT
        Tip.nume AS Tip_Restaurant,
        Restaurant.adresa AS Nume_Restaurant,
        Restaurant.numar_de_telefon AS Numar_Telefon,
        Oras.nume AS Oras,
        Nota.nota AS Nota,
        Utilizator.nume AS Nume_Utilizator,
        Utilizator.email AS Email_Utilizator
    FROM Nota
    INNER JOIN Restaurant ON Nota.id_restaurant = Restaurant.id_restaurant
    INNER JOIN Oras ON Restaurant.id_oras = Oras.id_oras
    INNER JOIN Tip ON Restaurant.id_tip = Tip.id_tip
    INNER JOIN Utilizator ON Nota.id_utilizator = Utilizator.id_utilizator
    WHERE Utilizator.email = @email;
    RETURN;
END;
GO
SELECT * FROM Functie('alexandrucoman@yahoo.com');
