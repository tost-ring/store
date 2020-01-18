package app.controller;

import app.core.agent.View;
import app.core.suite.Suite;
import app.core.suite.Subject;
import app.modules.model.items.Storable;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MiniatureView extends View {

    private Storable storable;
    private Button button;
    private String token;

    @Override
    protected Parent construct(Subject subject) {
        String buttonName = subject.get("buttonName");
        button = new Button(buttonName);
        button.setContentDisplay(ContentDisplay.TOP);

        try {
            String imageName = subject.god("imageName", null);
            if(imageName != null) {
                ImageView imageView = new ImageView();
                Image image = new Image(aproot().getImageResource(imageName).openStream(), 70, 70, true, false);
                imageView.setImage(image);
                button.setGraphic(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return button;
    }

    @Override
    protected Subject employ(Subject subject) {
        token = subject.get(tokenString);
        button.setOnAction(this::buttonAction);

        return Suite.set();
    }

    private void buttonAction(ActionEvent event) {
        order(Suite.set(token, storable));
    }
}
