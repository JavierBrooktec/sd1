// Implementación de la interfaz de servidor que define los métodos remotos
// para iniciar la carga y descarga de ficheros
package afs;

import java.rmi.*;
import java.rmi.server.*;
import java.io.FileNotFoundException;

public class ViceImpl extends UnicastRemoteObject implements Vice {
  public ViceImpl() throws RemoteException {
  }

  public ViceReader download(String fileName, String modo) throws RemoteException, FileNotFoundException {
    return new ViceReaderImpl(fileName, modo);
  }

  public ViceWriter upload(String fileName) throws RemoteException {
    return null;
  }
}
