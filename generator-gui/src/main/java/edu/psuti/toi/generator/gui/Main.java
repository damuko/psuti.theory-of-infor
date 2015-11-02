package edu.psuti.toi.generator.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private final static float MIN_HEIGHT = 550f;
    private final static float MIN_WIDTH = 500f;
    private final static String STAGE_TITLE = "Sequence Generator";
    private final static String FXML_PATH = "/generator.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(FXML_PATH));

        primaryStage.setTitle(STAGE_TITLE);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
