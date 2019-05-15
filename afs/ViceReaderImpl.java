// Implementación de la interfaz de servidor que define los métodos remotos
// para completar la descarga de un fichero
package afs;
import java.rmi.*;
import java.rmi.server.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ViceReaderImpl extends UnicastRemoteObject implements ViceReader {
    private static final String AFSDir = "AFSDir/";
    private RandomAccessFile f;

    public ViceReaderImpl(String fileName,String modo /* añada los parámetros que requiera */)
		    throws RemoteException, FileNotFoundException {
    	f = new RandomAccessFile(fileName, modo );	
    }
    public byte[] read(int tam) throws RemoteException {
        //A medio hacer
    	byte [] b = new byte[tam];
    	int  c=0;
    	try {
			while(f.read()!=-1) {
				b[c]=f.readByte();	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return b;
    }
    }
    public void close() throws RemoteException {
        return;
    }
}       

