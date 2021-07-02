package org.yoki_manager.orientdb.controls.titledpanes;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ScrollableTitledEntityPane extends VBox {
    private VBox lvl3EntitiesVBox;
    private ScrollPane lvl2ScrollPane;
    private VBox lvl1TitledPane;
    private TitleBox titleBox;

    private static String svgPathBaseStyle = ";-fx-background-color: transparent;-fx-border-style: none;";
    private static String buttonBaseStyle = "-fx-background-color: transparent;-fx-border-width: 0;";

    public ScrollableTitledEntityPane()
    {
        super();
        lvl3EntitiesVBox = new VBox();
        lvl2ScrollPane = new ScrollPane(lvl3EntitiesVBox);
        lvl2ScrollPane.setFitToWidth(true); //to let inner controls keep its size //https://stackoverflow.com/questions/17568688/how-to-resize-javafx-scrollpane-content-to-fit-current-size
        //increase scrollpane speed: https://stackoverflow.com/questions/56739913/how-to-increase-scrolling-speed-of-scrollpane-javafx
        lvl1TitledPane = new VBox();
        titleBox = new TitleBox();
        lvl1TitledPane.getChildren().addAll(titleBox, lvl2ScrollPane);
        lvl2ScrollPane.setStyle(svgPathBaseStyle);
        this.getChildren().add(lvl1TitledPane);
    }

    public VBox getLvl3EntitiesVBox() {
        return lvl3EntitiesVBox;
    }

    public ObservableList<Node> getEntitiesVBoxCollection()
    {
        return lvl3EntitiesVBox.getChildren();
    }

    private int top, right, bottom, left;

    public void setEntitiesVBoxPadding(int _top, int _right, int _bottom, int _left)
    {
        lvl3EntitiesVBox.setPadding(new Insets(_top, _right, _bottom, _left));
        top = _top;
        right = _right;
        bottom = _bottom;
        left = _left;
    }

    public void setCaption(String caption)
    {
        titleBox.setCaption(caption);
    }
}
