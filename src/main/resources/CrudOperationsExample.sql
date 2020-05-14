--DELETING ExampleJDBC DATABASE
USE master
GO
IF EXISTS(select * from sys.databases where name='ExampleJDBC')
DROP DATABASE ExampleJDBC
GO
--creating database
GO
CREATE DATABASE ExampleJDBC;
GO
USE ExampleJDBC;
GO
-- droping Car table
GO
IF OBJECT_ID('dbo.Car', 'U') IS NOT NULL
  DROP TABLE dbo.Car;
GO
--creating table
GO
CREATE TABLE Car(
ID int IDENTITY(1,1),
Name varchar(255) NOT NULL,
Model varchar(255) NOT NULL,
RegistryDate date
PRIMARY KEY(ID)
);
GO
--populating table
GO
INSERT INTO Car (Name, Model, RegistryDate)
VALUES ('Fiesta', 'Ford', '2019-01-11'),
	('Mondeo', 'Ford', '1997-02-13'),
	('Ibiza', 'Seat', '1998-03-11'),
	('Forester', 'Subaru', '1999-10-15'),
	('Gallardo', 'Lamborginii', '1999-12-13'),
	('126p', 'Fiat', '1999-02-13'),
	('Corolla', 'Toyota', '1999-02-13'),
	('Stilo', 'Fiat', '1933-02-23'),
	('Supra', 'Toyota', '2012-01-03'),
	('Mustang', 'Ford', '1999-12-11')
GO
--selecting table
GO
SELECT * FROM Car;
GO