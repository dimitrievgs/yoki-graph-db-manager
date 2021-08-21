package org.yoki_manager.orientdb.controls.odocument;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import org.yoki_manager.orientdb.OrientdbTalker;
import org.yoki_manager.orientdb.controls.oproperty.OPropertyCustomAttribute;
import org.yoki_manager.orientdb.controls.oproperty.OPropertyNode;
import org.yoki_manager.orientdb.controls.oproperty.RandomGeneratorPathButton;
import org.yoki_manager.orientdb.treetableview.OClassNode;

//https://stackoverflow.com/questions/42975041/javafx-extend-button-and-add-properties-through-fxml
/**
 * Vertically aligned Box for OClass Properties
 */
public class OClassVBox extends ODocumentVBox {
    private OClass oClass;
    private OrientdbTalker orientdb;
    private TreeTableView oClassesTree;

    public OClass getOClass() {
        return this.oClass;
    }

    public void setOClass(OClass value) {
        this.oClass = value;
    }

    static String oClassVBoxNameTextField = "oClassVBoxNameTextField";
    static String oClassVBoxDescriptionTextField = "oClassVBoxDescriptionTextField";

    Label nameLabel;
    TextField nameField;
    Label iconLabel;
    TextField iconPathField;
    Label descLabel;
    TextField descField;
    TableView<OPropertyNode> oPropertiesTable;

    TextField T2_New_PropertyName_TextField;
    ComboBox T2_New_Property_DataType_Combobox;
    Button T2_AddProperty_Button;
    Button T2_DeleteProperty_Button;
    Button Save_Button;

    public OClassVBox(OClass _oClass, OrientdbTalker _orientdb, TreeTableView _oClassesTree) {
        super();
        this.oClass = _oClass;
        orientdb = _orientdb;
        oClassesTree = _oClassesTree;

        String oClassName = oClass.getName();
        //titledPanesHbox.addNewEntityVBox(oClassVBox, oClassName, inNewTitledPane);
        ObservableList<Node> oClassVBoxChildren = getChildren();

        this.setSpacing(10);

        nameLabel = new Label();
        nameLabel.setText("Name");
        nameField = new TextField();
        nameField.setId(oClassVBoxNameTextField);
        nameField.setText(oClassName);

        iconLabel = new Label();
        iconLabel.setText("Icon");
        iconPathField = new TextField();
        iconPathField.setId(oClassVBoxNameTextField);
        iconPathField.setText("Choose Icon Path...");

        descLabel = new Label();
        descLabel.setText("Description");
        descField = new TextField();
        descField.setId(oClassVBoxDescriptionTextField);
        descField.setText(oClass.getDescription());

        oPropertiesTable = new TableView<>();
        setOClassOPropertiesTableColumns(oPropertiesTable);

        HBox hbox = new HBox();
        hbox.setSpacing(10);
        T2_New_PropertyName_TextField = new TextField();
        //T2_New_PropertyName_TextField.setPrefWidth(300.0);
        T2_New_PropertyName_TextField.setStyle("-fx-font-size: 12px;");
        T2_New_Property_DataType_Combobox = new ComboBox();
        //T2_New_Property_DataType_Combobox.setPrefWidth(100.0);
        T2_New_Property_DataType_Combobox.setStyle("-fx-font-size: 12px;");
        T2_New_Property_DataType_Combobox.getItems().addAll(OPropertyCustomAttribute.DataType.attribute.getPossibleValues());
        T2_New_Property_DataType_Combobox.getSelectionModel().select(0);
        T2_AddProperty_Button = new Button();
        T2_AddProperty_Button.setText("Add");
        //T2_AddProperty_Button.setPrefWidth(80.0);
        T2_AddProperty_Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addOProperty(oPropertiesTable, T2_New_PropertyName_TextField, T2_New_Property_DataType_Combobox);
            }
        });
        T2_DeleteProperty_Button = new Button();
        T2_DeleteProperty_Button.setText("Delete");
        //T2_DeleteProperty_Button.setPrefWidth(80.0);
        T2_DeleteProperty_Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteOProperty(oPropertiesTable);
            }
        });
        Save_Button = new Button();
        Save_Button.setText("Save");
        //Save_Button.setPrefWidth(80.0);
        Save_Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                savePropertiesToOClass();
            }
        });
        hbox.getChildren().addAll(T2_New_PropertyName_TextField, T2_New_Property_DataType_Combobox, T2_AddProperty_Button, T2_DeleteProperty_Button, Save_Button);

        oClassVBoxChildren.addAll(nameLabel, nameField, iconLabel, iconPathField, descLabel, descField, oPropertiesTable, hbox);
    }

    public void setOClassOPropertiesTableColumns(TableView<OPropertyNode> oPropertiesTable) {
        //Table for each class properties
        oPropertiesTable.setEditable(true);

        TableColumn<OPropertyNode, String> tv_name_Col = new TableColumn<>("Name");
        tv_name_Col.setMinWidth(100); //150
        tv_name_Col.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        tv_name_Col.setCellFactory(TextFieldTableCell.forTableColumn());
        //firstNameCol.setCellValueFactory(new PropertyValueFactory<OProperty_Node, String>("name"));

        TableColumn<OPropertyNode, String> tv_description_column = new TableColumn<>("Description");
        tv_description_column.setMinWidth(100); //150
        tv_description_column.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        tv_description_column.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<OPropertyNode, ComboBox> tv_Data_Type_column = new TableColumn<>("Data type");
        tv_Data_Type_column.setMinWidth(100);
        //tv_Data_Type_column.setCellValueFactory(new PropertyValueFactory<OProperty_Node, String>("Data_Type"));
        tv_Data_Type_column.setCellValueFactory(i -> {
            final ComboBox<String> value = i.getValue().getDataType();
            value.prefWidthProperty().bind(i.getTableColumn().widthProperty());
            // binding to constant value
            return Bindings.createObjectBinding(() -> value);
        });

        TableColumn<OPropertyNode, String> tv_orientdbtype_column = new TableColumn<>("OrientDB Type");
        tv_orientdbtype_column.setMinWidth(5); //100
        tv_orientdbtype_column.setCellValueFactory(cellData -> cellData.getValue().orientdbTypeProperty());

        TableColumn<OPropertyNode, RandomGeneratorPathButton> tvRandomGeneratorPathColumn = new TableColumn<>("Random Gen");
        tvRandomGeneratorPathColumn.setMinWidth(75);
        //tv_Data_Type_column.setCellValueFactory(new PropertyValueFactory<OProperty_Node, String>("Data_Type"));
        tvRandomGeneratorPathColumn.setCellValueFactory(i -> {
            final RandomGeneratorPathButton value = i.getValue().getRandomGeneratorPath();
            value.prefWidthProperty().bind(i.getTableColumn().widthProperty());
            // binding to constant value
            return Bindings.createObjectBinding(() -> value);
        });

        oPropertiesTable.getColumns().setAll(tv_name_Col, tv_description_column, tv_Data_Type_column, tv_orientdbtype_column, tvRandomGeneratorPathColumn);
    }

    public void addOProperty(TableView<OPropertyNode> oPropertiesTable, TextField T2_New_PropertyName_TextField, ComboBox T2_New_Property_DataType_Combobox) {
        //check if we have read already some OClass once and that we haven't deleted it yet
        String OProperty_Name = T2_New_PropertyName_TextField.getText();
        if (OProperty_Name.length() > 0 && oClass != null) {
            // Last_Read_OClass != null && orientdb.existsOClassExt(Last_Read_OClass)) {
            OPropertyNode property_node = new OPropertyNode(oClass, OProperty_Name, "",
                    T2_New_Property_DataType_Combobox.getSelectionModel().getSelectedItem().toString(), "");
            oPropertiesTable.getItems().add(property_node);
        }
    }

    public void deleteOProperty(TableView<OPropertyNode> oPropertiesTable) {
        //check if we have read already some OClass once and that we haven't deleted it yet
        OPropertyNode property_node = oPropertiesTable.getSelectionModel().getSelectedItem();
        if (property_node != null) //&& orientdb_connector1.exists_OClass(Last_Read_OClass)
        {
            /*OProperty_Node property_node = new OProperty_Node(OProperty_Name, "",
                    T2_New_Property_DataType_Combobox.getSelectionModel().getSelectedItem().toString());*/
            oPropertiesTable.getItems().remove(property_node);
        }
    }

    public void savePropertiesToOClass() {
        /*try {
            Object o = oClassesTree.getSelectionModel().getSelectedItem();
            TreeItem<OClassNode> ti = (TreeItem<OClassNode>) o;
            if (ti != null && OClass_Name_Is_Forbidden(ti.getValue().getName()) == false) {
                OClassNode class_node = ti.getValue();
                OClass oClass = class_node.getOClass();*/

        ObservableList<OClassNode> hi = oClassesTree.getRoot().getChildren();
        TreeItem<OClassNode> ti = findTreeTableViewItem(oClassesTree.getRoot(), oClass);
        String new_Name = nameField.getText();
        String new_Description = descField.getText();

        ObservableList<OPropertyNode> table_data_in = (ObservableList) oPropertiesTable.getItems();
        ObservableList<OPropertyNode> table_data_out = orientdb.writeOClassOProperties(oClass, new_Name, new_Description, table_data_in);
        if (table_data_out != null) {
            if (ti != null) {
                OClassNode class_node = ti.getValue();
                class_node.setName(new_Name); //в классе OClass_Node
                ti.setValue(class_node); //в TreeItem
            }
            oPropertiesTable.setItems(table_data_out);
        }
    }

    private TreeItem<OClassNode> findTreeTableViewItem(TreeItem<OClassNode> root, OClass oClass) {
        TreeItem<OClassNode> result = null;
        if (root.getChildren() != null) {
            for (TreeItem<OClassNode> ti : root.getChildren()) {
                OClass tiOClass = ti.getValue().getOClass();
                if (tiOClass == oClass) {
                    result = ti;
                    break;
                } else {
                    result = findTreeTableViewItem(ti, oClass);
                }
            }
        }
        return result;
    }

    //-------------------------------------------------------------------------
    //----------------------------------*****----------------------------------

    public TableView<OPropertyNode> getOPropertiesTable() {
        return oPropertiesTable;
    }

    public void setOPropertiesTable(TableView<OPropertyNode> oPropertiesTable) {
        this.oPropertiesTable = oPropertiesTable;
    }
}