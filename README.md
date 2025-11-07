# 🏦 Banco Andino Quinchos

Sistema de reservas internas para quinchos corporativos, desarrollado en **Spring Boot 3** con base de datos **H2 persistente**.  
El proyecto permite gestionar quinchos, usuarios, reservas y roles de administración.

## ⚙️ Tecnologías utilizadas
- Java 21  
- Spring Boot 3.3.3  
- Spring Web  
- Spring Data JPA  
- Lombok  
- H2 Database (modo persistente)  
- Gradle

## 🧩 Estructura del proyecto
```
cl.banco_andino_quinchos/
 ├── baq/
 │   ├── controller/
 │   │   ├── dto/
 │   │   │   ├── request/
 │   │   │   └── response/
 │   ├── repository/
 │   │   └── jpa/
 │   ├── service/
 │   └── model/
 ├── resources/
 │   ├── application.properties
 │   └── static / templates (si aplica)
```

## 💾 Configuración de base de datos
H2 persistente en archivo local:

```
jdbc:h2:file:./data/baqdb
```

Consola: [http://localhost:8081/h2-console](http://localhost:8081/h2-console)

| Campo | Valor |
|--------|-------|
| Driver | org.h2.Driver |
| URL | jdbc:h2:file:./data/baqdb |
| Usuario | root |
| Password | *system* |