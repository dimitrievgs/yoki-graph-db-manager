package org.vanilla_manager;

import java.io.IOException;

import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javafx.scene.web.HTMLEditor;
import javafx.stage.WindowEvent;
import org.vanilla_manager.orientdb.*;
import org.vanilla_manager.orientdb.oproperty.OPropertyNode;
import org.vanilla_manager.overtex_controls.TitledPanesHbox;

public class PrimaryController {
    public HBox T1_RecordName_parent;
    public Button T1_bLoadDB;
    public TextField T2_RecordName;
    public HBox T2_RecordName_parent;
    //public TextField T2_OClass_Name_TextField;
    public TableView<OPropertyNode> oPropertiesTable;
    //public TextArea T2_OClass_Description_TextField;
    //public ComboBox T2_New_Property_DataType_Combobox;
    //public TextField T2_New_PropertyName_TextField;
    @FXML
    private TabPane tabpane1;
    @FXML
    private VBox OVertexTabVBox;
    @FXML
    private Pane pane1;
    @FXML
    private HBox hbox0;
    @FXML
    private TitledPanesHbox titledPanesHbox;
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
    private TreeTableView oVerticesTree;
    @FXML
    private TreeTableView oClassesTree;
    @FXML
    private TextField T1_Record_Name_TextField;
    @FXML
    private Separator sep01;

    Stage stage;
    OrientdbJavafx orientdbJavafx;

    //-------------------------------------------------------------------------
    //--------------------------Init & GUI behavior----------------------------

    @FXML
    public void initialize() {
        bindCSSClasses();
        addMenu();
        setTextInControls();
    }

    //https://stackoverflow.com/questions/13246211/javafx-how-to-get-stage-from-controller-during-initialization
    public void postInitialize(Stage _stage, OrientdbJavafx _orientdb_javafx) {
        stage = _stage;
        this.orientdbJavafx = _orientdb_javafx;

        setSizesLocations();
        bindEvents();
    }

    private void bindCSSClasses() {
        //tabpane1.getStyleClass().clear();
        tabpane1.getStyleClass().add("tabpane1");
        hbox0.getStyleClass().add("vbox0");
        oVerticesTree.getStyleClass().add("treetableview1");
        //tab1_hbox0.getStyleClass().add("tab1_hbox0");
        OVertexTabVBox.getStyleClass().add("T1_vbox1");
    }

    private void addMenu() {
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

    private void setTextInControls() {
        //add images for htmleditor: https://stackoverflow.com/questions/10968000/javafx-htmleditor-insert-image-function
    }

    int treetableview_width = 300;

    private void setSizesLocations() {
        menubar1.prefWidthProperty().bind(stage.widthProperty());
        hbox0.prefWidthProperty().bind(stage.widthProperty());
        hbox0.prefHeightProperty().bind(stage.heightProperty());
        T1_RecordName.minWidthProperty().bind(T1_RecordName_parent.widthProperty()); //с prefHeightProperty не работает
        T2_RecordName.minWidthProperty().bind(T2_RecordName_parent.widthProperty()); //с prefHeightProperty не работает

        DoubleBinding foo = new DoubleBinding() {
            {
                super.bind(stage.widthProperty());
            }

            @Override
            protected double computeValue() {
                return stage.widthProperty().getValue() - OVertexTabVBox.widthProperty().getValue()
                        /*- OVertexTabVBox.getSpacing()*/ /*- OVertexTabVBox.getPadding().getLeft()*/ - 16; //where is this shift from??
            }
        };
        titledPanesHbox.prefWidthProperty().bind(foo);

        oVerticesTree.setMinWidth(treetableview_width);
        oClassesTree.setMinWidth(treetableview_width);

        T1_RecordName.setMaxWidth(100);
    }


    private void bindEvents() {
        //stage.setOnShowing();
        stage.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                orientdbJavafx.loadAndShowOVerticesList(oVerticesTree, T1_RecordName.getText());
                orientdbJavafx.loadAndShowOClassesTree(oClassesTree, true, true);
            }
        });

        //Prevent expand of child nodes after mouse double-click
        //Disable TreeItem's default expand/collapse on double click (JavaFX 8): https://stackoverflow.com/questions/46436974/disable-treeitems-default-expand-collapse-on-double-click-javafx-8
        oVerticesTree.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            if (event.getClickCount() % 2 == 0 && event.isPrimaryButtonDown()) {
                orientdbJavafx.readOPropertiesDataFromOVertex(oVerticesTree, titledPanesHbox, event.isControlDown());
                event.consume();
            }
        });

        //Prevent expand of child nodes after mouse double-click
        oClassesTree.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
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

    public void manageElements(ActionEvent actionEvent) {
    }

    public void manageClasses(ActionEvent actionEvent) {
    }

    //-------------------------------------------------------------------------
    //-----------------------------Graph DB - Load-----------------------------

    String Property_Description_Name = "Description";

    private final ImageView depIcon = new ImageView(
            new Image(getClass().getResourceAsStream("icons/department.png"))
    );

    private boolean is_Tree_DB() {
        return T1_bLoad_DB_Toggle.isSelected();
    }

    public void T1_LoadDB(ActionEvent actionEvent) {
        if (is_Tree_DB())
            orientdbJavafx.loadAndShowOVerticesChildsTree(oVerticesTree, T1_RecordName.getText());
        else orientdbJavafx.loadAndShowOVerticesList(oVerticesTree, T1_RecordName.getText());
    }

    public void T2_LoadDB(ActionEvent actionEvent) {
        orientdbJavafx.loadAndShowOClassesTree(oClassesTree, true, true);
    }

    //-------------------------------------------------------------------------
    //----------------------------Graph DB - OVertex---------------------------

    @FXML
    private void T1_Add_OVertex() throws IOException {
        orientdbJavafx.addOVertex(oVerticesTree, oPropertiesTable, T1_RecordName, T1_RecordName.getText(), is_Tree_DB());
    }

    public void T1_Duplicate_OVertex(ActionEvent actionEvent) {
        orientdbJavafx.duplicateOVertex(oVerticesTree);
    }

    public void T1_Delete_OVertex(ActionEvent actionEvent) {
        orientdbJavafx.deleteOVertex(oVerticesTree);
    }

    public void T1_Write_Properties_Data_To_OVertex(ActionEvent actionEvent) {
        orientdbJavafx.writeOPropertiesDataToOVertex(oVerticesTree, titledPanesHbox);
    }

    public void T1_Read_Properties_Data_From_OVertex(ActionEvent actionEvent) {
        orientdbJavafx.readOPropertiesDataFromOVertex(oVerticesTree, titledPanesHbox, false);
    }

    //-------------------------------------------------------------------------
    //----------------------------Graph DB - OClass----------------------------

    public void T2_Write_Properties_To_Class(ActionEvent actionEvent) {
        orientdbJavafx.writePropertiesToOClass(oClassesTree, titledPanesHbox);
    }

    public void T2_Read_Properties_From_Class(ActionEvent actionEvent) {
        orientdbJavafx.readPropertiesFromOClass(oClassesTree, titledPanesHbox, false);
    }

    public void T2_Add_OClass(ActionEvent actionEvent) {
        orientdbJavafx.addOClass(oClassesTree, T2_RecordName);
    }

    public void T2_Duplicate_OClass(ActionEvent actionEvent) {
        orientdbJavafx.duplicateOClass(oClassesTree);
    }

    public void T2_Delete_OClass(ActionEvent actionEvent) {
        orientdbJavafx.deleteOClass(oClassesTree);
    }

    /*public void T2_AddProperty(ActionEvent actionEvent) {
        orientdbJavafx.addOProperty(oPropertiesTable, T2_New_PropertyName_TextField, T2_New_Property_DataType_Combobox);
    }

    public void T2_DeleteProperty(ActionEvent actionEvent) {
        orientdbJavafx.deleteOProperty(oPropertiesTable);
    }*/
}
