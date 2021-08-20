package org.yoki_manager;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.yoki_manager.extra_controls.SVGButton;
import org.yoki_manager.general.Style;
import org.yoki_manager.orientdb.OrientdbJavafx;
import org.yoki_manager.orientdb.controls.titledpanes.TitledEntitiesPanes;
import org.yoki_manager.orientdb.controls.oproperty.OPropertyNode;

import java.io.IOException;

public class PrimaryController {
    public HBox T1_RecordName_parent;
    public Button btnT1LoadDB;
    public TextField T2_RecordName;
    public HBox T2_RecordName_parent;
    public TableView<OPropertyNode> oPropertiesTable;
    public Tab r2;
    public VBox leftVBox;
    public VBox OClassesTabVBox;
    public VBox rightVBox;
    public ToolBar toolbarBottom;
    @FXML
    private TabPane controlTabPane;
    @FXML
    private VBox OVertexTabVBox;
    @FXML
    private Pane centerPane;
    @FXML
    private SplitPane centerSplitPane;
    @FXML
    private TitledEntitiesPanes titledEntitiesPanes;
    @FXML
    private MenuBar menubar1;
    @FXML
    private ToggleButton btnT1LoadDBToggle;
    @FXML
    private TextField T1_RecordName;
    @FXML
    private TreeTableView oVerticesTree;
    @FXML
    private TreeTableView oClassesTree;

    Stage stage;
    OrientdbJavafx orientdbJavafx;

    //-------------------------------------------------------------------------
    //--------------------------Init & GUI behavior----------------------------

    @FXML
    public void initialize() {
        bindCSSClasses();
        addMenu();
        setTextInControls();
        addLeftVBoxButtons();
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
        controlTabPane.getStyleClass().add("tabpane1");
        centerSplitPane.getStyleClass().add("vbox0");
        oVerticesTree.getStyleClass().add("treetableview1");
        OVertexTabVBox.getStyleClass().add("T1_vbox1");
        leftVBox.getStyleClass().add("leftVBox");
        rightVBox.getStyleClass().add("rightVBox");
        menubar1.getStyleClass().add("menubar1");
        toolbarBottom.getStyleClass().add("toolbarBottom");
        titledEntitiesPanes.getStyleClass().add("titledEntitiesPanes");
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

    private int leftVBoxWidth = 40;
    private int leftVBoxPTop = 12, leftVBoxPRight = 5, leftVBoxPBottom = 12, leftVBoxPLeft = 5;

    private void addLeftVBoxButtons() {
        leftVBox.setPrefWidth(leftVBoxWidth);
        leftVBox.setPadding(new Insets(leftVBoxPTop, leftVBoxPRight, leftVBoxPBottom, leftVBoxPLeft));
        leftVBox.setSpacing(10);
        int btnWidth = leftVBoxWidth - leftVBoxPLeft - leftVBoxPRight;

        String expandCollapseSVG = "icons/GUI/expand_collapse_treetableviews.svg";
        SVGButton btnExpandCollapseTabPane = new SVGButton(expandCollapseSVG, btnWidth, SVGButton.ScaleOn.Width,
                Style.ColorBtnOff, Style.ColorBtnOn, Style.ColorBtnHover, true);
        btnExpandCollapseTabPane.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (btnExpandCollapseTabPane.getValue())
                {
                    centerSplitPane.setDividerPositions(0.0);
                }
                else
                {
                    centerSplitPane.setDividerPositions(startPosForCenterSplitPane);
                }
            }
        });

        String settingsSVG = "icons/GUI/settings.svg";
        SVGButton btnSettings = new SVGButton(settingsSVG, btnWidth, SVGButton.ScaleOn.Width,
                Style.ColorBtnOff, Style.ColorBtnHover);
        btnSettings.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
        });

        leftVBox.getChildren().addAll(btnExpandCollapseTabPane, btnSettings);
    }

    //int treetableview_width = 300;
    double startPosForCenterSplitPane = 0.2;

    private void setSizesLocations() {
        centerSplitPane.prefWidthProperty().bind(centerPane.widthProperty());
        centerSplitPane.prefHeightProperty().bind(centerPane.heightProperty());
        T1_RecordName.prefWidthProperty().bind(OVertexTabVBox.widthProperty()); //doesn't work with prefHeightProperty
        T2_RecordName.prefWidthProperty().bind(OClassesTabVBox.widthProperty()); //doesn't work with prefHeightProperty
        oVerticesTree.prefWidthProperty().bind(OVertexTabVBox.widthProperty());
        oClassesTree.prefWidthProperty().bind(OClassesTabVBox.widthProperty());

        DoubleBinding oVerticesTreeHB = new DoubleBinding() {
            {
                super.bind(OVertexTabVBox.heightProperty());
            }

            @Override
            protected double computeValue() {
                return OVertexTabVBox.heightProperty().getValue() - oVerticesTree.layoutYProperty().getValue();
            }
        };
        oVerticesTree.prefHeightProperty().bind(oVerticesTreeHB);

        DoubleBinding oClassesTreeHB = new DoubleBinding() {
            {
                super.bind(OClassesTabVBox.heightProperty());
            }

            @Override
            protected double computeValue() {
                return OClassesTabVBox.heightProperty().getValue() - oClassesTree.layoutYProperty().getValue();
            }
        };
        oClassesTree.prefHeightProperty().bind(oClassesTreeHB);

        stage.showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    centerSplitPane.setDividerPositions(startPosForCenterSplitPane);
                    observable.removeListener(this);
                }
            }
        });
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
                orientdbJavafx.readOPropertiesDataFromOVertex(oVerticesTree, titledEntitiesPanes, event.isControlDown());
                event.consume();
            }
        });

        //Prevent expand of child nodes after mouse double-click
        oClassesTree.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            if (event.getClickCount() % 2 == 0 && event.isPrimaryButtonDown()) {
                orientdbJavafx.readPropertiesFromOClass(oClassesTree, titledEntitiesPanes, event.isControlDown());
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
            btnT1LoadDB.setText(Base_name);
        });

        btnT1LoadDBToggle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String Text = (btnT1LoadDBToggle.isSelected()) ? "Tree" : "Plain (all)";
                btnT1LoadDBToggle.setText(Text);
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
        return btnT1LoadDBToggle.isSelected();
    }

    public void T1LoadDB(ActionEvent actionEvent) {
        if (is_Tree_DB())
            orientdbJavafx.loadAndShowOVerticesChildsTree(oVerticesTree, T1_RecordName.getText());
        else orientdbJavafx.loadAndShowOVerticesList(oVerticesTree, T1_RecordName.getText());
    }

    public void T2ReadDB(ActionEvent actionEvent) {
        orientdbJavafx.loadAndShowOClassesTree(oClassesTree, true, true);
    }

    //-------------------------------------------------------------------------
    //----------------------------Graph DB - OVertex---------------------------

    @FXML
    private void T1AddOVertex() throws IOException {
        orientdbJavafx.addOVertex(oVerticesTree, oPropertiesTable, T1_RecordName, T1_RecordName.getText(), is_Tree_DB());
    }

    public void T1DuplicateRecord(ActionEvent actionEvent) {
        orientdbJavafx.duplicateOVertex(oVerticesTree);
    }

    public void T1DeleteOVertex(ActionEvent actionEvent) {
        orientdbJavafx.deleteOVertex(oVerticesTree);
    }

    public void T1SaveOPropertiesToOVertices(ActionEvent actionEvent) {
        orientdbJavafx.saveOPropertiesDataToOVertex(oVerticesTree, titledEntitiesPanes);
    }

    public void T1ReadOPropertiesFromOVertices(ActionEvent actionEvent) {
        orientdbJavafx.readOPropertiesDataFromOVertex(oVerticesTree, titledEntitiesPanes, false);
    }

    //-------------------------------------------------------------------------
    //----------------------------Graph DB - OClass----------------------------

    public void T2SaveOPropertiesToOClass(ActionEvent actionEvent) {
        int t = 1;
        //orientdbJavafx.writePropertiesToOClass(oClassesTree, titledPanesHbox);
    }

    public void T2ReadOPropertiesFromOClass(ActionEvent actionEvent) {
        orientdbJavafx.readPropertiesFromOClass(oClassesTree, titledEntitiesPanes, false);
    }

    public void T2AddOClass(ActionEvent actionEvent) {
        orientdbJavafx.addOClass(oClassesTree, T2_RecordName);
    }

    public void T2DuplicateOClass(ActionEvent actionEvent) {
        orientdbJavafx.duplicateOClass(oClassesTree);
    }

    public void T2DeleteOClass(ActionEvent actionEvent) {
        orientdbJavafx.deleteOClass(oClassesTree);
    }

    /*public void T2_AddProperty(ActionEvent actionEvent) {
        orientdbJavafx.addOProperty(oPropertiesTable, T2_New_PropertyName_TextField, T2_New_Property_DataType_Combobox);
    }

    public void T2_DeleteProperty(ActionEvent actionEvent) {
        orientdbJavafx.deleteOProperty(oPropertiesTable);
    }*/
}
