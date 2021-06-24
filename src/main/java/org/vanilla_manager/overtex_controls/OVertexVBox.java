package org.vanilla_manager.overtex_controls;

import com.orientechnologies.orient.core.record.OVertex;
import javafx.scene.layout.VBox;

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
public class OVertexVBox extends VBox {
    private OVertex oVertex;
    //public final ObjectProperty<Enum<?>> buttonState = new SimpleObjectProperty<>(ButtonState.NORMAL);

    public OVertex getOVertex() {
        return this.oVertex;
    }

    public void setOVertex(OVertex value) {
        this.oVertex = value;
    }

    public OVertexVBox(OVertex _oVertex) {
        super();
        oVertex = _oVertex;
        /*styleProperty().bind(Bindings.
                when(buttonState.isEqualTo(ButtonState.CRITICAL)).
                then("-fx-base: red;").
                otherwise(""));*/
    }
}