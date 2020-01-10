package app.modules.dealer;

import app.modules.model.Store;

import java.io.*;

public class StoreDealer {

    public Store loadStore(File storeFile) throws IOException, ClassNotFoundException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(storeFile))){
            return (Store)objectInputStream.readObject();
        }
    }

    public void saveStore(Store store, File storeFile) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(storeFile))){
            objectOutputStream.writeObject(store);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
