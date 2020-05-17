package app.controller;

import app.controller.tool.GlyphProcessor;
import app.controller.tool.ParentHelper;
import app.core.agent.Controller;
import app.core.suite.Subject;
import app.core.suite.Suite;
import app.modules.dealer.StoreDealer;
import app.modules.model.Store;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class StoreController extends Controller {

    @FXML
    StackPane stack;

    @FXML
    TextField searchText;

    @FXML
    TableView<Subject> tableView;

    String searchString;

    StoreDealer storeDealer;
    Store store;
    File storeFile;

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
        store.getStored().add(0, sub);
        tableView.getItems().add(0, sub);
        tableView.getSelectionModel().clearAndSelect(0);
    }

    void resetSearch() {
        searchString = "";
        searchText.setText("");
    }

    abstract void resetTable();

    void resetTableItems() {
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

    void searchTextKeyAction(KeyEvent event) {
        switch (event.getCode()) {
            case ESCAPE -> {
                searchText.setText("");
                event.consume();
            }
            case ENTER -> {
                if (searchString.startsWith("!")) {
                    scriptAction();
                } else {
                    addAction(new ActionEvent());
                }
                event.consume();
            }
        }
    }

    void deleteSelected() {
        ParentHelper.confirmation(stack, aproot().getString("deleteConfirm"), () -> deleteSelected(true));
    }

    void deleteSelected(boolean confirmed) {
        if(confirmed) {
            Collection<Subject> selected = tableView.getSelectionModel().getSelectedItems();
            store.getStored().removeAll(selected);
            tableView.getItems().removeAll(selected);
        } else deleteSelected();
    }

    void insertBeforeSelected() {
        Subject sub = Suite.set();
        store.getStored().add(0, sub);
        tableView.getItems().add(tableView.getSelectionModel().getSelectedIndex(), sub);
    }

    void scriptAction() {
        switch (searchString) {
            case "!root" -> {
                store.setAdvancedMode(true);
                resetTable();
                searchText.setText("");
            }
            case "!user" -> {
                store.setAdvancedMode(false);
                resetTable();
                searchText.setText("");
            }
        }
    }
}
