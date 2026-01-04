USE Lant_de_cofetarii;

--INSERAM IN Table TABLES
GO
INSERT INTO Tables(Name) VALUES
('Client'),
('Comanda'),
('Cantitate');

--- un view ce conţine o comandă SELECT pe o tabelă
GO
CREATE OR ALTER VIEW v_Clienti AS
SELECT cod_client,nume_client,prenume_client
FROM Client;


-- un view ce conţine o comandă SELECT aplicată pe cel puţin două tabele
GO
CREATE OR ALTER VIEW v_Comenzi AS
SELECT c.numar_comanda, cl.nume_client, cl.prenume_client, cof.nume_administrator
FROM Comanda c
JOIN Client cl ON c.cod_client = cl.cod_client
JOIN Cofetarie cof ON c.cod_cofetarie = cof.cod_cofetarie;

--un view ce conţine o comandă SELECT aplicată pe cel puţin două tabele şi
--având o clauză GROUP BY.
GO
CREATE OR ALTER VIEW v_CantitatePrajituri AS
SELECT p.nume_patiser, p.prenume_patiser, SUM(c.porti) AS total_portii
FROM Cantitate c
JOIN Patiser p ON c.cod_patiser = p.cod_patiser
GROUP BY p.nume_patiser, p.prenume_patiser;


--INSERAM IN Table VIEWS
GO
INSERT INTO Views VALUES
('v_Cofetarii'),
('v_Comenzi'),
('v_VanzariPrajituri');

--INSERAM IN Table Tests (avem 6 teste)
GO
INSERT INTO Tests(Name) VALUES
('Insert10'),
('Insert100'),
('Insert1000'),
('Delete10'),
('Delete100'),
('Delete1000'),
('Eval');


--Inseram in TestTables(pentru fiecare test pe fiecare tabela)
-- 6 cazuri de test , 3 tabele => 18 cazuri de test
GO
INSERT INTO TestTables VALUES
(1, 1, 10, 1),
(2, 1, 100, 1),
(3, 1, 1000, 1),
(1, 2, 10, 2),
(2, 2, 100, 2),
(3, 2 ,1000, 2),
(1, 3, 10, 3),
(2, 3, 100, 3),
(3, 3, 1000, 3),
(4, 1, 10, 3),
(5, 1, 100, 3),
(6, 1, 1000, 3),
(4, 2, 10, 2),
(5, 2, 100, 2),
(6, 2 ,1000, 2),
(4, 3, 10, 1),
(5, 3, 100, 1),
(6, 3, 1000, 1);

--Inseram in TestViews cate un test EVAL pentru cele 3 view-uri
GO
INSERT INTO TestViews VALUES
(7,1),
(7,2),
(7,3);
--Inseram in Client
GO
CREATE OR ALTER PROCEDURE InsertClients (@rows int)
AS
BEGIN
    DECLARE @cod_client int
    DECLARE @nume_client VARCHAR(50)
    DECLARE @prenume_client VARCHAR(50)
    DECLARE @i int
    DECLARE @lastId int
    DECLARE @possible_names TABLE (name VARCHAR(50))
    INSERT INTO @possible_names (name)
    VALUES ('Ion'), ('Maria'), ('Vasile'), ('Elena'), ('Andrei'), ('Ana'), ('Mihai'), ('Gabriela'), ('George'), ('Ioana')

    SET @cod_client = 100
    SET @i = 1
	
    WHILE @i <= @rows
    BEGIN
        SET @cod_client = 100 + @i

        SELECT TOP 1 @lastId = C.cod_client FROM dbo.Client C 
        ORDER BY C.cod_client DESC
        --Verificam sa fe id ul unic
        IF @lastId > 100
            SET @cod_client = @lastId + 1
        
        -- Alegem un nume și un prenume aleatorii din lista posibilă
        SELECT TOP 1 @nume_client = name 
        FROM @possible_names 
        ORDER BY NEWID()

        SELECT TOP 1 @prenume_client = name 
        FROM @possible_names 
        ORDER BY NEWID()

        -- Inseram în tabelul Client
        SET IDENTITY_INSERT Client ON
        INSERT INTO Client(cod_client, nume_client, prenume_client) 
        VALUES (@cod_client, @nume_client, @prenume_client)
        SET IDENTITY_INSERT Client OFF

        SET @i = @i + 1
    END
END;

--Stergem din tabelul Client
GO
CREATE OR ALTER PROCEDURE DeleteClients(@rows int)
AS
BEGIN
	DECLARE @cod_client INT
    DECLARE @i INT
    DECLARE @lastId INT
	SET @cod_client=100
	SET @i=@rows

	WHILE @i > 0
	BEGIN
		SET @cod_client=100+@i

		SELECT TOP 1 @lastId = C.cod_client FROM dbo.Client C 
        ORDER BY C.cod_client DESC
		IF @lastId > @cod_client
			SET @cod_client = @lastId
		DELETE FROM Client WHERE Client.cod_client=@cod_client
		SET @i = @i -1
	END
END;

--Inseram in comanda
GO
CREATE OR ALTER PROCEDURE InsertComenzi (@rows int)
AS
BEGIN
	DECLARE @numar_comanda int
	DECLARE @cod_cofetarie int
	DECLARE @cod_client int
	DECLARE @i int
	DECLARE @lastID int
    
	DECLARE @maxCofetarieID INT
    DECLARE @maxClientID INT
	SELECT @maxCofetarieID = MAX(cod_cofetarie) FROM dbo.Cofetarie
    SELECT @maxClientID = MAX(cod_client) FROM dbo.Client
	IF @maxCofetarieID IS NULL
    BEGIN
        PRINT 'Nu există nicio cofetarire în tabelul Cofetarie!'
        RETURN
    END

    IF @maxClientID IS NULL
    BEGIN
        PRINT 'Nu există niciun client în tabelul Client!'
        RETURN
    END

	SET @numar_comanda=100
	SET @cod_cofetarie=1
	SET @cod_client=1
	SET @i=1

	WHILE @i<@rows
	BEGIN
		SET @numar_comanda=@numar_comanda+@i
		SELECT TOP 1 @lastID=C.numar_comanda FROM dbo.Comanda C 
		ORDER BY C.numar_comanda DESC
		IF @lastID>100
			SET @numar_comanda=@lastID +1
		
		-- Alegem un cod aleatoriu pentru cofetarie șs client
		SET @cod_cofetarie = 1 + (ABS(CHECKSUM(NEWID())) % @maxCofetarieID)
        SET @cod_client = 1 + (ABS(CHECKSUM(NEWID())) % @maxClientID)

        -- Asiguram ca ID-urile sunt valide
        IF EXISTS (SELECT 1 FROM dbo.Cofetarie WHERE cod_cofetarie = @cod_cofetarie)
            AND EXISTS (SELECT 1 FROM dbo.Client WHERE cod_client = @cod_client)
        BEGIN
            SET IDENTITY_INSERT Comanda ON
            INSERT INTO Comanda(numar_comanda, cod_cofetarie, cod_client) 
            VALUES (@numar_comanda, @cod_cofetarie, @cod_client)
            SET IDENTITY_INSERT Comanda OFF
        END
        ELSE
        BEGIN
            PRINT 'ID-urile Cofetarie sau Client nu sunt valide!'
        END
		SET @i = @i + 1
	END
END;

--STERGEM DIN COMANDA
GO
CREATE OR ALTER PROCEDURE DeleteComenzi(@rows int)
AS
BEGIN
	DECLARE @numar_comanda int
	DECLARE @i int
	DECLARE @lastId int

	SET @numar_comanda=100
	SET @i=@rows

	WHILE @i>0
	BEGIN
		SET @numar_comanda=100+@i
		SELECT TOP 1 @lastId = C.numar_comanda FROM dbo.Comanda C ORDER BY C.numar_comanda DESC
		IF @lastId >@numar_comanda
			SET @numar_comanda=@lastId
		DELETE FROM Comanda WHERE Comanda.numar_comanda=@numar_comanda
		SET @i=@i-1
	END
END;


--FUNCTII OPTIONALE---------------------------------------------------------------------------------------------------
GO
CREATE OR ALTER PROCEDURE InsertPatiseri(@rows INT)
AS
BEGIN
	DECLARE @cod_patiser INT
    DECLARE @nume_patiser VARCHAR(50)
    DECLARE @prenume_patiser VARCHAR(50)
	DECLARE @varsta INT
	DECLARE @cod_cofetarie INT
	DECLARE @i INT
	DECLARE @lastId INT
	DECLARE @possible_names TABLE (name VARCHAR(50))
    INSERT INTO @possible_names (name)
    VALUES ('Ion'), ('Maria'), ('Vasile'), ('Elena'), ('Andrei'), ('Ana'), ('Mihai'), ('Gabriela'), ('George'), ('Ioana')

    SET @cod_patiser = 100
    SET @i = 1
	SET @cod_cofetarie=2
	
    WHILE @i <= @rows
    BEGIN
        SET @cod_patiser = 100 + @i

        SELECT TOP 1 @lastId =P.cod_patiser FROM dbo.Patiser P
        ORDER BY P.cod_patiser DESC
        --Verificam sa fe id ul unic
        IF @lastId > 100
            SET @cod_patiser = @lastId + 1
        
        -- Alegem un nume și un prenume aleatorii din lista posibilă
        SELECT TOP 1 @nume_patiser = name 
        FROM @possible_names 
        ORDER BY NEWID()

        SELECT TOP 1 @prenume_patiser = name 
        FROM @possible_names 
        ORDER BY NEWID()

		SET @varsta = ABS(CHECKSUM(NEWID())) % 48 + 18  -- Varsta intre 18 și 65

        SET IDENTITY_INSERT Patiser ON
        INSERT INTO Patiser(cod_patiser,nume_patiser,prenume_patiser,varsta,cod_cofetarie) 
        VALUES (@cod_patiser,@nume_patiser,@prenume_patiser,@varsta,@cod_cofetarie)
        SET IDENTITY_INSERT Patiser OFF

        SET @i = @i + 1
    END
END;

GO
CREATE OR ALTER PROCEDURE InsertPrajituri(@rows INT)
AS
BEGIN
	DECLARE @cod_prajitura INT
    DECLARE @denumire VARCHAR(50)
    DECLARE @gramaj INT
	DECLARE @pret INT
	DECLARE @i INT
	DECLARE @lastId INT
	DECLARE @possible_cakes TABLE (cake_name VARCHAR(50))

	INSERT INTO @possible_cakes (cake_name)
    VALUES ('Tort de ciocolată'), ('Prajitura cu mere'), ('Chec cu nuci'), ('Ecler'), ('Prajitura cu crema de vanilie'), 
           ('Prajitura cu cirese'), ('Tarta cu fructe'), ('Prajitura cu crema de ciocolata'), ('Prajitura cu alune'), 
           ('Prajitura cu zmeura')

	SET @cod_prajitura = 100
    SET @i = 1
	
    WHILE @i <= @rows
    BEGIN
        SET @cod_prajitura = 100 + @i

        SELECT TOP 1 @lastId =P.cod_prajitura FROM dbo.Prajitura P
        ORDER BY P.cod_prajitura DESC
        --Verificam sa fie id ul unic
        IF @lastId > 100
            SET @cod_prajitura = @lastId + 1
        
        -- Alegem o denumire de prajitura lista posibilă
        SELECT TOP 1 @denumire = cake_name 
        FROM @possible_cakes 
        ORDER BY NEWID()

		--gramaj aleatoriu intre 80g și 2.5kg
		SET @gramaj = ABS(CHECKSUM(NEWID())) % (2500 - 80 + 1) + 80
		--pretul aleatoriu intre 10 si 500
		SET @pret = ABS(CHECKSUM(NEWID())) % (500 - 10 + 1) + 10
		
        SET IDENTITY_INSERT Prajitura ON
        INSERT INTO Prajitura(cod_prajitura,denumire,gramaj,pret) 
        VALUES (@cod_prajitura,@denumire,@gramaj,@pret) 
		SET IDENTITY_INSERT Prajitura OFF

        SET @i = @i + 1
    END

END;

GO
CREATE OR ALTER PROCEDURE DeletePatiseri(@rows INT)
AS
BEGIN
	DECLARE @cod_patiser INT
    DECLARE @i INT
    DECLARE @lastId INT
	SET @cod_patiser=100
	SET @i=@rows

	WHILE @i > 0
	BEGIN
		SET @cod_patiser=100+@i

		SELECT TOP 1 @lastId = P.cod_patiser FROM dbo.Patiser P 
        ORDER BY P.cod_patiser DESC
		IF @lastId > @cod_patiser
			SET @cod_patiser= @lastId
		DELETE FROM Patiser WHERE Patiser.cod_patiser=@cod_patiser
		SET @i = @i -1
	END
END;

GO
CREATE OR ALTER PROCEDURE DeletePrajituri(@rows INT)
AS
BEGIN
	DECLARE @cod_prajitura INT
    DECLARE @i INT
    DECLARE @lastId INT
	SET @cod_prajitura=100
	SET @i=@rows

	WHILE @i > 0
	BEGIN
		SET @cod_prajitura=100+@i

		SELECT TOP 1 @lastId = P.cod_prajitura FROM dbo.Prajitura P 
        ORDER BY P.cod_prajitura DESC
		IF @lastId > @cod_prajitura
			SET @cod_prajitura= @lastId
		DELETE FROM Prajitura WHERE Prajitura.cod_prajitura=@cod_prajitura
		SET @i = @i -1
	END
END;
---------------------------------------------------------------------------------------------------------------------

--Inseram in Cantitate
GO
CREATE OR ALTER PROCEDURE InsertCantitate (@rows INT)
AS
BEGIN
    DECLARE @i INT
    SET @i = 1
    EXEC InsertPatiseri @rows
    EXEC InsertPrajituri @rows

    DECLARE @cod_patiser INT
    DECLARE @cod_prajitura INT
    DECLARE @portii INT

    WHILE @i <= @rows
    BEGIN
        SELECT TOP 1 @cod_patiser = cod_patiser
        FROM Patiser
        ORDER BY NEWID(); 

        SELECT TOP 1 @cod_prajitura = cod_prajitura
        FROM Prajitura
        ORDER BY NEWID();

        SET @portii = 1 + (ABS(CHECKSUM(NEWID())) % 20); 

        INSERT INTO Cantitate (cod_patiser, cod_prajitura, porti)
        VALUES (@cod_patiser, @cod_prajitura, @portii);

        SET @i = @i + 1
    END
END;


--Stergem in Cantitate
GO
CREATE OR ALTER PROCEDURE DeleteCantitate (@rows INT)
AS
BEGIN
    DECLARE @i INT
    DECLARE @cod_patiser INT
    DECLARE @cod_prajitura INT

    SET @i = @rows
    DECLARE cursorCantitate CURSOR SCROLL FOR
    SELECT cod_patiser, cod_prajitura
    FROM Cantitate;

    OPEN cursorCantitate;
    FETCH LAST FROM cursorCantitate INTO @cod_patiser, @cod_prajitura;

    WHILE @i > 0 AND @@FETCH_STATUS = 0
    BEGIN
        DELETE FROM Cantitate
        WHERE cod_patiser = @cod_patiser AND cod_prajitura = @cod_prajitura;

        DELETE FROM Patiser WHERE cod_patiser = @cod_patiser
            AND NOT EXISTS (SELECT 1 FROM Cantitate WHERE cod_patiser = @cod_patiser);

        FETCH PRIOR FROM cursorCantitate INTO @cod_patiser, @cod_prajitura;
        SET @i = @i - 1
    END

    CLOSE cursorCantitate;
    DEALLOCATE cursorCantitate;
END;


--CREARE TESTE
GO
CREATE OR ALTER PROCEDURE Insert10 (@Table VARCHAR(20))
AS
BEGIN
	IF @Table='Client'
	exec  InsertClients 10
	IF @Table='Comanda'
	exec InsertComenzi 10
	IF @Table='Cantitate'
	exec InsertCantitate 10
	else PRINT 'Invalid name'
END;

GO
CREATE OR ALTER PROCEDURE Insert100 (@Table VARCHAR(20))
AS
BEGIN
	IF @Table='Client'
	exec  InsertClients 100
	IF @Table='Comanda'
	exec InsertComenzi 100
	IF @Table='Cantitate'
	exec InsertCantitate 100
	else PRINT 'Invalid name'
END;

GO
CREATE OR ALTER PROCEDURE Insert1000 (@Table VARCHAR(20))
AS
BEGIN
	IF @Table='Client'
	exec  InsertClients 1000
	IF @Table='Comanda'
	exec InsertComenzi 1000
	IF @Table='Cantitate'
	exec InsertCantitate 1000
	else PRINT 'Invalid name'
END;

GO
CREATE OR ALTER PROCEDURE Delete10 (@Table VARCHAR(20))
AS
BEGIN
	IF @Table='Client'
	exec  DeleteClients 10
	IF @Table='Comanda'
	exec DeleteComenzi 10
	IF @Table='Cantitate'
	exec DeleteCantitate 10
	else PRINT 'Invalid name'
END;

GO
CREATE OR ALTER PROCEDURE Delete100 (@Table VARCHAR(20))
AS
BEGIN
	IF @Table='Client'
	exec  DeleteClients 100
	IF @Table='Comanda'
	exec DeleteComenzi 100
	IF @Table='Cantitate'
	exec DeleteCantitate 100
	else PRINT 'Invalid name'
END;

GO
CREATE OR ALTER PROCEDURE Delete1000 (@Table VARCHAR(20))
AS
BEGIN
	IF @Table='Client'
	exec  DeleteClients 1000
	IF @Table='Comanda'
	exec DeleteComenzi 1000
	IF @Table='Cantitate'
	exec DeleteCantitate 1000
	else PRINT 'Invalid name'
END;

GO
CREATE OR ALTER PROCEDURE Eval (@View VARCHAR(20))
AS
BEGIN
	IF @View='Client'
	select * from v_Clienti
	IF @View='Comanda'
	select * from v_Comenzi
	IF @View='Cantitate'
	select * from v_CantitatePrajituri
	else 
	PRINT 'Invalid name'
END;

--CREARE FUNCTIA MAIN
GO
CREATE OR ALTER PROCEDURE Main (@Table VARCHAR(20))
AS
BEGIN
	DECLARE @t1 datetime, @t2 datetime, @t3 datetime
	DECLARE @desc NVARCHAR(2000)

	DECLARE @testInsert VARCHAR(20)
	DECLARE @testDelete VARCHAR(20)
	DECLARE @nrRows int
	DECLARE @idTest int

	SET @nrRows = 1000
	SET @testInsert='Insert' + CONVERT(VARCHAR (5),@nrRows)
	SET @testDelete='Delete' + CONVERT(VARCHAR (5),@nrRows)


	if @Table='Client'
		BEGIN
			SET @t1 =GETDATE()
			exec @testInsert Client
			exec @testDelete Client
			SET @t2 =GETDATE()
			exec Eval Client
			SET @t3 =GETDATE()
			SET @desc=N'The tests: '+@testInsert+', '+@testDelete+', and view on '+@Table
			INSERT INTO TestRuns VALUES (@desc,@t1,@t3)
			SELECT TOP 1 @idTest=T.TestRunID FROM dbo.TestRuns T ORDER BY T.TestRunID DESC
			INSERT INTO TestRunTables VALUES (@idTest,1,@t1,@t2)
			INSERT INTO TestRunViews VALUES (@idTest,1,@t2,@t3)
		END
	if @Table='Comanda'
		BEGIN
			SET @t1 =GETDATE()
			exec @testInsert Comanda
			exec @testDelete Comanda
			SET @t2 =GETDATE()
			exec Eval Comanda
			SET @t3 =GETDATE()
			SET @desc=N'The tests '+@testInsert+', '+@testDelete+', and view on '+@Table
			INSERT INTO TestRuns VALUES (@desc,@t1,@t3)
			SELECT TOP 1 @idTest=T.TestRunID FROM dbo.TestRuns T ORDER BY T.TestRunID DESC
			INSERT INTO TestRunTables VALUES (@idTest,3,@t1,@t2)
			INSERT INTO TestRunViews VALUES (@idTest,3,@t2,@t3)
		END
	if @Table='Cantitate'
		BEGIN
			SET @t1 =GETDATE()
			exec @testInsert Cantitate
			exec @testDelete Cantitate
			SET @t2 =GETDATE()
			exec Eval Cantitate
			SET @t3 =GETDATE()
			SET @desc=N'The tests '+@testInsert+', '+@testDelete+', and view on '+@Table
			INSERT INTO TestRuns VALUES (@desc,@t1,@t3)
			SELECT TOP 1 @idTest=T.TestRunID FROM dbo.TestRuns T ORDER BY T.TestRunID DESC
			INSERT INTO TestRunTables VALUES (@idTest,2,@t1,@t2)
			INSERT INTO TestRunViews VALUES (@idTest,2,@t2,@t3)
		END
	ELSE 
	PRINT 'Invalid Table'
END;

GO
CREATE OR ALTER PROCEDURE main1 AS
BEGIN
    DECLARE @tableName VARCHAR(100);

    DECLARE cursorT CURSOR FOR
    SELECT Name FROM Tables;

    OPEN cursorT;

    FETCH NEXT FROM cursorT INTO @tableName;
    WHILE @@FETCH_STATUS = 0
    BEGIN
        EXEC Main @tableName;
        PRINT 'Processing table: ' + @tableName;
        FETCH NEXT FROM cursorT INTO @tableName;
    END;

    CLOSE cursorT;
    DEALLOCATE cursorT;
END;


EXEC main1;

GO
select * from TestRuns;
GO
select * from TestRunTables;
GO
select * from TestRunViews;

GO
DELETE TestRuns
DELETE TestRunTables
DELETE TestRunViews
GO
exec Main 'Client';
exec Main 'Comanda';
exec Main 'Cantitate';