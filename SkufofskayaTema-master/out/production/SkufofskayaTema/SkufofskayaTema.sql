CREATE DATABASE SkufofskayaTema;
USE SkufofskayaTema;

CREATE TABLE Passports(
	PassportID INT PRIMARY KEY AUTO_INCREMENT,
    PassportIssued VARCHAR(150),
    PassportDateOfIssue DATE,
    PassportUnitCode VARCHAR(7),
    PassportSeries INT(4),
    PassportNumber INT(6)
);

CREATE TABLE Registration(
	RegistrationID INT PRIMARY KEY AUTO_INCREMENT,
    RegistrationPassword INT(4)
);

CREATE TABLE Clients (
	ClientID INT PRIMARY KEY AUTO_INCREMENT,
    ClientLastName VARCHAR(50),
    ClientFirstName VARCHAR(50),
    ClientPatronymic VARCHAR(50),
    ClientGender VARCHAR(7),
    ClientDateOfBirth DATE,
    ClientPlaceOfBirth VARCHAR(150),
    ClientPassportID INT,
    ClientRegistrationID INT,
		FOREIGN KEY (ClientPassportID) REFERENCES Passports(PassportID),
        FOREIGN KEY (ClientRegistrationID) REFERENCES Registration(RegistrationID)
);

INSERT INTO Passports (PassportIssued, PassportDateOfIssue, PassportUnitCode, PassportSeries, PassportNumber) VALUES
	('Department of State', '2000-05-15', '1234567', 1234, 567890),
	('Passport Office', '2010-08-20', '7654321', 5678, 123456),
	('Citizen Services Bureau', '2015-12-10', '9876543', 9876, 654321);

INSERT INTO Registration (RegistrationPassword) VALUES
	(1234),
	(5678),
	(9876);

INSERT INTO Clients (ClientLastName, ClientFirstName, ClientPatronymic, ClientGender, ClientDateOfBirth, ClientPlaceOfBirth, ClientPassportID, ClientRegistrationID) VALUES
	('Иванов', 'Петр', 'Иванович', 'Мужской', '1990-03-15', 'Москва', 1, 1),
	('Петрова', 'Мария', 'Петровна', 'Женский', '1985-07-20', 'Санкт-Петербург', 2, 2),
	('Сидоров', 'Алексей', 'Алексеевич', 'Мужской', '1995-11-10', 'Екатеринбург', 3, 3);


SELECT * FROM Clients;
SELECT * FROM Passports;
SELECT * FROM Registration;

DROP DATABASE SkufofskayaTema;