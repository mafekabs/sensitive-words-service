-- init.sql

-- Create database if it doesn't exist
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'swapp_db')
BEGIN
    CREATE DATABASE swapp_db;
END

-- Create login if it doesn't exist
IF NOT EXISTS (SELECT name FROM sys.sql_logins WHERE name = N'mafekabs')
    BEGIN
        CREATE LOGIN mafekabs WITH PASSWORD = 'mafeka@BSG1!';
    END

-- Create user in swapp_db and assign db_owner
USE swapp_db;
IF NOT EXISTS (SELECT name FROM sys.database_principals WHERE name = N'mafekabs')
    BEGIN
        CREATE USER mafekabs FOR LOGIN mafekabs;
        ALTER ROLE db_owner ADD MEMBER mafekabs;
    END