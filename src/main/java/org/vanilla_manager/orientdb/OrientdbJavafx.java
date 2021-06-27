package org.vanilla_manager.orientdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.record.OVertex;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.vanilla_manager.dialogs.MessageBox;
import org.vanilla_manager.orientdb.controls.ScrollableTitledEntityPane;
import org.vanilla_manager.orientdb.controls.TitledEntitiesPanes;
import org.vanilla_manager.orientdb.controls.odocument.OClassVBox;
import org.vanilla_manager.orientdb.controls.odocument.OVertexVBox;
import org.vanilla_manager.orientdb.controls.oproperty.OPropertyCustomAttribute;
import org.vanilla_manager.orientdb.controls.oproperty.OPropertyNode;
import org.vanilla_manager.orientdb.controls.oproperty.OPropertyTextArea;
import org.vanilla_manager.orientdb.treetableview.OClassNode;
import org.vanilla_manager.orientdb.treetableview.OVertexNode;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class OrientdbJavafx {
    public OrientdbTalker orientdb;

    public OrientdbJavafx() {
        orientdb = new OrientdbTalker();
        orientdb.init();
    }

    //-------------------------------------------------------------------------
    //---------------------------------Loading---------------------------------

    public void loadAndShowOVerticesList(TreeTableView<OVertexNode> oVerticesTree, String search_Fiels_Text) {
        List<TreeItem<OVertexNode>> tis = orientdb.readOVertices(search_Fiels_Text);

        OVertexNode super_class_node = new OVertexNode("All Vertices");

        final TreeItem<OVertexNode> root = super_class_node.getTreeItem();

        root.getChildren().setAll(tis);
        //TreeItem<OVertex_Node> rr = root.getChildren().get(0).getParent();

        TreeTableColumn<OVertexNode, String> name_column = new TreeTableColumn<>("Name");
        name_column.setPrefWidth(150);
        /*name_column.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<orientdb_connector.Employee, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getName())
        );*/
        name_column.setCellValueFactory(
                param -> param.getValue().getValue().nameProperty()
        );

        TreeTableColumn<OVertexNode, String> oclass_column =
                new TreeTableColumn<>("OClass");
        oclass_column.setPrefWidth(100);
        oclass_column.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<OVertexNode, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getOClass_Name())
        );

        TreeTableColumn<OVertexNode, String> rid_column =
                new TreeTableColumn<>("RID");
        rid_column.setPrefWidth(100);
        rid_column.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<OVertexNode, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getRID().toString())
        );

        oVerticesTree.setRoot(root);
        oVerticesTree.getColumns().setAll(name_column, oclass_column, rid_column);
        //oVerticesTree.setPrefWidth(152);
        oVerticesTree.setShowRoot(true);
    }

    //T1_RecordName.getText()
    public void loadAndShowOVerticesChildsTree(TreeTableView<OVertexNode> oVerticesTree, String search_Fiels_Text) {
        OVertexNode super_class_node = orientdb.readChildOVertices(search_Fiels_Text);

        final TreeItem<OVertexNode> root = super_class_node.getTreeItem();

        TreeTableColumn<OVertexNode, String> name_column = new TreeTableColumn<>("Name");
        name_column.setPrefWidth(250);
        name_column.setCellValueFactory(
                param -> param.getValue().getValue().nameProperty()
        );

        TreeTableColumn<OVertexNode, String> oclass_column =
                new TreeTableColumn<>("OClass");
        oclass_column.setPrefWidth(100);
        oclass_column.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<OVertexNode, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getOClass_Name())
        );

        oVerticesTree.setRoot(root);
        oVerticesTree.getColumns().setAll(name_column, oclass_column/*, rid_column*/);
        oVerticesTree.setPrefWidth(152);
        oVerticesTree.setShowRoot(true);
    }

    //another solutions for ComboBox: https://stackoverflow.com/questions/35131428/combobox-in-a-tableview-cell-in-javafx
    public void loadAndShowOClassesTree(TreeTableView<OClassNode> oClassesTree, boolean Read_V_OClasses, boolean Read_E_OClasses) {
        //Classes list
        OClassNode super_class_node = orientdb.readOClasses(Read_V_OClasses, Read_E_OClasses);

        final TreeItem<OClassNode> root = super_class_node.Tree_Item;

        TreeTableColumn<OClassNode, String> name_column = new TreeTableColumn<>("Name");
        name_column.setPrefWidth(150);
        name_column.setCellValueFactory(
                param -> param.getValue().getValue().nameProperty()
        );

        oClassesTree.setRoot(root); //root
        oClassesTree.getColumns().setAll(name_column/*, rid_column*/);
        //oClassesTree.setPrefWidth(152);
        oClassesTree.setShowRoot(true);
    }

    //-------------------------------------------------------------------------
    //---------------------------------OVertex---------------------------------

    private int getTreeItemIndexAsChild(TreeItem ti) {
        //root.getChildren().get(0).getParent();
        TreeItem Parent = ti.getParent();
        ObservableList<TreeItem> list = Parent.getChildren();
        return list.indexOf(ti);
    }

    private int getTreeItemChildsLastIndex(TreeItem ti) {
        return ti.getChildren().size() - 1;
    }

    @FXML
    public void addOVertex(TreeTableView<OVertexNode> oVerticesTree, TableView<OPropertyNode> oPropertiesTable,
                           TextField T1_RecordName, String search_Fiels_Text, boolean is_Tree_DB) {
        try {
            String new_Record_Name = search_Fiels_Text;
            int Selected_Index = oVerticesTree.getSelectionModel().getSelectedIndex();
            boolean _is_Tree_DB = is_Tree_DB;
            if (/*_is_Tree_DB == false &&*/ Selected_Index < 0)
                Selected_Index = 0;

            //if (new_Record_Name.length() > 0 && OVertex_Name_Is_Forbidden(new_Record_Name) == false) {
            TreeItem<OVertexNode> ti = null;
            /*if (_is_Tree_DB)*/
            ti = (TreeItem<OVertexNode>) oVerticesTree.getSelectionModel().getSelectedItem();
            if (ti == null) ti = oVerticesTree.getRoot();

            //if (ti != null) {
            OVertex Parent = null;
            if (_is_Tree_DB == true) {
                OVertexNode ee = ti.getValue();
                Parent = ee.getVertex();
            }

            //let's popup dialog to choose OClass
            //MyDialog dialog = new MyDialog("_");
            New_OVertex new_oVertex = dialogAddOVertex(oPropertiesTable, new_Record_Name);
            if (new_oVertex != null) {
                new_Record_Name = new_oVertex.getName();
                OClass oClass = new_oVertex.getOClass();
                OrientdbTalker.NewOVertexResult value = orientdb.addChildOVertex(new_Record_Name, oClass.getName(), Parent);
                if (value.wasCreated() == true) {
                    OVertexNode new_OVertex = new OVertexNode(value.getOVertex(), value.getOClass(), null);

                    if (_is_Tree_DB == true) {
                        int Childs_Index = getTreeItemChildsLastIndex(ti);
                        ti.getChildren().add(Childs_Index + 1, new_OVertex.getTreeItem()); //Selected_Index + 1
                    } else {
                        if (Selected_Index > 0) {
                            int Childs_Index = getTreeItemIndexAsChild(ti);
                            ti.getParent().getChildren().add(Childs_Index + 1, new_OVertex.getTreeItem());
                        } else ti.getChildren().add(0, new_OVertex.getTreeItem());
                    }
                }
                //}

                T1_RecordName.setText("");
            }
            //}
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    public New_OVertex dialogAddOVertex(TableView<OPropertyNode> oPropertiesTable, String new_Record_Name) {
        /*Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        Scene scene = new Scene(new Group(new Text(25, 25, "Hello World!")));
        dialog.setScene(scene);
        dialog.show();*/

        String titleTxt = "df";

        Dialog<New_OVertex> dialog = new Dialog<>();
        dialog.setTitle(titleTxt);
        dialog.setHeaderText("This is a custom dialog. Enter info and \n" +
                "press Ok (or click title bar 'X' for cancel).");
        dialog.setResizable(true);

        Label label1 = new Label("Name: ");
        TextField text1 = new TextField();
        text1.setText(new_Record_Name);

        TreeTableView<OClassNode> OClass_treetableview = new TreeTableView<>();

        HBox hbox = new HBox();
        ObservableList<Node> hbox_children = hbox.getChildren();
        hbox_children.add(OClass_treetableview);
        VBox vbox2 = new VBox();
        ObservableList<javafx.scene.Node> vbox2_children = vbox2.getChildren();
        vbox2_children.add(label1);
        vbox2_children.add(text1);
        hbox_children.add(vbox2);
        dialog.getDialogPane().setContent(hbox);

        hbox.setSpacing(10);

        loadAndShowOClassesTree(OClass_treetableview, true, false);

        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter(new Callback<ButtonType, New_OVertex>() {
            @Override
            public New_OVertex call(ButtonType b) {
                if (b == buttonTypeOk) {
                    boolean bool_result = true;
                    String error_message = "";
                    final String new_Record_Name = text1.getText();
                    if (new_Record_Name.length() == 0) {
                        error_message += "Name is empty! ";
                        bool_result = false;
                    }
                    if (oVertexNameIsForbidden(new_Record_Name) == true) {
                        error_message += "Name is forbidden! ";
                        bool_result = false;
                    }
                    TreeItem<OClassNode> ti = (TreeItem<OClassNode>) OClass_treetableview.getSelectionModel().getSelectedItem();
                    if (ti == null) {
                        error_message += "Treetableview item is not chosen!";
                        bool_result = false;
                    }
                    if (bool_result == true)
                        return new New_OVertex(text1.getText(), ti.getValue().getOClass());
                    else {
                        MessageBox.Show(error_message, "", "Error", Alert.AlertType.ERROR);
                        return null;
                    }
                }
                return null;
            }
        });

        Optional<New_OVertex> result = dialog.showAndWait();

        if (result.isPresent()) {
            return result.get();
        } else return null;
    }

    public class New_OVertex {
        private String Name;
        private OClass oClass;

        public New_OVertex(String _Name, OClass _oClass) {
            Name = _Name;
            oClass = _oClass;
        }

        public String getName() {
            return Name;
        }

        public OClass getOClass() {
            return oClass;
        }
    }

    public void duplicateOVertex(TreeTableView oVerticesTree) {
        try {
            int Selected_Index = oVerticesTree.getSelectionModel().getSelectedIndex();
            //MessageBox.Show(String.valueOf(Selected_Index));
            if (Selected_Index > -1) {
                TreeItem<OVertexNode> ti = (TreeItem<OVertexNode>) oVerticesTree.getSelectionModel().getSelectedItem();
                if (ti != null && oVertexNameIsForbidden(ti.getValue().getName()) == false) {
                    int Childs_Index = getTreeItemIndexAsChild(ti);
                    OVertexNode ee = ti.getValue();
                    //String RID = ee.getRID().toString();

                    OrientdbTalker.NewOVertexResult value = orientdb.duplicateOVertex(ee.getVertex());
                    OVertexNode ee2 = new OVertexNode(value.getOVertex(), value.getOClass(), null);

                    //тут нужен более простой способ быстро изъять обратно это из OElement

                    TreeItem<OVertexNode> ti2 = ee2.getTreeItem();
                    ti.getParent().getChildren().add(Childs_Index + 1, ti2); //Selected_Index + 1
                }
            }
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    public void deleteOVertex(TreeTableView oVerticesTree) {
        try {
            int Selected_Index = oVerticesTree.getSelectionModel().getSelectedIndex();
            Object o = oVerticesTree.getSelectionModel().getSelectedItem();
            TreeItem<OVertexNode> ti = (TreeItem<OVertexNode>) o;
            if (ti != null && oVertexNameIsForbidden(ti.getValue().getName()) == false) {
                //delete from database
                OVertexNode ee = ti.getValue();
                //String RID = ee.getRID().toString();
                orientdb.deleteOElement(ee.getRID());

                //delete from treetableview1
                ti.getParent().getChildren().remove(ti);
                oVerticesTree.getSelectionModel().select(Selected_Index);
                //treetableview1.getSelectionModel().clearSelection();
            }
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    private String[] Forbidden_Record_Names = new String[]{OrientdbTalker.Root_Vertex_Name};

    private boolean oVertexNameIsForbidden(String old_Name) {
        boolean name_is_forbidden = Arrays.stream(Forbidden_Record_Names).anyMatch(old_Name::equals);
        return name_is_forbidden;
    }

    static String oVertexVBoxNameTextField = "oVertexVBoxNameTextField";

    public void saveOPropertiesDataToOVertex(TreeTableView oVerticesTree, TitledEntitiesPanes titledPanesHbox) {
        try {
            Object o = oVerticesTree.getSelectionModel().getSelectedItem();
            TreeItem<OVertexNode> ti = (TreeItem<OVertexNode>) o;
            if (ti != null && oVertexNameIsForbidden(ti.getValue().getName()) == false) {
                OVertexNode oVertexTINode = ti.getValue();
                OVertex oVertex = oVertexTINode.getVertex();
                OClass oClass = orientdb.getOElementOClassExt(oVertex);

                ObservableList<javafx.scene.Node> vboxChildren = titledPanesHbox.getActivePane().getEntitiesVBoxCollection();
                Optional<javafx.scene.Node> findOVertexVBox = vboxChildren.parallelStream().filter(s ->
                        s instanceof OVertexVBox && ((OVertexVBox) s).getOVertex() == oVertex).findFirst();
                if (findOVertexVBox.isPresent()) {
                    OVertexVBox oVertexVBox = (OVertexVBox) findOVertexVBox.get();
                    ObservableList<javafx.scene.Node> oVertexVBoxChildren = oVertexVBox.getChildren();

                    try (ODatabaseSession db = orientdb.openDB();) {
                        db.begin();
                        String newName = oVertexTINode.getName();
                        for (javafx.scene.Node node : oVertexVBoxChildren) {
                            if (node instanceof TextField && node.getId() == oVertexVBoxNameTextField) {
                                //var f = oVertex.getPropertyNames();
                                TextField nameField = (TextField) node;
                                newName = nameField.getText();
                                orientdb.setOVertexName(oVertex, newName);
                            }
                            if (node instanceof OPropertyTextArea) {
                                OPropertyTextArea op_textarea = (OPropertyTextArea) node;
                                OProperty node_property = op_textarea.getOProperty();
                                String node_property_name = node_property.getName();
                                if (oClass.existsProperty(node_property_name)) {
                                    String s = op_textarea.getText();
                                    oVertex.setProperty(node_property_name, s);
                                }
                            }
                        }
                        oVertex.save();
                        db.commit();

                        oVertexTINode.setName(newName);
                        ti.setValue(oVertexTINode); //update item in TreeTableView
                    } catch (Exception e) {
                        MessageBox.Show(e);
                    }
                }
            }
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    public void readOPropertiesDataFromOVertex(TreeTableView oVerticesTree, TitledEntitiesPanes titledPanes, boolean inNewTitledPane) {
        try {
            Object o = oVerticesTree.getSelectionModel().getSelectedItem();
            TreeItem<OVertexNode> ti = (TreeItem<OVertexNode>) o;
            if (ti != null) {
                OVertex oVertex = ti.getValue().getVertex();
                OVertexVBox oVertexVBox = new OVertexVBox(oVertex);
                String oVertexName = orientdb.getOVertexName(oVertex);
                ScrollableTitledEntityPane scrollableTitledEntityPane = titledPanes.addNewEntityVBox(oVertexVBox, oVertexName, inNewTitledPane);
                ObservableList<javafx.scene.Node> oVertexVBoxChildren = oVertexVBox.getChildren();

                Label nameLabel = new Label();
                nameLabel.setText("Name");
                TextField nameField = new TextField();
                nameField.setId(oVertexVBoxNameTextField);
                nameField.setText(oVertexName);
                //nameField.minWidthProperty().bind(scrollableTitledEntityPane.entityInnerWidthProperty());
                //nameField.maxWidthProperty().bind(scrollableTitledEntityPane.entityInnerWidthProperty());
                oVertexVBoxChildren.addAll(nameLabel, nameField);

                Collection<OProperty> Properties = orientdb.getOClassOProperties(oVertex);
                for (OProperty property : Properties) {
                    String property_name = property.getName();
                    String dataType = property.getCustom(OPropertyCustomAttribute.DataType.attribute.getName());
                    Object value = oVertex.getProperty(property_name);
                    if (dataType != null && dataType.equals(OPropertyCustomAttribute.DataType.textPropertyType)) {
                        Label label = new Label();
                        label.setText(property_name);
                        String s_value = (String) value;
                        OPropertyTextArea TA = new OPropertyTextArea(property);
                        TA.setText(s_value);
                        //TA.minWidthProperty().bind(scrollableTitledEntityPane.entityInnerWidthProperty());
                        //TA.maxWidthProperty().bind(scrollableTitledEntityPane.entityInnerWidthProperty());
                        oVertexVBoxChildren.addAll(label, TA);
                    }
                }
            }
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    //-------------------------------------------------------------------------
    //----------------------------------OClass---------------------------------

    /*private void Erase_OClass_Text_Field() {
        T2_RecordName.setText("");
    }*/

    private String[] Forbidden_OClass_Names = new String[]{OrientdbTalker.Vertex_Class_Name,
            OrientdbTalker.Edge_Class_Name, OrientdbTalker.Edge_Child_Page_Class};

    private boolean OClass_Name_Is_Forbidden(String old_Name) {
        boolean name_is_forbidden = Arrays.stream(Forbidden_OClass_Names).anyMatch(old_Name::equals);
        return name_is_forbidden;
    }

    /**
     * may be it's better to incorporate it in oPropertiesTable
     */
    //private OClass Last_Read_OClass = null;

    public void readPropertiesFromOClass(TreeTableView oClassesTree, TitledEntitiesPanes titledPanesHbox, boolean inNewTitledPane) {
        try {
            Object o = oClassesTree.getSelectionModel().getSelectedItem();
            TreeItem<OClassNode> ti = (TreeItem<OClassNode>) o;
            if (ti != null) {
                OClass oClass = ti.getValue().getOClass();

                OClassVBox oClassVBox = new OClassVBox(oClass, orientdb, oClassesTree);
                String oClassName = oClass.getName();
                ScrollableTitledEntityPane scrollableTitledEntityPane = titledPanesHbox.addNewEntityVBox(oClassVBox, oClassName, inNewTitledPane);

                Collection<OProperty> properties = orientdb.readOClassOProperties(oClass);
                final ObservableList<OPropertyNode> data_for_table = FXCollections.observableArrayList();

                if (properties != null) {
                    for (OProperty property : properties) {
                        data_for_table.add(new OPropertyNode(oClass, property));
                    }
                    oClassVBox.getOPropertiesTable().setItems(data_for_table);
                }
                //Last_Read_OClass = oClass;
            }
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    public void addOClass(TreeTableView oClassesTree, TextField T2_RecordName) {
        try {
            String new_OClass_Name = T2_RecordName.getText();
            int Selected_Index = oClassesTree.getSelectionModel().getSelectedIndex();
            if (Selected_Index < 0)
                Selected_Index = 0;
            if (new_OClass_Name.length() > 0 && OClass_Name_Is_Forbidden(new_OClass_Name) == false) {
                TreeItem<OClassNode> ti = null;
                ti = (TreeItem<OClassNode>) oClassesTree.getSelectionModel().getSelectedItem();
                if (ti != null) {
                    int Childs_Index = getTreeItemChildsLastIndex(ti);
                    OClassNode ee = ti.getValue();
                    OClass Parent = ee.getOClass();
                    OrientdbTalker.NewOClassResult value = orientdb.addOClass(new_OClass_Name, Parent);
                    if (value.wasCreated() == true) {
                        OClassNode new_OClass = new OClassNode(value.getOClass(), null);
                        ti.getChildren().add(Childs_Index + 1, new_OClass.getTreeItem()); //Selected_Index + 1
                    }
                }

                T2_RecordName.setText("");
            }
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    public void duplicateOClass(TreeTableView oClassesTree) {
        try {
            int Selected_Index = oClassesTree.getSelectionModel().getSelectedIndex();
            //MessageBox.Show(String.valueOf(Selected_Index));
            if (Selected_Index > -1) {
                TreeItem<OClassNode> ti = (TreeItem<OClassNode>) oClassesTree.getSelectionModel().getSelectedItem();
                if (ti != null && OClass_Name_Is_Forbidden(ti.getValue().getName()) == false) {
                    int Childs_Index = getTreeItemIndexAsChild(ti);
                    OClassNode ee = ti.getValue();
                    OrientdbTalker.NewOClassResult value = orientdb.duplicateOClass(ee.getOClass());
                    OClassNode ee2 = new OClassNode(value.getOClass(), null);

                    //тут нужен более простой способ быстро изъять обратно это из OElement

                    TreeItem<OClassNode> ti2 = ee2.getTreeItem();
                    ti.getParent().getChildren().add(Childs_Index + 1, ti2); //Selected_Index + 1
                }
            }
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    public void deleteOClass(TreeTableView oClassesTree) {
        int Selected_Index = oClassesTree.getSelectionModel().getSelectedIndex();
        Object o = oClassesTree.getSelectionModel().getSelectedItem();
        TreeItem<OClassNode> ti = (TreeItem<OClassNode>) o;
        if (ti != null && OClass_Name_Is_Forbidden(ti.getValue().getName()) == false) {
            OClassNode class_node = ti.getValue();
            OClass oClass = class_node.getOClass();
            orientdb.removeOClassExt(oClass);
            ti.getParent().getChildren().remove(ti);
            oClassesTree.getSelectionModel().select(Selected_Index);
        }
    }
}
