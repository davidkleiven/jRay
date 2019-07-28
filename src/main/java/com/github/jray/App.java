package com.github.jray;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

import java.util.List;


/**
 * jRay Application
 *
 */
public class App extends Application
{
    private List<InputCard> cards;

    @FXML private StackPane paramInputMainPane;

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
        System.out.println("Application started...");
    }

    @FXML
    private void addMeshGeometry(ActionEvent event){
        System.out.println("Mesh was added");
        MeshCardGUI card = new MeshCardGUI();
        this.paramInputMainPane.getChildren().add(card);
        //this.getStage().getScene().add(card);
    }
}
