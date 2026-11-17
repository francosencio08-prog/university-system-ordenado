-- Ejecutar conectado a la BD master (SSMS / Azure Data Studio / sqlcmd)
IF DB_ID(N'UniversitySystem') IS NULL
    CREATE DATABASE UniversitySystem COLLATE Latin1_General_100_CI_AS_SC_UTF8;
GO
    