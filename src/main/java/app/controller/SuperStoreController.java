package app.controller;

import app.controller.tool.ActionTableColumn;
import app.controller.tool.ParentHelper;
import app.core.agent.Aproot;
import app.core.agent.Controller;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.dealer.StoreDealer;
import app.modules.model.GlyphProcessor;
import app.modules.model.Store;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SuperStoreController extends Controller {

    @FXML
    private StackPane stack;

    @FXML
    private TextField searchText;

    @FXML
    private TableView<Subject> tableView;

    private String searchString = "";
    private StoreDealer storeDealer;
    private Store store;
    private File storeFile;

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
    protected void undress() {

        store.getStored().removeIf(s -> s.size() == 0);
        storeDealer.saveStore(store, storeFile);
    }

    @FXML
    void addAction(ActionEvent event) {
        tableView.requestFocus();
        Subject sub = Suite.set();
        if(searchString != null) {
            String[] glyphs = GlyphProcessor.process(searchString, false);
            for(int i = 0;i < glyphs.length && i < tableView.getVisibleLeafColumns().size(); ++i) {
                sub.put(tableView.getVisibleLeafColumn(i).getText(), glyphs[i]);
            }
        }
        store.getStored().add(sub);
        tableView.getItems().add(0, sub);
        tableView.getSelectionModel().clearAndSelect(0);
    }

    private void resetSearch() {
        searchString = "";
        searchText.setText("");
    }

    private void resetTable() {
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
        list.get(2).setVisible(false);
        list.get(3).setVisible(false);
        TableColumn<Subject, String> publicActionColumn = ActionTableColumn.make("Akcja", Suite.
                        set("open", aproot().getString("open")).
                        set("delete", aproot().getString("delete")),
                s -> {
                    switch(s.get("value").orGiven("")) {
                        case "open":
                            tableView.getSelectionModel().clearAndSelect(s.get("row").asExpected());
                            enterSelected();
                            break;
                        case "delete":
                            tableView.getSelectionModel().clearAndSelect(s.get("row").asExpected());
                            deleteSelected();
                            break;
                    }
                });
        list.add(publicActionColumn);
        TableColumn<Subject, String> protectedActionColumn = ActionTableColumn.make("Akcja admin", Suite.
                        set("create", aproot().getString("create")),
                s -> {
                    switch(s.get("value").orGiven("")) {
                        case "create":
                            tableView.getSelectionModel().clearAndSelect(s.get("row").asExpected());
                            createSelected();
                            break;
                    }
                });
        protectedActionColumn.setVisible(false);
        list.add(protectedActionColumn);
        tableView.getColumns().setAll(list);
        resetTableItems();
    }

    private void resetTableItems() {
        List<Subject> items = new ArrayList<>();
        boolean pass, halt;
        String[] glyphs = GlyphProcessor.process(searchString, true);
        for(Subject it : store.getStored()) {
            pass = true;
            for(String glyph : glyphs) {
                halt = true;
                for(TableColumn<Subject, ?> column : tableView.getVisibleLeafColumns()) {
                    String str = it.get(column.getText()).orGiven("").toLowerCase();
                    if(str.contains(glyph)) {
                        halt = false;
                        break;
                    }
                }
                if(halt) {
                    pass = false;
                    break;
                }
            }
            if(pass) {
                items.add(it);
            }
        }

        tableView.getItems().setAll(items);
    }

    private void tableKeyAction(KeyEvent event) {
        switch (event.getCode()) {
            case DELETE:
                deleteSelected();
                event.consume();
                break;
            case INSERT:
                insertBeforeSelected();
                event.consume();
                break;
            case ENTER:
                enterSelected();
                event.consume();
                break;
        }
    }

    private void searchTextKeyAction(KeyEvent event) {
        switch (event.getCode()) {
            case ESCAPE:
                searchText.setText("");
                event.consume();
                break;
            case ENTER:
                if(searchString.startsWith("!")) {
                    scriptAction();
                } else {
                    addAction(new ActionEvent());
                }
                event.consume();
                break;
        }
    }

    private void deleteSelected() {
        ParentHelper.confirmation(stack, aproot().getString("deleteConfirm"), () -> {
            Collection<Subject> selected = tableView.getSelectionModel().getSelectedItems();
            store.getStored().removeAll(selected);
            tableView.getItems().removeAll(selected);
        });
    }

    private void insertBeforeSelected() {
        Subject sub = Suite.set();
        store.getStored().add(sub);
        tableView.getItems().add(tableView.getSelectionModel().getSelectedIndex(), sub);
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

    private void scriptAction() {
        switch (searchString) {
            case "!all":
                tableView.getColumns().forEach(col -> col.setVisible(true));
        }
    }
}
