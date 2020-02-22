package app.core.jorg;

import app.core.suite.Prospect;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.core.suite.action.Expression;

import java.util.ArrayList;
import java.util.HashMap;
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
        addType(HashMap.class, Prospect::mapSubjectively, Prospect::mapObjectively, HashMap::new);
        getConstructRouter().set(Subject.class, Expression.fromSupplier(Suite::set));
    }
}
