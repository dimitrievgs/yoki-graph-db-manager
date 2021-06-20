package org.vanilla_manager;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.shape.SVGPath;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

//https://stackoverflow.com/questions/19307622/java-says-filenotfoundexception-but-file-exists

/**
 * Main goals of this class:
 * 1) to use filepaths only with '/' delimeter
 * 2) to make work tha same relative filepaths under *org.main_package* like "icons/..." everywhere
 */
public class ResourcesManager {    //public ImageIcon KRLogo = new ImageIcon("/JavaGame/src/resources/kingdomraiderlogo.png");
    /**
     * For images, for example
     *
     * @param filepath
     * @return
     */
    public static InputStream getResourceAsStream(String filepath) {
        InputStream iconStream = ResourcesManager.class.getResourceAsStream(filepath);
        return iconStream;
    }

    /**
     * For fxml or css, for example
     *
     * @param filepath
     * @return
     */
    public static java.net.URL getResource(String filepath) {
        java.net.URL url = ResourcesManager.class.getResource(filepath);
        return url;
    }

    //https://stackoverflow.com/questions/40753613/javafx-button-with-svg
    public static Button newButtonWithSVG(String svgFilePath, double Width, double Height, double borderWidth, String fillColor, String hoverFillColor) {
        String dValue = getDValueFromSVG(svgFilePath);
        SVGPath path = new SVGPath();
        path.setContent(dValue);
        Bounds bounds = path.getBoundsInLocal();

        double s1 = (Width - 2 * borderWidth) / bounds.getWidth();
        double s2 = (Height - 2 * borderWidth) / bounds.getHeight();
        double scaleFactor = Math.min(s1, s2);

        return newButtonWithSVGCreate(path, Width, Height, scaleFactor, fillColor, hoverFillColor);
    }

    public static Button newButtonWithSVG(String svgFilePath, double Height, String fillColor, String hoverFillColor) {
        String dValue = getDValueFromSVG(svgFilePath);
        SVGPath path = new SVGPath();
        path.setContent(dValue);
        Bounds bounds = path.getBoundsInLocal();

        //double s1 = (Width - 2 * borderWidth) / bounds.getWidth();
        double s2 = Height / bounds.getHeight();
        double Width = s2 * bounds.getWidth();
        double scaleFactor = s2; //Math.min(s1, s2);

        return newButtonWithSVGCreate(path, Width, Height, scaleFactor, fillColor, hoverFillColor);
    }

    private static Button newButtonWithSVGCreate(SVGPath path, double Width, double Height, double scaleFactor, String fillColor, String hoverFillColor)
    {
        path.setScaleX(scaleFactor);
        path.setScaleY(scaleFactor);
        path.getStyleClass().add("button-icon");
        path.setStyle("-fx-fill:" + fillColor + ";");

        Button button = new Button();
        button.setPickOnBounds(true); // make sure transparent parts of the button register clicks too
        button.setGraphic(path);
        button.setAlignment(Pos.CENTER);
        //button.getStyleClass().add("icon-button");

        //if not to set min and max sizes both then it will behave in strange way
        button.setMaxWidth(Width);
        button.setMinWidth(Width);
        button.setMaxHeight(Height);
        button.setMinHeight(Height);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.setBorder(Border.EMPTY);
        button.setStyle("-fx-background-color: transparent;-fx-border-width: 0;"); //-fx-background-color: black;

        button.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue == false) {
                path.setStyle("-fx-fill:" + fillColor + ";-fx-background-color: transparent;");
            } else {
                path.setStyle("-fx-fill:" + hoverFillColor + ";-fx-border-style: none;");
            }
        });

        return button;
    }

    /**
     * It has to work with Inkscape files (this needs to check)
     *
     * @param svgFilePath
     * @return
     */
    public static String getDValueFromSVG(String svgFilePath) {
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
}
