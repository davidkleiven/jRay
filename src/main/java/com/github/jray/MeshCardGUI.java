package com.github.jray;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;


public class MeshCardGUI extends HBox
{
    @FXML
    private TextField gmshFile;

    public MeshCardGUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/meshCard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Could not load meshCard.fxml");
        }
    }
}