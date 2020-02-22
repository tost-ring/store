package app.modules.dealer;

import app.controller.InputView;
import app.core.flow.FlowArrayList;
import app.core.flow.FlowCollection;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.model.filter.*;
import app.modules.model.items.Storable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn;

import java.lang.reflect.Field;


public class StorableDealer {

    public FlowArrayList<InputView> createInputViews(Storable storable) {
        FlowArrayList<InputView> inputViews = new FlowArrayList<>();
        Subject params = storable.getParams();
        for(Field field : Suite.keys(params, Field.class)) {
            inputViews.add(new InputView(field, params.get(field)));
        }
        return inputViews;
    }

    public void pollInputViews(FlowCollection<InputView> inputViews, Storable storable) {
        Subject params = Suite.set();
        for(InputView inputView : inputViews) {
            params.set(inputView.getField(), inputView.getValue());
        }
        storable.setParams(params);
    }
}
