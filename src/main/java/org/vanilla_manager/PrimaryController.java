package org.vanilla_manager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.UnaryOperator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

public class PrimaryController {
    @FXML private ChoiceBox<String> choice_box; // this is fxml choicebox Id name given in fxml file
    @FXML private TextField NumericField1;
    @FXML private TextField TextField1;

    @FXML
    public void initialize() {
        ChoiceBox_Set();
        NumericField1_Set();
        TextField1_Set();
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

    //https://stackoverflow.com/questions/40472668/numeric-textfield-for-integers-in-javafx-8-with-textformatter-and-or-unaryoperat
    private void NumericField1_Set()
    {
        UnaryOperator<Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            // if proposed change results in a valid value, return change as-is:
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            } else if ("-".equals(change.getText()) ) {

                // if user types or pastes a "-" in middle of current text,
                // toggle sign of value:

                if (change.getControlText().startsWith("-")) {
                    // if we currently start with a "-", remove first character:
                    change.setText("");
                    change.setRange(0, 1);
                    // since we're deleting a character instead of adding one,
                    // the caret position needs to move back one, instead of
                    // moving forward one, so we modify the proposed change to
                    // move the caret two places earlier than the proposed change:
                    change.setCaretPosition(change.getCaretPosition()-2);
                    change.setAnchor(change.getAnchor()-2);
                } else {
                    // otherwise just insert at the beginning of the text:
                    change.setRange(0, 0);
                }
                return change ;
            }
            // invalid change, veto it by returning null:
            return null;
        };

        // modified version of standard converter that evaluates an empty string
        // as zero instead of null:
        StringConverter<Integer> converter = new IntegerStringConverter() {
            @Override
            public Integer fromString(String s) {
                if (s.isEmpty()) return 0 ;
                return super.fromString(s);
            }
        };

        TextFormatter<Integer> textFormatter =
                new TextFormatter<Integer>(converter, 1, integerFilter);
        NumericField1.setTextFormatter(textFormatter);
        NumericField1.setMaxWidth(100);
    }

    private void TextField1_Set()
    {
        TextField1.setMaxWidth(100);
    }
}
