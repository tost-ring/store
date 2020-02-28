package app.controller.tool;

import app.core.suite.action.Impression;
import app.core.suite.action.Statement;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ParentHelper {

    public static AnchorPane stretch(Node node) {
        AnchorPane anchorPane = new AnchorPane();
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        anchorPane.getChildren().add(node);
        return anchorPane;
    }

    public static void confirmation(Pane target, String message, Statement onConfirm) {
        Label closeConfirm = new Label(message);
        closeConfirm.setFocusTraversable(true);
        closeConfirm.setFont(Font.font("System", FontWeight.BOLD, 30.0));
        closeConfirm.setTextFill(Paint.valueOf("#FFFFFF"));
        closeConfirm.setMaxHeight(Double.MAX_VALUE);
        closeConfirm.setMaxWidth(Double.MAX_VALUE);
        closeConfirm.setAlignment(Pos.CENTER);
        closeConfirm.setStyle("-fx-background-color: rgba(0, 0, 0, 0.67)");
        closeConfirm.setOnKeyPressed(event -> {
            event.consume();
            target.getChildren().remove(closeConfirm);
            if(event.getCode() == KeyCode.ENTER) {
                onConfirm.revel();
            }
        });
        target.getChildren().add(closeConfirm);
        closeConfirm.requestFocus();
    }
}
