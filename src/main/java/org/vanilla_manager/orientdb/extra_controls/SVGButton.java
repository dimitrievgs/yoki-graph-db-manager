package org.vanilla_manager.orientdb.extra_controls;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.shape.SVGPath;
import org.vanilla_manager.ResourcesManager;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class SVGButton extends Button {
    SVGPath svgPathOff;
    SVGPath svgPathOn;
    String fillColorOff;
    String fillColorOn;
    String hoverFillColor;
    boolean switching;
    boolean value;

    private static String svgPathBaseStyle = ";-fx-background-color: transparent;-fx-border-style: none;";
    private static String buttonBaseStyle = "-fx-background-color: transparent;-fx-border-width: 0;";

    //https://stackoverflow.com/questions/40753613/javafx-button-with-svg
    /**
     * For non-switching button.
     * @param _svgFilePath
     * @param _Width
     * @param _Height
     * @param _borderWidth
     * @param _fillColor
     * @param _hoverFillColor
     */
    public SVGButton(String _svgFilePath, double _Width, double _Height,
                     double _borderWidth, String _fillColor, String _hoverFillColor) {
        setSwitching(false);
        setValue(false);
        SVGPath svgPath = getSVGPath(_svgFilePath);
        setSvgPathOff(svgPath);
        setFillColorOff(_fillColor);
        setHoverFillColor(_hoverFillColor);

        Bounds bounds = svgPath.getBoundsInLocal();
        double s1 = (_Width - 2 * _borderWidth) / bounds.getWidth();
        double s2 = (_Height - 2 * _borderWidth) / bounds.getHeight();
        double scaleFactor = Math.min(s1, s2);
        scalePath(svgPath, scaleFactor);

        create(svgPath, _Width, _Height, _hoverFillColor);
    }

    /**
     * For non-switching button.
     * @param _svgFilePath
     * @param _Height
     * @param _fillColor
     * @param _hoverFillColor
     */
    public SVGButton(String _svgFilePath, double _Height, String _fillColor,
                     String _hoverFillColor) {
        setSwitching(false);
        setValue(false);
        SVGPath svgPath = getSVGPath(_svgFilePath);
        setSvgPathOff(svgPath);
        setFillColorOff(_fillColor);
        setHoverFillColor(_hoverFillColor);

        Bounds bounds = svgPath.getBoundsInLocal();
        double s2 = _Height / bounds.getHeight();
        double Width = s2 * bounds.getWidth();
        double scaleFactor = s2; //Math.min(s1, s2);
        scalePath(svgPath, scaleFactor);

        create(svgPath, Width, _Height, _hoverFillColor);
    }

    /**
     * For switching button.
     * @param svgFilePathOff
     * @param Height
     * @param _fillColorOff
     * @param _fillColorOn
     * @param _hoverFillColor
     */
    public SVGButton(String svgFilePathOff, double Height,
                     String _fillColorOff, String _fillColorOn, String _hoverFillColor, boolean rotateForOnSVGPath) {
        setSwitching(true);
        setValue(false);
        SVGPath svgPathOff = getSVGPath(svgFilePathOff);
        SVGPath svgPathOn = getSVGPath(svgFilePathOff); //must be some clone() here!
        if (rotateForOnSVGPath)
        {
            svgPathOn.setRotate(180);
        }
        setSvgPathOff(svgPathOff);
        setSvgPathOn(svgPathOn);
        setFillColorOff(_fillColorOff);
        setFillColorOn(_fillColorOn);
        setHoverFillColor(_hoverFillColor);

        double maxWidth = 0;
        for (SVGPath svgPath : new SVGPath[] {svgPathOff, svgPathOn}) {
            Bounds bounds = svgPath.getBoundsInLocal();
            double s2 = Height / bounds.getHeight();
            double Width = s2 * bounds.getWidth();
            maxWidth = Math.max(maxWidth, Width); //???
            double scaleFactor = s2; //Math.min(s1, s2);
            scalePath(svgPath, scaleFactor);
        }

        create(svgPathOff, maxWidth, Height, hoverFillColor);
        addClickEvent();
    }

    /**
     * For switching button.
     * @param svgFilePathOff
     * @param svgFilePathOn
     * @param Height
     * @param _fillColorOff
     * @param _fillColorOn
     * @param _hoverFillColor
     */
    public SVGButton(String svgFilePathOff, String svgFilePathOn, double Height,
                     String _fillColorOff, String _fillColorOn, String _hoverFillColor) {
        setSwitching(true);
        setValue(false);
        setSvgPathOff(getSVGPath(svgFilePathOff));
        setSvgPathOn(getSVGPath(svgFilePathOn));
        setFillColorOff(_fillColorOff);
        setFillColorOn(_fillColorOn);
        setHoverFillColor(_hoverFillColor);

        double maxWidth = 0;
        for (SVGPath svgPath : new SVGPath[] {svgPathOff, svgPathOn}) {
            Bounds bounds = svgPath.getBoundsInLocal();
            double s2 = Height / bounds.getHeight();
            double Width = s2 * bounds.getWidth();
            maxWidth = Math.max(maxWidth, Width); //???
            double scaleFactor = s2; //Math.min(s1, s2);
            scalePath(svgPath, scaleFactor);
        }

        create(svgPathOff, maxWidth, Height, hoverFillColor);
        addClickEvent();
    }

    private void create(SVGPath svgPath, double Width, double Height, String hoverFillColor)
    {
        this.setPickOnBounds(true); // make sure transparent parts of the button register clicks too
        applySVGPath(getSvgPath(), getFillColor());
        this.setGraphic(svgPath);
        this.setAlignment(Pos.CENTER);
        //button.getStyleClass().add("icon-button");

        //if not to set min and max sizes both then it will behave in strange way
        this.setMaxWidth(Width);
        this.setMinWidth(Width);
        this.setMaxHeight(Height);
        this.setMinHeight(Height);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.setBorder(Border.EMPTY);
        this.setStyle(buttonBaseStyle); //-fx-background-color: black;

        this.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue == false) {
                applySVGPath(getSvgPath(), getFillColor());
            } else {
                applySVGPath(getSvgPath(), hoverFillColor);
            }
        });
    }

    private void addClickEvent()
    {
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                setValue(!getValue());
                applySVGPath(getSvgPath(), getFillColor());
                //event.consume();
            }
        });
    }

    private void applySVGPath(SVGPath svgPath, String fillColor)
    {
        svgPath.setStyle("-fx-fill:" + fillColor + ";" + svgPathBaseStyle);
        setGraphic(svgPath);
    }

    private SVGPath getSvgPath()
    {
        return getValue() ? getSvgPathOn() : getSvgPathOff();
    }

    private String getFillColor()
    {
        return getValue() ? getFillColorOn() : getFillColorOff();
    }

    /*********svg************/

    private SVGPath getSVGPath(String svgFilePath)
    {
        String dValue = getDValueFromSVG(svgFilePath);
        SVGPath path = new SVGPath();
        path.setContent(dValue);
        path.getStyleClass().add("button-icon");
        //path.setStyle("-fx-fill:" + fillColor + ";"); //hover-fill doesn't work for some reason
        return path;
    }

    private void scalePath(SVGPath path, double scaleFactor)
    {
        path.setScaleX(scaleFactor);
        path.setScaleY(scaleFactor);
    }

    /**
     * It has to work with Inkscape files (this needs to check)
     *
     * @param svgFilePath
     * @return
     */
    private static String getDValueFromSVG(String svgFilePath) {
        String dValue = "";
        try {
            File file = new File(ResourcesManager.getResource(svgFilePath).toURI());
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = null;
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            NodeList pathNode = document.getDocumentElement().getElementsByTagName("path");
            dValue = pathNode.item(0).getAttributes().getNamedItem("d").getNodeValue();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException | URISyntaxException e) {
            e.printStackTrace();
        }
        return dValue;
    }

    /*******properties*******/

    public SVGPath getSvgPathOff() {
        return svgPathOff;
    }

    public void setSvgPathOff(SVGPath svgPathOff) {
        this.svgPathOff = svgPathOff;
    }

    public SVGPath getSvgPathOn() {
        return svgPathOn;
    }

    public void setSvgPathOn(SVGPath svgPathOn) {
        this.svgPathOn = svgPathOn;
    }

    public String getFillColorOff() {
        return fillColorOff;
    }

    public void setFillColorOff(String fillColorOff) {
        this.fillColorOff = fillColorOff;
    }

    public String getFillColorOn() {
        return fillColorOn;
    }

    public void setFillColorOn(String fillColorOn) {
        this.fillColorOn = fillColorOn;
    }

    public String getHoverFillColor() {
        return hoverFillColor;
    }

    public void setHoverFillColor(String hoverFillColor) {
        this.hoverFillColor = hoverFillColor;
    }

    public boolean getSwitching() {
        return switching;
    }

    public void setSwitching(boolean switching) {
        this.switching = switching;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
