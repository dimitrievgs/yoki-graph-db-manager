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
}
