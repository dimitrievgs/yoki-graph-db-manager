package org.vanilla_manager.orientdb;

//orientdb: https://orientdb.com/docs/3.0.x/java/Java-API.html
//orientdb java tinkerpop example: https://github.com/duytri/OrientDbJavaExample/blob/master/src/main/java/OrientDbJavaExample.java
//working with database: https://orientdb.com/docs/3.0.x/java/Document-API-Database.html
//Modules A and B export package some.package to module C in Java 9:
//https://stackoverflow.com/questions/46277188/modules-a-and-b-export-package-some-package-to-module-c-in-java-9
//example of creating graph database: https://www.kwoxer.de/2014/11/12/daten-import-via-java-orientdb-real-beispiel-tutorial/
//quiries: https://orientdb.com/docs/last/fiveminute/java-4.html
//SQL syntax: https://orientdb.com/docs/last/sql/

//How do I list classes with a SQL query on OrientDB? https://stackoverflow.com/questions/37948531/how-do-i-list-classes-with-a-sql-query-on-orientdb

//OrientDb DROP PROPERTY does not remove the property values in records: https://stackoverflow.com/questions/29545198/orientdb-drop-property-does-not-remove-the-property-values-in-records

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.ODatabaseType;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.vanilla_manager.MessageBox;

import java.util.*;

public class orientdb_connector {
    String orientdb_path = "embedded:D:/orient_dbs/"; //"embedded:/tmp/"
    String db_name = "vanilla_db";
    String user_name = "admin";
    String password = "admin";

    //String OElementClass = "OElement"; //иначе надо обращаться
    public static String Vertex_Class_Name = "V";
    public static String Edge_Class_Name = "E";

    public static String Root_Vertex_Name = ".";
    public static String Edge_Child_Page_Class = "Child_Page";

    /**
     * Root Page for Tree Structure of Records
     */
    OVertex Root_Vertex;

    public orientdb_connector() {
    }

    public OrientDB orientDB;

    //-------------------------------------------------------------------------
    //--------------------------Init & Preparation-----------------------------

    public void init() {
        try {
            orientDB = new OrientDB(orientdb_path, OrientDBConfig.defaultConfig());
            //new OrientDB("embedded:/tmp/",  OrientDBConfig.defaultConfig());

            boolean db_exists = orientDB.exists(db_name);

            if (db_exists == false) {
                orientDB.create(db_name, ODatabaseType.PLOCAL);
            }

            Create_Base_Classes();
            Find_Root_Record();
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    private void Create_Base_Classes() {
        try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {
            OClass V_OClass = db.getClass(Vertex_Class_Name);
            OClass E_OClass = db.getClass(Edge_Class_Name);
            OSchema schema = db.getMetadata().getSchema();

            //db.createClassIfNotExist()

            if (V_OClass == null) {
                //db.createEdgeClass();
                OClass OVertex_OClass = schema.createClass(Vertex_Class_Name);
                OVertex_OClass.setDescription("OClass for OVertex. Main class for vertices.");
                //db.createClass(VertexClass);
            }
            if (E_OClass == null) {
                OClass OEdge_OClass = schema.createClass(Edge_Class_Name);
                OEdge_OClass.setDescription("OClass for OEdge. Main class for edges.");
            }

            OClass Route_OClass = db.createClassIfNotExist(Edge_Child_Page_Class, Edge_Class_Name);

            Create_Other_Test_Classes(db);
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    String Location_Class_name = "Location";
    String Route_Class_name = "Route";

    String Campaign_Manager_Type_Property_Attribute_Name = "CM_Type";
    String Random_Generator_Path_Property_Attribute_Name = "Random_Generator_Path";

    private void Create_Other_Test_Classes(ODatabaseSession db) {
        OClass Location_OClass = db.createClassIfNotExist(Location_Class_name, Vertex_Class_Name);
        OClass Route_OClass = db.createClassIfNotExist(Route_Class_name, Edge_Class_Name);

        String Settlement_OClass_name = "Settlement";
        OClass Settlement_OClass = db.createClassIfNotExist(Settlement_OClass_name, Location_Class_name);
        String Village_OClass_name = "Village";
        String City_OClass_name = "City";
        OClass Village_OClass = db.createClassIfNotExist(Village_OClass_name, Settlement_OClass_name);
        OClass City_OClass = db.createClassIfNotExist(City_OClass_name, Settlement_OClass_name);
        String Small_City_name = "Town";
        String Big_City_name = "Metropolis";
        OClass Small_City_OClass = db.createClassIfNotExist(Small_City_name, City_OClass_name);
        OClass Big_City_OClass = db.createClassIfNotExist(Big_City_name, City_OClass_name);
        String Metropolis_Propery_1_Name = "M_Property";
        String Metropolis_Propery_2_Name = "M_Property2";
        String Metropolis_Propery_3_Name = "M_Property5";
        if (Big_City_OClass.existsProperty(Metropolis_Propery_1_Name) == false) {
            OProperty prop1 = Big_City_OClass.createProperty(Metropolis_Propery_1_Name, OType.STRING);
            //prop1.setDescription(Text_Property_Type);
        }
        if (Big_City_OClass.existsProperty(Metropolis_Propery_2_Name) == false) {
            OProperty prop2 = Big_City_OClass.createProperty(Metropolis_Propery_2_Name, OType.INTEGER);
            //prop2.setDescription(Image_Property_Type);
        }
        if (Big_City_OClass.existsProperty(Metropolis_Propery_3_Name) == false) {
            OProperty prop3 = Big_City_OClass.createProperty(Metropolis_Propery_3_Name, OType.DOUBLE);
            //prop3.setDescription(Map_Property_Type);
            prop3.setCustom(OProperty_Custom_Attribute.Data_Type.Attribute.getName(), OProperty_Custom_Attribute.Data_Type.Text_Property_Type);
            prop3.setCustom(OProperty_Custom_Attribute.Random_Generator_Path.Attribute.getName(), "");
            int t = 1;
        }

        String Road_OClass_name = "Road";
        String Trail_OClass_name = "Trail";
        String Track_OClass_name = "Track";
        OClass Road_OClass = db.createClassIfNotExist(Road_OClass_name, Route_Class_name);
        OClass Trail_OClass = db.createClassIfNotExist(Trail_OClass_name, Route_Class_name);
        OClass Track_OClass = db.createClassIfNotExist(Track_OClass_name, Route_Class_name);
        String Highway_OClass_name = "Highway";
        String Crossroad_OClass_name = "Crossroad";
        OClass Highway_OClass = db.createClassIfNotExist(Highway_OClass_name, Road_OClass_name);
        OClass Crossroad_OClass = db.createClassIfNotExist(Crossroad_OClass_name, Road_OClass_name);
    }

    private void Find_Root_Record() {
        Root_Vertex = Add_OVertex(Root_Vertex_Name, Vertex_Class_Name).getOVertex();
    }

    //-------------------------------------------------------------------------
    //-------------------------------Get & Find--------------------------------

    public static String Get_OVertex_Name(OVertex t) {
        return t.getProperty("Name").toString();
    }

    /**
     * For some reason it doesn't work outside of "try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {"
     *
     * @param t
     * @return
     */
    public static OClass Get_OElement_OClass(OElement t) {
        var st = t.getSchemaType();
        if (st.isPresent() == true) {
            OClass oClass = st.get();
            return oClass;
        } else {
            return null; //Optional.Empty
        }
    }

    public OClass Get_OElement_OClass_Ext(OElement t) {
        try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {
            return Get_OElement_OClass(t);
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return null;
    }

    public OElement Find_OElement(ODatabaseSession db, ORID rid) {
        //ORecordId id = new ORecordId(rid);
        return db.getRecord(rid);
    }

    public OVertex Find_OVertex(ODatabaseSession db, String Name, String Classname) {
        List<OVertex> result = Find_OVertices(db, Name, Classname);
        if (result.size() > 0) return result.get(0);
        else return null;
    }

    //https://orientdb.com/docs/3.0.x/general/Types.html
    public List<OVertex> Find_OVertices(ODatabaseSession db, String Name, String Classname) {
        String query = "SELECT FROM " + Classname + " where Name = ?";
        OResultSet rs = db.query(query, Name);
        List<OVertex> result = new ArrayList<OVertex>();
        while (rs.hasNext()) {
            OResult item = rs.next();
            OVertex el = (OVertex) item.toElement();
            result.add(el);
        }
        rs.close();
        return result;
    }

    public List<OEdge> Find_OEdges(ODatabaseSession db, String Classname, String Name) {
        String query = "SELECT FROM " + Classname + " where Name = ?";
        OResultSet rs = db.query(query, Name);
        List<OEdge> result = new ArrayList<OEdge>();
        while (rs.hasNext()) {
            OResult item = rs.next();
            OEdge el = (OEdge) item.toElement();
            result.add(el);
        }
        rs.close();
        return result;
    }

    public OVertex Get_Parent_OVertex(OVertex t) {
        Iterator<OVertex> rs = t.getVertices(ODirection.IN, Edge_Child_Page_Class).iterator();
        List<OVertex> result = new ArrayList<OVertex>();
        while (rs.hasNext()) {
            OVertex el = rs.next();
            result.add(el);
        }
        if (result.size() > 0) return result.get(0);
        else return null;
    }

    public List<OVertex> Get_Child_OVertices(OVertex t) {
        Iterator<OVertex> rs = t.getVertices(ODirection.OUT, Edge_Child_Page_Class).iterator();
        List<OVertex> result = new ArrayList<OVertex>();
        while (rs.hasNext()) {
            OVertex el = rs.next();
            result.add(el);
        }
        return result;
    }

    //-------------------------------------------------------------------------
    //-------------------Manipulations - OVertices & OEdges--------------------

    public final class New_OVertex_Result {
        private final boolean created;
        private final OVertex Vertex;
        private final OClass oClass;

        public New_OVertex_Result(boolean _created, OVertex _value, OClass _oClass) {
            this.created = _created;
            this.Vertex = _value;
            this.oClass = _oClass;
        }

        public boolean wasCreated() {
            return created;
        }

        public OVertex getOVertex() {
            return Vertex;
        }

        public OClass getOClass() {
            return oClass;
        }
    }

    public New_OVertex_Result Add_OVertex(String Name, String Class_Name) {
        return Add_Child_OVertex(Name, Class_Name, null);
    }

    //change from class_name to OClass itself
    public New_OVertex_Result Add_Child_OVertex(String Name, String Class_Name, OVertex Parent) {
        boolean created = false;
        OVertex oVertex = null;
        OClass oClass = null;
        if (Name != "") {
            try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {
                boolean changed = false;
                oVertex = Find_OVertex(db, Name, Class_Name);
                if (oVertex == null) {
                    db.begin();
                    oVertex = db.newVertex(Class_Name);
                    created = true;
                    oVertex.setProperty("Name", Name, OType.STRING);
                    //t.save();
                    changed = true;
                    //db.commit();
                }

                if (Parent != null) {
                    OVertex parent = Get_Parent_OVertex(oVertex);
                    if (parent == null) {
                        Parent.addEdge(oVertex, Edge_Child_Page_Class);
                        changed = true;
                    }
                }

                if (changed) {
                    oVertex.save();
                    db.commit();
                }

                oClass = Get_OElement_OClass(oVertex);
            } catch (Exception e) {
                MessageBox.Show(e);
            }
        }
        return new New_OVertex_Result(created, oVertex, oClass);
    }

    String OVertex_Name_Extra_Char = "_";

    public New_OVertex_Result Duplicate_OVertex(OVertex prototype) {
        try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {
            if (prototype != null) {
                db.begin();
                OVertex oVertex2 = null;
                OClass oClass = Get_OElement_OClass(prototype);
                if (oClass != null) {
                    String Class_name = oClass.getName();
                    oVertex2 = db.newVertex(Class_name);
                } else {
                    oVertex2 = db.newVertex();
                }

                Set<String> PropertiesNames = prototype.getPropertyNames();
                for (String PropertyName : PropertiesNames) {
                    var p_value = prototype.getProperty(PropertyName); //Надо сделать для более общего случая, не только для String
                    if (PropertyName == "Name")
                        p_value = p_value.toString() + OVertex_Name_Extra_Char;
                    oVertex2.setProperty(PropertyName, p_value);
                }
                oVertex2.save();
                db.commit();
                return new New_OVertex_Result(true, oVertex2, oClass); //String name, OVertex _oVertex, OClass _oClass, List<OVertex_Page_Node> _Childs_Nodes
            }
            return null;
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return null;
    }

    public void Delete_OElement(ORID RID) {
        try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {
            db.delete(RID);
            db.commit();
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    //-------------------------------------------------------------------------
    //------------------------Manipulations - OClasses-------------------------

    private boolean Exists_OClass(ODatabaseSession db, String OClass_Name) {
        OSchema schema = db.getMetadata().getSchema();
        return schema.existsClass(OClass_Name);
    }

    public boolean Exists_OClass_Ext(OClass oClass) {
        try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {
            return Exists_OClass(db, oClass.getName());
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return false;
    }

    private OClass Find_OClass(ODatabaseSession db, String OClass_Name) {
        OSchema schema = db.getMetadata().getSchema();
        return schema.getClass(OClass_Name);
    }

    public final class New_OClass_Result {
        private final boolean created;
        private final OClass oClass;

        public New_OClass_Result(boolean _created, OClass _oClass) {
            this.created = _created;
            this.oClass = _oClass;
        }

        public boolean wasCreated() {
            return created;
        }

        public OClass getOClass() {
            return oClass;
        }
    }

    public New_OClass_Result Add_OClass(String OClass_Name, OClass Parent) {
        boolean created = false;
        OClass oClass = null;
        if (OClass_Name != "") {
            try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {
                oClass = Find_OClass(db, OClass_Name);
                if (oClass == null) {
                    oClass = db.createClassIfNotExist(OClass_Name, Parent.getName());
                    created = true;
                }
            } catch (Exception e) {
                MessageBox.Show(e);
            }
        }
        return new New_OClass_Result(created, oClass);
    }

    String OClass_Name_Extra_Char = "_";

    public New_OClass_Result Duplicate_OClass(OClass prototype) {
        New_OClass_Result result = null;
        try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {
            if (prototype != null) {
                String new_OClass2_name = prototype.getName();
                do {
                    new_OClass2_name += OClass_Name_Extra_Char;
                }
                while(db.getClass(new_OClass2_name) != null);
                OClass Parent = prototype.getSuperClasses().get(0);
                OClass oClass2 = db.createClass(new_OClass2_name, Parent.getName());

                Collection<OProperty> Properties = prototype.properties();
                for (OProperty property : Properties) {
                    if (property.getOwnerClass() == prototype)
                        oClass2.createProperty(property.getName(), property.getType());
                }
                result = new New_OClass_Result(true, oClass2); //String name, OVertex _oVertex, OClass _oClass, List<OVertex_Page_Node> _Childs_Nodes
            }
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return result;
    }

    public void Remove_OClass(ODatabaseSession db, OClass oClass) {
        OSchema schema = db.getMetadata().getSchema();
        List<OClass> oClasses = collect_sub_classes_from_bottom_to_top(oClass);
        for (OClass oClass1 : oClasses)
        {
            schema.dropClass(oClass1.getName());
        }
    }

    /**
     * Recursive
     * @param oClass
     */
    private List<OClass> collect_sub_classes_from_bottom_to_top(OClass oClass)
    {
        List<OClass> oClasses = new ArrayList<>();
        Collection<OClass> sub_OClasses = oClass.getSubclasses();
        for (OClass sub_oClass : sub_OClasses)
        {
            oClasses.addAll(collect_sub_classes_from_bottom_to_top(sub_oClass));
        }
        oClasses.add(oClass);
        return oClasses;
    }

    public boolean Remove_OClass_Ext(OClass oClass) {
        try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {
            Remove_OClass(db, oClass);
            //return schema.existsClass(oClass.getName());
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return false;
    }

    //-------------------------------------------------------------------------
    //------------------------------OProperties--------------------------------

    public boolean Write_Record_Properties(OElement el, List<String> PropertiesNames, List<String> values) {
        try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {
            //OElement el = fetch_Record(db, RID);
            if (el != null) {
                db.begin();
                int PropertiesCount = PropertiesNames.size();
                for (int i = 0; i < PropertiesCount; i++) {
                    String PropertyName = PropertiesNames.get(i);
                    String value = values.get(i);
                    el.setProperty(PropertyName, value);
                }
                el.save();
                db.commit();
            }
            return true;
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return false;
    }

    public List<String> Read_Record_Properties(OElement el, List<String> PropertiesNames) {
        try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {
            if (el != null) {
                db.begin();
                List<String> result = new ArrayList<>();
                for (String propertyName : PropertiesNames) {
                    String value = el.getProperty(propertyName);
                    if (value == null) value = "";
                    result.add(value);
                }
                return result;
            } else return null;
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return null;
    }

    public ObservableList<OProperty_Node> Write_OClass_Properties(OClass oClass, String new_Name, String new_Description, ObservableList<OProperty_Node> table_data) {
        try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {
            //OElement el = fetch_Record(db, RID);
            if (oClass != null) {
                oClass.setName(new_Name); //it needs to be changed under the ODatabaseSession, but outside of db.begin ... db.commit
                oClass.setDescription(new_Description);

                List<OProperty> old_properties_list = new ArrayList(oClass.properties());

                //db.begin();
                int PropertiesCount = table_data.size();
                for (int i = 0; i < PropertiesCount; i++) {
                    OProperty_Node Property_Node = table_data.get(i);
                    OProperty property = Property_Node.getOProperty();

                    String new_OProperty_Name = Property_Node.getName();
                    String new_OProperty_Description = Property_Node.getDescription();
                    String new_OProperty_Data_Type = Property_Node.getData_Type_Value();

                    //check if new property
                    if (property == null) {
                        OProperty old_property_with_same_name = oClass.getProperty(new_OProperty_Name);
                        if (old_property_with_same_name != null) {
                            OClass Owner_Class = old_property_with_same_name.getOwnerClass();
                            //it means we just want to delete old one and create new one with the same name. For example, to change OType which is impossible to do in other way.
                            if (Owner_Class == oClass) { //otherwise it will not allow to remove from this property
                                oClass.dropProperty(new_OProperty_Name);
                                old_properties_list.remove(old_property_with_same_name);
                            }
                        }
                        OType oType = OProperty_Custom_Attribute.Data_Type.Get_OType(new_OProperty_Data_Type);
                        property = oClass.createProperty(new_OProperty_Name, oType);
                        Property_Node.setOProperty(property);
                        Property_Node.setOrientDBType(oType.toString());
                    } else {
                        property.setName(new_OProperty_Name);
                        old_properties_list.remove(property);
                    }

                    property.setDescription(new_OProperty_Description);
                    property.setCustom(OProperty_Custom_Attribute.Data_Type.Attribute.getName(), new_OProperty_Data_Type);

                    //property.setType(OProperty_Custom_Attribute.Data_Type.Get_OType(new_data_type));
                    //Orientdb just doesn't allow to change OType after property creation! (at least in this way)
                }

                for (OProperty property : old_properties_list) {
                    if (property.getOwnerClass() == oClass) //otherwise it will not allow to remove from this property
                        oClass.dropProperty(property.getName());
                }
            }
            return table_data;
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return null;
    }

    public Collection<OProperty> Read_OClass_Properties(OClass oClass) {
        try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {
            if (oClass != null) {
                db.begin();
                List<String> result = new ArrayList<>();

                Collection<OProperty> props1 = oClass.properties();
                Collection<OProperty> props2 = oClass.declaredProperties();
                Collection<OProperty> props3 = oClass.getIndexedProperties();

                /*for (String propertyName : PropertiesNames) {

                    String value = oClass.getProperty(propertyName);
                    if (value == null) value = "";
                    result.add(value);
                }*/
                return props1;
            } else return null;
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return null;
    }

    public Collection<OProperty> Get_Properties(OElement t) {
        try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {
            OClass oClass = Get_OElement_OClass(t);
            Collection<OProperty> Properties = oClass.properties();
            return Properties;
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return null;
    }

    //-------------------------------------------------------------------------
    //---------------------------Read DB contents------------------------------

    public List<TreeItem<OVertex_Node>> Read_Vertices(String Search_String) {
        try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {
            String lc_Search_String = Search_String.toLowerCase();

            String query = "";
            if (Search_String.length() == 0)
                query = "SELECT from V";
            else query = "SELECT from V WHERE Name.toLowerCase() LIKE '%" + lc_Search_String + "%'"; //"SELECT from V"
            OResultSet rs = db.query(query, "");
            List<OVertex> vertices = new ArrayList<OVertex>();
            List<TreeItem<OVertex_Node>> tis = new ArrayList<TreeItem<OVertex_Node>>();
            while (rs.hasNext()) {
                OResult item = rs.next();
                if (item.isVertex() == true) {
                    OVertex el = (OVertex) item.toElement();
                    OClass oClass = orientdb_connector.Get_OElement_OClass(el);
                    OVertex_Node ee = new OVertex_Node(el, oClass);
                    TreeItem<OVertex_Node> ti = new TreeItem<OVertex_Node>(ee);
                    tis.add(ti);

                    vertices.add(el);
                }
            }
            rs.close();
            return tis;
        } catch (Exception e) {
            MessageBox.Show(e);
        } finally {

        }
        return null;
    }

    public OVertex_Node Read_Child_OVertices(String Search_String) {
        try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {
            OVertex_Node super_node = Get_Child_OVertices_Hierarchy(Root_Vertex, Search_String).getValue();
            return super_node;
        } catch (Exception e) {
            MessageBox.Show(e);
        } finally {

        }
        return null;
    }

    /**
     * Recursive method
     */
    private Get_Child_OVertices_Hierarchy_result Get_Child_OVertices_Hierarchy(OVertex parent_vertex, String Search_String) {
        List<OVertex> child_vertices = Get_Child_OVertices(parent_vertex);
        List<OVertex_Node> child_nodes = new ArrayList<>();
        for (OVertex child_vertex : child_vertices) {
            Get_Child_OVertices_Hierarchy_result child_node_w_result = Get_Child_OVertices_Hierarchy(child_vertex, Search_String);
            if (child_node_w_result.getResult() == true)
                child_nodes.add(child_node_w_result.getValue());
        }
        String name = Get_OVertex_Name(parent_vertex);
        OClass oCLass = Get_OElement_OClass(parent_vertex);
        //either the name is suitable, or the name of one of the heirs is suitable
        boolean contains = Search_String.length() == 0 || name.contains(Search_String) || child_nodes.size() > 0;
        return new Get_Child_OVertices_Hierarchy_result(contains, new OVertex_Node(parent_vertex, oCLass, child_nodes));
    }

    final class Get_Child_OVertices_Hierarchy_result {
        private final boolean result;
        private final OVertex_Node value;

        public Get_Child_OVertices_Hierarchy_result(boolean first, OVertex_Node second) {
            this.result = first;
            this.value = second;
        }

        public boolean getResult() {
            return result;
        }

        public OVertex_Node getValue() {
            return value;
        }
    }

    public OClass_Node Read_Classes(boolean Read_V_OClasses, boolean Read_E_OClasses) {
        try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {
            List<OClass_Node> Classes_Nodes = new ArrayList<>();
            if (Read_V_OClasses)
            {
                OClass V_Class = db.getClass(Vertex_Class_Name);
                OClass_Node V_nodes = Get_OClass_Hierarchy(V_Class);
                Classes_Nodes.add(V_nodes);
            }
            if (Read_E_OClasses)
            {
                OClass E_Class = db.getClass(Edge_Class_Name);
                OClass_Node E_nodes = Get_OClass_Hierarchy(E_Class);
                Classes_Nodes.add(E_nodes);
            }

            OClass_Node super_class = new OClass_Node("OElement", Classes_Nodes);

            return super_class;
        } catch (Exception e) {
            MessageBox.Show(e);
        } finally {

        }
        return null;
    }

    /**
     * Recursive method
     */
    private OClass_Node Get_OClass_Hierarchy(OClass parent_class) {
        List<OClass> sub_classes = new ArrayList<>(parent_class.getSubclasses());
        List<OClass_Node> child_nodes = new ArrayList<>();
        for (OClass sub_class : sub_classes) {
            child_nodes.add(Get_OClass_Hierarchy(sub_class));
        }
        return new OClass_Node(parent_class, child_nodes);
    }
}
