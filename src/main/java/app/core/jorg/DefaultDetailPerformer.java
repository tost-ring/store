package app.core.jorg;

import app.core.suite.Prospect;

import java.util.ArrayList;
import java.util.HashSet;

public class DefaultDetailPerformer extends DetailPerformer {

    private static DefaultDetailPerformer instance = new DefaultDetailPerformer();

    public static DefaultDetailPerformer getInstance() {
        return instance;
    }

    public DefaultDetailPerformer() {
        super();
        addType(ArrayList.class, Prospect::collectionSubjectively, Prospect::collectionObjectively, ArrayList::new);
        addType(HashSet.class, Prospect::collectionSubjectively, Prospect::collectionObjectively, HashSet::new);
//        getConstructRouter().set(Subject.class, Expression.fromSupplier(Suite::set)); TODO
    }
}
