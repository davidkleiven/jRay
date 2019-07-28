package com.github.jray;

import java.io.IOException;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;


public class MeshCardGUI extends HBox
{
    @FXML
    private TextField gmshFile;

    private StackPane mainLayout = null;

    public MeshCardGUI(StackPane mainLayout) {
        this.mainLayout = mainLayout;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/meshCard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Could not load meshCard.fxml");
        }
    }

    @FXML
    private void remove(ActionEvent e){
        this.mainLayout.getChildren().remove(this);
    }
}