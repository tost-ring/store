package app.modules.dealer;

import app.core.jorg.JorgReader;
import app.core.jorg.JorgWriter;
import app.modules.model.Store;

import java.io.*;

public class StoreDealer {

    public Store loadStore(File storeFile) {
        return JorgReader.read(storeFile);
    }

    public boolean saveStore(Store store, File storeFile) {
        return false; // TODO
//        return JorgWriter.write(store, storeFile);
    }
}
