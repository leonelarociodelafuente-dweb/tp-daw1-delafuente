
# TP Integrador · DAW1 — Sistema de Biblioteca

**Alumno/a:** De La Fuente, Leonela Rocío  
**Legajo:** COMPLETAR  
**Enunciado elegido:** biblioteca

## Descripción

Sistema de gestión de una biblioteca que permite administrar libros, socios y préstamos. Permite registrar usuarios, controlar la disponibilidad de libros y gestionar las devoluciones.

## Cómo ejecutarlo

Requisitos: Java 21 (Maven viene incluido con `mvnw`).

En Windows:
```powershell
.\mvnw.cmd spring-boot:run
```

La API queda en `http://localhost:8080/api`.
La base H2 se guarda en `data/`.
Consola H2: `http://localhost:8080/h2-console`.

## Modelo

| Entidad | Atributos principales | Relaciones |
|---|---|---|
| Libro | id, titulo, autor, isbn | Se relaciona con Prestamo |
| Socio | id, nombre, email | Se relaciona con Prestamo |
| Prestamo | id, estado, fechaPrestamo, fechaDevolucion | N a 1 con Libro y Socio |

Estados de Prestamo: `ACTIVO → DEVUELTO | VENCIDO` y `VENCIDO → DEVUELTO`.

## Endpoints

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| GET | /api/libros | Lista libros | 200 |
| GET | /api/libros/{id} | Busca libro | 200, 404 |
| POST | /api/libros | Crea libro | 201, 400, 409 |
| PUT | /api/libros/{id} | Modifica libro | 200 |
| DELETE | /api/libros/{id} | Borra libro | 204, 404 |
| GET | /api/socios | Lista socios | 200 |
| GET | /api/socios/{id} | Busca socio | 200, 404 |
| POST | /api/socios | Crea socio | 201, 400, 409 |
| PUT | /api/socios/{id} | Modifica socio | 200 |
| DELETE | /api/socios/{id} | Borra socio | 204, 404 |
| GET | /api/prestamos | Lista préstamos y filtra por estado | 200 |
| GET | /api/prestamos/{id} | Busca préstamo | 200, 404 |
| POST | /api/prestamos | Crea préstamo | 201, 400, 404, 409 |
| PATCH | /api/prestamos/{id}/estado | Cambia estado | 200, 400, 404, 409 |

## Reglas de negocio

1. No se permiten libros con ISBN duplicado. Devuelve 409.
2. No se permiten socios con email duplicado. Devuelve 409.
3. Un libro no puede tener más de un préstamo activo o vencido. Devuelve 409.
4. Un socio puede tener hasta tres préstamos activos o vencidos. Devuelve 409.
5. Los estados de los préstamos deben respetar las transiciones permitidas.

## Pruebas

En `pruebas.http` están las peticiones de ejemplo para ejecutar desde IntelliJ IDEA.
