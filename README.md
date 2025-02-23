# Gestor de Aulas

## Descripción
Gestor de Aulas es una aplicación web desarrollada con Spring Boot que permite administrar y gestionar eficientemente los recursos educativos de una institución. El sistema facilita la gestión de edificios, aulas, reservas y usuarios.

## Características Principales
- Gestión de edificios y plantas
- Administración de aulas y sus características
- Sistema de reservas de aulas
- Gestión de usuarios y roles (Admin/Usuario)
- Gestión de cursos y tipos de aulas
- Interfaz responsive con modo oscuro
- Sistema de autenticación y autorización

##  Tecnologías Utilizadas
- **Backend:**
  - Java 8
  - Spring Boot 2.6.3
  - Spring Security
  - Spring Data JPA
  - MySQL
    
- **Frontend:**
  - Thymeleaf
  - Bootstrap 4.6.2
  - Font Awesome 5.15.4
  - JavaScript

## Requisitos Previos
- JDK 1.8 o superior
- Maven 3.x
- MySQL 5.7 o superior
- IDE compatible con Spring Boot (recomendado: Spring Tool Suite para Eclipse)

## Instalación en Windows

### 1. Preparación del Entorno
1. Instalar Java:
   - Descargar JDK 8 desde [Oracle](https://www.oracle.com/java/technologies/downloads/#java8)
   - Ejecutar el instalador
   - Configurar JAVA_HOME en variables de entorno

2. Instalar Maven:
   - Descargar [Maven](https://maven.apache.org/download.cgi)
   - Extraer en C:\Program Files\Apache\maven
   - Añadir a PATH: C:\Program Files\Apache\maven\bin

3. Instalar MySQL:
   - Descargar [MySQL Installer](https://dev.mysql.com/downloads/installer/)
   - Ejecutar instalador
   - Configurar usuario root y contraseña

### 2. Configuración de Base de Datos
1. Abrir MySQL Command Line Client:
Crear la base de datos en MySQL, debe llamarse gestoraulas, una vez creada la BBDD, ejecutar la siguiente query:
```sql
USE gestoraulas;

-- Crear el tipo de usuario Admin (REQUERIDO)
INSERT INTO TIPOUSUARIO (tipo, descripcion, activo) 
VALUES ('Admin', 'Usuario con permisos totales del sistema', true);
```

### 3. Configuración del Proyecto
1. Clonar el repositorio:
```bash
git clone https://github.com/tu-usuario/gestor-aulas.git
cd gestor-aulas
```

2. Configurar application.properties:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestoraulas
spring.datasource.username=root
spring.datasource.password=tu_contraseña
```

3. Compilar el proyecto:
```bash
mvn clean install
```

4. Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

5. Acceder a la aplicación:
   - Abrir navegador: http://localhost:8080
   - Registrar el primer usuario que será automáticamente Admin

## Estructura del Proyecto
```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── KevinYLeandro/
│   │           ├── controller/
│   │           ├── model/
│   │           ├── repository/
│   │           ├── service/
│   │           └── GestorAulasApplication.java
│   └── resources/
│       ├── static/
│       │   ├── css/
│       │   └── js/
│       ├── templates/
│       └── application.properties
```

## Seguridad
La aplicación implementa Spring Security para la autenticación y autorización:
- Sistema de login
- Roles de usuario (ADMIN/USER)
- Protección de rutas según rol
- Encriptación de contraseñas

## Características de la Interfaz
- Diseño responsive
- Modo oscuro/claro
- Interfaz intuitiva
- Componentes reutilizables
- Mensajes de feedback para el usuario
- Calendario interactivo para reservas

## Contribución
Si deseas contribuir al proyecto:
1. Haz un Fork del repositorio
2. Crea una nueva rama (`git checkout -b feature/nueva-caracteristica`)
3. Realiza tus cambios y haz commit (`git commit -m 'Añadir nueva característica'`)
4. Push a la rama (`git push origin feature/nueva-caracteristica`)
5. Abre un Pull Request

## Autores
- Kevin
- Leandro

## Contacto
Para dudas o sugerencias:
- Email: 
  - leandro.perez@colegiomiralmonte.es
  - kevin.adan@colegiomiralmonte.es
- GitHub: 
  - [Skade2050](https://github.com/Skade2050)
  - [KevinAZ](https://github.com/KevinAZ)

## Solución de Problemas Comunes
1. Error de conexión a MySQL:
   - Verificar que MySQL está ejecutándose
   - Comprobar credenciales en application.properties
   - Verificar que el puerto 3306 está disponible

2. Error al compilar:
   - Verificar versión de Java
   - Limpiar y recompilar: `mvn clean install`

3. Error "Table 'TIPOUSUARIO' doesn't exist":
   - Ejecutar la query de inserción del Admin
   - Verificar que la base de datos se creó correctamente
