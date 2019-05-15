# Índice
  *  [Espacio de Nombres](#Espacio-de-Nombres)
  *  [Resolución de nombres](#Resolución-de-nombres)
  *  [Localización de ficheros](#Localización-de-ficheros)
  *  [Acceso a datos del fichero](#Acceso-a-datos-del-fichero)
  *  [Semántica de uso concurrente](#Semántica-de-uso-concurrente)
  *  [Modelo de acceso](#Modelo-de-acceso)
  *  [Modelo carga/descarga](#Modelo-carga/descarga)
  *  [Gestión de cache](#Gestión-de-cache)
  *  [Uso de cache en clientes](#Uso-de-cache-en-clientes)
  *  [Política de actualización](#Política-de-actualización)
  *  [Coherencia de cache](#Coherencia-de-cache)
  *  [Coherencia de cache: semántica de sesión](#Coherencia-de-cache:-semántica-de-sesión)
  *  [Gestión de cerrojos](#Gestión-de-cerrojos)
  *  [Andrew File System (AFS)](#Andrew-File-System)


# <a name="Espacio-de-Nombres"></a> Espacio de Nombres
  - Alternativas en la composición:
    + Único espacio de nombres en todas las máquinas (AFS)
      * Montaje en servidor: información de montaje se almacena en servidor
      * Espacio de nombres común para el SD

# <a name="Resolución-de-nombres"></a> Resolución de nombres  
  - Traducir una ruta que se extiende por varios servidores
  - ¿Quién busca cada componente de la ruta?
    + cliente: solicita contenido del directorio al servidor y busca (AFS)
  - “Cache de nombres” en clientes
    + Almacén de relaciones entre rutas y nombres internos 
    + Evita repetir proceso de resolución
      * Operación más rápida y menor consumo de red (escalabilidad)
    + Necesidad de coherencia
      * Fichero borrado y nombre interno reutilizado
      * Uso de contador de versión del inodo
        - id. de máquina + id. disco + id. partición + id. inodo + nº versión

# <a name="Localización-de-ficheros"></a> Localización de ficheros:
  - Resolución obtiene nombre interno (UFID)
  - Uso de UFID que no contenga información de máquina:
    + Por ejemplo (AFS):
        id. único de volumen + id. inodo + nº versión
    + Permite migración de volúmenes
    + Requiere esquema de localización: Volumen --> Máquina
  - Tablas que mantengan la información de ubicación (AFS)
  - Uso de “cache de localizaciones” en clientes

# <a name="Acceso-a-datos-del-fichero"></a> Acceso a datos del fichero
  - Una vez abierto el fichero, se tiene info. para acceder al mismo
  - Aspectos de diseño vinculados con acceso a datos:
    + ¿Qué se garantiza ante accesos concurrentes?
        * Semántica de uso concurrente
    + ¿Qué información se transfiere entre cliente y servidor?
        * Modelo de acceso
    + ¿Qué info. se guarda en cache y cómo se gestiona?
        * Gestión de cache

# <a name="Semántica-de-uso-concurrente"></a> Semántica de uso concurrente
  - Sesión: serie de accesos que realiza cliente entre open y close
  - La semántica especifica el efecto de varios procesos accediendo de forma simultánea al mismo fichero
  - Semántica de sesión (AFS)
    + Cambios a fichero abierto, visibles sólo en nodo que lo modificó
    + Una vez cerrado, cambios visibles sólo en sesiones posteriores
    + Múltiples imágenes simultáneas del fichero
    + Dos sesiones sobre mismo fichero que terminan concurrentemente:
      * La última deja el resultado final
    + No adecuada para procesos con acceso concurrente a un fichero

# <a name="Modelo-de-acceso"></a> Modelo de acceso
  - Modelo carga/descarga
    * Transferencias completas del fichero
    * Localmente se almacena en memoria o discos locales
    * Normalmente utiliza semántica de sesión (AFS)
    * Eficiencia en las transferencias
    * Llamada open con mucha latencia (Tarda mucho en transimitirse)

# <a name="Modelo-carga/descarga"></a> Modelo carga/descarga
  - Correspondencia petición de aplicación y mensaje de protocolo:
    + open --> mensaje de descarga (download)
      * se realiza traducción y servidor envía fichero completo
      * cliente almacena fichero en cache local
    + read/write/lseek  no implica mensajes de protocolo
      * lecturas y escrituras sobre copia local
    + close --> mensaje de carga (upload)
      * si se ha modificado, se envía fichero completo al servidor

# <a name="Gestión-de-cache"></a> Gestión de cache
  - El empleo de cache permite mejorar el rendimiento
  - Caches en múltiples niveles de un SD:
    + Cache en los clientes
      * Reduce el tráfico por la red
      * Reduce la carga en los servidores
      * Puede situarse en discos locales (no permite nodos sin disco)
        - Más capacidad pero más lento
        - No volátil, facilita la recuperación

# <a name="Uso-de-cache-en-clientes"></a> Uso de cache en clientes
  - Empleo de cache de datos en clientes
    * Mejora rendimiento y capacidad de crecimiento
    * Introduce problemas de coherencia
  - Otros tipos de cache
    * Cache de nombres
    * Cache de metadatos del sistema de ficheros
  - Políticas de gestión de cache de datos:
    * Política de actualización
    * Política de coherencia

# <a name="Política-de-actualización"></a> Política de actualización
  - No se dice nada en la práctica

# <a name="Coherencia-de-cache"></a> Coherencia de cache
  - El uso de cache en clientes produce problema de coherencia
    + ¿es coherente una copia en cache con el dato en el servidor?
  - Estrategia de validación iniciada por el servidor
    + servidor avisa a cliente (callback) al detectar que su copia es inválida
      * generalmente se usa write-invalidate (no write-update)
    + servidor almacena por cada cliente info. sobre qué ficheros guarda
      * implica un servicio con estado

# <a name="Coherencia-de-cache:-semántica-de-sesión"></a> Coherencia de cache: semántica de sesión
  - Validación iniciada por el servidor (usada en AFS versión 2):
    + Si hay copia en cache local, en apertura no se contacta con servidor
    + Servidor almacena información de qué clientes tienen copia local
    + Cuando cliente vuelca nueva versión del fichero al servidor:
      * servidor envía invalidaciones a clientes con copia
    + Disminuye nº de mensajes entre cliente y servidor
      * Mejor rendimiento y escalabilidad
    + Dificultad en la gestión de callbacks
      * No encajan fácilmente en modelo cliente-servidor clásico

# <a name="Gestión-de-cerrojos"></a> Gestión de cerrojos
  - SFD ofrecen cerrojos de lectura/escritura
    + múltiples lectores y un solo escritor
  - Peticiones lock/unlock generan mensajes correspondientes
    + lock: si factible retorna OK; sino no responde
    + unlock: envía a OK a cliente(s) en espera
  - Requiere un servicio con estado:
    + servidor almacena qué cliente(s) tienen un cerrojo de un fichero y cuáles están en espera
  - Problema: cliente con cerrojo puede caerse
    - Solución habitual: uso de leases
    - Cliente con cerrojo debe renovarlo periódicamente

# <a name="Andrew-File-System"></a> Andrew File System (AFS)
  - SFD desarrollado en Carnegie-Mellon (desde 1983)
    + Se presenta la versión AFS-2
  - Actualmente producto de Transarc (incluida en IBM)
    + OpenAFS: versión de libre distribución para UNIX y Windows 
  - Sistemas distribuidos a gran escala (5000 - 10000 nodos)
  - Distingue entre nodos cliente y servidores dedicados
    + Los nodos cliente tienen que tener disco
  - Ofrece a clientes dos espacios de nombres:
    + local y compartido (directorio /afs)
    + espacio local sólo para ficheros temporales o de arranque
  - Servidores gestionan el espacio compartido
  - Visión única en todos los clientes del espacio compartido

## Estructura de AFS
  - Dos componentes que ejecutan como procesos de usuario 
  - Venus:
    + ejecuta en los clientes
    + SO le redirecciona peticiones sobre ficheros compartidos
    + realiza las traducciones de nombres de fichero
      * resolución dirigida por el cliente
      * cliente lee directorios: requiere formato homogéneo en el sistema
  - Vice:
    + ejecuta en los servidores
    + procesa solicitudes remotas de clientes
  - Usan sistema de ficheros UNIX como almacén de bajo nivel

## Espacio de nombre compartido
  - Los ficheros se agrupan en volúmenes
    + Unidad más pequeña que un sistema de ficheros UNIX
  - Cada fichero tiene identificador único (UFID: 96 bits)
    + Número de volumen
    + Número de vnodo (dentro del volumen)
    + Número único: permite reutilizar números de vnodo
  - Los UFID son transparentes de la posición
    + un volumen pueden cambiar de un servidor a otro.
  - Soporte a la migración de volúmenes
  - Estrategia de localización
    - número de volumen --> servidor que lo gestiona
    - tabla replicada en cada servidor
    - cliente mantiene una cache de localización
      + si falla repite proceso de localización 
## Acceso a ficheros
  - Modelo de carga/descarga
    + En open servidor transfiere fichero completo al cliente
    + Versión actual: fragmentos de 64Kbytes
  - Venus almacena el fichero en la cache local de los clientes
    + Se utiliza el disco local (la cache es no volátil)
  - Lecturas/escrituras localmente sin intervenir Venus
    + Cache de UNIX opera aunque de manera transparente a AFS
  - Cuando un proceso cierra un fichero (close)
    + Si se ha modificado se envía al servidor (write-on-close)
    + Se mantiene en cache local para futuras sesiones
  - Modificaciones de directorios y atributos directamente al servidor

## Coherencia de cache
  - Semántica de sesión
  - Validación iniciada por servidor basada en callbacks
  - Cuando cliente abre fichero del que no tiene copia local (o no es válida), contacta con el servidor
    + el servidor “anota” que el fichero tiene un callback para ese cliente
  - Siguientes aperturas del fichero no contactan con servidor
  - Cuando cliente cierra un fichero que ha modificado:
    + Lo notifica y lo vuelca al servidor
    + Servidor avisa a los nodos con copia local para que la invaliden:
      * Eevoca el callback
    + Solicitud en paralelo usando una multiRPC
  - Cuando llega una revocación a un nodo:
    + procesos con fichero abierto continúan accediendo a copia anterior
    + nueva apertura cargará el nuevo contenido desde el servidor
  - Los clientes de AFS asumen que los datos en su cache son válidos mientras no se notifique lo contrario
  - El servidor almacena por cada fichero una lista de clientes que tienen copia del fichero en su cache:
    + la lista incluye a todos los clientes que tienen copia y no sólo a los que tienen abierto el fichero
  
