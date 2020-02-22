package app.modules.dealer;

import app.core.jorg.JorgReader;
import app.core.jorg.JorgWriter;
import app.modules.model.Store;

import java.io.*;

public class StoreDealer {

    public Store loadStore(File storeFile) throws FileNotFoundException {
        JorgReader reader = new JorgReader();
        reader.read(new FileInputStream(storeFile));
        return reader.getObjects().get("store");
    }

    public void saveStore(Store store, File storeFile) throws FileNotFoundException {
        JorgWriter writer = new JorgWriter();
        writer.addObject(store, "store");
        writer.write(new FileOutputStream(storeFile));
    }
}
