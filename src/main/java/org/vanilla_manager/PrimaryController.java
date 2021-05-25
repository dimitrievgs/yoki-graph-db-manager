package org.vanilla_manager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class PrimaryController {
    @FXML private ChoiceBox<String> choice_box; // this is fxml choicebox Id name given in fxml file

    @FXML
    public void initialize() {
        ChoiceBox_Set();
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    Random r = new Random();
    String[] pronoun_and_verb = {"I am", "He is", "She is", "It is", "They are", "We are", "You are"};

    private void ChoiceBox_Set()
    {
        List<String> list = Arrays.asList(pronoun_and_verb);
        //Pass it over in this manner
        ObservableList<String> observableList = FXCollections.observableList(list);
        choice_box.setItems(observableList);
        String s = pronoun_and_verb[r.nextInt(pronoun_and_verb.length)];
        choice_box.setValue(s);
        choice_box.setMinWidth(200);
    }

    @FXML
    private void changeChoiceBoxValue() throws IOException {
        String s = pronoun_and_verb[r.nextInt(pronoun_and_verb.length)];
        choice_box.setValue(s);
    }
}
