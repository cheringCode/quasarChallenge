# quasar
Challenge MercadoLibre: Fuego de Quasar

Las tecnologías usadas para desarrollar la aplicacion son:

* Java Spring Boot
* Gradle
* Junit
* JPA


La aplicación está deployada en: https://quasar-chering.herokuapp.com/

Endpoints disponibles:

* Satelites:
** GET:  /satelites - Obtener la lista de satelites cargados en la base de datos
** POST: /satelite - Crear o actualizar un satelite
** DELETE: /satelite - Eliminar un satelite
  
*Topsecret:
** POST: /topsecret: Recibe en el cuerpo el json con la información para obtener el mensaje y la posición original del mismo
  
*Topsecret_split:
** POST: /satelliteName - Guarda la transmision recibida de un satelite
** GET:  devuelve (si es posible) el mensaje completo formado por todas las transmisiones y la posicion de origen del mismo
