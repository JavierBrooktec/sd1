# Espacio de Nombres
  - Alternativas en la composición:
    + Único espacio de nombres en todas las máquinas (AFS)
      * Montaje en servidor: información de montaje se almacena en servidor
      * Espacio de nombres común para el SD

# Resolución de nombres  
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



# Localización de ficheros:
  - Resolución obtiene nombre interno (UFID)
  - Uso de UFID que no contenga información de máquina:
    + Por ejemplo (AFS):
        id. único de volumen + id. inodo + nº versión
    + Permite migración de volúmenes
    + Requiere esquema de localización: Volumen --> Máquina
  - Tablas que mantengan la información de ubicación (AFS)
  - Uso de “cache de localizaciones” en clientes

# Acceso a datos del fichero
  - Una vez abierto el fichero, se tiene info. para acceder al mismo
  - Aspectos de diseño vinculados con acceso a datos:
    + ¿Qué se garantiza ante accesos concurrentes?
        * Semántica de uso concurrente
    + ¿Qué información se transfiere entre cliente y servidor?
        * Modelo de acceso
    + ¿Qué info. se guarda en cache y cómo se gestiona?
        * Gestión de cache

# Semántica de uso concurrente
  - Sesión: serie de accesos que realiza cliente entre open y close
  - La semántica especifica el efecto de varios procesos accediendo de forma simultánea al mismo fichero
  - Semántica de sesión (AFS)
    + Cambios a fichero abierto, visibles sólo en nodo que lo modificó
    + Una vez cerrado, cambios visibles sólo en sesiones posteriores
    + Múltiples imágenes simultáneas del fichero
    + Dos sesiones sobre mismo fichero que terminan concurrentemente:
      * La última deja el resultado final
    + No adecuada para procesos con acceso concurrente a un fichero

# Modelo de acceso
  - Modelo carga/descarga
    * Transferencias completas del fichero
    * Localmente se almacena en memoria o discos locales
    * Normalmente utiliza semántica de sesión (AFS)
    * Eficiencia en las transferencias
    * Llamada open con mucha latencia (Tarda mucho en transimitirse)

# Modelo carga/descarga
  - Correspondencia petición de aplicación y mensaje de protocolo:
    + open --> mensaje de descarga (download)
      