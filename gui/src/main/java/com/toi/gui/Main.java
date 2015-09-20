package com.toi.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../../../mainStage/generator.fxml"));
        primaryStage.setTitle("Лабораторная работа №1");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
       /* FXMLLoader loader = new FXMLLoader();
        GeneratorController controller = loader.getController();
        controller.setMainApp(this);*/
    }


    public static void main(String[] args) {
        launch(args);
    }
}
