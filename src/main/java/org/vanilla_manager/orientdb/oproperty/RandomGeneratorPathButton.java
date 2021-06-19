package org.vanilla_manager.orientdb.oproperty;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.Optional;

//https://stackoverflow.com/questions/42975041/javafx-extend-button-and-add-properties-through-fxml
//public class AttributeButton<S> extends Button {
public class RandomGeneratorPathButton extends Button {
    private OClass oClass;
    private OClassesDependentStrings boundDataValue;

    public RandomGeneratorPathButton(OClass _oClass) {
        super();
        oClass = _oClass;
        boundDataValue = new OClassesDependentStrings();

        this.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                setBoundDataValueForOClass(dialogChangeAttributeValue(getBoundDataValueForOClass()));
            }
        });
    }

    public String castToString() {
        return this.boundDataValue.castToString();
    }

    public void castFromString(String sValue)
    {
        this.boundDataValue.castFromString(sValue);
        setCaption(getBoundDataValueForOClass());
    }

    //-------------------------------------------------------------------------
    //----------------------------Private methods------------------------------

    private String getBoundDataValueForOClass() {
        return this.boundDataValue.getOClassValue(oClass);
    }

    private void setBoundDataValueForOClass(String _oClassStringValue) {
        this.boundDataValue.setOClassValue(oClass, _oClassStringValue);
        setCaption(_oClassStringValue);
    }

    private void setCaption(String value)
    {
        String Caption = (value == null || value.length() == 0)  ? "Set" : "Change";
        this.setText(Caption);
    }

    private String dialogChangeAttributeValue(String value) {
        String titleTxt = "Attribute Value";

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(titleTxt);
        dialog.setHeaderText("Set or change Attribute Value.");
        dialog.setResizable(true);
        dialog.getDialogPane().setMinWidth(500);

        Label label1 = new Label("Value: ");
        //Label label2 = new Label("Phone: ");
        TextField text1 = new TextField();
        text1.setText(value);
        //TextField text2 = new TextField();

        VBox vbox = new VBox();
        ObservableList<javafx.scene.Node> vbox2_children = vbox.getChildren();
        vbox2_children.add(label1);
        vbox2_children.add(text1);
        dialog.getDialogPane().setContent(vbox);

        vbox.setSpacing(10);

        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter(new Callback<ButtonType, String>() {
            @Override
            public String call(ButtonType b) {
                if (b == buttonTypeOk) {
                    return text1.getText();
                }
                return null;
            }
        });

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            return result.get();
        } else return null;
    }
}
