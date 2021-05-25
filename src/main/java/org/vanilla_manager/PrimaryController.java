package org.vanilla_manager;

import java.io.IOException;
import java.util.*;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.record.OVertex;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javafx.scene.web.HTMLEditor;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.vanilla_manager.orientdb.*;

public class PrimaryController {
    public HBox T1_RecordName_parent;
    public Button T1_bLoadDB;
    public TextField T2_RecordName;
    public HBox T2_RecordName_parent;
    public TextField T2_OClass_Name_TextField;
    public TableView<OProperty_Node> T2_tableview1;
    public TextArea T2_OClass_Description_TextField;
    public ComboBox T2_New_Property_DataType_Combobox;
    public TextField T2_New_PropertyName_TextField;
    public HBox tab1_hbox0;
    @FXML
    private TabPane tabpane1;
    @FXML
    private VBox T1_vbox1;
    @FXML
    private Pane pane1;
    @FXML
    private VBox vbox0;
    @FXML
    private VBox T1_vbox2;
    @FXML
    private MenuBar menubar1;
    @FXML
    private Button T1_button1;
    @FXML
    private Button T1_bAddRecord;
    @FXML
    private ToggleButton T1_bLoad_DB_Toggle;
    @FXML
    private Button T1_bSetTextToRecord;
    @FXML
    private Button T1_bReadTextFromRecord;
    @FXML
    private Button T1_bDeleteRecord;
    @FXML
    private Button T1_bDuplicateRecord;
    @FXML
    private TextField T1_RecordName;
    @FXML
    private HTMLEditor T1_htmleditor1;
    @FXML
    private TreeTableView T1_treetableview1;
    @FXML
    private TreeTableView T2_treetableview1;
    @FXML
    private TextField T1_Record_Name_TextField;
    @FXML
    private Separator sep01;

    Stage stage;
    orientdb_connector orientdb_connector1;

    @FXML
    public void initialize() {
        Bind_CSS_Classes();
        Add_Menu();
        Set_Text_in_Controls();
    }

    //https://stackoverflow.com/questions/13246211/javafx-how-to-get-stage-from-controller-during-initialization
    public void post_initialize(Stage _stage, orientdb_connector _orientdb_connector) {
        stage = _stage;
        orientdb_connector1 = _orientdb_connector;

        Set_Sizes_Locations();
        Bind_Events();
    }

    private void Bind_CSS_Classes() {
        //tabpane1.getStyleClass().clear();
        tabpane1.getStyleClass().add("tabpane1");
        vbox0.getStyleClass().add("vbox0");
        T1_treetableview1.getStyleClass().add("treetableview1");
        tab1_hbox0.getStyleClass().add("tab1_hbox0");
        T1_vbox1.getStyleClass().add("T1_vbox1");
    }

    private void Add_Menu() {
        MenuBar menuBar = new MenuBar();
        Menu menu1 = new Menu("File");
        MenuItem menuItemA = new MenuItem("Item A");
        menuItemA.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Item A Clicked");
            }
        });
        MenuItem menuItemB = new MenuItem("Item B");
        menuItemB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Item B Clicked");
            }
        });
        MenuItem menuItemC = new MenuItem("Item C");
        menuItemC.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Item C Clicked");
            }
        });
        final Menu menu2 = new Menu("Options");
        final Menu menu3 = new Menu("Help");
        menu1.getItems().add(menuItemA);
        menu1.getItems().add(menuItemB);
        menu1.getItems().add(menuItemC);
        //menuBar.getMenus().add(menu1);
        menubar1.getMenus().addAll(menu1, menu2, menu3);
    }

    private void Set_Text_in_Controls() {
        T2_New_Property_DataType_Combobox.getItems().addAll(OProperty_Custom_Attribute.Data_Type.Attribute.getPossibleValues());
        T2_New_Property_DataType_Combobox.getSelectionModel().select(0);

        //add images for htmleditor: https://stackoverflow.com/questions/10968000/javafx-htmleditor-insert-image-function
    }

    int treetableview_width = 300;

    private void Set_Sizes_Locations() {
        menubar1.prefWidthProperty().bind(stage.widthProperty());
        vbox0.prefWidthProperty().bind(stage.widthProperty());
        vbox0.prefHeightProperty().bind(stage.heightProperty());
        T1_RecordName.minWidthProperty().bind(T1_RecordName_parent.widthProperty()); //с prefHeightProperty не работает
        T2_RecordName.minWidthProperty().bind(T2_RecordName_parent.widthProperty()); //с prefHeightProperty не работает

        DoubleBinding foo = new DoubleBinding() {
            {
                super.bind(stage.widthProperty());
            }

            @Override
            protected double computeValue() {
                return stage.widthProperty().getValue() - T1_vbox1.widthProperty().getValue() - tab1_hbox0.getSpacing() - T1_vbox1.getPadding().getLeft() - 7;
            }
        };
        T1_vbox2.prefWidthProperty().bind(foo);

        T1_treetableview1.setMinWidth(treetableview_width);
        T2_treetableview1.setMinWidth(treetableview_width);

        T1_RecordName.setMaxWidth(100);
    }


    private void Bind_Events() {
        //stage.setOnShowing();
        stage.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Load_DB_OVertices_to_T1_TTV_ALL(T1_treetableview1);
                Load_DB_Classes_to_T2_TTV(T2_treetableview1, true, true);
            }
        });

        //Prevent expand of child nodes after mouse double-click
        //Disable TreeItem's default expand/collapse on double click (JavaFX 8): https://stackoverflow.com/questions/46436974/disable-treeitems-default-expand-collapse-on-double-click-javafx-8
        T1_treetableview1.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            if (event.getClickCount() % 2 == 0 && event.isPrimaryButtonDown()) {
                T1_Read_Properties_Data_From_OVertex(null);
                event.consume();
            }
        });

        //Prevent expand of child nodes after mouse double-click
        T2_treetableview1.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            if (event.getClickCount() % 2 == 0 && event.isPrimaryButtonDown()) {
                T2_Read_Properties_From_Class(null);
                event.consume();
            }
        });

        T1_RecordName.textProperty().addListener((observable, oldValue, newValue) -> {
            //oldValue, newValue
            String Base_name = "";
            if (newValue.length() == 0)
                Base_name = "Reload";
            else
                Base_name = "Search";
            T1_bLoadDB.setText(Base_name);
        });

        T1_bLoad_DB_Toggle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String Text = (T1_bLoad_DB_Toggle.isSelected()) ? "Tree" : "Plain (all)";
                T1_bLoad_DB_Toggle.setText(Text);
                //event.consume();
            }
        });
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    Random r = new Random();
    String[] pronoun_and_verb = {"I am", "He is", "She is", "It is", "They are", "We are", "You are"};

    //-------------------------------------------------------------------------
    //-----------------------------Macro Buttons-------------------------------

    public void ManageElements(ActionEvent actionEvent) {
    }

    public void ManageClasses(ActionEvent actionEvent) {
    }

    //-------------------------------------------------------------------------
    //---------------------------Graph DB - Loading----------------------------

    String Property_Description_Name = "Description";

    private final ImageView depIcon = new ImageView(
            new Image(getClass().getResourceAsStream("icons/department.png"))
    );

    //https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/tree-table-view.htm
    //update of edited cells: https://stackoverflow.com/questions/23545051/javafx-treetableview-cell-value-not-getting-updated
    public void T1_Load_DB_Toggle(ActionEvent actionEvent) {

    }

    private boolean is_Tree_DB() {
        return T1_bLoad_DB_Toggle.isSelected();
    }

    public void T1_LoadDB(ActionEvent actionEvent) {
        if (is_Tree_DB())
            Load_DB_OVertices_to_T1_TTV_Child_Pages(T1_treetableview1);
        else Load_DB_OVertices_to_T1_TTV_ALL(T1_treetableview1);
    }

    public void T2_LoadDB(ActionEvent actionEvent) {
        Load_DB_Classes_to_T2_TTV(T2_treetableview1, true, true);
    }

    private void Load_DB_OVertices_to_T1_TTV_ALL(TreeTableView<OVertex_Node> T1_treetableview1) {
        List<TreeItem<OVertex_Node>> tis = orientdb_connector1.Read_Vertices(T1_RecordName.getText());

        OVertex_Node super_class_node = new OVertex_Node("All Vertices");

        final TreeItem<OVertex_Node> root = super_class_node.getTreeItem();

        root.getChildren().setAll(tis);
        //TreeItem<OVertex_Node> rr = root.getChildren().get(0).getParent();

        TreeTableColumn<OVertex_Node, String> name_column = new TreeTableColumn<>("Name");
        name_column.setPrefWidth(150);
        /*name_column.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<orientdb_connector.Employee, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getName())
        );*/
        name_column.setCellValueFactory(
                param -> param.getValue().getValue().nameProperty()
        );

        TreeTableColumn<OVertex_Node, String> oclass_column =
                new TreeTableColumn<>("OClass");
        oclass_column.setPrefWidth(100);
        oclass_column.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<OVertex_Node, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getOClass_Name())
        );

        TreeTableColumn<OVertex_Node, String> rid_column =
                new TreeTableColumn<>("RID");
        rid_column.setPrefWidth(100);
        rid_column.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<OVertex_Node, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getRID().toString())
        );

        T1_treetableview1.setRoot(root);
        T1_treetableview1.getColumns().setAll(name_column, oclass_column, rid_column);
        T1_treetableview1.setPrefWidth(152);
        T1_treetableview1.setShowRoot(true);
    }

    private void Load_DB_OVertices_to_T1_TTV_Child_Pages(TreeTableView<OVertex_Node> T1_treetableview1) {
        OVertex_Node super_class_node = orientdb_connector1.Read_Child_OVertices(T1_RecordName.getText());

        final TreeItem<OVertex_Node> root = super_class_node.getTreeItem();

        TreeTableColumn<OVertex_Node, String> name_column = new TreeTableColumn<>("Name");
        name_column.setPrefWidth(250);
        name_column.setCellValueFactory(
                param -> param.getValue().getValue().nameProperty()
        );

        TreeTableColumn<OVertex_Node, String> oclass_column =
                new TreeTableColumn<>("OClass");
        oclass_column.setPrefWidth(100);
        oclass_column.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<OVertex_Node, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getOClass_Name())
        );

        T1_treetableview1.setRoot(root);
        T1_treetableview1.getColumns().setAll(name_column, oclass_column/*, rid_column*/);
        T1_treetableview1.setPrefWidth(152);
        T1_treetableview1.setShowRoot(true);
    }

    //another solutions for ComboBox: https://stackoverflow.com/questions/35131428/combobox-in-a-tableview-cell-in-javafx
    private void Load_DB_Classes_to_T2_TTV(TreeTableView<OClass_Node> T2_treetableview1, boolean Read_V_OClasses, boolean Read_E_OClasses) {
        //Classes list
        OClass_Node super_class_node = orientdb_connector1.Read_Classes(Read_V_OClasses, Read_E_OClasses);

        final TreeItem<OClass_Node> root = super_class_node.Tree_Item;

        TreeTableColumn<OClass_Node, String> name_column = new TreeTableColumn<>("Name");
        name_column.setPrefWidth(150);
        name_column.setCellValueFactory(
                param -> param.getValue().getValue().nameProperty()
        );

        T2_treetableview1.setRoot(root); //root
        T2_treetableview1.getColumns().setAll(name_column/*, rid_column*/);
        T2_treetableview1.setPrefWidth(152);
        T2_treetableview1.setShowRoot(true);


        //Table for each class properties
        T2_tableview1.setEditable(true);

        TableColumn<OProperty_Node, String> tv_name_Col = new TableColumn<>("Name");
        tv_name_Col.setMinWidth(100); //150
        tv_name_Col.setCellValueFactory(cellData -> cellData.getValue().name_Property());
        tv_name_Col.setCellFactory(TextFieldTableCell.forTableColumn());
        //firstNameCol.setCellValueFactory(new PropertyValueFactory<OProperty_Node, String>("name"));

        TableColumn<OProperty_Node, String> tv_description_column = new TableColumn<>("Description");
        tv_description_column.setMinWidth(100); //150
        tv_description_column.setCellValueFactory(cellData -> cellData.getValue().description_Property());
        tv_description_column.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<OProperty_Node, ComboBox> tv_Data_Type_column = new TableColumn<>("Data type");
        tv_Data_Type_column.setMinWidth(100);
        //tv_Data_Type_column.setCellValueFactory(new PropertyValueFactory<OProperty_Node, String>("Data_Type"));
        tv_Data_Type_column.setCellValueFactory(i -> {
            final ComboBox<String> value = i.getValue().getData_Type();
            value.prefWidthProperty().bind(i.getTableColumn().widthProperty());
            // binding to constant value
            return Bindings.createObjectBinding(() -> value);
        });

        TableColumn<OProperty_Node, String> tv_orientdbtype_column = new TableColumn<>("OrientDB Type");
        tv_orientdbtype_column.setMinWidth(5); //100
        tv_orientdbtype_column.setCellValueFactory(cellData -> cellData.getValue().OrientDBType_Property());

        T2_tableview1.getColumns().setAll(tv_name_Col, tv_description_column, tv_Data_Type_column, tv_orientdbtype_column);
    }

    //-------------------------------------------------------------------------
    //--------------------Graph DB - Records - Manipulation--------------------

    private void Erase_OVertex_Text_Field() {
        T1_RecordName.setText("");
    }

    private int Get_TreeItem_Index_As_Child(TreeItem ti) {
        //root.getChildren().get(0).getParent();
        TreeItem Parent = ti.getParent();
        ObservableList<TreeItem> list = Parent.getChildren();
        return list.indexOf(ti);
    }

    private int Get_TreeItem_Childs_Last_Index(TreeItem ti) {
        return ti.getChildren().size() - 1;
    }

    @FXML
    private void T1_Add_OVertex() throws IOException {
        try {
            String new_Record_Name = T1_RecordName.getText();
            int Selected_Index = T1_treetableview1.getSelectionModel().getSelectedIndex();
            boolean _is_Tree_DB = is_Tree_DB();
            if (/*_is_Tree_DB == false &&*/ Selected_Index < 0)
                Selected_Index = 0;

            //if (new_Record_Name.length() > 0 && OVertex_Name_Is_Forbidden(new_Record_Name) == false) {
            TreeItem<OVertex_Node> ti = null;
            /*if (_is_Tree_DB)*/
            ti = (TreeItem<OVertex_Node>) T1_treetableview1.getSelectionModel().getSelectedItem();
            if (ti == null) ti = T1_treetableview1.getRoot();

            //if (ti != null) {
            OVertex Parent = null;
            if (_is_Tree_DB == true) {
                OVertex_Node ee = ti.getValue();
                Parent = ee.getVertex();
            }

            //let's popup dialog to choose OClass
            //MyDialog dialog = new MyDialog("_");
            New_OVertex new_oVertex = Dialog_Add_OVertex(new_Record_Name);
            if (new_oVertex != null) {
                new_Record_Name = new_oVertex.getName();
                OClass oClass = new_oVertex.getOClass();
                orientdb_connector.New_OVertex_Result value = orientdb_connector1.Add_Child_OVertex(new_Record_Name, oClass.getName(), Parent);
                if (value.wasCreated() == true) {
                    OVertex_Node new_OVertex = new OVertex_Node(value.getOVertex(), value.getOClass(), null);

                    if (_is_Tree_DB == true) {
                        int Childs_Index = Get_TreeItem_Childs_Last_Index(ti);
                        ti.getChildren().add(Childs_Index + 1, new_OVertex.getTreeItem()); //Selected_Index + 1
                    } else {
                        if (Selected_Index > 0) {
                            int Childs_Index = Get_TreeItem_Index_As_Child(ti);
                            ti.getParent().getChildren().add(Childs_Index + 1, new_OVertex.getTreeItem());
                        } else ti.getChildren().add(0, new_OVertex.getTreeItem());
                    }
                }
                //}

                Erase_OVertex_Text_Field();
            }
            //}
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    private New_OVertex Dialog_Add_OVertex(String new_Record_Name) {
        /*Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        Scene scene = new Scene(new Group(new Text(25, 25, "Hello World!")));
        dialog.setScene(scene);
        dialog.show();*/

        String titleTxt = "df";

        Dialog<New_OVertex> dialog = new Dialog<>();
        dialog.setTitle(titleTxt);
        dialog.setHeaderText("This is a custom dialog. Enter info and \n" +
                "press Okay (or click title bar 'X' for cancel).");
        dialog.setResizable(true);

        Label label1 = new Label("Name: ");
        //Label label2 = new Label("Phone: ");
        TextField text1 = new TextField();
        text1.setText(new_Record_Name);
        //TextField text2 = new TextField();

        TreeTableView<OClass_Node> OClass_treetableview = new TreeTableView<>();

        HBox hbox = new HBox();
        ObservableList<javafx.scene.Node> hbox_children = hbox.getChildren();
        hbox_children.add(OClass_treetableview);
        VBox vbox2 = new VBox();
        ObservableList<javafx.scene.Node> vbox2_children = vbox2.getChildren();
        vbox2_children.add(label1);
        vbox2_children.add(text1);
        //box2_children.add(label2);
        //vbox2_children.add(text2);
        hbox_children.add(vbox2);
        dialog.getDialogPane().setContent(hbox);

        hbox.setSpacing(10);
        //        vbox0.prefWidthProperty().bind(stage.widthProperty());

        Load_DB_Classes_to_T2_TTV(OClass_treetableview, true, false);

        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter(new Callback<ButtonType, New_OVertex>() {
            @Override
            public New_OVertex call(ButtonType b) {
                if (b == buttonTypeOk) {
                    boolean bool_result = true;
                    String error_message = "";
                    final String new_Record_Name = text1.getText();
                    if (new_Record_Name.length() == 0)
                    {
                        error_message += "Name is empty! ";
                        bool_result = false;
                    }
                    if (OVertex_Name_Is_Forbidden(new_Record_Name) == true)
                    {
                        error_message += "Name is forbidden! ";
                        bool_result = false;
                    }
                    TreeItem<OClass_Node> ti = (TreeItem<OClass_Node>) OClass_treetableview.getSelectionModel().getSelectedItem();
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

    public void T1_Duplicate_OVertex(ActionEvent actionEvent) {
        try {
            int Selected_Index = T1_treetableview1.getSelectionModel().getSelectedIndex();
            //MessageBox.Show(String.valueOf(Selected_Index));
            if (Selected_Index > -1) {
                TreeItem<OVertex_Node> ti = (TreeItem<OVertex_Node>) T1_treetableview1.getSelectionModel().getSelectedItem();
                if (ti != null && OVertex_Name_Is_Forbidden(ti.getValue().getName()) == false) {
                    int Childs_Index = Get_TreeItem_Index_As_Child(ti);
                    OVertex_Node ee = ti.getValue();
                    //String RID = ee.getRID().toString();

                    orientdb_connector.New_OVertex_Result value = orientdb_connector1.Duplicate_OVertex(ee.getVertex());
                    OVertex_Node ee2 = new OVertex_Node(value.getOVertex(), value.getOClass(), null);

                    //тут нужен более простой способ быстро изъять обратно это из OElement

                    TreeItem<OVertex_Node> ti2 = ee2.getTreeItem();
                    ti.getParent().getChildren().add(Childs_Index + 1, ti2); //Selected_Index + 1
                }
            }
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    public void T1_Delete_OVertex(ActionEvent actionEvent) {
        try {
            int Selected_Index = T1_treetableview1.getSelectionModel().getSelectedIndex();
            Object o = T1_treetableview1.getSelectionModel().getSelectedItem();
            TreeItem<OVertex_Node> ti = (TreeItem<OVertex_Node>) o;
            if (ti != null && OVertex_Name_Is_Forbidden(ti.getValue().getName()) == false) {
                //delete from database
                OVertex_Node ee = ti.getValue();
                //String RID = ee.getRID().toString();
                orientdb_connector1.Delete_OElement(ee.getRID());

                //delete from treetableview1
                ti.getParent().getChildren().remove(ti);
                T1_treetableview1.getSelectionModel().select(Selected_Index);
                //treetableview1.getSelectionModel().clearSelection();
            }
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    private String[] Forbidden_Record_Names = new String[]{orientdb_connector1.Root_Vertex_Name};

    private boolean OVertex_Name_Is_Forbidden(String old_Name) {
        boolean name_is_forbidden = Arrays.stream(Forbidden_Record_Names).anyMatch(old_Name::equals);
        return name_is_forbidden;
    }

    public void T1_Write_Properties_Data_To_OVertex(ActionEvent actionEvent) {
        try {
            Object o = T1_treetableview1.getSelectionModel().getSelectedItem();
            TreeItem<OVertex_Node> ti = (TreeItem<OVertex_Node>) o;
            if (ti != null && OVertex_Name_Is_Forbidden(ti.getValue().getName()) == false) {
                OVertex_Node vertex_node = ti.getValue();
                OVertex oVertex = vertex_node.getVertex();

                String new_Name = T1_Record_Name_TextField.getText();

                ArrayList<String> PropertiesNames = new ArrayList<String>();
                PropertiesNames.add("Name");
                PropertiesNames.add(Property_Description_Name);

                ArrayList<String> values = new ArrayList<String>();
                values.add(new_Name);
                values.add(T1_htmleditor1.getHtmlText());

                if (orientdb_connector1.Write_Record_Properties(oVertex, PropertiesNames, values) == true) {
                    vertex_node.setName(new_Name); //в классе OVertex_Node
                    ti.setValue(vertex_node); //в TreeItem
                }
            }
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    public void T1_Read_Properties_Data_From_OVertex(ActionEvent actionEvent) {
        try {
            Object o = T1_treetableview1.getSelectionModel().getSelectedItem();
            TreeItem<OVertex_Node> ti = (TreeItem<OVertex_Node>) o;
            if (ti != null) {
                OVertex oVertex = ti.getValue().getVertex();
            /*ArrayList<String> PropertiesNames = new ArrayList<String>();
            PropertiesNames.add("Name");
            PropertiesNames.add(Property_Description_Name);*/

                ObservableList<javafx.scene.Node> vbox_children = T1_vbox2.getChildren();
                vbox_children.clear();

                Collection<OProperty> Properties = orientdb_connector1.Get_Properties(oVertex);
                for (OProperty property : Properties) {
                    String property_name = property.getName();
                    String Data_Type = property.getCustom(OProperty_Custom_Attribute.Data_Type.Attribute.getName());
                    Object value = oVertex.getProperty(property_name);
                    if (Data_Type != null && Data_Type.equals(OProperty_Custom_Attribute.Data_Type.Text_Property_Type)) {
                        Label label = new Label();
                        label.setText(property_name);
                        String s_value = (String) value;
                        TextArea TA = new TextArea();
                        TA.setText(s_value);
                        vbox_children.add(label);
                        vbox_children.add(TA);
                    }
                }

            /*List<String> value = orientdb_connector1.Read_Record_Properties(oVertex, PropertiesNames);
            if (value != null) {
                //read and make controls in T1_vbox2


                T1_Record_Name_TextField.setText(value.get(0));
                T1_htmleditor1.setHtmlText(value.get(1));
            }*/
            }
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    //-------------------------------------------------------------------------
    //--------------------Graph DB - Classes - Manipulation--------------------

    private void Erase_OClass_Text_Field() {
        T2_RecordName.setText("");
    }

    private String[] Forbidden_OClass_Names = new String[]{orientdb_connector.Vertex_Class_Name,
            orientdb_connector.Edge_Class_Name, orientdb_connector.Edge_Child_Page_Class};

    private boolean OClass_Name_Is_Forbidden(String old_Name) {
        boolean name_is_forbidden = Arrays.stream(Forbidden_OClass_Names).anyMatch(old_Name::equals);
        return name_is_forbidden;
    }

    public void T2_Write_Properties_To_Class(ActionEvent actionEvent) {
        try {
            Object o = T2_treetableview1.getSelectionModel().getSelectedItem();
            TreeItem<OClass_Node> ti = (TreeItem<OClass_Node>) o;
            if (ti != null && OClass_Name_Is_Forbidden(ti.getValue().getName()) == false) {
                OClass_Node class_node = ti.getValue();
                OClass oClass = class_node.getOClass();

                String new_Name = T2_OClass_Name_TextField.getText();
                String new_Description = T2_OClass_Description_TextField.getText();

                ObservableList<OProperty_Node> table_data_in = (ObservableList) T2_tableview1.getItems();
                ObservableList<OProperty_Node> table_data_out = orientdb_connector1.Write_OClass_Properties(oClass, new_Name, new_Description, table_data_in);
                if (table_data_out != null) {
                    class_node.setName(new_Name); //в классе OClass_Node
                    ti.setValue(class_node); //в TreeItem
                    T2_tableview1.setItems(table_data_out);
                }
            }
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    private OClass Last_Read_OClass = null;

    public void T2_Read_Properties_From_Class(ActionEvent actionEvent) {
        try {
            Object o = T2_treetableview1.getSelectionModel().getSelectedItem();
            TreeItem<OClass_Node> ti = (TreeItem<OClass_Node>) o;
            if (ti != null) {
                OClass oClass = ti.getValue().getOClass();

                Collection<OProperty> properties = orientdb_connector1.Read_OClass_Properties(oClass);

                final ObservableList<OProperty_Node> data_for_table = FXCollections.observableArrayList();

                //data.add(new OProperty_Node("Name", ""));

                if (properties != null) {
                    for (OProperty property : properties) {
                        data_for_table.add(new OProperty_Node(property));
                    }

                    T2_OClass_Name_TextField.setText(oClass.getName());
                    T2_OClass_Description_TextField.setText(oClass.getDescription());
                    T2_tableview1.setItems(data_for_table);

                    Last_Read_OClass = oClass;
                    //T2_htmleditor1.setHtmlText(properties.get(1));
                }
            }
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    public void T2_Add_OClass(ActionEvent actionEvent) {
        try {
            String new_OClass_Name = T2_RecordName.getText();
            int Selected_Index = T2_treetableview1.getSelectionModel().getSelectedIndex();
            if (Selected_Index < 0)
                Selected_Index = 0;
            if (new_OClass_Name.length() > 0 && OClass_Name_Is_Forbidden(new_OClass_Name) == false) {
                TreeItem<OClass_Node> ti = null;
                ti = (TreeItem<OClass_Node>) T2_treetableview1.getSelectionModel().getSelectedItem();
                if (ti != null) {
                    int Childs_Index = Get_TreeItem_Childs_Last_Index(ti);
                    OClass_Node ee = ti.getValue();
                    OClass Parent = ee.getOClass();
                    orientdb_connector.New_OClass_Result value = orientdb_connector1.Add_OClass(new_OClass_Name, Parent);
                    if (value.wasCreated() == true) {
                        OClass_Node new_OClass = new OClass_Node(value.getOClass(), null);
                        ti.getChildren().add(Childs_Index + 1, new_OClass.getTreeItem()); //Selected_Index + 1
                    }
                }

                Erase_OClass_Text_Field();
            }
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    public void T2_Duplicate_OClass(ActionEvent actionEvent) {
        try {
            int Selected_Index = T2_treetableview1.getSelectionModel().getSelectedIndex();
            //MessageBox.Show(String.valueOf(Selected_Index));
            if (Selected_Index > -1) {
                TreeItem<OClass_Node> ti = (TreeItem<OClass_Node>) T2_treetableview1.getSelectionModel().getSelectedItem();
                if (ti != null && OClass_Name_Is_Forbidden(ti.getValue().getName()) == false) {
                    int Childs_Index = Get_TreeItem_Index_As_Child(ti);
                    OClass_Node ee = ti.getValue();
                    orientdb_connector.New_OClass_Result value = orientdb_connector1.Duplicate_OClass(ee.getOClass());
                    OClass_Node ee2 = new OClass_Node(value.getOClass(), null);

                    //тут нужен более простой способ быстро изъять обратно это из OElement

                    TreeItem<OClass_Node> ti2 = ee2.getTreeItem();
                    ti.getParent().getChildren().add(Childs_Index + 1, ti2); //Selected_Index + 1
                }
            }
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }

    public void T2_Delete_OClass(ActionEvent actionEvent) {
        int Selected_Index = T2_treetableview1.getSelectionModel().getSelectedIndex();
        Object o = T2_treetableview1.getSelectionModel().getSelectedItem();
        TreeItem<OClass_Node> ti = (TreeItem<OClass_Node>) o;
        if (ti != null && OClass_Name_Is_Forbidden(ti.getValue().getName()) == false) {
            OClass_Node class_node = ti.getValue();
            OClass oClass = class_node.getOClass();
            orientdb_connector1.Remove_OClass_Ext(oClass);
            ti.getParent().getChildren().remove(ti);
            T2_treetableview1.getSelectionModel().select(Selected_Index);
        }
    }

    public void T2_AddProperty(ActionEvent actionEvent) {
        //check if we have read already some OClass once and that we haven't deleted it yet
        String OProperty_Name = T2_New_PropertyName_TextField.getText();
        if (OProperty_Name.length() > 0 && Last_Read_OClass != null && orientdb_connector1.Exists_OClass_Ext(Last_Read_OClass)) {
            OProperty_Node property_node = new OProperty_Node(OProperty_Name, "",
                    T2_New_Property_DataType_Combobox.getSelectionModel().getSelectedItem().toString());
            T2_tableview1.getItems().add(property_node);
        }
    }

    public void T2_DeleteProperty(ActionEvent actionEvent) {
        //check if we have read already some OClass once and that we haven't deleted it yet
        OProperty_Node property_node = T2_tableview1.getSelectionModel().getSelectedItem();
        if (property_node != null) //&& orientdb_connector1.exists_OClass(Last_Read_OClass)
        {
            /*OProperty_Node property_node = new OProperty_Node(OProperty_Name, "",
                    T2_New_Property_DataType_Combobox.getSelectionModel().getSelectedItem().toString());*/
            T2_tableview1.getItems().remove(property_node);
        }
    }
}
