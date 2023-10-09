# User API Restful
Este aplicacion [spring boot](https://spring.io/projects/spring-boot) permite administrar usuarios en memoria mediante endpoints expuestos, usando la base de datos H2 y Spring JPA.

## Enpoints
A continuacion se listan los enpoints disponibles en la API que permiten realizar diferentes acciones sobre el esquema "Usuario":

#### GET USER
Permite obtener un usuario mediante su UUID. Se debe enviar por path el UUID del usuario.
#### GET ALL USER
Permite obtener la lista completa de usuarios creados en base de datos.
#### POST USER
Permite crear un usuario. Se debe enviar en body un objeto con la estructura de la entidad User.
#### PUT USER
Permite actualizar un usuario de forma completa. Se debe enviar por path el UUID del usuario y por body un objeto con la estructura de la entidad User.
#### PATCH USER
Permite actualizar un usuario de forma parcial. Se debe enviar por path el UUID del usuario y por body un objeto que contenga las claves y valores que se desean actualizar.
#### DELETE USER
Permite eliminar un usuario. Se debe enviar por path el UUID del usuario.

## Probar Endpoints
Para probar los enpoints, se debe invocar la url [http://localhost:8080/api/user/](http://localhost:8080/api/user/)

### Estructura JSON para crear y actualizar usuarios
```json
{
    "nombre": "nombre",
    "correo": "mail@mail.com",
    "contraseña": "password123",
    "telefonos": [
        {
            "numero": "23939213123",
            "codigoCiudad": "1",
            "codigoPais": "56"
        }
    ]
}
```
### Estructura JSON para actualizar parcialmente usuarios
```json
{
    "nombre": "Nuevo Nombre",
    "contrasenia": "nuevaPassword123"
}
```
```json
{
    "nombre": "Nuevo Nombre",
    "correo": "nuevomail@mail.com",
    "contrasenia": "nuevaPassword123"
}
```


## Manejo de dependencias
El manejo de dependencias utilizado es [Gradle](https://gradle.org/).
Las dependencias utilizadas son: 

* H2 Database
* Spring Data JPA persistence
* Spring web

Para ejecutar la aplicacion por consola, correr los siguientes comandos:

* gradle clean build
* gradle bootRun
