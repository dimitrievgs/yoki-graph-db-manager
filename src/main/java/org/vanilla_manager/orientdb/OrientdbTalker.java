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
import org.vanilla_manager.dialogs.MessageBox;
import org.vanilla_manager.orientdb.controls.oproperty.OPropertyCustomAttribute;
import org.vanilla_manager.orientdb.controls.oproperty.OPropertyNode;
import org.vanilla_manager.orientdb.treetableview.OClassNode;
import org.vanilla_manager.orientdb.treetableview.OVertexNode;

import java.util.*;

public class OrientdbTalker {
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

    public OrientdbTalker() {
    }

    private OrientDB orientDB;

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

            createBaseOClasses();
            findRootOVertex();
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    private void createBaseOClasses() {
        try (ODatabaseSession db = openDB();) {
            OClass V_OClass = db.getClass(Vertex_Class_Name);
            OClass E_OClass = db.getClass(Edge_Class_Name);
            OSchema schema = db.getMetadata().getSchema();

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

            //Create_Other_Test_Classes(db);
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    private void findRootOVertex() {
        Root_Vertex = addOVertex(Root_Vertex_Name, Vertex_Class_Name).getOVertex();
    }

    public ODatabaseSession openDB()
    {
        ODatabaseSession db = orientDB.open(db_name, user_name, password);
        return db;
    }

    //-------------------------------------------------------------------------
    //-------------------------------Get & Find--------------------------------

    public static String getOVertexName(OVertex t) {
        return t.getProperty("Name").toString();
    }

    public static void setOVertexName(OVertex t, String newName) {
        t.setProperty("Name", newName);
    }


    /**
     * For some reason it doesn't work outside of "try (ODatabaseSession db = orientDB.open(db_name, user_name, password);) {"
     *
     * @param t
     * @return
     */
    public static OClass getOElementOClass(OElement t) {
        var st = t.getSchemaType();
        if (st.isPresent() == true) {
            OClass oClass = st.get();
            return oClass;
        } else {
            return null; //Optional.Empty
        }
    }

    public OClass getOElementOClassExt(OElement t) {
        try (ODatabaseSession db = openDB();) {
            return getOElementOClass(t);
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return null;
    }

    public OElement findOElement(ODatabaseSession db, ORID rid) {
        //ORecordId id = new ORecordId(rid);
        return db.getRecord(rid);
    }

    public OVertex findOVertex(ODatabaseSession db, String Name, String Classname) {
        List<OVertex> result = findOVertices(db, Name, Classname);
        if (result.size() > 0) return result.get(0);
        else return null;
    }

    //https://orientdb.com/docs/3.0.x/general/Types.html
    //https://github.com/orientechnologies/orientdb/issues/3887
    public List<OVertex> findOVertices(ODatabaseSession db, String Name, String Classname) {
        String query = "SELECT FROM `" + Classname + "` WHERE Name = ?";
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

    public List<OEdge> findOEdges(ODatabaseSession db, String Classname, String Name) {
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

    public OVertex getParentOVertex(OVertex t) {
        Iterator<OVertex> rs = t.getVertices(ODirection.IN, Edge_Child_Page_Class).iterator();
        List<OVertex> result = new ArrayList<OVertex>();
        while (rs.hasNext()) {
            OVertex el = rs.next();
            result.add(el);
        }
        if (result.size() > 0) return result.get(0);
        else return null;
    }

    public List<OVertex> getChildOVertices(OVertex t) {
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

    public final class NewOVertexResult {
        private final boolean created;
        private final OVertex Vertex;
        private final OClass oClass;

        public NewOVertexResult(boolean _created, OVertex _value, OClass _oClass) {
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

    public NewOVertexResult addOVertex(String Name, String Class_Name) {
        return addChildOVertex(Name, Class_Name, null);
    }

    //change from class_name to OClass itself
    public NewOVertexResult addChildOVertex(String Name, String Class_Name, OVertex Parent) {
        boolean created = false;
        OVertex oVertex = null;
        OClass oClass = null;
        if (Name != "") {
            try (ODatabaseSession db = openDB();) {
                boolean changed = false;
                oVertex = findOVertex(db, Name, Class_Name);
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
                    OVertex parent = getParentOVertex(oVertex);
                    if (parent == null) {
                        Parent.addEdge(oVertex, Edge_Child_Page_Class);
                        changed = true;
                    }
                }

                if (changed) {
                    oVertex.save();
                    db.commit();
                }

                oClass = getOElementOClass(oVertex);
            } catch (Exception e) {
                MessageBox.Show(e);
            }
        }
        return new NewOVertexResult(created, oVertex, oClass);
    }

    String OVertex_Name_Extra_Char = "_";

    public NewOVertexResult duplicateOVertex(OVertex prototype) {
        try (ODatabaseSession db = openDB();) {
            if (prototype != null) {
                db.begin();
                OVertex oVertex2 = null;
                OClass oClass = getOElementOClass(prototype);
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
                return new NewOVertexResult(true, oVertex2, oClass); //String name, OVertex _oVertex, OClass _oClass, List<OVertex_Page_Node> _Childs_Nodes
            }
            return null;
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return null;
    }

    public void deleteOElement(ORID RID) {
        try (ODatabaseSession db = openDB();) {
            db.delete(RID);
            db.commit();
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    //-------------------------------------------------------------------------
    //------------------------Manipulations - OClasses-------------------------

    private boolean existsOClass(ODatabaseSession db, String OClass_Name) {
        OSchema schema = db.getMetadata().getSchema();
        return schema.existsClass(OClass_Name);
    }

    public boolean existsOClassExt(OClass oClass) {
        try (ODatabaseSession db = openDB();) {
            return existsOClass(db, oClass.getName());
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return false;
    }

    private OClass findOClass(ODatabaseSession db, String OClass_Name) {
        OSchema schema = db.getMetadata().getSchema();
        return schema.getClass(OClass_Name);
    }

    public final class NewOClassResult {
        private final boolean created;
        private final OClass oClass;

        public NewOClassResult(boolean _created, OClass _oClass) {
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

    public NewOClassResult addOClass(String OClass_Name, OClass Parent) {
        boolean created = false;
        OClass oClass = null;
        if (OClass_Name != "") {
            try (ODatabaseSession db = openDB();) {
                oClass = findOClass(db, OClass_Name);
                if (oClass == null) {
                    oClass = db.createClassIfNotExist(OClass_Name, Parent.getName());
                    created = true;
                }
            } catch (Exception e) {
                MessageBox.Show(e);
            }
        }
        return new NewOClassResult(created, oClass);
    }

    String OClass_Name_Extra_Char = "_";

    public NewOClassResult duplicateOClass(OClass prototype) {
        NewOClassResult result = null;
        try (ODatabaseSession db = openDB();) {
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
                result = new NewOClassResult(true, oClass2); //String name, OVertex _oVertex, OClass _oClass, List<OVertex_Page_Node> _Childs_Nodes
            }
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return result;
    }

    public void removeOClass(ODatabaseSession db, OClass oClass) {
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

    public boolean removeOClassExt(OClass oClass) {
        try (ODatabaseSession db = openDB();) {
            removeOClass(db, oClass);
            //return schema.existsClass(oClass.getName());
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return false;
    }

    //-------------------------------------------------------------------------
    //------------------------------OProperties--------------------------------

    public boolean writeOElementOProperties(OElement el, List<String> PropertiesNames, List<String> values) {
        try (ODatabaseSession db = openDB();) {
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

    public List<String> readOElementOProperties(OElement el, List<String> PropertiesNames) {
        try (ODatabaseSession db = openDB();) {
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

    public ObservableList<OPropertyNode> writeOClassOProperties(OClass oClass, String new_Name, String new_Description, ObservableList<OPropertyNode> table_data) {
        try (ODatabaseSession db = openDB();) {
            //OElement el = fetch_Record(db, RID);
            if (oClass != null) {
                oClass.setName(new_Name); //it needs to be changed under the ODatabaseSession, but outside of db.begin ... db.commit
                oClass.setDescription(new_Description);

                List<OProperty> old_properties_list = new ArrayList(oClass.properties());

                //db.begin();
                int PropertiesCount = table_data.size();
                for (int i = 0; i < PropertiesCount; i++) {
                    OPropertyNode Property_Node = table_data.get(i);
                    OProperty property = Property_Node.getOProperty();

                    String newName = Property_Node.getName();
                    String newDescription = Property_Node.getDescription();
                    String newDataType = Property_Node.getDataTypeValue();
                    String newRandomGeneratorPath = Property_Node.getRandomGeneratorPathValue();

                    //check if new property
                    if (property == null) {
                        OProperty old_property_with_same_name = oClass.getProperty(newName);
                        if (old_property_with_same_name != null) {
                            OClass Owner_Class = old_property_with_same_name.getOwnerClass();
                            //it means we just want to delete old one and create new one with the same name. For example, to change OType which is impossible to do in other way.
                            if (Owner_Class == oClass) { //otherwise it will not allow to remove from this property
                                oClass.dropProperty(newName);
                                old_properties_list.remove(old_property_with_same_name);
                            }
                        }
                        OType oType = OPropertyCustomAttribute.DataType.getOType(newDataType);
                        property = oClass.createProperty(newName, oType);
                        Property_Node.setOProperty(property);
                        Property_Node.setOrientdbType(oType.toString());
                    } else {
                        property.setName(newName);
                        old_properties_list.remove(property);
                    }

                    property.setDescription(newDescription);
                    property.setCustom(OPropertyCustomAttribute.DataType.attribute.getName(), newDataType);
                    property.setCustom(OPropertyCustomAttribute.RandomGeneratorPath.attribute.getName(), newRandomGeneratorPath);

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

    public Collection<OProperty> readOClassOProperties(OClass oClass) {
        try (ODatabaseSession db = openDB();) {
            if (oClass != null) {
                db.begin();
                List<String> result = new ArrayList<>();

                Collection<OProperty> props1 = oClass.properties();
                return props1;
            } else return null;
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return null;
    }

    public Collection<OProperty> getOClassOProperties(OElement t) {
        try (ODatabaseSession db = openDB();) {
            OClass oClass = getOElementOClass(t);
            Collection<OProperty> Properties = oClass.properties();
            return Properties;
        } catch (Exception e) {
            MessageBox.Show(e);
        }
        return null;
    }

    //-------------------------------------------------------------------------
    //---------------------------Read DB contents------------------------------

    public List<TreeItem<OVertexNode>> readOVertices(String Search_String) {
        try (ODatabaseSession db = openDB();) {
            String lc_Search_String = Search_String.toLowerCase();

            String query = "";
            if (Search_String.length() == 0)
                query = "SELECT from V";
            else query = "SELECT from V WHERE Name.toLowerCase() LIKE '%" + lc_Search_String + "%'"; //"SELECT from V"
            OResultSet rs = db.query(query, "");
            List<OVertex> vertices = new ArrayList<OVertex>();
            List<TreeItem<OVertexNode>> tis = new ArrayList<TreeItem<OVertexNode>>();
            while (rs.hasNext()) {
                OResult item = rs.next();
                if (item.isVertex() == true) {
                    OVertex el = (OVertex) item.toElement();
                    OClass oClass = OrientdbTalker.getOElementOClass(el);
                    OVertexNode ee = new OVertexNode(el, oClass);
                    TreeItem<OVertexNode> ti = new TreeItem<OVertexNode>(ee);
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

    public OVertexNode readChildOVertices(String Search_String) {
        try (ODatabaseSession db = openDB();) {
            OVertexNode super_node = getChildOVerticesHierarchy(Root_Vertex, Search_String).getValue();
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
    private getChildOVerticesHierarchyResult getChildOVerticesHierarchy(OVertex parent_vertex, String Search_String) {
        List<OVertex> child_vertices = getChildOVertices(parent_vertex);
        List<OVertexNode> child_nodes = new ArrayList<>();
        for (OVertex child_vertex : child_vertices) {
            getChildOVerticesHierarchyResult child_node_w_result = getChildOVerticesHierarchy(child_vertex, Search_String);
            if (child_node_w_result.getResult() == true)
                child_nodes.add(child_node_w_result.getValue());
        }
        String name = getOVertexName(parent_vertex);
        OClass oCLass = getOElementOClass(parent_vertex);
        //either the name is suitable, or the name of one of the heirs is suitable
        boolean contains = Search_String.length() == 0 || name.contains(Search_String) || child_nodes.size() > 0;
        return new getChildOVerticesHierarchyResult(contains, new OVertexNode(parent_vertex, oCLass, child_nodes));
    }

    final class getChildOVerticesHierarchyResult {
        private final boolean result;
        private final OVertexNode value;

        public getChildOVerticesHierarchyResult(boolean first, OVertexNode second) {
            this.result = first;
            this.value = second;
        }

        public boolean getResult() {
            return result;
        }

        public OVertexNode getValue() {
            return value;
        }
    }

    public OClassNode readOClasses(boolean Read_V_OClasses, boolean Read_E_OClasses) {
        try (ODatabaseSession db = openDB();) {
            List<OClassNode> Classes_Nodes = new ArrayList<>();
            if (Read_V_OClasses)
            {
                OClass V_Class = db.getClass(Vertex_Class_Name);
                OClassNode V_nodes = getOClassHierarchy(V_Class);
                Classes_Nodes.add(V_nodes);
            }
            if (Read_E_OClasses)
            {
                OClass E_Class = db.getClass(Edge_Class_Name);
                OClassNode E_nodes = getOClassHierarchy(E_Class);
                Classes_Nodes.add(E_nodes);
            }

            OClassNode super_class = new OClassNode("OElement", Classes_Nodes);

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
    private OClassNode getOClassHierarchy(OClass parent_class) {
        List<OClass> sub_classes = new ArrayList<>(parent_class.getSubclasses());
        List<OClassNode> child_nodes = new ArrayList<>();
        for (OClass sub_class : sub_classes) {
            child_nodes.add(getOClassHierarchy(sub_class));
        }
        return new OClassNode(parent_class, child_nodes);
    }
}
