package org.vanilla_manager.orientdb;

import com.orientechnologies.orient.core.metadata.schema.OType;

import java.util.Arrays;

/**
 * The OProperty by default has the attributes such as Name, Description and OType (internal type of data in Orientdb).
 * The class below sets additional attributes / fields for OProperty using CustomFields (.setCustom(str1,str2) / .getCustom())
 */
public class OProperty_Custom_Attribute {
    /**
     * The CustomFields are all String (set by str2 in .setCustom(str1,str2)) by default. Let's emulate variety.
     */
    public enum Attribute_Type {
        String,
        ComboBox,
        INT,
        DOUBLE
    }

    /**
     * str1 in .setCustom(str1,str2)
     */
    private String Name;
    /**
     * Attributes of OProperty (CustomFields) can store different kind of data:  String, choices of ComboBox, numeric data.
     */
    private Attribute_Type Type;
    /**
     * str2 in .setCustom(str1,str2)
     */
    private String Value;

    /**
     * For ComboBox Type
     */
    private String[] PossibleValues;

    /**
     * Using this static array, we will find the type of the attribute (CustomField) by Name (str1).
     */
    public static OProperty_Custom_Attribute[] List = {Data_Type.Attribute, Random_Generator_Path.Attribute};

    /**
     * Costructor for Combobox
     *
     * @param _Name
     * @param _Type
     * @param _PossibleValues
     */
    public OProperty_Custom_Attribute(String _Name, Attribute_Type _Type, String[] _PossibleValues) {
        Name = _Name;
        Type = _Type;
        PossibleValues = _PossibleValues;
    }

    /**
     * Constructor for String, Int, Double, etc.
     *
     * @param _Name
     * @param _Type
     */
    public OProperty_Custom_Attribute(String _Name, Attribute_Type _Type) {
        Name = _Name;
        Type = _Type;
        PossibleValues = null;
    }

    public String getName()
    {
        return Name;
    }

    public Attribute_Type getType()
    {
        return Type;
    }

    public String getValue()
    {
        return Value;
    }

    public String[] getPossibleValues()
    {
        return PossibleValues;
    }

    /**
     * Possible Data Types (Text, Image or Map) which are implemented using one or the other OType.
     */
    public static class Data_Type
    {
        public static String Text_Property_Type = "Text";
        public static String Map_Property_Type = "Map";
        public static String Image_Property_Type = "Image";

        public static String[] Data_Type_String_Values = new String[]{Text_Property_Type, Image_Property_Type, Map_Property_Type};
        public static OType[] Corresponding_OTypes = new OType[]{OType.STRING, OType.STRING, OType.STRING};

        /**
         * Data Types of this CM for data fields of Records.
         */
        public static OProperty_Custom_Attribute Attribute = new OProperty_Custom_Attribute("Data_Type", Attribute_Type.ComboBox, Data_Type_String_Values);

        /**
         * OType cannot be changed, so this method is meaningless (looks like)
         * @param Data_Type_String_Value
         * @return
         */
        public static OType Get_OType(String Data_Type_String_Value)
        {
            int i = Arrays.asList(Data_Type_String_Values).indexOf(Data_Type_String_Value);
            return Corresponding_OTypes[i];
        }
    }

    public static class Random_Generator_Path
    {
        public static OProperty_Custom_Attribute Attribute = new OProperty_Custom_Attribute("Random_Generator_Path", Attribute_Type.String);
    }
}
