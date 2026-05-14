# 🚀 Proyecto Spring Boot REST API

Este proyecto es una aplicación desarrollada con **Spring Boot** que expone una serie de **endpoints REST** y se ejecuta dentro de un contenedor **Docker** de forma local.

---

## 🧱 Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Web (REST)
- Maven / Gradle
- Docker

---

## 📦 Requisitos previos

Antes de ejecutar el proyecto asegúrate de tener instalado:

- Java JDK
- Maven o Gradle
- Docker

Verifica Docker:
```bash
docker --version 
```

## ▶ Ejecucción

Crear una imagen del dockerfile
```bash
docker build -t 'image-name' 
```

Ejercutar un coontenedor a partir de la imagen creada
```bash
docker run -d -p 8080:8080 'container-name' 
```

Verficar el funcionamineto en un navegador o un agente de pruebas (Postman, cURL)

