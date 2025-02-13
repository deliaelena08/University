USE Lant_de_cofetarii;
CREATE TABLE Versions(
actualversion int
);

GO
CREATE PROCEDURE procedure_1
AS
BEGIN
ALTER TABLE Cofetarie
ALTER COLUMN numar_de_telefon bigint
PRINT 'modificare tip coloana'
END

exec procedure_1

GO
CREATE PROCEDURE reversed_procedure_1
AS
BEGIN
ALTER TABLE Cofetarie
ALTER COLUMN numar_de_telefon int
PRINT 'modificare tip coloana'
END

exec reversed_procedure_1

GO
CREATE PROCEDURE procedure_2
AS
BEGIN
ALTER TABLE Adresa
ADD CONSTRAINT df_strada DEFAULT 'necunoscut'
FOR strada
PRINT 'adaugare default'
END

exec procedure_2

GO
CREATE PROCEDURE reversed_procedure_2
AS
BEGIN
ALTER TABLE Adresa
DROP CONSTRAINT df_strada
PRINT 'stergere default'
END

exec reversed_procedure_2

GO
CREATE PROCEDURE procedure_3
AS
BEGIN
CREATE TABLE Livrari(
	id_livrare INT IDENTITY(1,1) PRIMARY KEY,
	firma VARCHAR(100),
	numar_comenzi INT,
	numar_de_masini INT
);
PRINT 'creare tabel'
END

exec procedure_3

GO
CREATE PROCEDURE reversed_procedure_3
AS
BEGIN
DROP TABLE Livrari
PRINT 'stergere tabel'
END

exec reversed_procedure_3

GO
CREATE PROCEDURE procedure_4
AS
BEGIN
ALTER TABLE Livrari
ADD Salariu bigint
PRINT 'adaugare coloana'
END

exec procedure_4

GO
CREATE PROCEDURE reversed_procedure_4
AS
BEGIN
ALTER TABLE Livrari
DROP COLUMN Salariu
PRINT 'stergere coloana'
END

exec reversed_procedure_4

GO
CREATE PROCEDURE procedure_5
AS
BEGIN
ALTER TABLE Livrari
ADD CONSTRAINT fk_Livrari_Cofetarie FOREIGN KEY(id_livrare) REFERENCES Cofetarie(cod_cofetarie)
PRINT 'adaugare constrangere'
END

exec procedure_5

GO
CREATE PROCEDURE reversed_procedure_5
AS
BEGIN
ALTER TABLE Livrari
DROP CONSTRAINT fk_Livrari_Cofetarie
PRINT 'stergere default'
END

exec reversed_procedure_5

INSERT INTO Versions (actualversion) VALUES (0);
DECLARE @vers int
SET @vers=0
GO
CREATE PROCEDURE main
@vers int
AS
BEGIN
  DECLARE @current_version INT;
  DECLARE @procedure_name NVARCHAR(50);
  DECLARE @sql NVARCHAR(MAX);--comanda SQL pe care o executam
  IF @vers>5 OR @vers<0
  BEGIN
    PRINT 'versiunea nu este valida';
    RETURN;
  END
  SELECT @current_version = actualversion FROM Versions;
  IF @current_version= @vers
  BEGIN
    PRINT 'se afla deja la aceasta versiune';
	RETURN;
  END

  IF @vers > @current_version
  BEGIN
    WHILE @current_version < @vers
	BEGIN
	  SET @current_version = @current_version+1;
	  SET @procedure_name = 'procedure_'+CAST(@current_version AS NVARCHAR(10));
	  SET @sql = 'EXEC '+@procedure_name;
	  EXEC sp_executesql @sql;
	END
  END
  ELSE IF @vers<@current_version 
  BEGIN
    WHILE @current_version > @vers
	BEGIN
	  SET @procedure_name='reversed_procedure_' + CAST(@current_version AS nvarchar(10));
	  SET @sql = 'EXEC ' +@procedure_name;
	  EXEC sp_executesql @sql;
	  SET @current_version = @current_version-1;
	END
  END
  UPDATE Versions SET actualversion = @vers;
  PRINT 'Actualizare completa. Versiunea curenta este '+CAST(@vers AS VARCHAR(10));
END

EXEC main @vers = 6;
EXEC main @vers = -3;
EXEC main @vers = 0;
EXEC main @vers = 2;
EXEC main @vers = 5;
EXEC main @vers = 3;
EXEC main @vers = 0;