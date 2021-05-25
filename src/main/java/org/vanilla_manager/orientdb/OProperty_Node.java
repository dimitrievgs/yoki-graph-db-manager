package org.vanilla_manager.orientdb;

import com.orientechnologies.orient.core.metadata.schema.OProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ComboBox;

public class OProperty_Node {
    private SimpleStringProperty Name;
    private StringProperty Description;

    private ComboBox<String> Data_Type;
    private SimpleStringProperty OrientDBType;
    private OProperty oProperty;

    /**
     * To create new OProperty for OrientDB
     * @param _Name
     * @param _Description
     */
    public OProperty_Node(String _Name, String _Description, String _Data_Type) {
        this.Name = new SimpleStringProperty(_Name);
        this.Description = new SimpleStringProperty(_Description);

        this.Data_Type = new ComboBox();
        this.Data_Type.getItems().addAll(OProperty_Custom_Attribute.Data_Type.Attribute.getPossibleValues());
        setData_Type_Value(_Data_Type); //Value of ComboBox is not set here

        this.OrientDBType = new SimpleStringProperty(""); //OType.STRING.toString()

        oProperty = null; //Property can be created only under the OClass, not by themself
    }

    /**
     * Direct reading of fields from OProperty from OrientDB
     *
     * @param _oProperty
     */
    public OProperty_Node(OProperty _oProperty) {
        this.Name = new SimpleStringProperty(_oProperty.getName());
        this.Description = new SimpleStringProperty(_oProperty.getDescription());

        this.Data_Type = new ComboBox();
        this.Data_Type.getItems().addAll(OProperty_Custom_Attribute.Data_Type.Attribute.getPossibleValues());
        this.Data_Type.getSelectionModel().select(_oProperty.getCustom(OProperty_Custom_Attribute.Data_Type.Attribute.getName()));

        this.OrientDBType = new SimpleStringProperty(_oProperty.getType().toString());
        oProperty = _oProperty;
    }

    //-------------------------------------------------------------------------
    //-----------------------------Name Property-------------------------------

    public SimpleStringProperty name_Property() {
        if (Name == null) {
            Name = new SimpleStringProperty(this, "Name");
        }
        return Name;
    }

    public String getName() {
        return Name.get();
    }

    public void setName(String fName) {
        Name.set(fName);
    }

    //-------------------------------------------------------------------------
    //-----------------------------Type Property-------------------------------

    public SimpleStringProperty OrientDBType_Property() {
        if (OrientDBType == null) {
            OrientDBType = new SimpleStringProperty(this, "Name");
        }
        return OrientDBType;
    }

    public String getOrientDBType() {
        return OrientDBType.get();
    }

    public void setOrientDBType(String fName) {
        OrientDBType.set(fName);
    }

    //-------------------------------------------------------------------------
    //----------------------Description (Type) Property------------------------

    public StringProperty description_Property() {
        return Description;
    }

    public String getDescription() {
        return Description.get();
    }

    public void setDescription(String value) {
        Description.set(value);
    }

    //-------------------------------------------------------------------------
    //------------------------OrientDB Type Property---------------------------

    public ComboBox getData_Type() {
        return Data_Type;
    }

    public String getData_Type_Value() {
        return Data_Type.getSelectionModel().getSelectedItem();
    }

    public void setData_Type(ComboBox value) {
        this.Data_Type = value;
    }

    public void setData_Type_Value(String value) {
        Data_Type.getSelectionModel().select(value);
    }

    //-------------------------------------------------------------------------
    //------------------------OrientDB Type Property---------------------------

    public OProperty getOProperty() {
        return oProperty;
    }

    public String getOProperty_Name() {
        if (oProperty != null)
            return oProperty.getName();
        else return "";
    }

    public void setOProperty(OProperty value) {
        oProperty = value;
    }
}