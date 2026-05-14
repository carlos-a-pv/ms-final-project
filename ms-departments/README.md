# ms-departments

Microservicio para la gestión de **departamentos**.

Expone una API REST para:

- Crear departamentos
- Consultar por id
- Listar todos
- Actualizar
- Eliminar

Incluye documentación con **OpenAPI/Swagger** y un modelo de error estándar (`APIError`) para respuestas **4xx/5xx**.

## Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- PostgreSQL
- springdoc-openapi (Swagger UI)
- Docker (multi-stage build)

## Configuración

Por defecto, la aplicación usa el puerto `8081` (ver `src/main/resources/application.properties`).

### Base de datos (PostgreSQL)

Propiedades actuales (por defecto):

- `spring.datasource.url=${SPRING_DATASOURCE_URL}`
- `spring.datasource.username=${SPRING_DATASOURCE_USERNAME}`
- `spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}`

Asegúrate de que:

- PostgreSQL esté corriendo.
- Exista la base de datos `deparments_db`.
- Exista el usuario configurado y tenga permisos.

## Documentación (Swagger)

Con el servicio en ejecución:

- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`

## Despliegue con Docker

El `Dockerfile` construye el jar usando Maven (stage de build) y ejecuta en una imagen runtime liviana (distroless).

### 1) Build de la imagen

Desde la raíz del proyecto (donde está el `Dockerfile`):

```bash
docker build -t ms-departments:latest .
```

### 2) Ejecutar el contenedor

```bash
docker run --rm -p 8081:8081 --name ms-departments ms-departments:latest
```

### Nota importante: conexión a PostgreSQL desde Docker

Si PostgreSQL corre en tu **máquina host**, dentro del contenedor `localhost` NO apunta al host. En Docker Desktop (Windows/Mac) puedes usar:

- `host.docker.internal`

Ejemplo (recomendado para pruebas rápidas):

1. Edita `application.properties` (o mejor, sobreescribe por variable de entorno):

```bash
-e "SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/deparments_db"
```

Ejemplo completo:

```bash
docker run --rm -p 8081:8081 --name ms-departments \
  -e "SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/deparments_db" \
  -e "SPRING_DATASOURCE_USERNAME=admin" \
  -e "SPRING_DATASOURCE_PASSWORD=admin123" \
  ms-departments:latest
```

Si PostgreSQL está en **otro contenedor**, lo ideal es usar Docker Compose y referenciarlo por nombre de servicio (por ejemplo `jdbc:postgresql://postgres:5432/deparments_db`).

## Pruebas rápidas (API)

Base URL:

- `http://localhost:8081/api/departments`

### Crear

```bash
curl -X POST "http://localhost:8081/api/departments" \
  -H "Content-Type: application/json" \
  -d "{\"nombre\":\"Ingeniería\",\"descripcion\":\"Departamento encargado de procesos de ingeniería.\"}"
```

### Consultar por id

```bash
curl "http://localhost:8081/api/departments/1"
```

### Listar

```bash
curl "http://localhost:8081/api/departments"
```

### Actualizar

```bash
curl -X PUT "http://localhost:8081/api/departments/1" \
  -H "Content-Type: application/json" \
  -d "{\"nombre\":\"Ingeniería\",\"descripcion\":\"Actualizado\"}"
```

### Eliminar

```bash
curl -X DELETE "http://localhost:8081/api/departments/1"
```

## Manejo de errores

Las respuestas de error retornan un objeto `APIError` que incluye:

- `timestamp`, `status`, `error`, `message`, `path`, `traceId`
- `fieldErrors` cuando hay validaciones fallidas (400)

Errores típicos:

- `400` Validación / JSON inválido / parámetros inválidos
- `404` Recurso no encontrado
- `409` Conflicto de persistencia (violación de constraints)
- `500` Error inesperado
