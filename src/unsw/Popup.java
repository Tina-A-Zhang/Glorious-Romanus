package unsw.gloriaromanus;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class Popup {

    public static void display() {
        Stage popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Congratulations");

        Label label1 = new Label("You have won the game");

        Button button1 = new Button("Stay in the game");
        Button button2 = new Button("Exit");

        button1.setOnAction(e -> popupwindow.close());
        button2.setOnAction(e -> Platform.exit());

        VBox layout = new VBox(10);

        layout.getChildren().addAll(label1, button1,button2);

        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 300, 250);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();

    }

}
