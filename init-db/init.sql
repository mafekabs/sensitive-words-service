-- Create database if it doesn't exist
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'swapp_db')
BEGIN
CREATE DATABASE swapp_db;
END
GO

-- Create login if it doesn't exist
IF NOT EXISTS (SELECT name FROM sys.sql_logins WHERE name = 'mafekabs')
BEGIN
CREATE LOGIN mafekabs WITH PASSWORD='mafeka@BSG1';
END
GO

-- Switch to the new database
USE swapp_db;
GO

-- Create user for that login
IF NOT EXISTS (SELECT name FROM sys.database_principals WHERE name = 'mafekabs')
BEGIN
CREATE USER mafekabs FOR LOGIN mafekabs;
ALTER ROLE db_owner ADD MEMBER mafekabs;
END
GO

---------------------------- Test DB -------------------------------------------
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'test_db')
    BEGIN
        CREATE DATABASE test_db;
    END
GO

-- Create login if it doesn't exist
IF NOT EXISTS (SELECT name FROM sys.sql_logins WHERE name = 'test')
    BEGIN
        CREATE LOGIN test WITH PASSWORD='mafeka@BSG1';
    END
GO

-- Switch to the new database
USE test_db;
GO

-- Create user for that login
IF NOT EXISTS (SELECT name FROM sys.database_principals WHERE name = 'test')
    BEGIN
        CREATE USER test FOR LOGIN test;
        ALTER ROLE db_owner ADD MEMBER test;
    END
GO