package org.vanilla_manager.orientdb.controls.odocument;

import com.orientechnologies.orient.core.record.OVertex;

/**
 * Vertically aligned Box for OVertex Properties
 */
public class OVertexVBox extends ODocumentVBox {
    private OVertex oVertex;

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