package org.vanilla_manager.orientdb.oproperty;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ComboBox;

public class OPropertyNode {
    /**
     * Some attributes values can store values for different OClasses. This is OClass for which this OProperty state is shown.
     */
    private OClass oClass;
    private OProperty oProperty;

    private SimpleStringProperty name;
    private StringProperty description;
    private ComboBox<String> dataType;
    private SimpleStringProperty orientDBType;
    private RandomGeneratorPathButton randomGeneratorPath;

    /**
     * To create new OProperty for OrientDB
     * @param _name
     * @param _description
     */
    public OPropertyNode(OClass _oClass, String _name, String _description, String _dataType, String _randomGeneratorPathValue) {
        oClass = _oClass;
        oProperty = null; //Property can be created only under the OClass, not by themself

        this.name = new SimpleStringProperty(_name);
        this.description = new SimpleStringProperty(_description);
        this.orientDBType = new SimpleStringProperty(""); //OType.STRING.toString()

        this.dataType = new ComboBox();
        this.dataType.getItems().addAll(OPropertyCustomAttribute.DataType.attribute.getPossibleValues());
        setDataTypeValue(_dataType); //Value of ComboBox is not set here

        this.randomGeneratorPath = new RandomGeneratorPathButton(oClass);
        setRandomGeneratorPathValue(_randomGeneratorPathValue); //Value of ComboBox is not set here
    }

    /**
     * Direct reading of fields from OProperty from OrientDB
     *
     * @param _oProperty
     */
    public OPropertyNode(OClass _oClass, OProperty _oProperty) {
        oClass = _oClass;
        oProperty = _oProperty;

        this.name = new SimpleStringProperty(_oProperty.getName());
        this.description = new SimpleStringProperty(_oProperty.getDescription());
        this.orientDBType = new SimpleStringProperty(_oProperty.getType().toString());

        this.dataType = new ComboBox();
        this.dataType.getItems().addAll(OPropertyCustomAttribute.DataType.attribute.getPossibleValues());
        this.dataType.getSelectionModel().select(_oProperty.getCustom(OPropertyCustomAttribute.DataType.attribute.getName()));

        this.randomGeneratorPath = new RandomGeneratorPathButton(oClass);
        setRandomGeneratorPathValue(_oProperty.getCustom(OPropertyCustomAttribute.RandomGeneratorPath.attribute.getName())); //Value of ComboBox is not set here
    }

    //-------------------------------------------------------------------------
    //----------------------------------Name-----------------------------------

    public SimpleStringProperty nameProperty() {
        if (name == null) {
            name = new SimpleStringProperty(this, "Name");
        }
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String fName) {
        name.set(fName);
    }

    //-------------------------------------------------------------------------
    //-----------------------------OrientDB Type-------------------------------

    public SimpleStringProperty orientdbTypeProperty() {
        if (orientDBType == null) {
            orientDBType = new SimpleStringProperty(this, "Name");
        }
        return orientDBType;
    }

    public String getOrientdbType() {
        return orientDBType.get();
    }

    public void setOrientdbType(String fName) {
        orientDBType.set(fName);
    }

    //-------------------------------------------------------------------------
    //------------------------------Description--------------------------------

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String value) {
        description.set(value);
    }

    //-------------------------------------------------------------------------
    //------------------------------Data Type----------------------------------

    public ComboBox getDataType() {
        return dataType;
    }

    public String getDataTypeValue() {
        return dataType.getSelectionModel().getSelectedItem();
    }

    public void setDataType(ComboBox value) {
        this.dataType = value;
    }

    public void setDataTypeValue(String value) {
        dataType.getSelectionModel().select(value);
    }

    //-------------------------------------------------------------------------
    //-------------------------RandomGeneratorPath-----------------------------

    public RandomGeneratorPathButton getRandomGeneratorPath() {
        return randomGeneratorPath;
    }

    public String getRandomGeneratorPathValue() {
        return randomGeneratorPath.castToString();
    }

    public void setRandomGeneratorPathValue(String value) {
        randomGeneratorPath.castFromString(value);
    }

    //-------------------------------------------------------------------------
    //------------------------------OProperty----------------------------------

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