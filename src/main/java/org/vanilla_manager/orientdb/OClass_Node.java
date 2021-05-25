package org.vanilla_manager.orientdb;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.List;

public class OClass_Node {
    private SimpleStringProperty Name;
    private OClass oClass;
    public TreeItem<OClass_Node> Tree_Item;
    private List<OClass_Node> Childs_Nodes;

    public OClass_Node(OClass _Class, List<OClass_Node> _Childs_Nodes) {
        String _Name = _Class.getName();
        this.Name = new SimpleStringProperty(_Name);
        oClass = _Class;
        Tree_Item = new TreeItem<OClass_Node>(this);
        Tree_Item.setExpanded(true);
        Childs_Nodes = _Childs_Nodes;

        if (Childs_Nodes != null) {
            List<TreeItem<OClass_Node>> child_tree_items = new ArrayList<>();
            for (OClass_Node child_node : Childs_Nodes) {
                child_tree_items.add(child_node.Tree_Item);
            }
            Tree_Item.getChildren().setAll(child_tree_items);
        }
    }

    public OClass_Node(String _Name, List<OClass_Node> _Childs_Nodes) {
        this.Name = new SimpleStringProperty(_Name);
        oClass = null;
        Tree_Item = new TreeItem<OClass_Node>(this);
        Tree_Item.setExpanded(true);
        Childs_Nodes = _Childs_Nodes;

        if (Childs_Nodes != null) {
            List<TreeItem<OClass_Node>> child_tree_items = new ArrayList<>();
            for (OClass_Node child_node : Childs_Nodes) {
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

    public TreeItem<OClass_Node> getTreeItem() {
        return Tree_Item;
    }
}
