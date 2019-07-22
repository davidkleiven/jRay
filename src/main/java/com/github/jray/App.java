package com.github.jray;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

/**
 * jRay Application
 *
 */
public class App extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/parameterInput.fxml"));
        
        primaryStage.setTitle("jRay");
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
