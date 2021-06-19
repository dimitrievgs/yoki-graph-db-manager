package org.vanilla_manager.OVertex_Controls;

import com.orientechnologies.orient.core.metadata.schema.OProperty;
import javafx.scene.control.TextArea;

/*@DefaultProperty(value = "extension")
public class OProperty_TextArea extends TextArea
{
    @FXML
    private HBox extension;

    public ObservableList<Node> getExtension() {
        return extension.getChildren();
    }
    // ... more component specific code
}*/

//https://stackoverflow.com/questions/42975041/javafx-extend-button-and-add-properties-through-fxml
public class OPropertyTextArea extends TextArea {

    public OProperty Property;
    //public final ObjectProperty<Enum<?>> buttonState = new SimpleObjectProperty<>(ButtonState.NORMAL);

    public OProperty get_OProperty() {
        return this.Property;
    }

    public void setOProperty(OProperty value) {
        this.Property = value;
    }


    /*public final OProperty getButtonState() {
        return this.get_OProperty();
    }
*/

    /*public final void setButtonState(final Enum<?> buttonState) {
        this.get_OProperty().set(buttonState);
    }*/

    public OPropertyTextArea(OProperty _Property) {
        super();
        Property= _Property;
        /*styleProperty().bind(Bindings.
                when(buttonState.isEqualTo(ButtonState.CRITICAL)).
                then("-fx-base: red;").
                otherwise(""));*/


    }

}