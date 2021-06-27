package org.vanilla_manager.orientdb.controls.oproperty;

import com.orientechnologies.orient.core.metadata.schema.OType;

import java.util.Arrays;

/**
 * The OProperty by default has the attributes such as Name, Description and OType (internal type of data in Orientdb).
 * The class below sets additional attributes / fields for OProperty using CustomFields (.setCustom(str1,str2) / .getCustom())
 */
public class OPropertyCustomAttribute {
    /**
     * The CustomFields are all String (set by str2 in .setCustom(str1,str2)) by default. Let's emulate variety.
     */
    public enum AttributeType {
        String,
        ComboBox,
        INT,
        DOUBLE,
        OClassDependentString
    }

    /**
     * str1 in .setCustom(str1,str2)
     */
    private String name;
    /**
     * Attributes of OProperty (CustomFields) can store different kind of data:  String, choices of ComboBox, numeric data.
     */
    private AttributeType type;
    /**
     * str2 in .setCustom(str1,str2)
     */
    private String value;

    /**
     * For ComboBox Type
     */
    private String[] possibleValues;

    /**
     * Using this static array, we will find the type of the attribute (CustomField) by Name (str1).
     */
    public static OPropertyCustomAttribute[] list = {DataType.attribute, RandomGeneratorPath.attribute};

    /**
     * Costructor for Combobox
     *
     * @param _Name
     * @param _Type
     * @param _PossibleValues
     */
    public OPropertyCustomAttribute(String _Name, AttributeType _Type, String[] _PossibleValues) {
        name = _Name;
        type = _Type;
        possibleValues = _PossibleValues;
    }

    /**
     * Constructor for String, Int, Double, etc.
     *
     * @param _Name
     * @param _Type
     */
    public OPropertyCustomAttribute(String _Name, AttributeType _Type) {
        name = _Name;
        type = _Type;
        possibleValues = null;
    }

    public String getName()
    {
        return name;
    }

    public AttributeType getType()
    {
        return type;
    }

    public String getValue()
    {
        return value;
    }

    public String[] getPossibleValues()
    {
        return possibleValues;
    }

    /**
     * Possible Data Types (Text, Image or Map) which are implemented using one or the other OType.
     */
    public static class DataType
    {
        public static String textPropertyType = "Text";
        public static String mapPropertyType = "Map";
        public static String imagePropertyType = "Image";

        public static String[] dataTypeStringValues = new String[]{textPropertyType, imagePropertyType, mapPropertyType};
        public static OType[] correspondingOTypes = new OType[]{OType.STRING, OType.STRING, OType.STRING};

        /**
         * Data Types of this CM for data fields of Records.
         */
        public static OPropertyCustomAttribute attribute = new OPropertyCustomAttribute("Data_Type", AttributeType.ComboBox, dataTypeStringValues);

        /**
         * OType cannot be changed, so this method is meaningless (looks like)
         * @param Data_Type_String_Value
         * @return
         */
        public static OType getOType(String Data_Type_String_Value)
        {
            int i = Arrays.asList(dataTypeStringValues).indexOf(Data_Type_String_Value);
            return correspondingOTypes[i];
        }
    }

    public static class RandomGeneratorPath
    {
        public static OPropertyCustomAttribute attribute = new OPropertyCustomAttribute("Random_Generator_Path", AttributeType.OClassDependentString);
    }
}
