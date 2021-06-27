package org.vanilla_manager.extra_controls;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class SLabel extends Label {
    /**
     * Hook. https://stackoverflow.com/questions/30983584/how-to-get-the-size-of-a-label-before-it-is-laid-out
     * External methods on this label like getChildren().add(...) and setLeft() must be called after this method.
     * @return
     */
    public double calcHeight()
    {
        Group root = new Group();
        Label label = this;
        root.getChildren().add(label);
        Scene scene = new Scene(root);
        root.applyCss();
        root.layout();
        return label.getHeight();
    }
}
