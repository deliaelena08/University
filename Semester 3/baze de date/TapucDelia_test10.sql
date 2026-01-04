CREATE DATABASE Cafenea;
USE Cafenea;
GO
CREATE TABLE Sortimente(
	id_sortiment INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
	denumire VARCHAR(100),
	descriere VARCHAR(100)
);
GO
CREATE TABLE Prajitura(
	id_prajitura INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	denumire VARCHAR(100),
	gramaj INT
);
GO
CREATE TABLE Cafea(
	id_cafea INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
	denumire VARCHAR(100),
	pret INT,
	gramaj INT,
	id_sortiment INT FOREIGN KEY REFERENCES Sortimente(id_sortiment),
	id_prajitura INT FOREIGN KEY REFERENCES Prajitura(id_prajitura)
);
GO
CREATE TABLE Student(
	id_student INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
	nume VARCHAR(100),
	prenume VARCHAR(100),
	gen VARCHAR(100),
	data_nastere DATETIME
);
GO
CREATE TABLE Cafele_consumate(
	id_student INT FOREIGN KEY REFERENCES Student(id_student),
	id_cafea INT FOREIGN KEY REFERENCES Cafea(id_cafea),
	cantitate INT,
	cost INT,
	PRIMARY KEY(id_student,id_cafea)
);

GO
INSERT INTO Sortimente(denumire,descriere) VALUES
('Cappucino','un shot de cafea si 100 ml lapte'),
('Latte Machiatto','2 shot-uri de cafea si 150 ml lapte'),
('Espresso scurt','un shot de cafea'),
('Americano','un shot de cafea si 100 ml apa'),
('Espresso lung','2 shot-uri de cafea si 50 ml lapte');
GO
INSERT INTO Prajitura(denumire,gramaj) VALUES
('Cookie cu ciocolata',80),
('Brownie cu ciocolata neagra',120),
('Baton cu multicereale',60),
('Carrot cake',160),
('Tiramisu',140),
('Profiterol',80);
GO
SELECT * FROM Sortimente;
SELECT * FROM Prajitura;
GO
INSERT INTO Cafea(denumire,pret,gramaj,id_sortiment,id_prajitura) VALUES
('Chococinno',22,120,1,2),
('Happy Latte',29,300,2,5),
('Dark Coffee',19,220,4,1),
('Strong Morning',15,120,3,3);
GO
INSERT INTO Student(nume,prenume,gen,data_nastere) VALUES
('Trifan','Alexandra','feminin','2004-09-15'),
('Toda','Emanuela','feminin','2004-09-09'),
('Trifan','Dacian','masculin','2005-09-20'),
('Tapuc','Adrian','masculin','1974-01-01');
GO
SELECT * FROM Student;
SELECT * FROM Cafea;
GO
INSERT INTO Cafele_consumate(id_cafea,id_student,cantitate,cost) VALUES
(1,1,2,44),
(2,4,4,50),
(3,2,1,29),
(4,3,3,45);
GO
CREATE PROCEDURE


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