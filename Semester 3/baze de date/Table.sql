CREATE DATABASE Campionat_de_table;
USE Campionat_de_table;
CREATE TABLE Angajat(
	id_angajat INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	nume VARCHAR(250),
	nr_contact INT
);
GO
CREATE TABLE Masa(
	id_masa INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	culoare VARCHAR(255)
);
GO
CREATE TABLE Meci(
	id_meci INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
	id_jucator1 INT FOREIGN KEY REFERENCES Angajat(id_angajat),
	id_jucator2 INT FOREIGN KEY REFERENCES Angajat(id_angajat),
	id_masa INT FOREIGN KEY REFERENCES Masa(id_masa),
	data_meciului DATETIME
);
GO
CREATE TABLE Mutare(
	id_mutare INT IDENTITY NOT NULL PRIMARY KEY,
	valoare INT,
	piesa_mutata_de_la VARCHAR (100),
	piesa_mutata_la VARCHAR(100),
	id_jucator INT FOREIGN KEY REFERENCES Angajat(id_angajat),
	id_meci INT FOREIGN KEY REFERENCES Meci(id_meci)
);
GO
CREATE TABLE Spectator(
	id_spectator INT NOT NULL FOREIGN KEY REFERENCES Angajat(id_angajat),
	id_meci INT NOT NULL FOREIGN KEY REFERENCES Meci(id_meci),
	PRIMARY KEY(id_spectator,id_meci)
);
GO
INSERT INTO Angajat(nume,nr_contact) VALUES
('Andrei Muresan',0719890728),
('Victoria Lipan',0711232323),
('Andone Isabela',0755253199),
('Popovici Marian',0743987201),
('Cristian Andrei',0788654390),
('Ana Maria',0789654431);
GO
INSERT INTO Masa(culoare) VALUES
('Maro'),
('Negru'),
('Indigo');
GO
INSERT INTO Meci(id_jucator1,id_jucator2,id_masa,data_meciului) VALUES
(1,2,1,'2019-09-09'),
(2,3,2,'2019-09-10'),
(1,3,3,'2019-09-17');
GO
INSERT INTO Mutare(valoare,piesa_mutata_de_la,piesa_mutata_la,id_meci,id_jucator) VALUES
(5,'Linia 1','Linia 6',4,1),
(2,'Linia 16','Linia 2',6,1),
(7,'Linia 3','Linia 10',5,2),
(4,'Linia 9','Linia 13',5,3),
(1,'Linia 7','Linia 8',4,2);
GO
INSERT INTO Spectator(id_spectator,id_meci) VALUES
(4,4),
(4,5),
(5,6),
(6,5),
(6,6);

GO
SELECT * FROM Angajat;
SELECT * FROM Meci;

GO
CREATE PROCEDURE premiu
@id_angajat INT
AS
BEGIN
    DECLARE @puncte INT = 0;

    -- 1. Adăugăm 100 de puncte pentru fiecare meci în care angajatul a fost jucător
    SET @puncte = @puncte + (
        SELECT COUNT(*) * 100
        FROM Meci
        WHERE id_jucator1 = @id_angajat OR id_jucator2 = @id_angajat
    );

    -- 2. Adăugăm 10 puncte pentru fiecare meci în care angajatul a fost spectator, dar nu jucător
    SET @puncte = @puncte + (
        SELECT COUNT(*) * 10
        FROM Spectator S
        INNER JOIN Meci M ON S.id_meci = M.id_meci
        WHERE S.id_spectator = @id_angajat
          AND NOT (M.id_jucator1 = @id_angajat OR M.id_jucator2 = @id_angajat)
    );

    -- 3. Scădem 10 puncte pentru fiecare meci în care angajatul nu a fost nici jucător, nici spectator
    SET @puncte = @puncte - (
        SELECT COUNT(*) *10
        FROM Meci
        WHERE id_meci NOT IN (
            SELECT id_meci
            FROM Meci
            WHERE id_jucator1 = @id_angajat OR id_jucator2 = @id_angajat
            UNION
            SELECT id_meci
            FROM Spectator
            WHERE id_spectator = @id_angajat
        )
    );

    -- 4. Asigurăm că premiul nu este mai mic de 0
    IF @puncte < 0
        SET @puncte = 0;

    -- 5. Afișăm premiul total calculat
    PRINT 'Premiul total calculat este: ' + CAST(@puncte AS VARCHAR);
END;
GO
DROP PROCEDURE premiu;
GO
EXEC premiu @id_angajat = 1;

GO
CREATE VIEW Dubluri AS
SELECT 
    A.nume AS jucator, 
    A.nr_contact, 
    COUNT(*) AS numar_dubluri
FROM 
    Mutare M
INNER JOIN 
    Angajat A ON M.id_jucator = A.id_angajat
WHERE 
    M.valoare IN (2, 4, 6, 8, 10, 12)
	-- Dublurile (1+1, 2+2, ..., 6+6)
GROUP BY 
    A.nume, A.nr_contact;

INSERT INTO Mutare(valoare, piesa_mutata_de_la, piesa_mutata_la, id_meci, id_jucator) VALUES
(2, 'Linia 1', 'Linia 2', 4, 1), -- Dublu (1+1) pentru jucător 1
(6, 'Linia 3', 'Linia 4', 4, 1), -- Dublu (3+3) pentru jucător 1
(8, 'Linia 5', 'Linia 6', 5, 2), -- Dublu (4+4) pentru jucător 2
(4, 'Linia 7', 'Linia 8', 6, 3), -- Dublu (2+2) pentru jucător 3
(7, 'Linia 9', 'Linia 10', 6, 3); 

SELECT * FROM Dubluri;
