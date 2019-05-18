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

    public ViceReaderImpl(String fileName, String modo) throws RemoteException, FileNotFoundException {
        fileName = AFSDir + fileName;
        f = new RandomAccessFile(fileName, modo);
    }

    public byte[] read(int tamLecture) throws RemoteException, IOException {

        byte[] result = new byte[tamLecture];
        int tamFile = (int) f.length();
        int puntero = 0;

        while (puntero < tamFile || puntero < tamLecture) {

            f.seek(puntero);
            result[puntero] = f.readByte();
            puntero++;
        }
        return result;
    }

    public void close() throws RemoteException, IOException {
        f.close();
    }
}
