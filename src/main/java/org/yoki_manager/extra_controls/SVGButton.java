package org.yoki_manager.extra_controls;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.shape.SVGPath;
import org.yoki_manager.ResourcesManager;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.yoki_manager.dialogs.MessageBox;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    //-------------------------------------------------------------------------
    //------------------------------Constructors-------------------------------

    //https://stackoverflow.com/questions/40753613/javafx-button-with-svg

    /**
     * For non-switching button.
     *
     * @param _svgFilePath
     * @param _Width
     * @param _Height
     * @param _borderWidth
     * @param _fillColor
     * @param _hoverFillColor
     */
    public SVGButton(String _svgFilePath, double _Width, double _Height, double _borderWidth,
                     String _fillColor, String _hoverFillColor) {
        setSwitching(false);
        setValue(false);
        SVGPath svgPath = getSVGPath(_svgFilePath);
        setSvgPathOff(svgPath);
        setFillColorOff(_fillColor);
        setHoverFillColor(_hoverFillColor);

        SVGScaleAndDimensions sd = getScaleAndDimensions(svgPath, _Width, _Height, _borderWidth, ScaleOn.Both);
        scalePath(svgPath, sd.scaleFactor);

        create(svgPath, sd.Width, sd.Height, _hoverFillColor);
    }

    /**
     * For non-switching button.
     *
     * @param _svgFilePath
     * @param linearDimension
     * @param _fillColor
     * @param _hoverFillColor
     */
    public SVGButton(String _svgFilePath, double linearDimension, ScaleOn scaleOn,
                     String _fillColor, String _hoverFillColor) {
        setSwitching(false);
        setValue(false);
        SVGPath svgPath = getSVGPath(_svgFilePath);
        setSvgPathOff(svgPath);
        setFillColorOff(_fillColor);
        setHoverFillColor(_hoverFillColor);

        SVGScaleAndDimensions sd = getScaleAndDimensions(svgPath, linearDimension, 0, scaleOn);
        scalePath(svgPath, sd.scaleFactor);

        create(svgPath, sd.Width, sd.Height, _hoverFillColor);
    }

    /**
     * For switching button.
     *
     * @param svgFilePathOff
     * @param linearDimension
     * @param _fillColorOff
     * @param _fillColorOn
     * @param _hoverFillColor
     */
    public SVGButton(String svgFilePathOff, double linearDimension, ScaleOn scaleOn,
                     String _fillColorOff, String _fillColorOn, String _hoverFillColor,
                     boolean rotateForOnSVGPath) {
        setSwitching(true);
        setValue(false);
        SVGPath svgPathOff = getSVGPath(svgFilePathOff);
        SVGPath svgPathOn = getSVGPath(svgFilePathOff); //must be some clone() here!
        if (rotateForOnSVGPath) {
            svgPathOn.setRotate(180);
        }
        setSvgPathOff(svgPathOff);
        setSvgPathOn(svgPathOn);
        setFillColorOff(_fillColorOff);
        setFillColorOn(_fillColorOn);
        setHoverFillColor(_hoverFillColor);

        double maxWidth = 0, maxHeight = 0;
        SVGScaleAndDimensions sd = new SVGScaleAndDimensions();
        for (SVGPath svgPath : new SVGPath[]{svgPathOff, svgPathOn}) {
            sd = getScaleAndDimensions(svgPath, linearDimension, 0, scaleOn);
            maxWidth = Math.max(maxWidth, sd.Width);
            maxHeight = Math.max(maxHeight, sd.Height);
            scalePath(svgPath, sd.scaleFactor);
        }

        create(svgPathOff, maxWidth, maxHeight, hoverFillColor);
        addClickEvent();
    }

    /**
     * For switching button.
     *
     * @param svgFilePathOff
     * @param svgFilePathOn
     * @param linearDimension
     * @param _fillColorOff
     * @param _fillColorOn
     * @param _hoverFillColor
     */
    public SVGButton(String svgFilePathOff, String svgFilePathOn, double linearDimension, ScaleOn scaleOn,
                     String _fillColorOff, String _fillColorOn, String _hoverFillColor) {
        setSwitching(true);
        setValue(false);
        setSvgPathOff(getSVGPath(svgFilePathOff));
        setSvgPathOn(getSVGPath(svgFilePathOn));
        setFillColorOff(_fillColorOff);
        setFillColorOn(_fillColorOn);
        setHoverFillColor(_hoverFillColor);

        double maxWidth = 0, maxHeight = 0;
        SVGScaleAndDimensions sd = new SVGScaleAndDimensions();
        for (SVGPath svgPath : new SVGPath[]{svgPathOff, svgPathOn}) {
            sd = getScaleAndDimensions(svgPath, linearDimension, 0, scaleOn);
            maxWidth = Math.max(maxWidth, sd.Width);
            maxHeight = Math.max(maxHeight, sd.Height);
            scalePath(svgPath, sd.scaleFactor);
        }

        create(svgPathOff, maxWidth, maxHeight, hoverFillColor);
        addClickEvent();
    }

    //-------------------------------------------------------------------------
    //---------------------------Scale and Dimensions--------------------------

    private SVGScaleAndDimensions getScaleAndDimensions(SVGPath svgPath, double linearDimension, double borderWidth, ScaleOn scaleOn) {
        double width = 0;
        double height = 0;
        if (scaleOn == ScaleOn.Height)
            height = linearDimension;
        else width = linearDimension;
        return getScaleAndDimensions(svgPath, width, height, borderWidth, scaleOn);
    }

    private SVGScaleAndDimensions getScaleAndDimensions(SVGPath svgPath, double width, double height, double borderWidth, ScaleOn scaleOn) {
        double s1, s2, scaleFactor = 0;
        Bounds bounds = svgPath.getBoundsInLocal();
        switch (scaleOn) {
            case Both: //+
                s1 = (width - 2 * borderWidth) / bounds.getWidth();
                s2 = (height - 2 * borderWidth) / bounds.getHeight();
                scaleFactor = Math.min(s1, s2);
                break;
            case Width:
                s1 = (width - 2 * borderWidth) / bounds.getWidth();
                height = s1 * bounds.getHeight() + 2 * borderWidth;
                scaleFactor = s1;
                break;
            case Height: //+
                s2 = (height - 2 * borderWidth) / bounds.getHeight();
                width = s2 * bounds.getWidth() + 2 * borderWidth;
                scaleFactor = s2;
                break;
            default:
                break;
        }
        return new SVGScaleAndDimensions(width, height, scaleFactor);
    }

    public enum ScaleOn {
        Both,
        Width,
        Height
    }

    private class SVGScaleAndDimensions {
        public double Width, Height, scaleFactor;

        public SVGScaleAndDimensions(double _Width, double _Height, double _scaleFactor) {
            Width = _Width;
            Height = _Height;
            scaleFactor = _scaleFactor;
        }
        public SVGScaleAndDimensions()
        {}
    }

    //-------------------------------------------------------------------------
    //---------------------------------Create----------------------------------

    private void create(SVGPath svgPath, double Width, double Height, String hoverFillColor) {
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

    private void addClickEvent() {
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                setValue(!getValue());
                applySVGPath(getSvgPath(), getFillColor());
                //event.consume();
            }
        });
    }

    private void applySVGPath(SVGPath svgPath, String fillColor) {
        svgPath.setStyle("-fx-fill:" + fillColor + ";" + svgPathBaseStyle);
        setGraphic(svgPath);
    }

    private SVGPath getSvgPath() {
        return getValue() ? getSvgPathOn() : getSvgPathOff();
    }

    private String getFillColor() {
        return getValue() ? getFillColorOn() : getFillColorOff();
    }

    //-------------------------------------------------------------------------
    //-----------------------------------SVG-----------------------------------

    private SVGPath getSVGPath(String svgFilePath) {
        String dValue = getDValueFromSVG(svgFilePath);
        SVGPath path = new SVGPath();
        path.setContent(dValue);
        path.getStyleClass().add("button-icon");
        //path.setStyle("-fx-fill:" + fillColor + ";"); //hover-fill doesn't work for some reason
        return path;
    }

    private void scalePath(SVGPath path, double scaleFactor) {
        path.setScaleX(scaleFactor);
        path.setScaleY(scaleFactor);
    }

    /**
     * It eats only vectorized (in Inkscape) raster! (it looks like) With only one d-tag
     * @param svgFilePath
     * @return
     */
    private static String getDValueFromSVG(String svgFilePath) {
        String dValue = "";
        try {
            //https://stackoverflow.com/questions/18055189/why-is-my-uri-not-hierarchical
            InputStream is = ResourcesManager.getResourceAsStream(svgFilePath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder = null;
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(is);

            NodeList pathNode = document.getDocumentElement().getElementsByTagName("path");
            dValue = pathNode.item(0).getAttributes().getNamedItem("d").getNodeValue();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return dValue;
    }

    //-------------------------------------------------------------------------
    //-------------------------------Properties--------------------------------

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
