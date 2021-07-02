package org.yoki_manager.orientdb.controls.titledpanes;


import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import org.yoki_manager.orientdb.controls.JavafxEvent;
import org.yoki_manager.orientdb.controls.odocument.ODocumentVBox;

/**
 * TitledPanesHbox -> TitledPane -> VBox (for OVertices) -> Vbox (for specific OVertex)
 */
public class TitledEntitiesPanes extends SplitPane {
    public int activePaneIndex;

    public TitledEntitiesPanes() {
        super();
        activePaneIndex = 0;
        this.setStyle("-fx-padding: 0;");
    }

    public void setActivePaneIndex(int value)
    {
        activePaneIndex = value;
    }

    public int getActivePaneIndex()
    {
        return activePaneIndex;
    }

    public ScrollableTitledEntityPane getActivePane()
    {
        return (ScrollableTitledEntityPane)(this.getItems().get(activePaneIndex));
    }

    private static int top = 12, right = 12, bottom = 12, left = 12;
    private int titledPaneMinWidth = 100;

    public ScrollableTitledEntityPane addActivePane()
    {
        ObservableList<Node> titledPanesChildren = this.getItems();
        ScrollableTitledEntityPane stPane = new ScrollableTitledEntityPane();
        stPane.addEventHandler(JavafxEvent.JAVAFX_EVENT_TYPE, new TitlesPanesEventHandler() {
            @Override
            public void onClosePaneEvent(int param0) {
                titledPanesChildren.remove(stPane);
            }

            @Override
            public void onEvent2(String param0) {
                //System.out.println("string parameter: "+param0);
            }
        });

        titledPanesChildren.add(stPane); //titledPane
        setActivePaneIndex(titledPanesChildren.size() - 1);

        stPane.setEntitiesVBoxPadding(top, right, bottom, left);
        return stPane;
    }

    public void nameActivePane(String name)
    {
        getActivePane().setCaption(name);
    }

    public ScrollableTitledEntityPane addNewEntityVBox(ODocumentVBox newEntityVBox, String newEntityName, boolean inNewTitledPane)
    {
        ObservableList<javafx.scene.Node> titledPanesChildren = getItems();
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

        nameActivePane(newEntityName);
        return scrollableTitledEntityPane;
    }
}
