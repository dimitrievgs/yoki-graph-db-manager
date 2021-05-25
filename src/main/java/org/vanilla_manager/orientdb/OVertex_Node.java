package org.vanilla_manager.orientdb;

import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.record.OVertex;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.List;

public class OVertex_Node {
    private SimpleStringProperty Name;
    private ORID rid;
    private OClass oClass;
    private OVertex oVertex;
    private TreeItem<OVertex_Node> Tree_Item;
    /**
     * For Tree View Approximation
     */
    private List<OVertex_Node> Childs_Nodes;

    public OVertex_Node(String name) {
        this(name, null, null, null);
    }

    public OVertex_Node(OVertex _oVertex, OClass _oClass) {
        this(orientdb_connector.Get_OVertex_Name(_oVertex), _oVertex, _oClass, null);
    }

    public OVertex_Node(OVertex _oVertex, OClass _oClass, List<OVertex_Node> _Childs_Nodes) {
        this(orientdb_connector.Get_OVertex_Name(_oVertex), _oVertex, _oClass, _Childs_Nodes);
    }

    private OVertex_Node(String name, OVertex _oVertex, OClass _oClass, List<OVertex_Node> _Childs_Nodes) {
        this.Name = new SimpleStringProperty(name);
        if (_oVertex != null) {
            rid = _oVertex.getIdentity();
        }
        else {
            rid = new ORecordId(0);
        }
        oVertex = _oVertex;
        oClass = _oClass;
        Tree_Item = new TreeItem<OVertex_Node>(this);
        Tree_Item.setExpanded(true);
        Childs_Nodes = _Childs_Nodes;

        if (Childs_Nodes != null) {
            List<TreeItem<OVertex_Node>> child_tree_items = new ArrayList<>();
            for (OVertex_Node child_node : Childs_Nodes) {
                child_tree_items.add(child_node.Tree_Item);
            }
            Tree_Item.getChildren().setAll(child_tree_items);
        }
    }

    public SimpleStringProperty nameProperty() {
        if (Name == null) {
            Name = new SimpleStringProperty(this, "Name");
        }
        return Name;
    }

    public ORID ridProperty() {
            /*if (rid == null) {
                rid = new SimpleStringProperty(this, "RID");
            }*/
        return rid;
    }

    public String getName() {
        return Name.get();
    }

    public void setName(String fName) {
        Name.set(fName);
    }

    public ORID getRID() {
        return rid;/*.get();*/
    }

    public void setRid(ORecordId new_rid) {
        rid = new_rid; /*.set(fName);*/
    }

    public OVertex getVertex() {
        return oVertex;
    }

    public OClass getOClass() {
        return oClass;
    }

    public String getOClass_Name() {
        if (oClass != null)
            return oClass.getName();
        else return "";
    }

    public TreeItem<OVertex_Node> getTreeItem() {
        return Tree_Item;
    }
}