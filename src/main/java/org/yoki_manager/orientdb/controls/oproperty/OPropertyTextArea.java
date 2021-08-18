package org.yoki_manager.orientdb.controls.oproperty;

import com.orientechnologies.orient.core.metadata.schema.OProperty;
import javafx.scene.control.TextArea;
import javafx.scene.web.HTMLEditor;

//https://stackoverflow.com/questions/42975041/javafx-extend-button-and-add-properties-through-fxml
public class OPropertyTextArea extends HTMLEditor {
    private OProperty property;

    public OProperty getOProperty() {
        return this.property;
    }

    public void setOProperty(OProperty value) {
        this.property = value;
    }

    public OPropertyTextArea(OProperty _property) {
        super();
        property = _property;
    }
}