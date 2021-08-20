package org.yoki_manager;

import java.io.File;
import java.io.InputStream;

//https://stackoverflow.com/questions/19307622/java-says-filenotfoundexception-but-file-exists

/**
 * Must be in core package! Like "org.top_package_name" Main goals of this
 * class: 1) to use filepaths only with '/' delimeter 2) to make work tha same
 * relative filepaths under *org.main_package* like "icons/..." everywhere
 */
public class ResourcesManager { // public ImageIcon KRLogo = new
                                // ImageIcon("/JavaGame/src/resources/kingdomraiderlogo.png");
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

    public static String getAppPath() {
        String path = "/";
        try {
            path = new File(".").getCanonicalPath();
        } catch (Exception e) {
            // TODO: handle exception
        }

        return path;
    }
}
