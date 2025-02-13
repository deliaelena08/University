---------------------CERINTA 1 ------------------------------------

CREATE DATABASE Trenuri;
USE Trenuri;
GO
CREATE TABLE Tip(
	id_tip INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
	descriere VARCHAR(100)
);
GO
CREATE TABLE Tren(
	id_tren INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
	nume VARCHAR(100),
	tip INT,
	FOREIGN KEY (tip) REFERENCES Tip(id_tip)
);
GO
CREATE TABLE Ruta(
	id_ruta INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
	nume VARCHAR(100),
	tren INT,
	FOREIGN KEY (tren) REFERENCES Tren(id_tren)
);
GO
CREATE TABLE Statie(
	id_statie INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
	nume VARCHAR(50)
);
GO
CREATE TABLE Statii_Rute(
	ruta INT,
	FOREIGN KEY (ruta) REFERENCES Ruta(id_ruta),
	statie INT,
	FOREIGN KEY (statie) REFERENCES Statie(id_statie),
	PRIMARY KEY(ruta,statie),
	ora_sosirii TIME,
	ora_plecarii TIME
);

INSERT INTO Tip(descriere) VALUES ('Inter_Ragio'),('CFR'),('Ala nou');
Select * From Tip;
INSERT INTO Tren(nume,tip) VALUES ('Orient Express',3),('Iasi-Timisoara',2),('Cluj-Budapesta',1);
INSERT INTO Statie(nume) VALUES ('Gara de Nord'),('Gara centrala'),('Pascani');
INSERT INTO Ruta(nume,tren) VALUES ('Cluj-Napoca:Brasov',1),('Cluj-Napoca:Suceava',2),('Cluj-Napoca:Vaslui',3);
INSERT INTO Statii_Rute(ruta,statie,ora_plecarii,ora_sosirii) VALUES 
(1,1,'08:30:00','20:00:00'),
(1,2,'11:45:00','13:47:20'),
(2,3,'10:00:00','15:30:19'),
(3,2,'11:11:11','06:30:30');

----------------------------CERINTA 2--------------------

GO
CREATE PROCEDURE Station
@id_ruta INT,
@id_statie INT,
@ora_sosirii TIME,
@ora_plecarii TIME
AS
BEGIN
	IF EXISTS(SELECT * FROM Statii_Rute WHERE statie=@id_statie AND ruta=@id_ruta)
	BEGIN
		UPDATE Statii_Rute
		SET ora_plecarii=@ora_plecarii, ora_sosirii=@ora_sosirii WHERE ruta=@id_ruta AND statie=@id_statie;
		PRINT 'S-a modificat o inregistrare'
	END
	ELSE
	BEGIN
		INSERT INTO Statii_Rute(ruta,statie,ora_plecarii,ora_sosirii)
		VALUES (@id_ruta,@id_statie,@ora_plecarii,@ora_sosirii);
		PRINT 'S-a inserat o valoare noua'
	END
END;
EXEC Station 1,3,'10:00:00','11:00:00';

----------------- CERINTA 3 --------------------------------
GO
CREATE VIEW view_rute
AS
    SELECT Ruta.nume
    FROM Ruta
    INNER JOIN Statii_Rute ON Ruta.id_ruta = Statii_Rute.ruta
    GROUP BY Ruta.id_ruta, Ruta.nume
    HAVING COUNT(*) = (SELECT COUNT(*) FROM Statie)

Select * FROM view_rute;