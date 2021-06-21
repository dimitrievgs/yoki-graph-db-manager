package org.vanilla_manager.overtex_controls;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * TitledPanesHbox -> TitledPane -> VBox (for OVertices) -> Vbox (for specific OVertex)
 */
public class TitledPanesHbox extends HBox {
    public int activePaneIndex;

    public TitledPanesHbox() {
        super();
        activePaneIndex = 0;
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
    public VBox getActivePaneVBox()
    {
        return (VBox) (((ScrollPane)(getActivePane().getContent())).getContent());
    }

    private TitledPane getActivePane()
    {
        return (TitledPane)this.getChildren().get(activePaneIndex);
    }

    private static int top = 12, right = 12, bottom = 12, left = 12;

    public VBox addActivePaneVBox()
    {
        TitledPane titledPane = new TitledPane();
        ObservableList<Node> titledPanesHboxChildren = this.getChildren();
        titledPanesHboxChildren.add(titledPane);
        setActivePaneIndex(titledPanesHboxChildren.size() - 1);

        VBox commonVBox = new VBox();
        ScrollPane scrollPane = new ScrollPane(commonVBox);
        //increase scrollpane speed: https://stackoverflow.com/questions/56739913/how-to-increase-scrolling-speed-of-scrollpane-javafx
        //scrollPane.set
        titledPane.setContent(scrollPane);

        commonVBox.setPadding(new Insets(top, right, bottom, left));
        return commonVBox;
    }

    public void nameActivePane(String name)
    {
        getActivePane().setText(name);
    }
}
