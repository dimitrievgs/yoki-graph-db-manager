package org.yoki_manager.orientdb.treetableview;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.List;

public class OClassNode {
    private SimpleStringProperty Name;
    private OClass oClass;
    public TreeItem<OClassNode> Tree_Item;
    private List<OClassNode> Childs_Nodes;

    public OClassNode(OClass _Class, List<OClassNode> _Childs_Nodes) {
        String _Name = _Class.getName();
        this.Name = new SimpleStringProperty(_Name);
        oClass = _Class;
        Tree_Item = new TreeItem<OClassNode>(this);
        Tree_Item.setExpanded(true);
        Childs_Nodes = _Childs_Nodes;

        if (Childs_Nodes != null) {
            List<TreeItem<OClassNode>> child_tree_items = new ArrayList<>();
            for (OClassNode child_node : Childs_Nodes) {
                child_tree_items.add(child_node.Tree_Item);
            }
            Tree_Item.getChildren().setAll(child_tree_items);
        }
    }

    public OClassNode(String _Name, List<OClassNode> _Childs_Nodes) {
        this.Name = new SimpleStringProperty(_Name);
        oClass = null;
        Tree_Item = new TreeItem<OClassNode>(this);
        Tree_Item.setExpanded(true);
        Childs_Nodes = _Childs_Nodes;

        if (Childs_Nodes != null) {
            List<TreeItem<OClassNode>> child_tree_items = new ArrayList<>();
            for (OClassNode child_node : Childs_Nodes) {
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

    public String getName() {
        return Name.get();
    }

    public OClass getOClass() {
        return oClass;
    }

    public void setName(String fName) {
        Name.set(fName);
    }

    public TreeItem<OClassNode> getTreeItem() {
        return Tree_Item;
    }
}
