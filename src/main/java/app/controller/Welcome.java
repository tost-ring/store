package app.controller;

import app.core.agent.Controller;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.core.flow.FlowArrayList;
import app.modules.model.Item;
import app.modules.model.Store;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.Comparator;

public class Welcome extends Controller {

    public static final Object fieldsToken = new Object();
    public static final Object disableColumnsEditor = new Object();

    @FXML
    private StackPane stack;

    @FXML
    private HBox path;

    @FXML
    private TextField searchText;

    @FXML
    private ListView<Item> list;

    @FXML
    private TextField selectedText;

    @FXML
    private TextField editedText;

    @FXML
    private CheckBox oneLevelCheckbox;

    private Item item;
    private Store store;
    private String searchString = "";

    @Override
    protected Subject employ(Subject subject) {

        item = subject.get(Item.class);
        store = subject.get(Store.class);

        oneLevelCheckbox.setSelected(subject.is("OneLevel"));

        list.setOnKeyPressed(this::listKeyAction);

        list.setOnMouseClicked(event -> {
            if(event.getClickCount() % 2 == 0) {
                pickSelectedAction(new ActionEvent());
            }
        });

        list.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                selectedText.setText(str(newValue));
                editedText.setText(str(newValue));
        });

        list.setOnDragDetected(event -> {
            Item selected = list.getSelectionModel().getSelectedItem();
            if(selected != null){
                Dragboard dragboard = list.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                order("Suite").set(Item.class, selected);
                content.putString("Suite");
                dragboard.setContent(content);
            }

            event.consume();
        });

        list.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if(dragboard.hasString()){
                String mode = dragboard.getString();
                Subject s = order(mode);
                if(s.is(Item.class)) {
                    Item dropped = s.get(Item.class);
                    if(!item.isInPathOf(dropped)) {
                        dropped.setParent(item);
                        resetList();
                        success = true;
                    }
                }
            }
            event.setDropCompleted(success);

            event.consume();
        });

        list.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
            event.consume();
        });

        list.setOnDragDone(event -> {
            resetList();
        });

        list.setOnContextMenuRequested(this::openSelectedAction);

        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            searchString = newValue;
            resetList();
        });

        oneLevelCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> resetList());

        resetSearch();
        resetPath();
        resetList();

        return Suite.set();
    }

    private void resetSearch() {
        searchString = "";
        searchText.setText("");
    }

    private void resetPath() {
        FlowArrayList<Node> nodes = new FlowArrayList<Node>();
        for(Item it = item;it != null;it = it.getParent()) {
            Item finalIt = it;
            Button itButton = new Button(it.getName());
            itButton.setOnAction(event -> {
                item = finalIt;
                resetSearch();
                resetPath();
                resetList();
            });
            itButton.setOnContextMenuRequested(event -> {
                aproot().showView(this, Suite
                        .set(fxml, "welcome")
                        .set("StageTitle", "Magazynier")
                        .set(employStuff, Suite
                                .set(Store.class, store)
                                .set(Item.class, finalIt)));
//                                .sif("OneLevel", oneLevelCheckbox.isSelected())));
            });
            nodes.add(0, itButton);
            if(it.getParent() != null) {
                nodes.add(0, new Label(">"));
            }
        }

        path.getChildren().setAll(nodes);
    }

    private void resetList() {
        FlowArrayList<Item> items = new FlowArrayList<>();
        for(Item it : store) {
            if(it.getName().contains(str(searchString)) && it.isComponentOf(item, oneLevelCheckbox.isSelected())) {
                items.add(it);
            }
        }

        list.getItems().setAll(items.sortedBy(Comparator.comparing(Item::getName)));
    }

    @FXML
    private void listKeyAction(KeyEvent event) {
        switch (event.getCode()) {
            case DELETE:
                deleteSelectedAction(new ActionEvent());
                break;
            case ENTER:
                editedText.requestFocus();
                break;
            case INSERT:
                createAction(new ActionEvent());
        }
    }

    @FXML
    private void editorKeyAction(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
            case DOWN:
            case ESCAPE:
                list.requestFocus();
                event.consume();
                break;
        }
    }

    @FXML
    void clearSearchAction(ActionEvent event) {
        resetSearch();
        searchText.requestFocus();
    }

    @FXML
    void createAction(ActionEvent event) {
        String name = editedText.getText();
        if(name.isBlank()) {
            editedText.requestFocus();
        } else {
            Item created = new Item(name, item);
            store.add(created);
            resetSearch();
            resetList();
            list.getSelectionModel().select(created);
        }
    }

    @FXML
    void deleteSelectedAction(ActionEvent event) {
        int selected = list.getSelectionModel().getSelectedIndex();
        if(selected >= 0) {
            store.remove(list.getItems().get(selected));
            resetList();
            list.getSelectionModel().select(selected > 0 ? selected - 1 : 0);
        }
    }

    @FXML
    void editSelectedAction(ActionEvent event) {
        Item selected = list.getSelectionModel().getSelectedItem();
        if(selected != null) {
            String newName = editedText.getText();
            if(newName.isBlank()) {
                editedText.requestFocus();
            } else {
                selected.setName(newName);
                resetSearch();
                resetList();
                list.getSelectionModel().select(selected);
            }
        }
    }

    void pickSelectedAction(ActionEvent event) {
        Item selected = list.getSelectionModel().getSelectedItem();
        if(selected != null) {
            item = selected;
            resetSearch();
            resetPath();
            resetList();
        }
    }

    void openSelectedAction(ContextMenuEvent event) {
        Item selected = list.getSelectionModel().getSelectedItem();
        if(selected != null) {
            aproot().showView(this, Suite
                    .set(fxml, "welcome")
                    .set("StageTitle", "Magazynier")
                    .set(employStuff, Suite
                            .set(Store.class, store)
                            .set(Item.class, selected)));
//                            .sif("OneLevel", oneLevelCheckbox.isSelected())));
        }
    }

    private String str(Object o) {
        return o != null ? o.toString() : "";
    }
}
