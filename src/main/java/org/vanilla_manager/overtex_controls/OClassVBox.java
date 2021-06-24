package org.vanilla_manager.overtex_controls;

import com.orientechnologies.orient.core.metadata.schema.OClass;
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
public class OClassVBox extends VBox {
    private OClass oClass;

    public OClass getOClass() {
        return this.oClass;
    }

    public void setOClass(OClass value) {
        this.oClass = value;
    }

    public OClassVBox(OClass _oClass) {
        super();
        this.oClass = _oClass;
    }
}