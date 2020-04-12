package app.controller;

import app.Main;
import app.controller.tool.ActionTableColumn;
import app.controller.tool.ParentHelper;
import app.core.agent.Controller;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.dealer.StoreDealer;
import app.modules.model.Store;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StoreController extends Controller {

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

        storeDealer = subject.get(StoreDealer.class).orDo(StoreDealer::new);

        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            searchString = newValue;
            resetTableItems();
        });

        searchText.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE) {
                searchText.setText("");
                event.consume();
            }
        });

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableView.setOnKeyPressed(this::tableKeyAction);



        return Suite.set();
    }

    @Override
    protected Subject dress(Subject subject) {

        store = subject.get(Store.class).asExpected();
        storeFile = subject.get(File.class).asExpected();

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
            String[] glyphs = stringToGlyphs(searchString, false);
            for(int i = 0;i < glyphs.length && i < tableView.getVisibleLeafColumns().size(); ++i) {
                sub.put(tableView.getVisibleLeafColumn(i).getText(), glyphs[i]);
            }
        }
        store.getStored().add(sub);
        tableView.getItems().add(0, sub);
        tableView.getSelectionModel().clearAndSelect(0);
    }

    @FXML
    void changeStoreAction(ActionEvent event) {
        order(Main.Please.showSuperStore);
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
        TableColumn<Subject, String> actionColumn = ActionTableColumn.make("Akcja", Suite.set("delete", aproot().getString("delete")),
                s -> {
                    switch(s.get("value").orGiven("")) {
                        case "delete":
                            tableView.getSelectionModel().clearAndSelect(s.get("row").asExpected());
                            deleteSelected();
                            break;
                    }
                });
        list.add(actionColumn);
        tableView.getColumns().setAll(list);

        resetTableItems();
    }

    private void resetTableItems() {
        List<Subject> items = new ArrayList<>();
        String[] glyphs = stringToGlyphs(searchString, true);
        boolean pass, halt;
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

    private String str(Object o) {
        return o != null ? o.toString() : "";
    }

    private String[] stringToGlyphs(String str, boolean toLowerCase) {
        str = str(str);
        if(toLowerCase) {
            str = str.toLowerCase();
        }
        String[] glyphs = str.split("\\s+");
        for(int i = 0;i < glyphs.length; ++i) {
            glyphs[i] = glyphs[i].replaceAll("([^_])?_([^_])?", "$1 $2");
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


}
