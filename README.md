# AureaEcommerce - Tienda de Deportes Extremos

Aplicación móvil nativa Android desarrollada en Kotlin y Jetpack Compose para la venta de artículos deportivos (Skate, Roller, BMX). El proyecto implementa una arquitectura MVVM y consume una API REST propia desarrollada en Spring Boot.

## 1. Integrantes del Equipo
* Kevin Mejia
* Pablo contreras


---

## 2. Funcionalidades Principales

La aplicación cuenta con gestión de roles (Cliente y Administrador) y las siguientes características:

### Para Todos los Usuarios:
* **Login de Usuarios:** Autenticación segura mediante usuario y contraseña con persistencia de sesión usando DataStore.
* **Catálogo de Productos:** Visualización de productos filtrados por categorías (Skate, Roller, BMX) o vista general.
* **Detalle de Producto:** Vista individual con descripción, precio e imagen detallada.
* **Carrito de Compras:**
    * Agregar productos.
    * Modificar cantidades (aumentar/disminuir).
    * Visualizar total calculado en tiempo real.
    * Finalizar compra (simulación).
* **Navegación Intuitiva:** Uso de `Navigation Drawer` (menú lateral) y barra superior con navegación hacia atrás.
* **Información:** Pantalla "Quiénes Somos" con información institucional.

### Exclusivo Rol Administrador:
* **Panel de Administración:** Acceso protegido a la gestión de inventario.
* **CRUD de Productos:**
    * **Crear:** Formulario para ingresar nuevos productos.
    * **Editar:** Modificación de nombre, precio, descripción y categoría.
    * **Eliminar:** Borrado de productos de la base de datos.

---

## 3. Endpoints y Arquitectura

### Microservicio (Backend)
La aplicación se conecta a una API REST desarrollada en Spring Boot (Java 21) alojada localmente para desarrollo o en la nube.
* **Repositorio API:** <a href="https://github.com/Pablitttoo/aurea-api"> aurea-api </a> 
* **Base de Datos:** PostgreSQL (Supabase).
* **URL Base en Android:** `http://10.0.2.2:8080/api/` (Loopback para emulador Android).

### Endpoints Utilizados
La comunicación se realiza mediante **Retrofit**.

| Método | Endpoint          | Descripción                                      | Requiere Token |
| :---   | :---              | :---                                             | :---: |
| `POST` | `/login`          | Autenticación de usuario y obtención de Token.   | NO    |
| `GET`  | `/products`       | Obtener lista completa de productos.             | NO    |
| `GET`  | `/products/{id}`  | Obtener detalles de un producto específico.      | NO    |
| `POST` | `/products`       | Crear un nuevo producto (Admin).                 | SI    |
| `PUT`  | `/products/{id}`  | Actualizar un producto existente (Admin).        | SI    |
| `DELETE`| `/products/{id}` | Eliminar un producto (Admin).                    | SI    |

---

## 4. Pasos para Ejecutar el Proyecto

### Requisitos Previos
1.  **Android Studio:** Ladybug o superior.
2.  **JDK:** Versión 17 o 21 configurada en el IDE.
3.  **Backend Corriendo:** Asegurarse de que el proyecto `aurea-api` (Spring Boot) esté ejecutándose en el puerto `8080` y conectado a la base de datos Supabase.

### Instrucciones
1.  **Clonar/Abrir:** Abrir la carpeta `AureaEcommerce` en Android Studio.
2.  **Sincronizar:** Permitir que Gradle descargue las dependencias (Retrofit, Coil, Room, Compose, etc.).
3.  **Configurar IP (Opcional):** Si usas un dispositivo físico en lugar del emulador, cambia la `BASE_URL` en `RetrofitClient.kt` por la IP local de tu PC (ej: `192.168.1.X`).
4.  **Ejecutar:** Presionar el botón "Run" (Shift+F10) seleccionando un emulador o dispositivo conectado.

---

## 5. Evidencia de Entrega (APK Firmado y Keystore)

### Captura del archivo .jks y APK generado
<img width="415" height="137" alt="image" src="https://github.com/user-attachments/assets/732544f7-685a-4d92-a243-b5e740e407ed" />


