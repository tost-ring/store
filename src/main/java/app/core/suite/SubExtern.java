package app.core.suite;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SubExtern {

    public static boolean exportBinary(Subject subject, OutputStream stream) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream);
            objectOutputStream.writeObject(subject);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
