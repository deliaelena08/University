Create Database WomanShoes;
Use WomanShoes;
GO
Create table Shop(
	id_shop INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	nume VARCHAR(255),
	oras VARCHAR(255)
);
GO
Create table Woman(
	id_woman INT NOT NULL IDENTITY (1,1) PRIMARY KEY,
	nume VARCHAR(255),
	suma INT
);
GO
Create table ShoeModel(
	id_model INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	nume VARCHAR(100),
	sezon VARCHAR(100)
);
GO
Create table Shoe(
	id_shoe INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	id_model INT FOREIGN KEY REFERENCES ShoeModel(id_model),
	pret INT
);
GO
Create table AvailableShoes(
	id_shop INT FOREIGN KEY REFERENCES Shop(id_shop),
	id_shoe INT FOREIGN KEY REFERENCES Shoe(id_shoe),
	nr_valabile INT,
	PRIMARY KEY(id_shop,id_shoe)
);
GO
Create table ShoeBought(
	id_woman INT FOREIGN KEY REFERENCES Woman(id_woman),
	id_shoe INT FOREIGN KEY REFERENCES Shoe(id_shoe),
	nr_bought INT,
	spent_amount INT,
	PRIMARY KEY (id_woman,id_shoe)
);
GO
INSERT INTO Shop (nume,oras) VALUES
('CCC','Piatra-Neamt'),
('ROMARTA','Gura Humorului'),
('Deichmann','Cluj-Napoca'),
('Magazin','Vaslui');
GO
INSERT INTO Woman (nume,suma) VALUES
('Maricica',100),
('Andreea',1200),
('Ioana',300),
('Cristina',288);
GO
INSERT INTO ShoeModel(nume,sezon) VALUES
('Ghete','Iarna'),
('Cizme','Toamna'),
('Sandale','Vara'),
('Tenisi','Primavara');
GO
SELECT * FROM Shoe;
SELECT * FROM Woman;
INSERT INTO Shoe(id_model,pret) VALUES
(1,320),
(2,460),
(3,210),
(4,190);
GO
INSERT INTO AvailableShoes(id_shoe,id_shop,nr_valabile) VALUES
(1,1,10),
(2,1,7),
(1,2,6),
(3,2,1),
(4,3,9),
(3,4,10);
GO
INSERT INTO ShoeBought(id_woman,id_shoe,nr_bought,spent_amount) VALUES
(1,3,2,640),
(2,2,1,460),
(3,1,5,1500),
(4,4,3,600);
GO
CREATE PROCEDURE nustiu
@id_shoe INT,
@id_shop INT,
@number INT
AS
BEGIN
	INSERT INTO AvailableShoes(id_shoe,id_shop,nr_valabile)
	VALUES (@id_shoe,@id_shop,@number);
END;

SELECT * FROM AvailableShoes;
EXEC nustiu 1,3,5;

GO
CREATE VIEW femeinebune AS
SELECT 
	W.nume AS femeie, 
    S.id_model AS model, 
    SB.nr_bought AS numar_pantofi
FROM Woman W
INNER JOIN ShoeBought SB ON SB.id_woman = W.id_woman
INNER JOIN Shoe S ON SB.id_shoe = S.id_shoe
GROUP BY S.id_model,W.nume,SB.nr_bought
HAVING SB.nr_bought >= 2;

SELECT * FROM femeinebune;
DROP VIEW femeinebune;
GO
CREATE FUNCTION listapapuci(@T INT)
RETURNS @Rezultat TABLE
(
	nume Varchar(100),
	pret INT
)
AS
BEGIN
	INSERT INTO @Rezultat
	SELECT
	ShoeModel.nume AS nume,
	Shoe.pret AS pret
	FROM AvailableShoes
	INNER JOIN Shoe ON AvailableShoes.id_shoe=Shoe.id_shoe
	INNER JOIN ShoeModel ON ShoeModel.id_model=Shoe.id_model
	GROUP BY Shoe.pret,ShoeModel.nume,Shoe.id_shoe
	HAVING (SELECT COUNT (AvailableShoes.id_shop) FROM AvailableShoes WHERE Shoe.id_shoe=AvailableShoes.id_shoe)>=@T
	RETURN;
END;
GO
SELECT * FROM listapapuci(1);