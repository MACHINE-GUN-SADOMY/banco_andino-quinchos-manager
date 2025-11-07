# Banco Andino – Sistema de Reservas de Quinchos (MVP)

Prototipo funcional para gestionar **quinchos**, **usuarios** y **reservas** con validación de **no solapamiento** por quincho.  
Arquitectura **monolítica por capas** (Controller / Service / Repository) con **JPA** y **H2**.

---

## ✨ Alcance del MVP

**Incluye**
- Entidades JPA en `Repository.JPA`:
  - `UsuarioJPA (id, nombre, correo, rol, activo, password)`
  - `QuinchoJPA (id, nombre, capacidad, disponible, ubicacion)`
  - `ReservaJPA (id, fechaInicio, fechaFin, estado, aprobado, id_quincho*, id_usuario*)`
  - `BitacoraJPA (id, usuario, accion, fecha)` *(solo entidad, sin uso aún)*
- Reglas de negocio:
  - **No solapamiento** de reservas por quincho (consulta en `ReservaRepository`).
- Configuración de base de datos **H2** (memoria o archivo).
- Consola H2 habilitada.

**Fuera de alcance (por ahora)**
- Integración de **Bitácora** (servicio/controlador).
- Autenticación real (JWT/Spring Security).
- Auditoría, notificaciones, paginación y búsquedas avanzadas.
- Pruebas automatizadas extensivas, Docker/CI.

> Nota: se prioriza **flujo funcional** y **estructura compatible** con las vistas y diagramas (4+1 y UML).
