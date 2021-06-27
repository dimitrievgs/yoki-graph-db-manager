package org.vanilla_manager.orientdb.controls.titledpanes;

import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.vanilla_manager.extra_controls.SLabel;
import org.vanilla_manager.extra_controls.SVGButton;
import org.vanilla_manager.general.Style;

public class TitleBox extends BorderPane {
    private final SLabel label;
    private SVGButton closeButton;

    private int textPadding = 5;
    private int leftVBoxWidth = 40;
    private int textTop = 4, textRight = 7, textBottom = 4, textPLeft = 12;
    private int btnHeight = 32;

    public TitleBox() {
        label = new SLabel();
        this.setPadding(new Insets(textTop, textRight, textBottom, textPLeft));
        label.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;"); //
        double textHeight = label.calcHeight();
        this.setStyle("-fx-background-color:" + Style.TitleBoxColor + ";");
        setLeft(label);

        HBox buttonsHBox = new HBox();
        String settingsSVG = "icons/GUI/close.svg";
        closeButton = new SVGButton(settingsSVG, textHeight, SVGButton.ScaleOn.Height,
                Style.ColorBtnOff, Style.ColorBtnHover);
        //https://stackoverflow.com/questions/46649406/custom-javafx-events
        closeButton.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            closeButton.fireEvent(new ClosePaneEvent(42));
        });
        buttonsHBox.getChildren().add(closeButton);
        setRight(buttonsHBox);
    }

    public void setCaption(String caption) {
        label.setText(caption);
    }
}


