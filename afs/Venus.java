// Clase de cliente que inicia la interacción con el servicio de
// ficheros remotos
package afs;

import java.rmi.*;

public class Venus {

    Vice srv;
    String blocksize;

    public Venus() throws RemoteException {
        String host = System.getenv().get("REGISTRY_HOST");
        String port = System.getenv().get("REGISTRY_PORT");
        blocksize = System.getenv().get("BLOCKSIZE");

        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            srv = (Vice) Naming.lookup("//" + host + ":" + port + "/AFS");
        } catch (RemoteException e) {
            System.err.println("Error de comunicacion: " + e.toString());
        } catch (Exception e) {
            System.err.println("Excepcion en ClienteLog:");
            e.printStackTrace();
        }

    }

    /**
     * @return the srv
     */
    public Vice getSrv() {
        return srv;
    }

    /**
     * @return the blocksize
     */
    public String getBlocksize() {
        return blocksize;
    }

}
