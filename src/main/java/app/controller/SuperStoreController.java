package app.controller;

import app.controller.tool.ActionTableColumn;
import app.controller.tool.ParentHelper;
import app.core.agent.Aproot;
import app.core.agent.Controller;
import app.core.flow.FlowArrayList;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.dealer.StoreDealer;
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
import java.util.Collection;

public class SuperStoreController extends Controller {

    @FXML
    private StackPane stack;

    @FXML
    private TextField searchText;

    @FXML
    private TableView<Subject> tableView;

    private String searchString;
    private StoreDealer storeDealer;
    private Store store;
    private File storeFile;

    @Override
    protected Subject employ(Subject subject) {

        storeDealer = subject.gon(StoreDealer.class);
        storeFile = new File("super-store.jorg");
        store = storeDealer.loadStore(storeFile);
        if(store == null) {
            store = new Store("Nazwa", "Dodatkowe informacje", "Lokalizacja", "Kolumny");
            store.getStored().add(Suite.set("Nazwa", "Przykladowy magazyn").set("Lokalizacja", "store.jorg"));
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

        store.getStored().removeIf(s -> Suite.size(s) == 0);
        storeDealer.saveStore(store, storeFile);
    }

    @FXML
    void addAction(ActionEvent event) {
        tableView.requestFocus();
        Subject sub = Suite.set();
        if(searchString != null) {
            String[] glyphs = stringToGlyphs(searchString, false);
            for(int i = 0;i < glyphs.length && i < tableView.getVisibleLeafColumns().size(); ++i) {
                sub.sos(tableView.getVisibleLeafColumn(i).getText(), glyphs[i]);
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
        FlowArrayList<TableColumn<Subject, String>> list = new FlowArrayList<>();
        for(String it : store.getColumns()) {
            TableColumn<Subject, String> column = new TableColumn<>(it);
            column.setMaxWidth(Double.MAX_VALUE);
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            column.setCellValueFactory(subjectStringCellDataFeatures -> new StringBinding() {
                @Override
                protected String computeValue() {
                    return subjectStringCellDataFeatures.getValue().god(it, "");
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
        TableColumn<Subject, String> publicActionColumn = ActionTableColumn.make("Akcja", Suite.set("Otworz").set("Usun"),
                s -> {
                    switch(s.getAs("value", String.class)) {
                        case "Otworz":
                            tableView.getSelectionModel().clearAndSelect(s.getAs("row", Integer.class));
                            enterSelected();
                            break;
                        case "Usun":
                            tableView.getSelectionModel().clearAndSelect(s.getAs("row", Integer.class));
                            deleteSelected();
                            break;
                    }
                });
        list.add(publicActionColumn);
        TableColumn<Subject, String> protectedActionColumn = ActionTableColumn.make("Akcja admin", Suite.set("Utworz"),
                s -> {
                    switch(s.getAs("value", String.class)) {
                        case "Utworz":
                            tableView.getSelectionModel().clearAndSelect(s.getAs("row", Integer.class));
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
        FlowArrayList<Subject> items = new FlowArrayList<>();
        String[] glyphs = stringToGlyphs(searchString, true);
        boolean pass, halt;
        for(Subject it : store.getStored()) {
            pass = true;
            for(String glyph : glyphs) {
                halt = true;
                for(TableColumn<Subject, ?> column : tableView.getVisibleLeafColumns()) {
                    String str = it.god(column.getText(), "").toLowerCase();
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

    private String[] stringToGlyphs(String str, boolean toLowerCase) {
        if(toLowerCase) {
            str = str.toLowerCase();
        }
        String[] glyphs = str.split("\\s+");
        for(int i = 0;i < glyphs.length; ++i) {
            glyphs[i] = glyphs[i].replaceAll("([^_])_([^_])", "$1 $2");
            glyphs[i] = glyphs[i].replaceAll("__", "_");
        }
        return glyphs;
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
        ParentHelper.confirmation(stack, "Potwierdz usuniecie klikajac ENTER", () -> {
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
            String path = subject.god("Lokalizacja", null);
            if(path != null) {
                String name = subject.god("Nazwa", "Magazyn bez nazwy");
                File storeFile = new File(path);
                Store store = storeDealer.loadStore(storeFile);

                if (store == null) {
                    new Alert(Alert.AlertType.WARNING, "Nie znaleziono magazynu na dysku!").show();
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
            String path = subject.god("Lokalizacja", null);
            if(path != null) {
                String name = subject.god("Nazwa", "Magazyn bez nazwy");
                File storeFile = new File(path);
                if(storeFile.exists()) {
                    new Alert(Alert.AlertType.WARNING, "Podany plik juz istnieje!").show();
                } else {
                    Store store = new Store(stringToGlyphs(subject.getAs("Kolumny", String.class), false));
                    order(Suite.
                            set(Aproot.Please.showView).
                            set(Controller.fxml, "store").
                            set(Window.class, stage()).
                            set("StageTitle", "Magazynier - " + name).
                            set(Controller.dressStuff, Suite.
                                    set(Store.class, store).
                                    set(File.class, storeFile)));
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
