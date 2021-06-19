package org.vanilla_manager.orientdb;

import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.record.OVertex;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.List;

public class OVertexNode {
    private SimpleStringProperty Name;
    private ORID rid;
    private OClass oClass;
    private OVertex oVertex;
    private TreeItem<OVertexNode> Tree_Item;
    /**
     * For Tree View Approximation
     */
    private List<OVertexNode> Childs_Nodes;

    public OVertexNode(String name) {
        this(name, null, null, null);
    }

    public OVertexNode(OVertex _oVertex, OClass _oClass) {
        this(OrientdbTalker.getOVertexName(_oVertex), _oVertex, _oClass, null);
    }

    public OVertexNode(OVertex _oVertex, OClass _oClass, List<OVertexNode> _Childs_Nodes) {
        this(OrientdbTalker.getOVertexName(_oVertex), _oVertex, _oClass, _Childs_Nodes);
    }

    private OVertexNode(String name, OVertex _oVertex, OClass _oClass, List<OVertexNode> _Childs_Nodes) {
        this.Name = new SimpleStringProperty(name);
        if (_oVertex != null) {
            rid = _oVertex.getIdentity();
        }
        else {
            rid = new ORecordId(0);
        }
        oVertex = _oVertex;
        oClass = _oClass;
        Tree_Item = new TreeItem<OVertexNode>(this);
        Tree_Item.setExpanded(true);
        Childs_Nodes = _Childs_Nodes;

        if (Childs_Nodes != null) {
            List<TreeItem<OVertexNode>> child_tree_items = new ArrayList<>();
            for (OVertexNode child_node : Childs_Nodes) {
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

    public TreeItem<OVertexNode> getTreeItem() {
        return Tree_Item;
    }
}