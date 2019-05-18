// Clase de cliente que define la interfaz a las aplicaciones.
// Proporciona la misma API que RandomAccessFile.
package afs;

import java.rmi.*;
import java.io.*;

public class VenusFile {

    public static final String cacheDir = "Cache/";
    RandomAccessFile f;

    public VenusFile(Venus venus, String fileName, String mode)
            throws RemoteException, IOException, FileNotFoundException {
        String filePath = cacheDir + fileName;
        File file = new File(filePath);
        System.out.println("filePath: " + filePath);
        if (file.exists() && !file.isDirectory()) {
            f = new RandomAccessFile(filePath, mode);
        } else {
            // file.createNewFile();
            f = new RandomAccessFile(filePath, mode);
            ViceReader viceReader = venus.srv.download(fileName, mode);
            int tam = Integer.parseInt(venus.getBlocksize());
            byte[] b = viceReader.read(tam);
            f.write(b);

        }

    }

    public int read(byte[] b) throws RemoteException, IOException {
        return f.read(b);
    }

    public void write(byte[] b) throws RemoteException, IOException {
        return;
    }

    public void seek(long p) throws RemoteException, IOException {
        return;
    }

    public void setLength(long l) throws RemoteException, IOException {
        return;
    }

    public void close() throws RemoteException, IOException {
        return;
    }
}
