# Alkywall

Este proyecto es una aplicación que simula un sistema de gestión de billeteras electrónicas. Proporciona una API para crear y administrar usuarios, realizar transacciones y administrar saldos en diferentes monedas.

## Documentación de la API con Swagger

Este proyecto proporciona documentación de la API utilizando Swagger, lo que te permite explorar e interactuar con los diferentes endpoints de forma sencilla. Swagger UI ofrece una documentación interactiva que te permite probar los endpoints directamente desde el navegador.

## Cómo acceder a la documentación de la API con Swagger

Para acceder a la documentación de la API con Swagger, seguí estos pasos:

1. Ejecuta la aplicación: asegurate de que el proyecto esté en funcionamiento.

2. Abrí tu navegador web.

3. Ingresa la URL de Swagger UI: en la barra de direcciones, escribe la siguiente URL: `http://localhost:8080/api/docs`

4. Explorá la API: se mostrará la página de Swagger UI con una lista de los endpoints disponibles, sus descripciones, los parámetros de solicitud y ejemplos de respuestas. Podés explorar los endpoints, probarlos y ver las respuestas directamente desde Swagger UI.

## Autenticación para la documentación de la API con Swagger

La documentación de la API con Swagger está protegida con autenticación JWT (JSON Web Token). Para acceder a los endpoints protegidos a través de Swagger UI, tenés que proporcionar un token de autenticación. Seguí estos pasos para autenticarte:

1. Obtené un token JWT: para obtener un token JWT válido, utiliza el endpoint `/auth/login` con las credenciales de un usuario válido. La respuesta va a incluir el token JWT, que vas a poder utilizar para la autenticación.

2. Autenticate en Swagger UI: En la esquina superior derecha de la página de Swagger UI, hacé click en el botón "Authorize". Ingresa `"Bearer {tu_token_jwt}"` en el campo "Value" y clickeá en "Authorize".

3. Accedé a los endpoints protegidos: después de una autenticación exitosa, vas a poder acceder a los endpoints protegidos y probarlos directamente desde Swagger UI.

Tené en cuenta que, para los endpoints protegidos, debés incluir el token de autenticación en el encabezado de la solicitud con el formato `"Authorization: Bearer {tu_token_jwt}"`.

## Detalles de la documentación de la API

La documentación de la API ofrece información detallada sobre cada endpoint, incluidos los parámetros de entrada, los formatos de respuesta y posibles respuestas de error. También se incluyen ejemplos de solicitudes y respuestas para ayudarte a comprender cómo interactuar con la API de manera efectiva.

## Datos de prueba

Se han generado usuarios de prueba para facilitar el uso y la evaluación del sistema. Los datos de prueba se generan a través de scripts de inicialización que se ejecutan al iniciar la aplicación, siempre y cuando no existan registros previos en las tablas correspondientes. A continuación se detallan los datos de prueba disponibles y el orden en que se ejecutan los scripts:

1. Script `RoleSeeder`:
    - Condiciones de ejecución: Se ejecuta solo si no existen registros en la tabla `roles`.
    - Propósito: Crea los roles de prueba para asignar permisos y controlar el acceso de los usuarios.

2. Script `UserSeeder`:
    - Condiciones de ejecución: Se ejecuta solo si no existen registros en la tabla `users`.
    - Propósito: Crea los usuarios de prueba con sus respectivas credenciales y roles asignados.

3. Script `AccountSeeder`:
    - Condiciones de ejecución: Se ejecuta solo si no existen registros en la tabla `accounts`.
    - Propósito: Crea las cuentas de prueba para los primeros diez usuarios con el rol de "Usuario", simulando saldos en diferentes monedas y límites de transacción.

4. Script `TransactionSeeder`:
    - Condiciones de ejecución: Se ejecuta solo si no existen registros en la tabla `transactions`.
    - Propósito: Crea transacciones de prueba con campos relevantes, como el tipo de transacción, el monto y las cuentas involucradas, para simular diferentes escenarios y probar la funcionalidad de transacciones del sistema.

Estos scripts se ejecutan en el orden mencionado, asegurando que los datos de prueba se generen correctamente y las relaciones entre usuarios, roles y cuentas se establezcan adecuadamente.

Es importante tener en cuenta que, al iniciar la aplicación por primera vez, los datos de prueba se generarán automáticamente y estarán disponibles para su uso. Si se realizan cambios en los datos de prueba o se necesitan más datos adicionales, es posible modificar los scripts correspondientes según las necesidades del proyecto.

## Usuarios

A continuación se detallan los usuarios de prueba disponibles:

1. Usuario: Rodrigo Juarez
    - Correo electrónico: rodrigo@gmail.com
    - Contraseña: pass-rodrigo
    - Rol: Usuario

2. Usuario: Renata Juarez
    - Correo electrónico: renata@gmail.com
    - Contraseña: pass-renata
    - Rol: Usuario

3. Usuario: Omar Juarez
    - Correo electrónico: omar@gmail.com
    - Contraseña: pass-omar
    - Rol: Usuario

4. Usuario: Marina Calandra
    - Correo electrónico: marina@gmail.com
    - Contraseña: pass-marina
    - Rol: Usuario

5. Usuario: Simón Neiva
    - Correo electrónico: simon@gmail.com
    - Contraseña: pass-simon
    - Rol: Usuario

6. Usuario: Iara Pou
    - Correo electrónico: iara@gmail.com
    - Contraseña: pass-iara
    - Rol: Usuario

7. Usuario: Valentino Veralli
    - Correo electrónico: valentino@gmail.com
    - Contraseña: pass-valentino
    - Rol: Usuario

8. Usuario: Carmen Juarez
    - Correo electrónico: carmen@gmail.com
    - Contraseña: pass-carmen
    - Rol: Usuario

9. Usuario: Irma Lauletta
    - Correo electrónico: irma@gmail.com
    - Contraseña: pass-irma
    - Rol: Usuario

10. Usuario: Yamil Ventura
    - Correo electrónico: yamil@gmail.com
    - Contraseña: pass-yamil
    - Rol: Usuario

11. Usuario: Juan Pérez
    - Correo electrónico: juan@gmail.com
    - Contraseña: pass-juan
    - Rol: Administrador

12. Usuario: María González
    - Correo electrónico: maria@gmail.com
    - Contraseña: pass-maria
    - Rol: Administrador

13. Usuario: Carlos López
    - Correo electrónico: carlos@gmail.com
    - Contraseña: pass-carlos
    - Rol: Administrador

14. Usuario: Ana Sánchez
    - Correo electrónico: ana@gmail.com
    - Contraseña: pass-ana
    - Rol: Administrador

15. Usuario: Pedro Martínez
    - Correo electrónico: pedro@gmail.com
    - Contraseña: pass-pedro
    - Rol: Administrador

16. Usuario: Laura Torres
    - Correo electrónico: laura@gmail.com
    - Contraseña: pass-laura
    - Rol: Administrador

17. Usuario: José Rodríguez
    - Correo electrónico: jose@gmail.com
    - Contraseña: pass-jose
    - Rol: Administrador

18. Usuario: Luis Hernández
    - Correo electrónico: luis@gmail.com
    - Contraseña: pass-luis
    - Rol: Administrador

19. Usuario: Marta López
    - Correo electrónico: marta@gmail.com
    - Contraseña: pass-marta
    - Rol: Administrador

20. Usuario: Javier Fernández
    - Correo electrónico: javier@gmail.com
    - Contraseña: pass-javier
    - Rol: Administrador

Los usuarios de prueba están disponibles para su uso y pueden ser utilizados para acceder al sistema y evaluar sus funcionalidades. A continuación se detallan las instrucciones para acceder al sistema utilizando las credenciales de los usuarios de prueba:

1. URL de la API: [http://localhost:8080](http://localhost:8080)
2. Utiliza uno de los usuarios de prueba mencionados anteriormente para realizar una solicitud de inicio de sesión (POST /auth/login) proporcionando el correo electrónico y la contraseña correspondientes.
3. Una vez autenticado, puedes utilizar los demás endpoints de la API para probar las diferentes funcionalidades del sistema.

La dirección de correo electrónico y la contraseña generadas para los usuarios de prueba siguen una estrategia mnemotécnica simple:

- Correo electrónico: Se utiliza el primer nombre del usuario seguido de "@gmail.com". Por ejemplo, si el primer nombre es "Juan", el correo electrónico será "juan@gmail.com".

- Contraseña: Se utiliza el prefijo "pass-" seguido del primer nombre del usuario. Por ejemplo, si el primer nombre es "Juan", la contraseña será "pass-juan".

Esto facilita la identificación y memorización de los datos de acceso para cada usuario de prueba. Los usuarios de prueba proporcionados en el código anterior siguen esta estrategia y se pueden utilizar junto con sus credenciales correspondientes para acceder al sistema.

## Roles

Se han generado roles de prueba para facilitar la administración de usuarios y su asignación de permisos. El script `RoleSeeder` se ejecuta siempre y cuando no existan registros en la tabla `roles`. A continuación se detallan los roles disponibles:

1. Rol: Usuario
    - Descripción: Rol básico para los usuarios normales.

2. Rol: Administrador
    - Descripción: Rol con permisos ampliados para administrar el sistema.

Los roles de prueba se utilizan para asignar permisos y controlar el acceso a las funcionalidades del sistema. Podés utilizar estos roles predefinidos al crear o modificar usuarios para establecer sus roles y permisos correspondientes.

## Cuentas

Se han generado cuentas de prueba para los primeros diez usuarios con el rol de "Usuario". Estas cuentas simulan la funcionalidad de tener saldos en diferentes monedas y límites de transacción. 

Estos datos de prueba te permiten trabajar con cuentas y simular transacciones en diferentes monedas con límites de transacción personalizados. Podés utilizar estas cuentas para probar y evaluar la funcionalidad del sistema.

## Transacciones

Se han generado transacciones de prueba con campos relevantes, como el tipo de transacción, el monto y las cuentas involucradas. Estas transacciones simulan diferentes escenarios y permiten probar la funcionalidad de transacciones del sistema.

A partir de estos datos podés trabajar con transacciones y evaluar la respuesta del sistema en diferentes situaciones.