package org.vanilla_manager.orientdb.controls;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class ScrollableTitledEntityPane extends VBox {
    private VBox entitiesVBox;
    private ScrollPane scrollPane;
    private TitledPane titledPane;

    public ScrollableTitledEntityPane()
    {
        super();
        entitiesVBox = new VBox();
        scrollPane = new ScrollPane(entitiesVBox);
        scrollPane.setFitToWidth(true); //to let inner controls keep its size //https://stackoverflow.com/questions/17568688/how-to-resize-javafx-scrollpane-content-to-fit-current-size
        //increase scrollpane speed: https://stackoverflow.com/questions/56739913/how-to-increase-scrolling-speed-of-scrollpane-javafx
        titledPane = new TitledPane();
        titledPane.setContent(scrollPane);
        titledPane.setCollapsible(false);
        this.getChildren().add(titledPane);
    }

    public VBox getEntitiesVBox() {
        return entitiesVBox;
    }

    public ObservableList<Node> getEntitiesVBoxCollection()
    {
        return entitiesVBox.getChildren();
    }

    private int top, right, bottom, left;

    public void setEntitiesVBoxPadding(int _top, int _right, int _bottom, int _left)
    {
        entitiesVBox.setPadding(new Insets(_top, _right, _bottom, _left));
        top = _top;
        right = _right;
        bottom = _bottom;
        left = _left;
    }

    public void setCaption(String caption)
    {
        titledPane.setText(caption);
    }

    public DoubleBinding entityInnerWidthProperty()
    {
        VBox topVBox = this;
        DoubleBinding doubleBinding = new DoubleBinding() {
            {
                super.bind(topVBox.widthProperty());
            }

            @Override
            protected double computeValue() {
                //MessageBox.Show(topVBox.widthProperty().getValue() +" " +  titledPane.widthProperty().getValue() +" "
                 //       +  scrollPane.widthProperty().getValue() +" " +  entitiesVBox.widthProperty().getValue() +" " +  left + " " + right);
                return topVBox.widthProperty().getValue() - left - right - 2; //OVertexTabVBox.widthProperty().getValue()
                        /*- OVertexTabVBox.getSpacing()*/ /*- OVertexTabVBox.getPadding().getLeft()*/ //- 16; //where is this shift from??
            }
        };
        return doubleBinding;
        //titledPanesHbox.prefWidthProperty().bind(foo);

        //TA.prefWidthProperty().bind(scrollableTitledEntityPane.widthProperty());
    }
}
