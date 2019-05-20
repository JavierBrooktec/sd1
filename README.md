Bienvenido al proyecto en la wiki esta la teoría asociada al proyecto y las partes importantes del enunciado.

# Ejecución de la práctica
Aunque para toda la gestión del ciclo de desarrollo del código de la práctica se puede usar el IDE que se considere oportuno, para aquellos que prefieran no utilizar una herramienta de este tipo, se proporcionan una serie de scripts que permiten realizar toda la labor requerida. En esta sección, se explica cómo trabajar con estos scripts.
Para probar la práctica, debería, en primer lugar, compilar todo el código desarrollado que se encuentra en el directorio, y paquete, afs, generando los ficheros JAR requeridos por el cliente y el servidor.

cd afs <br>
./compila_y_construye_JARS.sh <br>
A continuación, hay que compilar y ejecutar el servidor, activando previamente rmiregistry. <br>
cd servidor <br>
./compila_servidor.sh <br>
./arranca_rmiregistry 12345 & <br>
./ejecuta_servidor.sh 12345 <br>
Por último, hay que compilar y ejecutar el cliente de prueba. <br>
cd cliente1 <br>
./compila_test.sh <br>
export REGISTRY_HOST=triqui3.fi.upm.es <br>
export REGISTRY_PORT=12345 <br>
export BLOCKSIZE=... # el tamaño que considere oportuno <br>
./ejecuta_test.sh <br>
Nótese que el servidor y el cliente pueden ejecutarse en distintas máquinas. Además, tenga en cuenta que, si ejecuta varios clientes en la misma máquina, debería hacerlo en diferente directorio de cliente (cliente1, cliente2...).