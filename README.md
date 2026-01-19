# Proyecto Vulcano F

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?style=flat-square&logo=spring-boot)
![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=flat-square&logo=apache-maven)
![JWT](https://img.shields.io/badge/JWT-Auth-blue?style=flat-square&logo=jwt)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)
![Status](https://img.shields.io/badge/Status-In%20Development-yellow?style=flat-square)

Proyecto Vulcano F es un servicio REST construido con Spring Boot que recrea el sistema de fundición del popular juego free-to-play Warframe. Proporciona APIs CRUD básicas para entidades relacionadas con la fundición: jugadores, recursos, objetos, planos, componentes, colas e inventarios.

## Descripción

Este repositorio contiene la base de un backend en Java (Spring Boot) que modela la lógica de fundición. Los controladores actuales son boilerplate y responden con mensajes simples en cada endpoint; están pensados para ser rellenados con la lógica real y acceso a la base de datos a través de los repositorios existentes.

## Tecnologías

- Java 17+ (según configuración del proyecto)
- Spring Boot
- Maven (se incluye `mvnw` para ejecutar sin depender de una instalación global de Maven)
- Jakarta Persistence (JPA)
- Lombok (para getters/setters)
- Spring Security OAuth2 con JWT

## Seguridad y Autenticación

Este proyecto implementa **autenticación y autorización basada en JWT (JSON Web Tokens)**:

- **Algoritmo de firma**: HS256
- **Encriptación de contraseñas**: BCrypt
- **Control de acceso**: STATELESS (sin sesiones)
- **Autorización**: Basada en roles con Spring Security

### Endpoints Públicos
- `/auth/login` - Iniciar sesión
- `/auth/register` - Registrar nuevo usuario

### Endpoints Protegidos
Todos los demás endpoints requieren un token JWT válido en el header:
```
Authorization: Bearer <token_jwt>
```

## Ejecutar localmente

Compilar y empaquetar:

```bash
./mvnw -DskipTests package
```

Ejecutar la aplicación:

```bash
./mvnw spring-boot:run
```

La API quedará disponible por defecto en `http://localhost:8080` (según configuración de Spring Boot).

## Endpoints (boilerplate)

A continuación se listan los endpoints CRUD generados automáticamente. Actualmente devuelven mensajes de ejemplo; reemplaza las respuestas por la lógica real cuando implementes los servicios.

Base path: `/api`

- Jugadores
  - GET  /api/jugadores -> Listar todos los jugadores
  - GET  /api/jugadores/{id} -> Obtener jugador por id
  - POST /api/jugadores -> Crear nuevo jugador (body: jugador)
  - PUT  /api/jugadores/{id} -> Actualizar jugador por id (body: jugador)
  - DELETE /api/jugadores/{id} -> Eliminar jugador por id

- Recursos
  - GET  /api/recursos -> Listar todos los recursos
  - GET  /api/recursos/{id} -> Obtener recurso por id
  - POST /api/recursos -> Crear nuevo recurso (body: recurso)
  - PUT  /api/recursos/{id} -> Actualizar recurso por id (body: recurso)
  - DELETE /api/recursos/{id} -> Eliminar recurso por id

- Objetos
  - GET  /api/objetos -> Listar todos los objetos
  - GET  /api/objetos/{id} -> Obtener objeto por id
  - POST /api/objetos -> Crear nuevo objeto (body: objeto)
  - PUT  /api/objetos/{id} -> Actualizar objeto por id (body: objeto)
  - DELETE /api/objetos/{id} -> Eliminar objeto por id

- Planos
  - GET  /api/planos -> Listar todos los planos
  - GET  /api/planos/{id} -> Obtener plano por id
  - POST /api/planos -> Crear nuevo plano (body: plano)
  - PUT  /api/planos/{id} -> Actualizar plano por id (body: plano)
  - DELETE /api/planos/{id} -> Eliminar plano por id

- Componentes
  - GET  /api/componentes -> Listar todos los componentes
  - GET  /api/componentes/{id} -> Obtener componente por id
  - POST /api/componentes -> Crear nuevo componente (body: componente)
  - PUT  /api/componentes/{id} -> Actualizar componente por id (body: componente)
  - DELETE /api/componentes/{id} -> Eliminar componente por id

- Queues (colas)
  - GET  /api/queues -> Listar todas las colas
  - GET  /api/queues/{id} -> Obtener queue por id
  - POST /api/queues -> Crear nueva queue (body: queue)
  - PUT  /api/queues/{id} -> Actualizar queue por id (body: queue)
  - DELETE /api/queues/{id} -> Eliminar queue por id

- Inventarios (clave compuesta: jugador + recurso)
  - GET  /api/inventarios -> Listar todos los inventarios
  - GET  /api/inventarios/{jugadorId}/{recursoId} -> Obtener inventario por jugador y recurso
  - POST /api/inventarios -> Crear nuevo inventario (body: inventario)
  - PUT  /api/inventarios/{jugadorId}/{recursoId} -> Actualizar inventario
  - DELETE /api/inventarios/{jugadorId}/{recursoId} -> Eliminar inventario

## Ejemplos de cuerpos (placeholders)

- Crear Recurso (POST /api/recursos)

```json
{
  "id": 1,
  "nombre": "Ferrita",
  "desc": "Material básico",
  "rareza": "comun"
}
```

- Crear Plano (POST /api/planos)

```json
{
  "id": 1,
  "nombre": "Plano básico",
  "desc": "Plano para fabricar X",
  "coste": 100,
  "tiempoConstrucion": 60,
  "objeto": { "id": 1 }
}
```

- Crear Inventario (POST /api/inventarios)

```json
{
  "id": { "jugadorId": 1, "recursoId": 1 },
  "jugador": { "id": 1 },
  "recurso": { "id": 1 },
  "cantidad": 50
}
```

Nota: Los bodies son ejemplos; adapta los atributos según tus DTOs o entidades.

## Siguientes pasos sugeridos

- Implementar servicios (service layer) que usen los repositorios existentes para la lógica CRUD real.
- Crear DTOs y validaciones (ej. con javax.validation) para las solicitudes entrantes.
- Añadir manejo de errores (ControllerAdvice) y respuestas consistentes.
- Añadir pruebas unitarias y de integración para los controladores y servicios.

---

Si quieres, puedo:

- Generar los servicios CRUD usando los repositorios existentes.
- Implementar la lógica básica en los controladores (inyección de servicios y mapeo DTO <-> entidad).
- Añadir ejemplos de tests y/o Postman collection.

Dime qué prefieres que implemente a continuación.
