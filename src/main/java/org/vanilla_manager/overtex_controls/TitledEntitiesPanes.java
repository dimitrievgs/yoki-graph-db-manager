package org.vanilla_manager.overtex_controls;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

/**
 * TitledPanesHbox -> TitledPane -> VBox (for OVertices) -> Vbox (for specific OVertex)
 */
public class TitledEntitiesPanes extends SplitPane { //Hbox
    public int activePaneIndex;

    public TitledEntitiesPanes() {
        super();
        activePaneIndex = 0;
        this.setStyle("-fx-padding: 0;");
        //this.setMinHeight(600);
        //this.setMaxHeight(600);
    }

    public void setActivePaneIndex(int value)
    {
        activePaneIndex = value;
    }

    public int getActivePaneIndex()
    {
        return activePaneIndex;
    }

    /**
     * return commonVBox
     * @return
     */
    /*public VBox getActivePaneVBox()
    {
        return (VBox) (((ScrollPane)(getActivePane().getContent())).getContent());
    }

    private TitledPane getActivePane()
    {
        return (TitledPane)(((VBox)this.getItems().get(activePaneIndex)).getChildren().get(0));
    }*/

    public ScrollableTitledEntityPane getActivePane()
    {
        return (ScrollableTitledEntityPane)(this.getItems().get(activePaneIndex));
        //return (TitledPane)(((VBox)this.getItems().get(activePaneIndex)).getChildren().get(0));
    }

    private static int top = 12, right = 12, bottom = 12, left = 12;
    private int titledPaneMinWidth = 100;

    public ScrollableTitledEntityPane addActivePane()
    {
        //top_vbox -> titledPane -> scrollPane -> entitiesVBoxCollection
        /*VBox entitiesVBoxCollection = new VBox();
        ScrollPane scrollPane = new ScrollPane(entitiesVBoxCollection);
        //increase scrollpane speed: https://stackoverflow.com/questions/56739913/how-to-increase-scrolling-speed-of-scrollpane-javafx
        TitledPane titledPane = new TitledPane();
        titledPane.setContent(scrollPane);
        titledPane.setCollapsible(false);
        VBox top_vbox = new VBox(titledPane); //titledPane itself just goes to center, not to top, so we use vbox*/
        //top_vbox.setMinWidth(titledPaneMinWidth);

        ScrollableTitledEntityPane stPane = new ScrollableTitledEntityPane();
        
        ObservableList<Node> titledPanesChildren = this.getItems(); //getChildren();
        titledPanesChildren.add(stPane); //titledPane
        setActivePaneIndex(titledPanesChildren.size() - 1);

        stPane.setEntitiesVBoxPadding(top, right, bottom, left);
        //entitiesVBoxCollection.setPadding(new Insets(top, right, bottom, left));
        return stPane; //entitiesVBoxCollection;
    }

    public void nameActivePane(String name)
    {
        getActivePane().setCaption(name);
    }

    public ScrollableTitledEntityPane addNewEntityVBox(EntityVBox newEntityVBox, String newEntityName, boolean inNewTitledPane)
    {
        ObservableList<javafx.scene.Node> titledPanesChildren = getItems();//getChildren();
        ScrollableTitledEntityPane scrollableTitledEntityPane;
        if (inNewTitledPane == false)
        {
            if (titledPanesChildren.size() == 0)
            {
                scrollableTitledEntityPane = addActivePane();
            }
            else
            {
                scrollableTitledEntityPane = getActivePane();
            }
        }
        else
        {
            scrollableTitledEntityPane = addActivePane();
        }

        scrollableTitledEntityPane.getEntitiesVBoxCollection().clear();
        scrollableTitledEntityPane.getEntitiesVBoxCollection().add(newEntityVBox);

        /*var s1 = scrollableTitledPane.getParent();
        var s2 = s1.getParent();
        var s3 = s2.getParent();
        var s4 = s3.getParent();


        newEntityVBox.setTopParentTitledContainer((VBox)(scrollableTitledPane.getParent().getParent().getParent()));*/
        nameActivePane(newEntityName);
        return scrollableTitledEntityPane;
    }

    /*public OClassVBox getOClassVBoxes(OClass oClass)
    {
        ObservableList<Node> titledPanesHboxChildren = this.getChildren();
        for (var t : titledPanesHboxChildren)
        {

        }
    }*/
}
