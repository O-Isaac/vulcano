# Proyecto Vulcano F

![Java](https://img.shields.io/badge/Java-25-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-green?style=flat-square&logo=spring-boot)
![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=flat-square&logo=apache-maven)
![MariaDB](https://img.shields.io/badge/MariaDB-10.x-003545?style=flat-square&logo=mariadb)
![JWT](https://img.shields.io/badge/JWT-Auth-blue?style=flat-square&logo=jwt)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-6DB33F?style=flat-square&logo=spring-security)
![Scalar](https://img.shields.io/badge/Scalar-API%20Docs-2d333b?style=flat-square)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=flat-square&logo=swagger)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)
![Status](https://img.shields.io/badge/Status-In%20Development-yellow?style=flat-square)

Proyecto Vulcano F es un servicio REST construido con Spring Boot que implementa un sistema de fundición y fabricación de materiales asíncrono, inspirado en mecánicas comunes en diversos videojuegos de gestión y RPG. Este sistema permite a los jugadores recolectar recursos, adquirir planos y "craftear" objetos en tiempo real (que requieren un tiempo de espera para completarse).

Proporciona APIs CRUD básicas para entidades relacionadas con la fundición: jugadores, recursos, objetos, planos, componentes, colas e inventarios.

## Repositorios relacionados

- **[vulcano-admin](https://github.com/O-Isaac/vulcano-admin)** - Panel de administración para gestionar el sistema Vulcano

## Documentación de API

La documentación interactiva de la API está disponible en:

- **[Scalar](http://localhost:8080/scalar/index.html)** - Documentación moderna y visual de OpenAPI
- **[Swagger UI](http://localhost:8080/swagger-ui.html)** - Interfaz estándar de Swagger

Ambas herramientas permiten explorar, probar y entender los endpoints disponibles de forma interactiva.

## Descripción

Vulcano es un backend completo desarrollado con **Spring Boot 4.0.1** que implementa un sistema robusto de fundición y fabricación de materiales asíncrono. El proyecto incluye:

- **Sistema de autenticación y autorización** basado en JWT con roles (ADMIN, USER)
- **APIs CRUD completas** para todas las entidades del juego
- **Servicios de negocio** con lógica de validación, gestión de recursos y cola de construcción
- **DTOs y mappers** para la transformación de datos entre capas
- **Manejo de errores** centralizado con excepciones personalizadas
- **Documentación interactiva** de API con Scalar y Swagger UI
- **Validaciones** usando Jakarta Validation
- **Transaccionalidad** en todas las operaciones críticas

## Tecnologías

- **Java 25** - Lenguaje de programación
- **Spring Boot 4.0.1** - Framework web
- **Spring Security 7.0.2** - Seguridad y autenticación
- **MariaDB** - Base de datos relacional
- **Maven** - Gestor de dependencias (incluye `mvnw`)
- **Jakarta Persistence (JPA)** - ORM
- **Lombok** - Reducción de boilerplate (getters/setters/constructores)
- **MapStruct** - Mapeo de DTOs a entidades
- **Spring OAuth2 JWT** - Autenticación con JSON Web Tokens
- **SpringDoc OpenAPI** - Documentación de API
- **Scalar & Swagger UI** - Interfaces interactivas para explorar la API
- **Spring Validation** - Validación de datos con anotaciones
- **Spring Actuator** - Health checks y métricas

## Seguridad y Autenticación

Este proyecto implementa **autenticación y autorización basada en JWT (JSON Web Tokens)**:

- **Algoritmo de firma**: HS256 (HMAC con SHA-256)
- **Encriptación de contraseñas**: BCrypt con salt
- **Política de sesión**: STATELESS (sin sesiones HTTP)
- **Autorización**: Basada en roles con Spring Security (`@PreAuthorize`)
- **Proveedores de autenticación**: DaoAuthenticationProvider

### Endpoints Públicos (sin autenticación)
- `POST /api/auth/login` - Iniciar sesión y obtener JWT
- `POST /api/auth/register` - Registrar nuevo usuario
- `POST /api/auth/refresh` - Refrescar token JWT
- `/api-docs/**` - Documentación OpenAPI en JSON
- `/scalar/**` - Interfaz Scalar para explorar la API
- `/swagger-ui/**` - Interfaz Swagger para explorar la API

### Endpoints Protegidos
Todos los demás endpoints requieren un token JWT válido en el header:
```
Authorization: Bearer <token_jwt>
```

El token se valida en cada solicitud usando el algoritmo HS256 y se extrae el usuario del claim `sub` (subject).

## Requisitos Previos

Necesitas tener una instancia de **MariaDB** ejecutándose. Por defecto, la aplicación espera la siguiente configuración (puedes cambiarla en `src/main/resources/application.properties`):

- **Host**: `localhost`
- **Puerto**: `3307`
- **Base de datos**: `fundicion`
- **Usuario**: `root`
- **Contraseña**: *(vacía)*

## Módulos de la API

La aplicación expone endpoints para gestionar los siguientes recursos del juego:

### 🔐 Auth (`/api/auth`)
- **POST /login** - Autenticación de usuarios (genera JWT)
- **POST /register** - Registro de nuevos jugadores
- **POST /refresh** - Renovación de token JWT

### 👥 Jugadores (`/api/jugadores`)
- **GET** - Listar todos los jugadores
- **GET /{id}** - Obtener jugador por ID
- **POST** - Crear nuevo jugador
- **PUT /{id}** - Actualizar perfil del jugador
- **DELETE /{id}** - Eliminar jugador

Atributos: id, nombre, correo, password (encriptada con BCrypt), role, nivel, créditos

### 📦 Recursos (`/api/recursos`)
- **GET** - Listar recursos (con paginación)
- **GET /{id}** - Obtener recurso por ID
- **POST** - Crear nuevo recurso
- **PUT /{id}** - Actualizar recurso
- **DELETE /{id}** - Eliminar recurso

Atributos: id, nombre, descripción, rareza (COMUN, RARO, LEGENDARIO)

### 📋 Planos (`/api/planos`)
- **GET** - Listar todos los planos
- **GET /{id}** - Obtener plano por ID
- **POST** - Crear nuevo plano
- **PUT /{id}** - Actualizar plano
- **DELETE /{id}** - Eliminar plano

Atributos: id, nombre, descripción, coste (en créditos), tiempoConstrucion (en milisegundos), recursoFabricado (referencia al recurso que produce)

### ⚙️ Componentes (`/api/componentes`)
- **GET** - Listar componentes
- **GET /{id}** - Obtener componente por ID
- **POST** - Crear componente
- **PUT /{id}** - Actualizar componente
- **DELETE /{id}** - Eliminar componente

Atributos: id, cantidad, plano (referencia), recurso (referencia)
*Los componentes define qué recursos necesita un plano para fabricarse*

### 📦 Inventario (`/api/inventarios`)
- **GET** - Listar inventarios
- **GET /{jugadorId}/{recursoId}** - Obtener inventario de un jugador para un recurso
- **POST** - Crear entrada de inventario
- **PUT /{jugadorId}/{recursoId}** - Actualizar cantidad
- **DELETE /{jugadorId}/{recursoId}** - Eliminar entrada

Atributos: jugadorId, recursoId, cantidad
*Clave compuesta: (jugadorId, recursoId)*

Endpoints especiales (protegidos):
- **GET /me** - Ver mi inventario (requiere autenticación)

### ⏳ Queue / Colas de Construcción (`/api/queues`)
- **GET** - Listar todas las construcciones (filtrable por estado)
- **GET /{id}** - Obtener construcciones de un jugador
- **POST** - Iniciar construcción de un plano

Atributos: id, plano, jugador, estado (EN_CONSTRUCCION, FINALIZADO), inicioTime, finalTime

Lógica de construcción:
1. **Validación**: Verifica que el jugador tenga suficientes créditos y componentes
2. **Deducción de recursos**: Se consumen los componentes del inventario
3. **Deducción de créditos**: Se cobran los créditos según el coste del plano
4. **Creación de entrada en queue**: Se registra el tiempo de inicio y fin
5. **Finalización automática**: Cuando finalTime <= ahora, se completa y se entrega el recurso fabricado

*Las tareas finalizadas se procesan automáticamente y entregan el recurso al inventario del jugador*

## Ejecutar localmente

1. Asegúrate de tener la base de datos creada:
   ```sql
   CREATE DATABASE fundicion;
   ```

2. Compilar y empaquetar:

```bash
./mvnw -DskipTests package
```

3. Ejecutar la aplicación:

```bash
./mvnw spring-boot:run
```

La API quedará disponible por defecto en `http://localhost:8080` (según configuración de Spring Boot).

## Testeo de API

Se incluye una colección de endpoints en **Bruno** (`Vulcano API Endpoints/`):

- **Auth**: Login, Register, Refresh Token
- **Jugadores**: CRUD de usuarios
- **Recursos**: CRUD de recursos
- **Planos**: CRUD de planos
- **Componentes**: CRUD de componentes
- **Inventario**: CRUD de inventarios + endpoints personales (`/me`)
- **Queue**: Crear y listar construcciones

Puedes importar la colección en Bruno o cualquier herramienta REST (Postman, Insomnia, etc.)

### Variables de entorno
- **Host**: `http://localhost:8080`
- **JWT Token**: Se obtiene automáticamente tras login y se usa en solicitudes subsecuentes

## Ejemplos de cuerpos de solicitud

### Crear Recurso
```json
POST /api/recursos

{
  "nombre": "Ferrita",
  "desc": "Material básico para la fabricación",
  "rareza": "COMUN"
}
```

### Crear Plano
```json
POST /api/planos

{
  "nombre": "Plano de Excalibur Prime",
  "desc": "El plano maestro para ensamblar al legendario Excalibur Prime",
  "coste": 250000,
  "tiempoConstrucion": 30000,
  "recursoFabricadoId": 1
}
```

### Crear Componente
```json
POST /api/componentes

{
  "cantidad": 5,
  "planoId": 1,
  "recursoId": 2
}
```

### Crear Inventario
```json
POST /api/inventarios

{
  "jugadorId": 1,
  "recursoId": 1,
  "cantidad": 100
}
```

### Iniciar Construcción
```json
POST /api/queues

{
  "planoId": 1
}
```
*Requiere: Token JWT válido, créditos suficientes, componentes necesarios en inventario*

### Registrarse
```json
POST /api/auth/register

{
  "nombre": "IsaacDev",
  "correo": "isaac@example.com",
  "password": "miPassword123"
}
```

### Login
```json
POST /api/auth/login

{
  "correo": "isaac@example.com",
  "password": "miPassword123"
}
```
*Respuesta incluye un JWT token que debe usarse en solicitudes posteriores*

## Características Especiales

### Sistema de Construcción Asíncrono
- Las construcciones se ejecutan de forma **no bloqueante**
- Se almacena un timestamp de inicio y fin en la base de datos
- Un **scheduler** verifica periódicamente qué construcciones han finalizado
- Al finalizar, el recurso fabricado se **entrega automáticamente** al inventario del jugador

### Validaciones de Negocio
- **Verificación de duplicados**: No permite dos construcciones del mismo plano en paralelo
- **Validación de recursos**: Verifica que el jugador tenga todos los componentes necesarios
- **Validación de créditos**: Comprueba saldo antes de iniciar construcción
- **Gestión transaccional**: Usa `@Transactional` para garantizar consistencia

### Mapeo de Entidades
- Usa **MapStruct** para conversión automática DTO <-> Entity
- Evita exponer entidades completas en respuestas API
- DTOs separadas para creación y actualización con validaciones específicas

### Manejo de Errores
- Excepciones personalizadas (`BadRequestException`, `EntityNotFoundException`)
- **ControllerAdvice** centralizado para respuestas consistentes
- Mensajes de error descriptivos para depuración

```
src/main/java/io/github/isaac/vulcano/
├── VulcanoApplication.java          # Punto de entrada
├── configs/                         # Configuraciones (Spring Security, CORS, JWT)
├── controllers/                     # Controladores REST
├── services/                        # Lógica de negocio
├── repositories/                    # Acceso a datos (JPA)
├── entities/                        # Modelos de base de datos
├── dtos/                           # Objetos de transferencia de datos
├── mappers/                        # Conversión DTO <-> Entity (MapStruct)
├── exceptions/                     # Excepciones personalizadas
└── schedulers/                     # Tareas programadas (finalizacion de colas)
```

## Estado del Desarrollo

✅ **Completado:**
- Modelo de datos completo con todas las entidades
- Sistema de autenticación y autorización con JWT
- Servicios CRUD para todas las entidades
- Lógica de negocio para la cola de construcción (validación, deducción de recursos, cálculo de tiempos)
- DTOs y validaciones
- Documentación interactiva (Scalar y Swagger)
- CORS configurado para desarrollo
- Manejo centralizado de errores

🔧 **En desarrollo o pendiente:**
- Tests unitarios e integración
- Optimizaciones de consultas a base de datos
- Métricas y monitoreo avanzado
- Paginación en todos los endpoints

## Siguientes pasos sugeridos

- Ejecutar tests (actualmente disponibles en `src/test`)
- Implementar paginación en listados grandes
- Añadir más validaciones de negocio según requisitos
- Integrar con vulcano-admin para gestión centralizada
- Documentar API responses estándar

## Contribución

Las contribuciones son bienvenidas. Para cambios importantes:
1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Realiza tus cambios y commits
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT. Ver archivo `LICENSE` para más detalles.

## Contacto e Información

- **Autor**: Isaac
- **Repositorio Principal**: https://github.com/O-Isaac/vulcano
- **Panel Admin**: https://github.com/O-Isaac/vulcano-admin
- **Documentación API**: Disponible en `http://localhost:8080/scalar` (Scalar) o `http://localhost:8080/swagger-ui.html` (Swagger)

---

**Última actualización**: Febrero 2026  
**Versión**: 0.0.1-SNAPSHOT
