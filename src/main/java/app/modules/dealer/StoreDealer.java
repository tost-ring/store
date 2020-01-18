package app.modules.dealer;

import app.core.jorg.GeneralPerformer;
import app.core.jorg.JorgReader;
import app.core.jorg.JorgWriter;
import app.core.suite.Suite;
import app.modules.model.Store;

import java.io.*;

public class StoreDealer {

    public Store loadStore(File storeFile) throws FileNotFoundException {
        GeneralPerformer performer = new GeneralPerformer(Suite.set());
        JorgReader reader = new JorgReader(performer);
        reader.read(new FileInputStream(storeFile));
        return reader.getObjects().get("store");
    }

    public void saveStore(Store store, File storeFile) throws FileNotFoundException {
        GeneralPerformer performer = new GeneralPerformer(Suite.set());
        JorgWriter writer = new JorgWriter(performer);
        writer.addObject(store, "store");
        writer.write(new FileOutputStream(storeFile));
    }
}
