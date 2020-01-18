package app.controller;

import app.controller.tool.PathName;
import app.core.agent.Controller;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.core.flow.FlowArrayList;
import app.modules.model.Store;
import app.modules.model.items.Storable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.Comparator;

public class SearchController extends Controller {

    @FXML
    private StackPane stack;

    @FXML
    private HBox path;

    @FXML
    private TextField searchText;

    @FXML
    private TreeView<PathName> tree;

    @FXML
    private FlowPane flow;

    private String contextString = "";
    private Subject treeNodes = Suite.set();
    private Store store;
    private String searchString;

    @Override
    protected Subject employ(Subject subject) {

        store = subject.get(Store.class);
        contextString = subject.god("context", "");


        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            searchString = newValue;
            resetFlow();
        });

        tree.setRoot(makeTree(store));

        tree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                contextString = newValue.getValue().getPathname();
                resetPath();
                resetFlow();
            }
        });



        resetSearch();
        resetPath();
        resetFlow();

        return Suite.set();
    }

    @FXML
    void addAction(ActionEvent event) {

    }

    private void resetSearch() {
        searchString = "";
        searchText.setText("");
    }

    private void resetPath() {
        FlowArrayList<Node> nodes = new FlowArrayList<>();
        String[] context = contextString.split("\\.");
        PathName pathname = new PathName();
        for(int i = 0; i < context.length; ++i) {
            pathname = pathname.extended(context[i]);
            PathName finalIt = pathname;
            Button itButton = new Button(context[i]);
            itButton.setOnAction(event -> {
                contextString = finalIt.getPathname();
                resetTreeSelection();
                resetSearch();
                resetPath();
                resetFlow();
            });
            itButton.setOnContextMenuRequested(event -> {
                aproot().showView(this, Suite.
                        set(fxml, "welcome").
                        set("StageTitle", "Magazynier").
                        set(employStuff, Suite.
                                set(Store.class, store).
                                set("context", finalIt)));
            });
            nodes.add(itButton);
            if(i + 1 < context.length) {
                nodes.add(new Label(">"));
            }
        }

        path.getChildren().setAll(nodes);
    }

    private void resetTreeSelection() {
        String[] context = contextString.split("\\.");
        TreeItem<PathName> treeItem = tree.getRoot();
        for(String it : context) {
            for(TreeItem<PathName> ti : treeItem.getChildren()) {
                if(ti.getValue().getName().equals(it)) {
                    treeItem = ti;
                    break;
                }
            }
        }
        tree.getSelectionModel().select(treeItem);
    }

    private void resetFlow() {
        FlowArrayList<Parent> items = new FlowArrayList<>();
        for(Storable it : store.getStored()) {
            if(it.searchPass(str(searchString)) && it.pathPass(contextString)) {
                items.add(makeMiniature(it));
            }
        }

        flow.getChildren().setAll(items);
    }

    private Parent makeMiniature(Storable storable) {
        Subject option = aproot().loadView(this, Suite
                .set(Controller.class, new MiniatureView()).set(employStuff, Suite
                        .set(Storable.class, storable)
                        .set(tokenString, "openElement")));
        return option.gac(Controller.class).parent();
    }

    private TreeItem<PathName> makeTree(Store store) {
        return makeTree(store.getCategoryTree(), new PathName());
    }

    private TreeItem<PathName> makeTree(Subject categoryTree, PathName pathname) {
        TreeItem<PathName> treeItem = new TreeItem<>(pathname);
        for(String it : Suite.keys(categoryTree, String.class)) {
            treeItem.getChildren().add(makeTree(categoryTree.godAs(it, Suite.set(), Subject.class), pathname.extended(it)));
        }
        return treeItem;
    }

    private String str(Object o) {
        return o != null ? o.toString() : "";
    }
}
