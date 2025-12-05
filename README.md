# 🏛️ Banco Andino – Sistema de Reservas de Quinchos  
**Proyecto académico – Arquitectura de Software (Monolito por capas)**

Este sistema permite gestionar **quinchos**, **usuarios** y **reservas** internas del Banco Andino, resolviendo los problemas actuales de sobrecarga administrativa, conflictos de horario y falta de trazabilidad.  
El proyecto está construido bajo una **arquitectura monolítica por capas**, utilizando **Spring Boot**, **JPA/Hibernate** y base de datos **H2**.

---

## 📌 Objetivo del Sistema
Digitalizar el proceso de reserva de quinchos corporativos, otorgando una plataforma clara y centralizada para:

- Empleados (reservar, consultar disponibilidad, ver su historial)
- Administradores (gestionar usuarios, quinchos, reservas y bitácora)

---

# 🧱 Arquitectura del Proyecto

### ✔️ Arquitectura Monolítica por Capas  
El sistema sigue la estructura clásica:

- **Controller** → Manejo de solicitudes HTTP.
- **Service** → Lógica de negocio, validaciones y reglas.
- **Repository** → Acceso a datos mediante JPA.
- **JPA (Modelos)** → Entidades que representan las tablas.

📂 *Paquete raíz:*  
`cl.banco_andino_quinchos`

### Componentes principales:
- **Módulo Usuario**
- **Módulo Quincho**
- **Módulo Reserva**
- **Módulo Bitácora**
- **Frontend HTML/CSS** en carpeta `resources`

---

# 📦 Entidades del Sistema (JPA)

### 👤 **UsuarioJPA**
Campos:
- id, nombre, correo, rol, activo, password

Acciones:
- reservarQuincho()
- consultarReserva()

---

### 🏡 **QuinchoJPA**
Campos:
- id, nombre, capacidad, disponible, ubicación

Acciones:
- reservar()

---

### 📅 **ReservaJPA**
Campos:
- idReserva  
- fechaInicio / fechaFin  
- estado  
- aprobado (boolean)  
- idUsuario*  
- idQuincho*

Reglas implementadas:
- ❌ **No se permiten reservas solapadas para el mismo quincho**

---

### 📝 **BitacoraJPA**
Campos:
- idBitacora  
- idUsuario  
- acción  
- fecha

> El sistema ya cuenta con Bitácora funcional en el front/back.

---

# ⚙️ Backend – Funcionalidades implementadas

### ✔️ Usuarios
- Crear, editar, eliminar usuarios
- Asignación de roles (Administrador/Empleado)
- Activación / desactivación
- Validaciones de datos

### ✔️ Quinchos
- Crear y eliminar quinchos
- Consultar disponibilidad
- Validar capacidad/disponibilidad

### ✔️ Reservas
- Crear reservas con validación de **no solapamiento**
- Cancelar reservas
- Consultar reservas por usuario y globales

### ✔️ Bitácora
- Registrar acciones del sistema
- Consultar historial para auditoría

---

# 🌐 Front-End (HTML/CSS)

El MVP incluye:

- Vistas completas para **Usuarios**, **Quinchos**, **Reservas** y **Bitácora**
- Formularios de creación/edición
- Tablas dinámicas
- Navegación entre módulos
- Mensajes de confirmación

Las vistas viven en:

src/main/resources/static/


---

# 🗄️ Base de Datos

### Base utilizada
**H2 Database** (modo archivo o memoria)

Configuración incluida:
- Consola H2 habilitada
- Schema generado automáticamente con Hibernate

---

# 🧪 Reglas de Negocio Implementadas

- Verificación de solapamiento por quincho:  
  `ReservaRepository.existsByQuinchoAndFechas()`
- Estados de reservas (pendiente, aprobada, cancelada)
- Validación de usuario activo
- Registro automático de acciones en Bitácora

---

# 🚀 Cómo ejecutar el proyecto

1. Importar como proyecto **Maven**.
2. Ejecutar desde IDE o consola: 
	mvn spring-boot:run
3. Acceder a la aplicación:
	http://localhost:8080
4. Consola H2:
	http://localhost:8080/h2-console


---

# 📚 Documentación asociada

El proyecto está documentado bajo el modelo **4+1**, incluyendo:

- Diagrama de Clases ✔️
- Diagrama de Comunicación ✔️
- Diagrama de Secuencia ✔️
- Diagrama de Paquetes ✔️
- Diagrama de Componentes ✔️
- Diagrama de Despliegue ✔️
- Mockups del sistema ✔️
- Casos de uso ✔️
- Atributos de calidad + escenarios ✔️
- Matriz de riesgos ✔️

Toda la documentación se integra con la estructura real del código.

---

# 🧩 Próximas mejoras (Roadmap)

- Integración de **Spring Security + JWT**
- Auditoría avanzada en Bitácora
- Filtrado, paginación y búsqueda
- Notificaciones internas
- Migración a PostgreSQL
- Pruebas unitarias e integración
- Dockerización del sistema

---

# 📌 Estado Actual del Proyecto
El proyecto se encuentra **funcional**, con:

✔ Front-end completo  
✔ Servicios implementados  
✔ Validaciones operativas  
✔ Bitácora integrada  
✔ Estructura coherente con la documentación 4+1  

---

# 📄 Autor
**Cristian Parra Hernández**  
DuocUC – Escuela de Informática  
2025
