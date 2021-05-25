package org.vanilla_manager;

import java.io.IOException;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SecondaryController {
    @FXML private Label label1;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    Random r = new Random();

    @FXML
    private void switchTo3() throws IOException {
        String[] pronoun_and_verb = {"I am", "He is", "She is", "It is", "They are", "We are", "You are"};
        String[] adjective = {"lucky", "sleepy", "funky", "strong", "weak", "persistent", "compliant",
            "red", "black", "yellow", "white"};
        String s = pronoun_and_verb[r.nextInt(pronoun_and_verb.length)] + " " +
                adjective[r.nextInt(adjective.length)] + " and " + adjective[r.nextInt(adjective.length)];
        label1.setText(s);
    }
}