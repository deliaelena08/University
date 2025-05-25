Use Lant_de_cofetarii;

--validare gramaj
GO
CREATE FUNCTION fn_validare_pret_gramaj (@pret INT, @gramaj INT)
RETURNS BIT
AS
BEGIN
    IF @pret > 0 AND @gramaj > 0
    BEGIN
        RETURN 1; -- Valori valide
    END
    RETURN 0; -- Valori invalide  
END;
--validare pret
GO
CREATE FUNCTION fn_validare_varsta(@varsta INT)
RETURNS BIT
AS
BEGIN
	IF @varsta > 0
	BEGIN
		RETURN 1;
	END
	RETURN 0;
END;
--functie pentru concatenarea numelui complet al clientului
GO
CREATE FUNCTION fn_nume_complet_client (@nume_client VARCHAR(200), @prenume_client VARCHAR(200))
RETURNS VARCHAR(400)
AS
BEGIN
    RETURN TRIM(@nume_client + ' ' + @prenume_client)
END;

--procedura stocata in care adaugam clienti - CREATE
GO
CREATE PROCEDURE sp_client
    @nume_client VARCHAR(200),
    @prenume_client VARCHAR(200),
    @cod_client_cautat INT,
    @prenume_nou VARCHAR(200),
  --  @noOfrows INT,
    @cod_client INT OUTPUT
AS
BEGIN
        -- CREATE: Adaugă un nou client
        INSERT INTO Client (nume_client, prenume_client)
        VALUES (@nume_client, @prenume_client);

        -- Obține ID-ul clientului adăugat
        SET @cod_client = SCOPE_IDENTITY();

        -- READ
        SELECT *
        FROM Client
        WHERE nume_client LIKE 'A%';

        -- UPDATE
        UPDATE Client
        SET prenume_client = @prenume_nou
        WHERE cod_client = @cod_client_cautat;

        -- DELETE
        DELETE FROM Client
        WHERE cod_client = @cod_client;
END;


--Comenzi_Prajituri cu relatie m-n 
GO
CREATE PROCEDURE sp_crud_comanda_prajituri
	 @cantitate INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    -- Variabile pentru date aleatorii
    DECLARE @numar_comanda INT;
    DECLARE @cod_prajitura INT;

    SET @cantitate = ABS(CHECKSUM(NEWID())) % 50 + 1;
    SELECT TOP 1 @numar_comanda = numar_comanda
    FROM Comanda
    ORDER BY NEWID();  -- selecteaza un numar de comandă aleator
    SELECT TOP 1 @cod_prajitura = cod_prajitura
    FROM Prajitura
    ORDER BY NEWID();  -- selecteaza un `cod_prajitura` aleator

    -- CREATE
    INSERT INTO Comenzi_Prajituri (numar_comanda, cod_prajitura, cantitate)
    VALUES (@numar_comanda, @cod_prajitura, @cantitate);

    --READ
    SELECT PR.denumire, CP.cantitate
    FROM Comenzi_Prajituri CP
    JOIN Prajitura PR ON CP.cod_prajitura = PR.cod_prajitura
    WHERE CP.numar_comanda = @numar_comanda;

    -- UPDATE
    UPDATE Comenzi_Prajituri
    SET cantitate = ABS(CHECKSUM(NEWID())) % 50 + 1  -- O nouă cantitate între 1 și 50
    WHERE numar_comanda = @numar_comanda AND cod_prajitura = @cod_prajitura;

    -- DELETE
    DELETE FROM Comenzi_Prajituri
    WHERE numar_comanda = @numar_comanda AND cod_prajitura = @cod_prajitura;
END;

--Prajitura CRUD
GO
CREATE PROCEDURE sp_prajitura
   @denumire VARCHAR(200),
   @gramaj INT,
   @pret INT,
   @pret_nou INT,
   @cod_prajitura_nou INT OUTPUT
AS
BEGIN
	IF dbo.fn_validare_pret_gramaj(@pret, @gramaj) = 1
	BEGIN
    -- CREATE
    INSERT INTO Prajitura(denumire, gramaj, pret)
    VALUES (@denumire,@gramaj, @pret);
	SET @cod_prajitura_nou =SCOPE_IDENTITY();
	--READ
	SELECT * FROM Prajitura WHERE @gramaj<1000;

	--UPDATE
        UPDATE Prajitura
        SET pret = @pret_nou
        WHERE cod_prajitura = @cod_prajitura_nou;

	--DELETE
	DELETE FROM Prajitura WHERE cod_prajitura=@cod_prajitura_nou;
	END;
	ELSE
	BEGIN
		RAISERROR ('Gramajul și prețul trebuie să fie mai mari decât 0.', 16, 1);
	END;
END;


--Crud Cantitate 
GO
CREATE PROCEDURE sp_Cantitate
	@porti INT OUTPUT
AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @cod_prajitura INT;
    DECLARE @cod_patiser INT;

    SET @porti = ABS(CHECKSUM(NEWID())) % 50 + 3;
    SELECT TOP 1 @cod_prajitura = cod_prajitura
    FROM Prajitura
    ORDER BY NEWID(); 
    SELECT TOP 1 @cod_patiser = cod_patiser
    FROM Patiser
    ORDER BY NEWID();  

	--CREATE
	INSERT INTO Cantitate(cod_patiser,cod_prajitura,porti) 
	VALUES (@cod_patiser,@cod_prajitura,@porti);

	--READ
    SELECT P.nume_patiser, PR.denumire, C.porti
    FROM Cantitate C
    JOIN Patiser P ON C.cod_patiser = P.cod_patiser
    JOIN Prajitura PR ON C.cod_prajitura = PR.cod_prajitura;

	--UPDATE
	UPDATE Cantitate
    SET porti = ABS(CHECKSUM(NEWID())) % 50 + 1  
    WHERE cod_patiser = @cod_patiser AND cod_prajitura = @cod_prajitura;

    -- DELETE
    DELETE FROM Cantitate
    WHERE cod_patiser = @cod_patiser AND cod_prajitura = @cod_prajitura;
END;

--CRUD PAtiser
GO
CREATE PROCEDURE sp_patiser
    @nume_patiser VARCHAR(200),
    @prenume_patiser VARCHAR(200),
    @varsta INT,
    @cod_patiser_nou INT OUTPUT
AS
BEGIN
	IF dbo.fn_validare_varsta(@varsta)=1
    BEGIN
    SET NOCOUNT ON;

    -- Variabile pentru date aleatorii
    DECLARE @cod_cofetarie INT;
    DECLARE @cod_patiser INT;
    
    -- Alege aleatoriu un cod_cofetarie din tabelul Cofetarie
    SELECT TOP 1 @cod_cofetarie = cod_cofetarie
    FROM Cofetarie
    ORDER BY NEWID(); 
    
    -- CREATE
    INSERT INTO Patiser (nume_patiser, prenume_patiser, varsta, cod_cofetarie)
    VALUES (@nume_patiser, @prenume_patiser, @varsta, @cod_cofetarie);
    SET @cod_patiser_nou = SCOPE_IDENTITY();

    -- READ
    SELECT * FROM Patiser WHERE varsta < 50;

    -- UPDATE
    
        UPDATE Patiser
        SET varsta = @varsta
        WHERE cod_patiser = @cod_patiser_nou;
    
	-- DELETE
    DELETE FROM Patiser WHERE cod_patiser = @cod_patiser_nou;
	END
    
	ELSE
    BEGIN
        RAISERROR('Vârsta trebuie să fie un număr pozitiv.', 16, 1);
    END
END;

--VIEW COMENZI_PRAJITURI
GO
CREATE VIEW vw_comenzi_prajituri
AS
SELECT 
    C.numar_comanda,
    P.denumire AS denumire_prajitura,
    CP.cantitate
FROM 
    Comenzi_Prajituri CP
JOIN 
    Comanda C ON CP.numar_comanda = C.numar_comanda
JOIN 
    Prajitura P ON CP.cod_prajitura = P.cod_prajitura;
--VIEW CANTITATE
GO
CREATE VIEW vw_cantitate_patiser_prajitura
AS
SELECT 
    P.nume_patiser,
    PR.denumire AS denumire_prajitura,
    C.porti
FROM 
    Cantitate C
JOIN 
    Patiser P ON C.cod_patiser = P.cod_patiser
JOIN 
    Prajitura PR ON C.cod_prajitura = PR.cod_prajitura;

GO
CREATE NONCLUSTERED INDEX idx_comenzi_prajituri 
ON Comenzi_Prajituri(numar_comanda, cod_prajitura);
GO
CREATE NONCLUSTERED INDEX idx_cantitate 
ON Cantitate(cod_patiser, cod_prajitura);
GO
CREATE NONCLUSTERED INDEX idx_patiser_cofetarie_varsta
ON Patiser(cod_cofetarie, varsta);

--sp_crud_comanda_prajituri:
DECLARE @cantitate INT;
EXEC sp_crud_comanda_prajituri @cantitate OUTPUT;
PRINT @cantitate; 

--sp_Cantitate
DECLARE @porti INT;
EXEC sp_Cantitate @porti OUTPUT;
PRINT @porti;


GO
Select * from vw_cantitate_patiser_prajitura;
Select * from vw_comenzi_prajituri;

UPDATE STATISTICS Cantitate;
UPDATE STATISTICS Patiser;
UPDATE STATISTICS Prajitura;

--user_seeks: Arată de câte ori un index a fost folosit pentru căutări rapide (Index Seek)
--user_scans: Indică numărul de operațiuni de scanare completă (Index Scan)


GO
SELECT 
    ix.name AS IndexName,
    ix.type_desc AS IndexType,
    dm.user_seeks, 
    dm.user_scans, 
    dm.user_lookups
FROM sys.dm_db_index_usage_stats dm
JOIN sys.indexes ix 
    ON dm.object_id = ix.object_id 
    AND dm.index_id = ix.index_id
WHERE dm.database_id = DB_ID('Lant_de_cofetarii')
    AND ix.name IS NOT NULL
    AND dm.database_id = DB_ID();




------------------------------------------------TESTE-----------------------------------------------


SELECT * FROM vw_comenzi_prajituri;
Select * from Cantitate;
Select * from Comenzi_Prajituri;
Select * from Prajitura;
Select * from Client;
Select * from Comanda;
Select * from Cofetarie;

UPDATE STATISTICS Cantitate;
SET SHOWPLAN_XML ON;
EXEC sp_lista_cantitati;
SET SHOWPLAN_XML OFF;

SELECT C.porti
FROM Cantitate C
WHERE C.cod_patiser = 1 AND C.cod_prajitura = 2;
SELECT * FROM Comenzi_Prajituri;

