package app.controller;

import app.Main;
import app.controller.tool.ActionTableColumn;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommonStoreController extends StoreController {

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

        searchText.setOnKeyPressed(this::searchTextKeyAction);

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

    @FXML
    void changeStoreAction(ActionEvent event) {
        order(Main.Please.showSuperStore);
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
        TableColumn<Subject, String> actionColumn = ActionTableColumn.make("Akcja", Suite.set("delete", aproot().getString("delete")),
                s -> {
                    if ("delete".equals(s.get("value").orGiven(""))) {
                        tableView.getSelectionModel().clearAndSelect(s.get("row").asExpected());
                        deleteSelected(store.isAdvancedMode());
                    }
                });
        list.add(actionColumn);
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
        }
    }
}
