package org.vanilla_manager.orientdb.oproperty;

import com.orientechnologies.orient.core.metadata.schema.OClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class OClassesDependentStrings {
    List<OClassAndValue> list;

    public OClassesDependentStrings() {
        list = new ArrayList<>();
    }

    private static char delimiter1 = ':';
    private static char delimiter2 = ';';

    //https://stackoverflow.com/questions/599161/best-way-to-convert-an-arraylist-to-a-string
    public String castToString() {
        String sValue = "";
        for (OClassAndValue cV : list)
        {
            sValue += cV.oClassName + delimiter1 + cV.value + delimiter2;
        }
        return sValue;
    }

    public void castFromString(String sValue) {
        if (sValue != null && sValue.length() > 0) {
            String[] sCVS = sValue.split(Pattern.quote(String.valueOf(delimiter2))); //input.split("[@\\^]");
            if (sCVS.length > 0) {
                for (String sCV : sCVS) {
                    String[] cV_fields = sCV.split(Pattern.quote(String.valueOf(delimiter1))); //input.split("[@\\^]");
                    if (cV_fields.length > 1) {
                        OClassAndValue cV = new OClassAndValue(cV_fields[0], cV_fields[1]);
                        list.add(cV);
                    }
                }
            }
        }
    }

    public void setOClassValue(OClass oClass, String _value) {
        String _oClassName = oClass != null ? oClass.getName() : null;
        if (_oClassName != null) {
            OClassAndValue oClassAndValue = findOClassAndValue(_oClassName);
            if (oClassAndValue != null)
                oClassAndValue.setValue(_value);
            else {
                oClassAndValue = new OClassAndValue(_oClassName, _value);
                list.add(oClassAndValue);
            }
        }
    }

    public String getOClassValue(OClass oClass) {
        String _oClassName = oClass != null ? oClass.getName() : "";
        if (_oClassName != null) {
            OClassAndValue oClassAndValue = findOClassAndValue(_oClassName);
            if (oClassAndValue != null)
                return oClassAndValue.getValue();
            else return "";
        }
        return null;
    }

    private OClassAndValue findOClassAndValue(String  _oClassName) {
        Optional<OClassAndValue> findOClassAndValue = list.parallelStream().filter(s ->
                s.getOClassName().equals(_oClassName)).findFirst();
        if (findOClassAndValue.isPresent())
            return findOClassAndValue.get();
        else return null;
    }

    private class OClassAndValue {
        private String oClassName;
        private String value;

        public OClassAndValue(String _oClassName, String _value) {
            oClassName = _oClassName;
            value = _value;
        }

        public String getOClassName() {
            return oClassName;
        }

        public void setValue(String _value) {
            value = _value;
        }

        public String getValue() {
            return value;
        }
    }
}
