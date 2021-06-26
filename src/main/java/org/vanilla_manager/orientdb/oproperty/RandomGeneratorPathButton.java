package org.vanilla_manager.orientdb.oproperty;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.util.Callback;
import org.vanilla_manager.MessageBox;
import org.vanilla_manager.ResourcesManager;
import org.vanilla_manager.orientdb.extra_controls.SVGButton;

import java.io.File;
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

    public void castFromString(String sValue) {
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

    private void setCaption(String value) {
        String Caption = (value == null || value.length() == 0) ? "Set" : "Change";
        this.setText(Caption);
    }

    //https://stackoverflow.com/questions/50654923/javafx-create-button-from-icon
    //https://docs.oracle.com/javafx/2/ui_controls/button.htm
    private String dialogChangeAttributeValue(String value) {
        try {
            String titleTxt = "Attribute Value";

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle(titleTxt);
            dialog.setHeaderText("Set or change Attribute Value.");
            dialog.setResizable(true);
            //dialog.getDialogPane().setMinWidth(500);

            Label label1 = new Label("Value: ");
            TextField text1 = new TextField();
            text1.setText(value);
            text1.setPrefWidth(400);

            String svg = "icons/GUI/open_file_1.svg";
            SVGButton buttonChangePath = new SVGButton(svg, 25 , "#999999", "#9283d8");

            VBox vbox = new VBox();
            ObservableList<javafx.scene.Node> vbox_children = vbox.getChildren();
            vbox_children.add(label1);
            HBox hbox = new HBox();
            ObservableList<javafx.scene.Node> hbox_children = hbox.getChildren();
            hbox_children.add(text1);
            hbox_children.add(buttonChangePath);
            vbox_children.add(hbox);
            dialog.getDialogPane().setContent(vbox);

            vbox.setSpacing(10);
            hbox.setSpacing(7);

            ButtonType buttonOk = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonOk, buttonCancel);

            dialog.setResultConverter(new Callback<ButtonType, String>() {
                @Override
                public String call(ButtonType b) {
                    if (b == buttonOk) {
                        return text1.getText();
                    }
                    return value;
                }
            });

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                return result.get();
            } else return value;
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return value;
    }
}
