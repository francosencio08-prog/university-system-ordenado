# University System (Java 21 + Spring Boot + SQL Server)

> Este proyecto NO es Python: es Java con Maven. Las dependencias están en `pom.xml`
> y Maven las descarga solo; no hay `pip install`.

## 1. Requisitos
- JDK 21 (`java -version` debe decir 21)
- SQL Server (Express/Developer) con **TCP/IP habilitado en el puerto 1433** (SQL Server Configuration Manager)
  y autenticación mixta (usuario `sa` con contraseña)
- No hace falta instalar Maven: se usa `mvnw` / `mvnw.cmd` (necesita internet la primera vez)

## 2. Crear la base de datos
Abre `db/01_create_database.sql` en SSMS / Azure Data Studio y ejecútalo.
O por consola:
    sqlcmd -S localhost -U sa -P TU_CLAVE -i db/01_create_database.sql

Las tablas las crea la app sola al arrancar (`ddl-auto=update`).
`db/02_schema.sql` es opcional (esquema manual equivalente).

## 3. Configurar conexión
Por defecto: localhost:1433, BD `UniversitySystem`, usuario `sa`, clave `123456`.
Cambia con variables de entorno (sin tocar el código):
    DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD, GEMINI_API_KEY
o edita `src/main/resources/application.properties`.

## 4. Ejecutar
Windows:  `mvnw.cmd spring-boot:run`
Linux/Mac: `./mvnw spring-boot:run`
Luego abre http://localhost:8080/login.html
Admin creado automáticamente: usuario `j4v` / clave `admin123`

## 5. Gemini (opcional)
Sin `GEMINI_API_KEY` todo funciona, pero las recomendaciones de IA quedan en estado FAILED.

## Errores típicos
- "Cannot open database UniversitySystem" -> falta ejecutar el paso 2.
- "Login failed for user 'sa'" -> clave incorrecta o `sa` deshabilitado / sin autenticación mixta.
- "Connection refused" / timeout -> TCP/IP del puerto 1433 no habilitado o servicio detenido.
- "invalid target release: 21" -> tu JAVA_HOME no apunta a JDK 21.
