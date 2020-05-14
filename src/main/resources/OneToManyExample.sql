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
--droping tables
GO
IF OBJECT_ID('dbo.CityBuildingsFK', 'U') IS NOT NULL
  DROP TABLE dbo.CityBuildingsFK;
GO
GO
IF OBJECT_ID('dbo.City', 'U') IS NOT NULL
  DROP TABLE dbo.City;
GO
GO
IF OBJECT_ID('dbo.Buildings', 'U') IS NOT NULL
  DROP TABLE dbo.Buildings;
GO

--creating tables
GO
CREATE TABLE City(
ID int IDENTITY(1,1),
Name varchar(255),
Population int
PRIMARY KEY(ID)
);
GO
GO
CREATE TABLE Buildings(
ID int IDENTITY(1,1),
Name varchar(255)
PRIMARY KEY(ID)
);
GO
GO
CREATE TABLE CityBuildingsFK(
CityID int,
BuildingsID int UNIQUE,
CONSTRAINT FK_City FOREIGN KEY (CityID)
    REFERENCES City(ID),
CONSTRAINT FK_Buildings FOREIGN KEY (BuildingsID)
    REFERENCES Buildings(ID)
);
GO
--populating tables
GO
INSERT INTO City (Name, Population)
VALUES ('Warsaw', 123321),
	('Tokio', 1222231),
	('New York', 5000000),
	('Paris', 2312222)
GO
GO
INSERT INTO Buildings (Name)
VALUES ('Church'),
	('Museum'),
	('Zoo'),
	('Office')
GO
GO
INSERT INTO CityBuildingsFK (CityID, BuildingsID)
VALUES (1,2),
	(1,3),
	(2,4),
	(3,1)
GO
--selecting tables
GO
SELECT * FROM City
GO
GO
SELECT * FROM Buildings
GO
GO
SELECT * FROM CityBuildingsFK
GO

