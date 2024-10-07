
# Proyecto de Cryptomonedas

## Script de Creación de la Base de Datos MySQL

```sql
CREATE DATABASE IF NOT EXISTS crypto_db;
USE crypto_db;

CREATE TABLE IF NOT EXISTS cryptocurrencies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cryptocurrency_prices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    crypto_id BIGINT NOT NULL,
    price DECIMAL(18, 8) NOT NULL,
    price_date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS cryptocurrency_names_from_service (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);
```

## Uso de Docker para la Base de Datos

### Correr el contenedor MySQL:

Para iniciar un contenedor de MySQL con Docker, ejecuta el siguiente comando:

```bash
sudo docker run -d --name mysql-container -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root mysql:latest
```

### Acceder a la base de datos dentro del contenedor:

Para acceder al estado de la base de datos y conectarse a MySQL dentro del contenedor:

```bash
sudo docker exec -it mysql-container mysql -u root -p
# Te pedirá la contraseña: root
```

Para salir de la consola MySQL:

```bash
Ctrl + D
```

### Manejo de Volúmenes y Mapeo de Puertos:

Si deseas manejar volúmenes y mapear puertos para persistir datos en el subsistema Linux:

```bash
sudo docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=crypto_db -p 3306:3306 -v /c/Users/pablo.nicola/Documents/mysql-data:/var/lib/mysql -d mysql
```

## Notas Importantes

- **R2DBC no maneja relaciones automáticas:** A diferencia de JPA, Spring Data R2DBC no maneja automáticamente las relaciones entre tablas.
- **Gestión de Relaciones:** Deberás manejar las uniones y relaciones entre tablas de manera manual, utilizando consultas personalizadas o gestionando las relaciones de manera explícita en tu código.
