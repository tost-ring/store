package app.controller;

import app.controller.tool.ActionTableColumn;
import app.core.agent.Aproot;
import app.core.agent.Controller;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.dealer.StoreDealer;
import app.controller.tool.GlyphProcessor;
import app.modules.model.Store;
import javafx.beans.binding.StringBinding;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuperStoreController extends StoreController {

    @Override
    protected Subject employ(Subject subject) {

        storeDealer = subject.get(StoreDealer.class).orDo(StoreDealer::new);
        storeFile = new File("super-store.jorg");
        store = storeDealer.loadStore(storeFile);
        if(store == null) {
            store = new Store("Nazwa", "Dodatkowe informacje", "Lokalizacja", "Kolumny");
            store.getStored().add(Suite.set("Nazwa", "Przykładowy magazyn").set("Lokalizacja", "store.jorg"));
        }

        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            searchString = newValue == null ? "" : newValue;
            if(!searchString.startsWith("!")) {
                resetTableItems();
            }
        });

        searchText.setOnKeyPressed(this::searchTextKeyAction);

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableView.setOnKeyPressed(this::tableKeyAction);

        resetSearch();
        resetTable();

        return Suite.set();
    }

    @Override
    void resetTable() {
        List<TableColumn<Subject, String>> list = new ArrayList<>();
        for(String it : store.getColumns()) {
            TableColumn<Subject, String> column = new TableColumn<>(it);
            column.setMaxWidth(Double.MAX_VALUE);
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            column.setCellValueFactory(subjectStringCellDataFeatures -> new StringBinding() {
                @Override
                protected String computeValue() {
                    return subjectStringCellDataFeatures.getValue().get(it).orGiven("");
                }
            });
            column.setOnEditCommit(subjectStringCellEditEvent -> {
                String newValue = subjectStringCellEditEvent.getNewValue();
                if(newValue == null || newValue.isEmpty()) {
                    subjectStringCellEditEvent.getRowValue().unset(it);
                } else {
                    subjectStringCellEditEvent.getRowValue().set(it, newValue);
                }
            });
            column.setEditable(true);
            list.add(column);
        }
        list.get(2).setVisible(store.isAdvancedMode());
        list.get(3).setVisible(store.isAdvancedMode());
        TableColumn<Subject, String> publicActionColumn = ActionTableColumn.make("", Suite.
                        set("open", aproot().getString("open")),
                s -> {
                    if ("open".equals(s.get("value").orGiven(""))) {
                        tableView.getSelectionModel().clearAndSelect(s.get("row").asExpected());
                        enterSelected();
                    }
                });
        list.add(publicActionColumn);

        TableColumn<Subject, String> protectedActionColumn = ActionTableColumn.make("", Suite.
                        set("create", aproot().getString("create")).
                        set("delete", aproot().getString("delete")),
                s -> {
                    switch (s.get("value").orGiven("")) {
                        case "create" -> {
                            tableView.getSelectionModel().clearAndSelect(s.get("row").asExpected());
                            createSelected();
                        }
                        case "delete" -> {
                            tableView.getSelectionModel().clearAndSelect(s.get("row").asExpected());
                            deleteSelected(store.isAdvancedMode());
                        }
                    }
                });
        protectedActionColumn.setVisible(store.isAdvancedMode());
        list.add(protectedActionColumn);

        tableView.getItems().clear();
        tableView.getColumns().setAll(list);
        resetTableItems();
    }

    private void tableKeyAction(KeyEvent event) {
        switch (event.getCode()) {
            case DELETE -> {
                deleteSelected(store.isAdvancedMode());
                event.consume();
            }
            case INSERT -> {
                insertBeforeSelected();
                event.consume();
            }
            case ENTER -> {
                enterSelected();
                event.consume();
            }
        }
    }

    private void enterSelected() {
        Subject subject = tableView.getSelectionModel().getSelectedItem();
        if(subject != null) {
            String path = subject.get("Lokalizacja").orGiven(null);
            if(path != null) {
                String name = subject.get("Nazwa").orGiven("Magazyn bez nazwy");
                File storeFile = new File(path);
                Store store = storeDealer.loadStore(storeFile);

                if (store == null) {
                    new Alert(Alert.AlertType.ERROR, aproot().getString("storeReadFail")).show();
                } else {
                    order(Suite.
                            set(Aproot.Please.showView).
                            set(Controller.fxml, "store").
                            set(Window.class, stage()).
                            set(Scene.class, scene()).
                            set("StageTitle", "Magazynier - " + name).
                            set(Controller.dressStuff, Suite.
                                    set(Store.class, store).
                                    set(File.class, storeFile)));
                }
            }
        }
    }

    private void createSelected() {
        Subject subject = tableView.getSelectionModel().getSelectedItem();
        if(subject != null) {
            String path = subject.get("Lokalizacja").orGiven(null);
            if( path == null || path.isBlank()) {
                new Alert(Alert.AlertType.ERROR, aproot().getString("emptyFileLocationError")).show();
            } else {
                String name = subject.get("Nazwa").orGiven("Magazyn bez nazwy");
                File storeFile = new File(path);
                try {
                    if(storeFile.createNewFile()) {
                        Store store = new Store(GlyphProcessor.process(subject.get("Kolumny").orGiven(""), false));
                        store.setAdvancedMode(this.store.isAdvancedMode());
                        order(Suite.
                                set(Aproot.Please.showView).
                                set(Controller.fxml, "store").
                                set(Window.class, stage()).
                                set(Scene.class, scene()).
                                set("StageTitle", "Magazynier - " + name).
                                set(Controller.dressStuff, Suite.
                                        set(Store.class, store).
                                        set(File.class, storeFile)));
                    } else {
                        new Alert(Alert.AlertType.ERROR, aproot().getString("fileExistError")).show();
                    }
                } catch (IOException | SecurityException e) {
                    new Alert(Alert.AlertType.ERROR, aproot().getString("fileCreationError")).show();
                }
            }
        }
    }
}
